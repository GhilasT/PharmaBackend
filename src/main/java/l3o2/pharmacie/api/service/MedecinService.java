package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;
import l3o2.pharmacie.api.model.entity.Medecin;
import l3o2.pharmacie.api.repository.MedecinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class MedecinService {

    private final MedecinRepository medecinRepository;

    @Autowired
    public MedecinService(MedecinRepository medecinRepository) {
        this.medecinRepository = medecinRepository;
    }

    // Créer un médecin et renvoyer le MedecinResponse
    public MedecinResponse createMedecin(MedecinCreateRequest request) {
        // Vérifier si un médecin avec le même numéro RPPS existe déjà
        Optional<Medecin> existingMedecin = medecinRepository.findByRppsMedecin(request.getRppsMedecin());

        if (existingMedecin.isPresent()) {
            // Si un médecin existe déjà avec ce numéro RPPS, on lance une exception
            throw new RuntimeException("Un médecin avec ce numéro RPPS existe déjà.");
        }

        // Si aucun médecin n'existe, on crée un nouveau médecin
        Medecin medecin = new Medecin();
        medecin.setCivilite(request.getCivilite());
        medecin.setNomExercice(request.getNomExercice());
        medecin.setPrenomExercice(request.getPrenomExercice());
        medecin.setRppsMedecin(request.getRppsMedecin());
        medecin.setCategorieProfessionnelle(request.getCategorieProfessionnelle());
        medecin.setProfession(request.getProfession());
        medecin.setModeExercice(request.getModeExercice());
        medecin.setQualifications(request.getQualifications());
        medecin.setStructureExercice(request.getStructureExercice());
        medecin.setFonctionActivite(request.getFonctionActivite());
        medecin.setGenreActivite(request.getGenreActivite());

        // Sauvegarder le médecin dans la base de données
        medecin = medecinRepository.save(medecin);

        // Retourner la réponse en DTO
        return mapToResponse(medecin);
    }
    // Récupérer un médecin par son ID
    public MedecinResponse getMedecinById(UUID id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        return mapToResponse(medecin);
    }

    // Récupérer tous les médecins
    public List<MedecinResponse> getAllMedecins() {
        return medecinRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Récupérer les médecins paginés
    public Page<MedecinResponse> getMedecinsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Medecin> medecinsPage = medecinRepository.findAll(pageable);

        return medecinsPage.map(this::mapToResponse);
    }

    // Mettre à jour un médecin
    public MedecinResponse updateMedecin(UUID id, MedecinCreateRequest request) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        // Mettre à jour les champs du médecin
        medecin.setCivilite(request.getCivilite());
        medecin.setNomExercice(request.getNomExercice());
        medecin.setPrenomExercice(request.getPrenomExercice());
        medecin.setRppsMedecin(request.getRppsMedecin());
        medecin.setCategorieProfessionnelle(request.getCategorieProfessionnelle());
        medecin.setProfession(request.getProfession());
        medecin.setModeExercice(request.getModeExercice());
        medecin.setQualifications(request.getQualifications());
        medecin.setStructureExercice(request.getStructureExercice());
        medecin.setFonctionActivite(request.getFonctionActivite());
        medecin.setGenreActivite(request.getGenreActivite());

        medecin = medecinRepository.save(medecin);

        return mapToResponse(medecin);
    }

    // Supprimer un médecin
    public void deleteMedecin(UUID id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        medecinRepository.delete(medecin);
    }

    // Mapper de Medecin à MedecinResponse
    private MedecinResponse mapToResponse(Medecin medecin) {
        return new MedecinResponse(
                medecin.getIdMedecin(),
                medecin.getCivilite(),
                medecin.getNomExercice(),
                medecin.getPrenomExercice(),
                medecin.getRppsMedecin(),
                medecin.getCategorieProfessionnelle(),
                medecin.getProfession(),
                medecin.getModeExercice(),
                medecin.getQualifications(),
                medecin.getStructureExercice(),
                medecin.getFonctionActivite(),
                medecin.getGenreActivite()
        );
    }

    public MedecinResponse checkMedecinByRpps(String rpps) {
        // Rechercher un médecin avec le numéro RPPS
        Optional<Medecin> medecinOptional = medecinRepository.findByRppsMedecin(rpps);

        // Vérifier si le médecin est trouvé
        if (medecinOptional.isPresent()) {
            return mapToResponse(medecinOptional.get());  // Si médecin trouvé, retourner sa réponse
        }
        return null;  // Si pas trouvé, retourner null
    }

    // Supprimer un médecin par RPPS
    public void deleteMedecinByRpps(String rpps) {
        Medecin medecin = medecinRepository.findByRppsMedecin(rpps)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec RPPS: " + rpps));

        // Supprimer le médecin trouvé
        medecinRepository.delete(medecin);
    }

    // Recherche des médecins par nom ou prénom
    public List<MedecinResponse> searchMedecins(String term) {
        List<Medecin> medecins = medecinRepository.searchByNomPrenomCombinaison(term);
        return medecins.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}