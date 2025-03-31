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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicamentService {

    private final MedicamentRepository medicamentRepository;
    private final CisCipBdpmRepository cisCipBdpmRepository;
    private final CisCpdBdpmRepository cisCpdBdpmRepository;

    /**
     * Création d'un nouveau médicament.
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
     * Récupération d'un médicament par ID.
     */
    public MedicamentResponse getMedicamentById(Long id) {
        StockMedicament medicament = medicamentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Médicament introuvable (ID: " + id + ")"
                ));
        return mapToResponse(medicament);
    }

    /**
     * Récupération de tous les médicaments.
     */
    public List<MedicamentResponse> getAll() {
        return medicamentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Mise à jour d'un médicament.
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
     * Suppression d'un médicament.
     */
    public void deleteMedicament(Long id) {
        if (!medicamentRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Médicament introuvable (ID: " + id + ")"
            );
        }
        medicamentRepository.deleteById(id);
    }


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
     * Convertit l'entité Medicament en DTO MedicamentResponse.
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
}