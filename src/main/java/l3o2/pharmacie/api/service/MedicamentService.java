package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.MedicamentRequest;
import l3o2.pharmacie.api.model.dto.response.MedicamentResponse;
import l3o2.pharmacie.api.model.entity.medicament.CisCipBdpm;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import l3o2.pharmacie.api.repository.CisCipBdpmRepository;
import l3o2.pharmacie.api.repository.CisCpdBdpmRepository;
import l3o2.pharmacie.api.repository.MedicamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service pour gérer les médicaments en stock.
 * Fournit la logique métier pour les opérations CRUD sur les médicaments,
 * ainsi que des fonctionnalités spécifiques comme la vérification de la nécessité d'une ordonnance.
 */
@Service
@RequiredArgsConstructor
public class MedicamentService {

    private final MedicamentRepository medicamentRepository;
    private final CisCipBdpmRepository cisCipBdpmRepository;
    private final CisCpdBdpmRepository cisCpdBdpmRepository;

    /**
     * Crée un nouveau médicament en stock.
     *
     * @param request Les informations du médicament à créer.
     * @return Une {@link MedicamentResponse} représentant le médicament créé.
     * @throws ResponseStatusException si aucune présentation n'est trouvée pour le code CIP13 fourni.
     */
    public MedicamentResponse createMedicament(MedicamentRequest request) {

        CisCipBdpm presentation = cisCipBdpmRepository.findByCodeCip13(request.getCodeCip13())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aucune présentation trouvée pour le code CIP13 : " + request.getCodeCip13()
                ));

        // Construire l'entité Medicament
        StockMedicament medicament = new StockMedicament();
        medicament.setPresentation(presentation);
        medicament.setQuantite(request.getQuantite());
        medicament.setNumeroLot(request.getNumeroLot());
        medicament.setDatePeremption(request.getDatePeremption());
        medicament.setDateMiseAJour(
                request.getDateMiseAJour() != null ? request.getDateMiseAJour() : LocalDate.now()
        );
        if (medicament.getSeuilAlerte() == null) {
            medicament.setSeuilAlerte(6);
        }
        medicament.setEmplacement(request.getEmplacement());

        StockMedicament saved = medicamentRepository.save(medicament);
        return mapToResponse(saved);
    }

    /**
     * Récupère un médicament par son identifiant unique.
     *
     * @param id L'identifiant du médicament.
     * @return Une {@link MedicamentResponse} représentant le médicament trouvé.
     * @throws ResponseStatusException si aucun médicament n'est trouvé pour l'ID fourni.
     */
    public MedicamentResponse getMedicamentById(Long id) {
        StockMedicament medicament = medicamentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Médicament introuvable (ID: " + id + ")"
                ));
        return mapToResponse(medicament);
    }

    /**
     * Récupère la liste de tous les médicaments en stock.
     *
     * @return Une liste de {@link MedicamentResponse}.
     */
    public List<MedicamentResponse> getAll() {
        return medicamentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Met à jour les informations d'un médicament existant.
     *
     * @param id L'identifiant du médicament à mettre à jour.
     * @param request Les nouvelles informations du médicament.
     * @return Une {@link MedicamentResponse} représentant le médicament mis à jour.
     * @throws ResponseStatusException si le médicament ou la présentation (CIP13) n'est pas trouvé.
     */
    public MedicamentResponse updateMedicament(Long id, MedicamentRequest request) {
        StockMedicament medicament = medicamentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Médicament introuvable (ID: " + id + ")"
                ));

        if (request.getCodeCip13() != null) {
            CisCipBdpm pres = cisCipBdpmRepository.findByCodeCip13(request.getCodeCip13())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND,
                            "CIP13 introuvable: " + request.getCodeCip13()
                    ));
            medicament.setPresentation(pres);
        }
        if (request.getQuantite() != null) {
            medicament.setQuantite(request.getQuantite());
        }
        if (request.getNumeroLot() != null) {
            medicament.setNumeroLot(request.getNumeroLot());
        }
        if (request.getDatePeremption() != null) {
            medicament.setDatePeremption(request.getDatePeremption());
        }
        if (request.getDateMiseAJour() != null) {
            medicament.setDateMiseAJour(request.getDateMiseAJour());
        }
        if (request.getSeuilAlerte() != null) {
            medicament.setSeuilAlerte(request.getSeuilAlerte());
        }
        if (request.getEmplacement() != null) {
            medicament.setEmplacement(request.getEmplacement());
        }

        StockMedicament updated = medicamentRepository.save(medicament);
        return mapToResponse(updated);
    }

    /**
     * Supprime un médicament par son identifiant.
     *
     * @param id L'identifiant du médicament à supprimer.
     * @throws ResponseStatusException si aucun médicament n'est trouvé pour l'ID fourni.
     */
    public void deleteMedicament(Long id) {
        if (!medicamentRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Médicament introuvable (ID: " + id + ")"
            );
        }
        medicamentRepository.deleteById(id);
    }

    /**
     * Vérifie si une ordonnance est requise pour un médicament donné.
     *
     * @param medicamentId L'identifiant du médicament en stock.
     * @return {@code true} si une ordonnance est requise, {@code false} sinon.
     * @throws ResponseStatusException si aucun médicament n'est trouvé pour l'ID fourni.
     */
    public boolean isOrdonnanceRequise(Long medicamentId) {

        StockMedicament medicament = medicamentRepository.findById(medicamentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Médicament introuvable (ID: " + medicamentId + ")"
                ));

        String codeCis = medicament.getPresentation().getCisBdpm().getCodeCis();

        boolean exists = cisCpdBdpmRepository.existsByCisBdpm_CodeCis(codeCis);
        return exists;
    }

    /**
     * Convertit une entité {@link StockMedicament} en un DTO {@link MedicamentResponse}.
     *
     * @param medicament L'entité médicament à convertir.
     * @return Le DTO {@link MedicamentResponse} correspondant.
     */
    private MedicamentResponse mapToResponse(StockMedicament medicament) {
        return MedicamentResponse.builder()
                .id(medicament.getId())
                .codeCip13(medicament.getPresentation().getCodeCip13())
                .numeroLot(medicament.getNumeroLot())
                .quantite(medicament.getQuantite())
                .datePeremption(medicament.getDatePeremption())
                .dateMiseAJour(medicament.getDateMiseAJour())
                .seuilAlerte(medicament.getSeuilAlerte())
                .emplacement(medicament.getEmplacement())
                .build();
    }

    /**
     * Trouve le dernier stock de médicament enregistré pour un code CIP13 donné,
     * basé sur la date de mise à jour la plus récente.
     *
     * @param codeCip13 Le code CIP13 de la présentation du médicament.
     * @return Le {@link StockMedicament} le plus récent pour le code CIP13 donné.
     * @throws ResponseStatusException si aucun stock de médicament n'est trouvé pour le code CIP13.
     */
    public StockMedicament findLatestStockByCodeCip13(String codeCip13) {
        return medicamentRepository.findTopByPresentation_CodeCip13OrderByDateMiseAJourDesc(codeCip13)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Aucun medicamantstock trouvé pour le code CIP13 : " + codeCip13
                ));
    }

    /**
     * Récupère le code CIP13 associé à un code CIS donné.
     *
     * @param codeCis Le code CIS du médicament.
     * @return Un {@link Optional} contenant le code CIP13 s'il est trouvé, sinon un {@link Optional#empty()}.
     */
    public Optional<String> getCodeCip13FromCodeCis(String codeCis) {
        return cisCipBdpmRepository
                .findFirstByCisBdpm_CodeCis(codeCis)
                .map(CisCipBdpm::getCodeCip13);
    }
}