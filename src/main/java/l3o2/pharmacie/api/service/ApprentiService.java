package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateEmailProException;
import l3o2.pharmacie.api.exceptions.InvalidDataException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.request.ApprentiCreateRequest;
import l3o2.pharmacie.api.model.dto.request.ApprentiUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.ApprentiResponse;
import l3o2.pharmacie.api.model.entity.Apprenti;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.repository.ApprentiRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des apprentis en pharmacie.
 */
@Service
@RequiredArgsConstructor
public class ApprentiService {

    private final ApprentiRepository apprentiRepository;

    /**
     * Création d'un apprenti.
     * 
     * @param request Données de l'apprenti.
     * @return L'apprenti créé.
     */
    private final EmployeService employeService;

    @Transactional
    public ApprentiResponse createApprenti(ApprentiCreateRequest request) {
        // Utiliser EmployeService pour vérifier l'email
        if (employeService.existsByEmailPro(request.getEmailPro())) {
            throw new DuplicateEmailProException(request.getEmailPro());
        }
        Apprenti apprenti = Apprenti.builder()
                .nom(request.getNom().trim())
                .prenom(request.getPrenom().trim())
                .email(request.getEmail().toLowerCase().trim())
                .telephone(request.getTelephone().replaceAll("\\s+", ""))
                .adresse(request.getAdresse() != null ? request.getAdresse().trim() : null)
                .dateEmbauche(request.getDateEmbauche())
                .salaire(request.getSalaire())
                .poste(PosteEmploye.APPRENTI)
                .statutContrat(request.getStatutContrat())
                .diplome(request.getDiplome() != null ? request.getDiplome().trim() : null)
                .ecole(request.getEcole().trim())
                .emailPro(request.getEmailPro().trim())
                .password(request.getPassword())
                .permissions(List.of("COMMANDER")) //liste permissions backend
                .build();

        // Générer le matricule automatiquement en fonction du poste
        String baseMatricule = apprenti.getPoste().toString().substring(0, 5); // Exemple : "APPREN" -> "APPRE"
        apprenti.generateMatricule(baseMatricule); // Générer un matricule automatique

        try {
            Apprenti savedApprenti = apprentiRepository.save(apprenti);
            return mapToResponse(savedApprenti);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Données invalides ou contraintes violées");

        }
    }

    /**
     * Récupère tous les apprentis.
     * 
     * @return Liste des apprentis.
     */
    @Transactional(readOnly = true)
    public List<ApprentiResponse> getAllApprentis() {
        return apprentiRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un apprenti par son ID.
     * 
     * @param id ID de l'apprenti.
     * @return L'apprenti correspondant.
     */
    @Transactional(readOnly = true)
    public ApprentiResponse getApprentiById(UUID id) {
        Apprenti apprenti = apprentiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Apprenti", "id", id));
        return mapToResponse(apprenti);
    }

    /**
     * Suppression d'un apprenti.
     * 
     * @param id ID de l'apprenti.
     */
    @Transactional
    public void deleteApprenti(UUID id) {
        if (!apprentiRepository.existsById(id)) {
            throw new ResourceNotFoundException("Apprenti", "id", id);

        }
        apprentiRepository.deleteById(id);
    }

    /**
     * Conversion d'une entité Apprenti en DTO.
     * 
     * @param entity L'entité à convertir.
     * @return Le DTO correspondant.
     */
    private ApprentiResponse mapToResponse(Apprenti entity) {
        return ApprentiResponse.builder()
                .idPersonne(entity.getIdPersonne())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .email(entity.getEmail())
                .telephone(entity.getTelephone())
                .adresse(entity.getAdresse())
                .matricule(entity.getMatricule())
                .dateEmbauche(entity.getDateEmbauche())
                .salaire(entity.getSalaire())
                .poste(entity.getPoste())
                .statutContrat(entity.getStatutContrat())
                .diplome(entity.getDiplome())
                .ecole(entity.getEcole())
                .emailPro(entity.getEmailPro())
                .build();
    }

    // Méthode de mise à jour
    /**
     * Met à jour les informations d'un apprenti existant.
     * 
     * @param id L'identifiant UUID de l'apprenti à mettre à jour.
     * @param request Les données de mise à jour de l'apprenti.
     * @return L'apprenti mis à jour sous forme de ApprentiResponse.
     * @throws ResourceNotFoundException si l'apprenti avec l'ID spécifié n'est pas trouvé.
     * @throws DuplicateEmailProException si le nouvel email professionnel est déjà utilisé.
     * @throws InvalidDataException si les données fournies sont invalides ou violent des contraintes.
     */
    @Transactional
    public ApprentiResponse updateApprenti(UUID id, ApprentiUpdateRequest request) {
        Apprenti existing = apprentiRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Apprenti", "id", id));

        if (request.getNom() != null)
            existing.setNom(request.getNom().trim());
        if (request.getPrenom() != null)
            existing.setPrenom(request.getPrenom().trim());
        if (request.getEmail() != null)
            existing.setEmail(request.getEmail().toLowerCase().trim());
        if (request.getTelephone() != null)
            existing.setTelephone(request.getTelephone().replaceAll("\\s+", ""));
        if (request.getAdresse() != null)
            existing.setAdresse(request.getAdresse().trim());
        if (request.getDateEmbauche() != null)
            existing.setDateEmbauche(request.getDateEmbauche());
        if (request.getSalaire() != null)
            existing.setSalaire(request.getSalaire());
        if (request.getPoste() != null)
            existing.setPoste(request.getPoste());
        if (request.getStatutContrat() != null)
            existing.setStatutContrat(request.getStatutContrat());
        if (request.getDiplome() != null)
            existing.setDiplome(request.getDiplome().trim());
        if (request.getEcole() != null)
            existing.setEcole(request.getEcole().trim());
        if (request.getPassword() != null)
            existing.setPassword(request.getPassword());

        // Vérification de l'email professionnel
        if (request.getEmailPro() != null && !request.getEmailPro().equals(existing.getEmailPro())) {
            if (employeService.existsByEmailPro(request.getEmailPro())) {
                throw new DuplicateEmailProException(request.getEmailPro());
            }
            existing.setEmailPro(request.getEmailPro().trim());
        }

        try {
            Apprenti updated = apprentiRepository.save(existing);
            return mapToResponse(updated);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Données invalides ou contraintes violées");
        }
    }

    /**
     * Recherche des apprentis en fonction d'un terme de recherche.
     * La recherche s'effectue sur le nom, le prénom, l'email, le matricule ou l'école de l'apprenti (insensible à la casse).
     * 
     * @param term Le terme de recherche.
     * @return Une liste d'ApprentiResponse correspondant aux critères de recherche.
     */
    @Transactional(readOnly = true)
    public List<ApprentiResponse> searchApprentis(String term) {
        String searchTerm = term.toLowerCase();
        List<Apprenti> apprentis = apprentiRepository.searchByTerm("%" + searchTerm + "%");
        return apprentis.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
}