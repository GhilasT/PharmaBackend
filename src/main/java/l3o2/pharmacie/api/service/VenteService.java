package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.VenteCreateRequest;
import l3o2.pharmacie.api.model.dto.request.VenteUpdateRequest;
import l3o2.pharmacie.api.model.dto.request.MedicamentPanierRequest;
import l3o2.pharmacie.api.model.dto.response.MedicamentResponse;
import l3o2.pharmacie.api.model.dto.response.VenteResponse;
import l3o2.pharmacie.api.model.entity.Client;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import l3o2.pharmacie.api.model.entity.Vente;
import l3o2.pharmacie.api.model.entity.medicament.MedicamentPanier;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import l3o2.pharmacie.api.repository.ClientRepository;
import l3o2.pharmacie.api.repository.CisCipBdpmRepository;
import l3o2.pharmacie.api.repository.MedicamentRepository;
import l3o2.pharmacie.api.repository.PharmacienAdjointRepository;
import l3o2.pharmacie.api.repository.VenteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.logging.Logger;

/**
 * Service gérant la logique métier pour les ventes.
 * Fournit des méthodes pour créer, récupérer, mettre à jour et supprimer des ventes.
 */
@Service
@RequiredArgsConstructor
public class VenteService {

    private final VenteRepository venteRepository;
    private final ClientRepository clientRepository;
    private final PharmacienAdjointRepository pharmacienAdjointRepository;
    private final MedicamentRepository medicamentRepository;
    private final MedicamentService medicamentService;
    private final CisCipBdpmRepository cisCipBdpmRepository;
    private static final Logger LOGGER = Logger.getLogger(VenteService.class.getName());

    /**
     * Récupère toutes les ventes.
     * @return Une liste de toutes les ventes sous forme de DTO.
     */
    @Transactional(readOnly = true)
    public List<VenteResponse> getAll() {
        return venteRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une vente par son identifiant.
     * @param idVente L'identifiant de la vente.
     * @return La vente correspondante sous forme de DTO.
     * @throws EntityNotFoundException si aucune vente n'est trouvée pour l'identifiant donné.
     */
    @Transactional(readOnly = true)
    public VenteResponse getById(UUID idVente) {
        return venteRepository.findById(idVente)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Vente non trouvée"));
    }

    /**
     * Récupère toutes les ventes triées par date de vente décroissante.
     * @return Une liste de toutes les ventes triées par date, sous forme de DTO.
     */
    @Transactional(readOnly = true)
    public List<VenteResponse> getAllOrderByDate() {
        return venteRepository.findAll(Sort.by(Sort.Direction.DESC, "dateVente")).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Crée une nouvelle vente.
     * Gère la mise à jour des stocks de médicaments, les notifications de stock et la vérification des ordonnances.
     * @param request Les informations pour la création de la vente.
     * @return La vente créée sous forme de DTO, avec d'éventuelles notifications.
     * @throws ResponseStatusException si un médicament est introuvable, si la quantité est insuffisante,
     *                                 si le pharmacien adjoint ou le client est introuvable, ou si une ordonnance est requise mais non fournie.
     */
    @Transactional
    public VenteResponse createVente(VenteCreateRequest request) {
        System.out.println("Début de la création de la vente...");
        System.out.println("Requête reçue : " + request);

        // 1) Préparer les notifications de stock
        StringBuilder notifications = new StringBuilder();

        List<MedicamentPanier> medicamentsPanier = request.getMedicaments().stream()
                .map(medRequest -> {
                    String codeInitial = medRequest.getCodeCip13();
                    String finalCode = codeInitial.length() == 8
                            ? medicamentService.getCodeCip13FromCodeCis(codeInitial)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Aucune présentation trouvée pour le code CIS : " + codeInitial
                            ))
                            : codeInitial;

                    System.out.println("Code utilisé (CIP13) : " + finalCode);

                    StockMedicament stock = medicamentRepository
                            .findTopByPresentation_CodeCip13OrderByDateMiseAJourDesc(finalCode)
                            .orElseThrow(() -> {
                                System.out.println("Stock introuvable pour le code CIP13 : " + finalCode);
                                return new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Stock introuvable pour le code CIP13 : " + finalCode
                                );
                            });

                    System.out.println("Stock trouvé (id: " + stock.getId() + ") | Quantité : " + stock.getQuantite());

                    if (stock.getQuantite() < medRequest.getQuantite()) {
                        System.out.println("Quantité insuffisante");
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Quantité insuffisante pour le médicament : " + finalCode
                        );
                    }

                    // Met à jour le stock
                    stock.setQuantite(stock.getQuantite() - medRequest.getQuantite());
                    medicamentRepository.save(stock);

                    // Notifications de stock faible / rupture
                    if (stock.getQuantite() <= stock.getSeuilAlerte()) {
                        notifications.append("Stock faible pour ").append(finalCode).append("\n");
                    }
                    if (stock.getQuantite() == 0) {
                        notifications.append("Rupture de stock : ").append(finalCode).append("\n");
                    }

                    System.out.println("Stock OK !");
                    MedicamentPanier mp = new MedicamentPanier();
                    mp.setStockMedicament(stock);
                    mp.setQuantite(medRequest.getQuantite());
                    return mp;
                })
                .collect(Collectors.toList());

        // 2) Chargement du pharmacien adjoint et du client
        PharmacienAdjoint pharmacienAdjoint = pharmacienAdjointRepository.findById(request.getPharmacienAdjointId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pharmacien adjoint non trouvé"
                ));
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Client non trouvé"
                ));

        // 3) Vérification de l’ordonnance requise AVANT création
        List<String> sousOrdonnance = request.getMedicaments().stream()
                .filter(med -> {
                    String cip13 = med.getCodeCip13();
                    if (cip13.length() == 8) {
                        cip13 = medicamentService.getCodeCip13FromCodeCis(cip13).orElse(null);
                    }
                    return cip13 != null && medicamentService.isOrdonnanceRequise(
                            medicamentRepository
                                    .findTopByPresentation_CodeCip13OrderByDateMiseAJourDesc(cip13)
                                    .orElseThrow()
                                    .getId()
                    );
                })
                .map(MedicamentPanierRequest::getCodeCip13)
                .collect(Collectors.toList());

        if (!request.isOrdonnanceAjoutee() && !sousOrdonnance.isEmpty()) {
            System.out.println("Ordonnance requise");
            // Conserve le blocage stricte (428) de la V2
            LOGGER.warning("Ordonnance requise avant paiement");
            throw new ResponseStatusException(
                    HttpStatus.PRECONDITION_REQUIRED,
                    "Impossible de finaliser la vente : ordonnance requise."
            );
        }

        // 4) Création de l’entité Vente
        Vente vente = Vente.builder()
                .dateVente(request.getDateVente())
                .modePaiement(request.getModePaiement())
                .montantTotal(request.getMontantTotal())
                .montantRembourse(request.getMontantRembourse())
                .pharmacienAdjoint(pharmacienAdjoint)
                .client(client)
                .medicamentsPanier(medicamentsPanier)
                .build();

        medicamentsPanier.forEach(mp -> mp.setVente(vente));

        Vente savedVente = venteRepository.save(vente);

        // 5) Construction de la réponse
        VenteResponse response = mapToResponse(savedVente);

        // Injecter les notifications de stock (V1)
        if (notifications.length() > 0) {
            response.setNotification(notifications.toString());
        } else {
            // Par défaut, indiquer que tout est OK (V1)
            response.setNotification("Pas d'ordonnance requise");
        }

        System.out.println("Vente enregistrée avec succès !");
        return response;
    }

    /**
     * Met à jour une vente existante.
     * Permet de modifier le pharmacien adjoint, le client, la date de vente, le mode de paiement et les médicaments du panier.
     * Gère la remise en stock des anciens médicaments et la décrémentation du stock pour les nouveaux.
     * Calcule le nouveau montant total et le montant remboursé.
     * @param idVente L'identifiant de la vente à mettre à jour.
     * @param request Les informations de mise à jour de la vente.
     * @return La vente mise à jour sous forme de DTO, avec d'éventuelles notifications de stock.
     * @throws EntityNotFoundException si la vente n'est pas trouvée.
     * @throws ResponseStatusException si le pharmacien adjoint ou le client est introuvable,
     *                                 si un médicament du nouveau panier est introuvable ou si la quantité est insuffisante.
     */
    @Transactional
    public VenteResponse updateVente(UUID idVente, VenteUpdateRequest request) {
        Vente vente = venteRepository.findById(idVente)
                .orElseThrow(() -> new EntityNotFoundException("Vente non trouvée"));

        StringBuilder notifications = new StringBuilder();

        try {
            // Mise à jour des champs modifiables
            if (request.getPharmacienAdjointId() != null) {
                PharmacienAdjoint pharmacienAdjoint = pharmacienAdjointRepository.findById(request.getPharmacienAdjointId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Pharmacien adjoint non trouvé"
                        ));
                vente.setPharmacienAdjoint(pharmacienAdjoint);
            }

            if (request.getClientId() != null) {
                Client client = clientRepository.findById(request.getClientId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Client non trouvé"
                        ));
                vente.setClient(client);
            }

            if (request.getDateVente() != null) {
                vente.setDateVente(request.getDateVente());
            }

            if (request.getModePaiement() != null) {
                vente.setModePaiement(request.getModePaiement());
            }

            // Gestion de la mise à jour des médicaments
            if (request.getMedicaments() != null && !request.getMedicaments().isEmpty()) {
                // 1. Vérifier si les médicaments demandés sont disponibles avant de modifier quoi que ce soit
                for (MedicamentPanierRequest medRequest : request.getMedicaments()) {
                    String codeInitial = medRequest.getCodeCip13();
                    // Utiliser une variable différente qui ne sera pas modifiée
                    final String codeToUse = getFinalCode(codeInitial);

                    // Vérification de la disponibilité du médicament
                    StockMedicament stock = medicamentRepository
                            .findTopByPresentation_CodeCip13OrderByDateMiseAJourDesc(codeToUse)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Stock introuvable pour le code CIP13 : " + codeToUse
                            ));

                    if (stock.getQuantite() < medRequest.getQuantite()) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Quantité insuffisante pour le médicament : " + codeToUse + " (disponible: " + stock.getQuantite() + ", demandé: " + medRequest.getQuantite() + ")"
                        );
                    }
                }

                // 2. Récupérer les médicaments du panier actuel pour remettre le stock
                for (MedicamentPanier mp : vente.getMedicamentsPanier()) {
                    StockMedicament stock = mp.getStockMedicament();
                    if (stock != null) {
                        // Réincrémenter le stock pour les médicaments qui seront remplacés
                        stock.setQuantite(stock.getQuantite() + mp.getQuantite());
                        medicamentRepository.save(stock);
                    }
                }

                // 3. Vider le panier actuel
                vente.getMedicamentsPanier().clear();

                // 4. Créer les nouveaux médicaments du panier
                double nouveauMontantTotal = 0.0;
                double nouveauMontantRembourse = 0.0;

                List<MedicamentPanier> nouveauxMedicamentsPanier = new ArrayList<>();

                for (MedicamentPanierRequest medRequest : request.getMedicaments()) {
                    String codeInitial = medRequest.getCodeCip13();
                    // Utiliser une variable différente qui ne sera pas modifiée
                    final String codeToUse = getFinalCode(codeInitial);

                    StockMedicament stock = medicamentRepository
                            .findTopByPresentation_CodeCip13OrderByDateMiseAJourDesc(codeToUse)
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND,
                                    "Stock introuvable pour le code CIP13 : " + codeToUse
                            ));

                    // Mise à jour du stock
                    stock.setQuantite(stock.getQuantite() - medRequest.getQuantite());
                    medicamentRepository.save(stock);

                    // Notifications de stock faible / rupture
                    if (stock.getQuantite() <= stock.getSeuilAlerte()) {
                        notifications.append("Stock faible pour ").append(codeToUse).append("\n");
                    }
                    if (stock.getQuantite() == 0) {
                        notifications.append("Rupture de stock : ").append(codeToUse).append("\n");
                    }

                    // Création du nouveau MedicamentPanier
                    MedicamentPanier mp = new MedicamentPanier();
                    mp.setStockMedicament(stock);
                    mp.setQuantite(medRequest.getQuantite());
                    mp.setVente(vente);
                    nouveauxMedicamentsPanier.add(mp);

                    // Calcul du nouveau montant total
                    if (stock.getPresentation().getPrixTTC() != null) {
                        nouveauMontantTotal += stock.getPresentation().getPrixTTC().doubleValue() * medRequest.getQuantite();
                        
                        // Calcul du remboursement
                        String tauxStr = stock.getPresentation().getTauxRemboursement();
                        if (tauxStr != null && !tauxStr.isEmpty()) {
                            try {
                                // Nettoyage du format du taux (suppression des caractères non numériques)
                                String cleanTaux = tauxStr.replaceAll("[^0-9.]", "");
                                if (!cleanTaux.isEmpty()) {
                                    double taux = Double.parseDouble(cleanTaux) / 100.0;
                                    nouveauMontantRembourse += stock.getPresentation().getPrixTTC().doubleValue() * medRequest.getQuantite() * taux;
                                }
                            } catch (NumberFormatException e) {
                                // En cas d'erreur, on ne modifie pas le remboursement
                                System.err.println("Erreur lors du parsing du taux de remboursement: " + tauxStr);
                            }
                        }
                    }
                }

                // Mise à jour du panier et recalcul du montant total
                vente.getMedicamentsPanier().addAll(nouveauxMedicamentsPanier);
                vente.setMontantTotal(nouveauMontantTotal);
                vente.setMontantRembourse(nouveauMontantRembourse);
            }

            // Sauvegarde de la vente mise à jour
            Vente updatedVente = venteRepository.save(vente);
            
            // Construction de la réponse
            VenteResponse response = mapToResponse(updatedVente);
            
            // Ajout des notifications éventuelles
            if (notifications.length() > 0) {
                response.setNotification(notifications.toString());
            }
            
            return response;
            
        } catch (Exception e) {
            // Log l'erreur pour le débogage
            System.err.println("Erreur lors de la mise à jour de la vente: " + e.getMessage());
            e.printStackTrace();
            
            // Relancer l'exception
            throw e;
        }
    }

    /**
     * Méthode utilitaire pour obtenir le code CIP13 final à partir d'un code initial (CIS ou CIP13).
     * Si le code initial est un code CIS (8 caractères), il est converti en code CIP13.
     * @param codeInitial Le code initial (CIS ou CIP13).
     * @return Le code CIP13 correspondant.
     * @throws ResponseStatusException si aucune présentation n'est trouvée pour le code CIS fourni.
     */
    private String getFinalCode(String codeInitial) {
        if (codeInitial.length() == 8) {
            return medicamentService.getCodeCip13FromCodeCis(codeInitial)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "Aucune présentation trouvée pour le code CIS : " + codeInitial
                    ));
        }
        return codeInitial;
    }

    /**
     * Supprime une vente par son identifiant.
     * @param idVente L'identifiant de la vente à supprimer.
     * @throws EntityNotFoundException si aucune vente n'est trouvée pour l'identifiant donné.
     */
    @Transactional
    public void delete(UUID idVente) {
        if (!venteRepository.existsById(idVente)) {
            throw new EntityNotFoundException("Vente non trouvée");
        }
        venteRepository.deleteById(idVente);
    }

    /**
     * Mappe une entité Vente vers un DTO VenteResponse.
     * @param vente L'entité Vente à mapper.
     * @return Le DTO VenteResponse correspondant.
     */
    private VenteResponse mapToResponse(Vente vente) {
        return VenteResponse.builder()
                .idVente(vente.getIdVente())
                .dateVente(vente.getDateVente())
                .modePaiement(vente.getModePaiement())
                .montantTotal(vente.getMontantTotal())
                .montantRembourse(vente.getMontantRembourse())
                .pharmacienAdjointId(vente.getPharmacienAdjoint().getIdPersonne())
                .clientId(vente.getClient().getIdPersonne())
                .medicamentIds(
                        vente.getMedicamentsPanier().stream()
                                .map(medPanier -> {
                                    StockMedicament stock = medPanier.getStockMedicament();
                                    return MedicamentResponse.builder()
                                            .id(stock.getId())
                                            .codeCip13(stock.getPresentation().getCodeCip13())
                                            .numeroLot(stock.getNumeroLot())
                                            .quantite(medPanier.getQuantite())
                                            .datePeremption(stock.getDatePeremption())
                                            .dateMiseAJour(stock.getDateMiseAJour())
                                            .seuilAlerte(stock.getSeuilAlerte())
                                            .emplacement(stock.getEmplacement())
                                            // Remettre la dénomination (V1)
                                            .denomination(stock.getPresentation().getCisBdpm().getDenomination())
                                            .build();
                                })
                                .collect(Collectors.toList())
                )
                .build();
    }

    /**
     * Récupère toutes les ventes associées à un pharmacien adjoint spécifique.
     * @param pharmacienId L'identifiant du pharmacien adjoint.
     * @return Une liste des ventes associées au pharmacien, sous forme de DTO.
     */
    @Transactional(readOnly = true)
    public List<VenteResponse> getByPharmacienId(UUID pharmacienId) {
        return venteRepository.findByPharmacienAdjoint_IdPersonne(pharmacienId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupère toutes les ventes associées à un client spécifique.
     * @param clientId L'identifiant du client.
     * @return Une liste des ventes associées au client, sous forme de DTO.
     */
    @Transactional(readOnly = true)
    public List<VenteResponse> getByClientId(UUID clientId) {
        return venteRepository.findByClient_IdPersonne(clientId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}