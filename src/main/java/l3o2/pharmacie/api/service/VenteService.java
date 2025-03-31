package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.VenteCreateRequest;
import l3o2.pharmacie.api.model.dto.response.MedicamentResponse;
import l3o2.pharmacie.api.model.dto.response.VenteResponse;
import l3o2.pharmacie.api.model.entity.Client;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import l3o2.pharmacie.api.model.entity.Vente;
import l3o2.pharmacie.api.model.entity.medicament.MedicamentPanier;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import l3o2.pharmacie.api.repository.ClientRepository;
import l3o2.pharmacie.api.repository.PharmacienAdjointRepository;
import l3o2.pharmacie.api.repository.VenteRepository;
import l3o2.pharmacie.api.repository.MedicamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenteService {

    private final VenteRepository venteRepository;
    private final ClientRepository clientRepository;
    private final PharmacienAdjointRepository pharmacienAdjointRepository;
    private final MedicamentRepository medicamentRepository;
    private final MedicamentService medicamentService;

    @Transactional(readOnly = true)
    public List<VenteResponse> getAll() {
        return venteRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VenteResponse getById(UUID idVente) {
        return venteRepository.findById(idVente)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Vente non trouvée"));
    }

    @Transactional(readOnly = true)
    public List<VenteResponse> getAllOrderByDate() {
        return venteRepository.findAll(Sort.by(Sort.Direction.DESC, "dateVente")).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Crée une nouvelle vente.
     * Vérifie la disponibilité du stock, met à jour les quantités et envoie une alerte si le stock est faible.
     */
    @Transactional
    public VenteResponse createVente(VenteCreateRequest request) {
        StringBuilder notifications = new StringBuilder();
        List<MedicamentPanier> medicamentsPanier = request.getMedicaments().stream()
                .map(medRequest -> {
                    StockMedicament stock = medicamentRepository.findById(medRequest.getStockMedicament().getId())
                            .orElseThrow(() -> new ResponseStatusException(
                                    HttpStatus.NOT_FOUND, "Stock introuvable pour le médicament ID: " + medRequest.getStockMedicament().getId()
                            ));

                    // Vérification de la quantité de stock
                    if (stock.getQuantite() < medRequest.getQuantite()) {
                        throw new ResponseStatusException(
                                HttpStatus.BAD_REQUEST, "Quantité insuffisante en stock pour le médicament ID: " + medRequest.getStockMedicament().getId()
                        );
                    }

                    // Création de la ligne de panier
                    MedicamentPanier medicamentPanier = new MedicamentPanier();
                    medicamentPanier.setStockMedicament(stock);
                    medicamentPanier.setQuantite(medRequest.getQuantite());

                    // Décrémenter le stock
                    stock.setQuantite(stock.getQuantite() - medRequest.getQuantite());
                    medicamentRepository.save(stock);

                    // Vérification du seuil d'alerte
                    if (stock.getQuantite() <= stock.getSeuilAlerte()) {
                        notifications.append(" ! Alerte ! : le stock du médicament ").append(stock.getPresentation().getCodeCip13())
                                .append(" est inférieur au seuil d'alerte.\n");
                    }

                    // Vérification de la rupture de stock
                    if (stock.getQuantite() == 0) {
                        notifications.append("! Rupture de stock ! : le médicament ").append(stock.getPresentation().getCodeCip13())
                                .append(" est en rupture de stock.\n");
                    }

                    return medicamentPanier;
                })
                .collect(Collectors.toList());

        // Création de la vente
        PharmacienAdjoint pharmacienAdjoint = pharmacienAdjointRepository.findById(request.getPharmacienAdjointId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pharmacien adjoint non trouvé"));

        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé"));

        // Associer la vente à chaque medicamentPanier
        Vente vente = Vente.builder()
                .dateVente(new Date())
                .modePaiement(request.getModePaiement())
                .montantTotal(request.getMontantTotal())
                .montantRembourse(request.getMontantRembourse())
                .pharmacienAdjoint(pharmacienAdjoint)
                .client(client)
                .medicamentsPanier(medicamentsPanier)
                .build();

        // Associer chaque MedicamentPanier à la vente
        medicamentsPanier.forEach(med -> med.setVente(vente));

        Vente savedVente = venteRepository.save(vente);


        VenteResponse response = mapToResponse(savedVente);

// Ajouter les notifications de rupture de stock et d'alerte de seuil
        if (notifications.length() > 0) {
            response.setNotification(response.getNotification() != null ? response.getNotification() + "\n" + notifications.toString() : notifications.toString());
        }

// Notifications de médicaments nécessitant une ordonnance
        List<String> medicamentsSousOrdonnance = request.getMedicaments().stream()
                .filter(medRequest -> medicamentService.isOrdonnanceRequise(medRequest.getStockMedicament().getId()))
                .map(medRequest -> medicamentRepository.findById(medRequest.getStockMedicament().getId())
                        .orElseThrow(() -> new RuntimeException("Médicament non trouvé"))
                        .getNumeroLot())
                .collect(Collectors.toList());

        String notificationOrdonnance = null;
        if (!medicamentsSousOrdonnance.isEmpty()) {
            notificationOrdonnance = "Les médicaments suivants nécessitent une ordonnance : "
                    + String.join(", ", medicamentsSousOrdonnance);
        }


        if (notificationOrdonnance != null) {
            response.setNotification(response.getNotification() != null ? response.getNotification() + " " + notificationOrdonnance : notificationOrdonnance);
        }

        return response;
    }
    @Transactional
    public void delete(UUID idVente) {
        if (!venteRepository.existsById(idVente)) {
            throw new EntityNotFoundException("Vente non trouvée");
        }
        venteRepository.deleteById(idVente);
    }

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
                                    // Récupère le StockMedicament du MedicamentPanier
                                    StockMedicament stockMedicament = medPanier.getStockMedicament();

                                    // Crée un MedicamentResponse en extrayant les informations du StockMedicament
                                    MedicamentResponse response = MedicamentResponse.builder()
                                            .id(stockMedicament.getId())
                                            .codeCip13(stockMedicament.getPresentation().getCodeCip13())
                                            .numeroLot(stockMedicament.getNumeroLot())
                                            .quantite(medPanier.getQuantite())
                                            .datePeremption(stockMedicament.getDatePeremption())
                                            .dateMiseAJour(stockMedicament.getDateMiseAJour())
                                            .seuilAlerte(stockMedicament.getSeuilAlerte())
                                            .emplacement(stockMedicament.getEmplacement())
                                            .build();

                                    return response;
                                })
                                .collect(Collectors.toList())
                )
                .build();
    }
}