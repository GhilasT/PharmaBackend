package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.ApprentiCreateRequest;
import l3o2.pharmacie.api.model.dto.response.ApprentiResponse;
import l3o2.pharmacie.api.model.entity.Apprenti;
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
     * @param request Données de l'apprenti.
     * @return L'apprenti créé.
     */
    private final EmployeService employeService;

    @Transactional
    public ApprentiResponse createApprenti(ApprentiCreateRequest request) {
        // Utiliser EmployeService pour vérifier l'email
        if (employeService.existsByEmailPro(request.getEmailPro())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un Apprenti avec cet email professionnel existe déjà.");
        }
        Apprenti apprenti = Apprenti.builder()
                .nom(request.getNom().trim())
                .prenom(request.getPrenom().trim())
                .email(request.getEmail().toLowerCase().trim())
                .telephone(request.getTelephone().replaceAll("\\s+", ""))
                .adresse(request.getAdresse() != null ? request.getAdresse().trim() : null)
                .dateEmbauche(request.getDateEmbauche())
                .salaire(request.getSalaire())
                .poste(request.getPoste())
                .statutContrat(request.getStatutContrat())
                .diplome(request.getDiplome() != null ? request.getDiplome().trim() : null)
                .ecole(request.getEcole().trim())
                .emailPro(request.getEmailPro().trim())
                .password(request.getPassword())
                .build();

        // Générer le matricule automatiquement en fonction du poste
        String baseMatricule = apprenti.getPoste().toString().substring(0, 5); // Exemple : "APPREN" -> "APPRE"
        apprenti.generateMatricule(baseMatricule); // Générer un matricule automatique

        try {
            Apprenti savedApprenti = apprentiRepository.save(apprenti);
            return mapToResponse(savedApprenti);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données dupliquées ou invalides");
        }
    }


    /**
     * Récupère tous les apprentis.
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
     * @param id ID de l'apprenti.
     * @return L'apprenti correspondant.
     */
    @Transactional(readOnly = true)
    public ApprentiResponse getApprentiById(UUID id) {
        Apprenti apprenti = apprentiRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Apprenti non trouvé"));
        return mapToResponse(apprenti);
    }

    /**
     * Suppression d'un apprenti.
     * @param id ID de l'apprenti.
     */
    @Transactional
    public void deleteApprenti(UUID id) {
        if (!apprentiRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Apprenti non trouvé");
        }
        apprentiRepository.deleteById(id);
    }

    /**
     * Conversion d'une entité Apprenti en DTO.
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
}