package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.VenteCreateRequest;
import l3o2.pharmacie.api.model.dto.request.MedicamentPanierRequest;
import l3o2.pharmacie.api.model.dto.response.MedicamentResponse;
import l3o2.pharmacie.api.model.dto.response.VenteResponse;
import l3o2.pharmacie.api.model.entity.Client;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import l3o2.pharmacie.api.model.entity.Vente;
import l3o2.pharmacie.api.model.entity.medicament.MedicamentPanier;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import l3o2.pharmacie.api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.server.ResponseStatusException;
import java.util.logging.Logger;
import java.util.logging.Level;

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
    private final CisCipBdpmRepository cisCipBdpmRepository;
    private static final Logger LOGGER = Logger.getLogger(VenteService.class.getName());

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

    @Transactional
    public VenteResponse createVente(VenteCreateRequest request) {
        System.out.println("Début de la création de la vente...");
        System.out.println("Requête reçue : " + request);

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
                                return new ResponseStatusException(HttpStatus.NOT_FOUND,
                                        "Stock introuvable pour le code CIP13 : " + finalCode);
                            });

                    System.out.println("Stock trouvé (id: " + stock.getId() + ") | Quantité : " + stock.getQuantite());

                    if (stock.getQuantite() < medRequest.getQuantite()) {
                        System.out.println("quantiter insufisante");
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "Quantité insuffisante pour le médicament : " + finalCode);
                    }

                    stock.setQuantite(stock.getQuantite() - medRequest.getQuantite());
                    medicamentRepository.save(stock);

                    if (stock.getQuantite() <= stock.getSeuilAlerte()) {
                        notifications.append("Stock faible pour ").append(finalCode).append("\n");
                    }
                    if (stock.getQuantite() == 0) {
                        notifications.append("Rupture de stock : ").append(finalCode).append("\n");
                    }

                    System.out.println("sotck est ok !");
                    MedicamentPanier mp = new MedicamentPanier();
                    mp.setStockMedicament(stock);
                    mp.setQuantite(medRequest.getQuantite());
                    return mp;
                })
                .collect(Collectors.toList());
        System.out.println("la liste aussi !");
        PharmacienAdjoint pharmacienAdjoint = pharmacienAdjointRepository.findById(request.getPharmacienAdjointId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pharmacien adjoint non trouvé"));
        System.out.println("pharmacinet ok ?! ");
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé"));
        System.out.println("client ok ?! ");
        Vente vente = Vente.builder()
                .dateVente(request.getDateVente())
                .modePaiement(request.getModePaiement())
                .montantTotal(request.getMontantTotal())
                .montantRembourse(request.getMontantRembourse())
                .pharmacienAdjoint(pharmacienAdjoint)
                .client(client)
                .medicamentsPanier(medicamentsPanier)
                .build();

        medicamentsPanier.forEach(med -> med.setVente(vente));

        Vente savedVente = venteRepository.save(vente);
        VenteResponse response = mapToResponse(savedVente);

        if (notifications.length() > 0) {
            response.setNotification(notifications.toString());
        }

        List<String> sousOrdonnance = request.getMedicaments().stream()
                .filter(med -> {
                    String cip13 = med.getCodeCip13();
                    if (cip13.length() == 8) {
                        cip13 = medicamentService.getCodeCip13FromCodeCis(cip13).orElse(null);
                    }
                    return cip13 != null && medicamentService.isOrdonnanceRequise(
                            medicamentRepository.findTopByPresentation_CodeCip13OrderByDateMiseAJourDesc(cip13)
                                    .orElseThrow().getId());
                })
                .map(MedicamentPanierRequest::getCodeCip13)
                .collect(Collectors.toList());

        if (!request.isOrdonnanceAjoutee() && !sousOrdonnance.isEmpty()) {
            System.out.println("Ordonnance requise");

            response.setNotification(" Ordonnance requise");
        }else{
            response.setNotification("Pas d'ordonnance requise");
        }

        System.out.println(" Vente enregistrée avec succès !");
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
                                            .build();
                                })
                                .collect(Collectors.toList())
                )
                .build();
    }
}