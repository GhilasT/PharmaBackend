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

/**
 * Service pour la gestion des médecins.
 * Fournit les opérations CRUD pour les médecins, ainsi que des fonctionnalités de recherche et de pagination.
 */
@Service
public class MedecinService {

    private final MedecinRepository medecinRepository;

    /**
     * Constructeur pour l'injection de dépendances.
     * @param medecinRepository Le repository pour l'accès aux données des médecins.
     */
    @Autowired
    public MedecinService(MedecinRepository medecinRepository) {
        this.medecinRepository = medecinRepository;
    }

    /**
     * Crée un nouveau médecin.
     * Vérifie si un médecin avec le même numéro RPPS existe déjà avant la création.
     *
     * @param request Les informations du médecin à créer.
     * @return Un {@link MedecinResponse} représentant le médecin créé.
     * @throws RuntimeException si un médecin avec le même numéro RPPS existe déjà.
     */
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

    /**
     * Récupère un médecin par son identifiant unique (UUID).
     *
     * @param id L'identifiant UUID du médecin.
     * @return Un {@link MedecinResponse} représentant le médecin trouvé.
     * @throws RuntimeException si aucun médecin n'est trouvé pour l'ID fourni.
     */
    public MedecinResponse getMedecinById(UUID id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        return mapToResponse(medecin);
    }

    /**
     * Récupère la liste de tous les médecins.
     *
     * @return Une liste de {@link MedecinResponse}.
     */
    public List<MedecinResponse> getAllMedecins() {
        return medecinRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupère une page de médecins.
     *
     * @param page Le numéro de la page (commence à 0).
     * @param size Le nombre d'éléments par page.
     * @return Une {@link Page} de {@link MedecinResponse}.
     */
    public Page<MedecinResponse> getMedecinsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Medecin> medecinsPage = medecinRepository.findAll(pageable);

        return medecinsPage.map(this::mapToResponse);
    }

    /**
     * Met à jour les informations d'un médecin existant.
     *
     * @param id L'identifiant UUID du médecin à mettre à jour.
     * @param request Les nouvelles informations du médecin.
     * @return Un {@link MedecinResponse} représentant le médecin mis à jour.
     * @throws RuntimeException si aucun médecin n'est trouvé pour l'ID fourni.
     */
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

    /**
     * Supprime un médecin par son identifiant unique (UUID).
     *
     * @param id L'identifiant UUID du médecin à supprimer.
     * @throws RuntimeException si aucun médecin n'est trouvé pour l'ID fourni.
     */
    public void deleteMedecin(UUID id) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        medecinRepository.delete(medecin);
    }

    /**
     * Convertit une entité {@link Medecin} en un DTO {@link MedecinResponse}.
     *
     * @param medecin L'entité médecin à convertir.
     * @return Le DTO {@link MedecinResponse} correspondant.
     */
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

    /**
     * Vérifie l'existence d'un médecin par son numéro RPPS.
     *
     * @param rpps Le numéro RPPS du médecin à rechercher.
     * @return Un {@link MedecinResponse} si le médecin est trouvé, sinon {@code null}.
     */
    public MedecinResponse checkMedecinByRpps(String rpps) {
        // Rechercher un médecin avec le numéro RPPS
        Optional<Medecin> medecinOptional = medecinRepository.findByRppsMedecin(rpps);

        // Vérifier si le médecin est trouvé
        if (medecinOptional.isPresent()) {
            return mapToResponse(medecinOptional.get());  // Si médecin trouvé, retourner sa réponse
        }
        return null;  // Si pas trouvé, retourner null
    }

    /**
     * Supprime un médecin par son numéro RPPS.
     *
     * @param rpps Le numéro RPPS du médecin à supprimer.
     * @throws RuntimeException si aucun médecin n'est trouvé pour le numéro RPPS fourni.
     */
    public void deleteMedecinByRpps(String rpps) {
        Medecin medecin = medecinRepository.findByRppsMedecin(rpps)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé avec RPPS: " + rpps));

        // Supprimer le médecin trouvé
        medecinRepository.delete(medecin);
    }

    /**
     * Recherche des médecins par nom, prénom ou une combinaison des deux.
     *
     * @param term Le terme de recherche.
     * @return Une liste de {@link MedecinResponse} correspondant aux critères de recherche.
     */
    public List<MedecinResponse> searchMedecins(String term) {
        List<Medecin> medecins = medecinRepository.searchByNomPrenomCombinaison(term);
        return medecins.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Compte le nombre total de médecins enregistrés.
     *
     * @return Le nombre total de médecins.
     */
    public long countAllMedecins() {
        return medecinRepository.count();
    }
    
}