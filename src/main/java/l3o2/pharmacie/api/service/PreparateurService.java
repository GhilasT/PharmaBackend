package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.PreparateurCreateRequest;
import l3o2.pharmacie.api.model.dto.response.PreparateurResponse;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.Preparateur;
import l3o2.pharmacie.api.repository.PreparateurRepository;
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
 * Service pour la gestion des préparateurs en pharmacie.
 */
@Service
@RequiredArgsConstructor
public class PreparateurService {

    private final PreparateurRepository preparateurRepository;

    /**
     * Création d'un préparateur.
     * @param request Données du préparateur.
     * @return Le préparateur créé.
     */
    @Transactional
    public PreparateurResponse createPreparateur(PreparateurCreateRequest request) {
        Preparateur preparateur = Preparateur.builder()
                .nom(request.getNom().trim())
                .prenom(request.getPrenom().trim())
                .email(request.getEmail().toLowerCase().trim())
                .telephone(request.getTelephone().replaceAll("\\s+", ""))
                .adresse(request.getAdresse() != null ? request.getAdresse().trim() : null)
                .dateEmbauche(request.getDateEmbauche())
                .salaire(request.getSalaire())
                .poste(PosteEmploye.PREPARATEUR)
                .statutContrat(request.getStatutContrat())
                .diplome(request.getDiplome() != null ? request.getDiplome().trim() : null)
                .emailPro(request.getEmailPro().trim())
                .password(request.getPassword())
                .build();

        try {
            Preparateur savedPreparateur = preparateurRepository.save(preparateur);
            return mapToResponse(savedPreparateur);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données dupliquées ou invalides");
        }
    }

    /**
     * Récupère tous les préparateurs.
     * @return Liste des préparateurs.
     */
    @Transactional(readOnly = true)
    public List<PreparateurResponse> getAllPreparateurs() {
        return preparateurRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un préparateur par son ID.
     * @param id ID du préparateur.
     * @return Le préparateur correspondant.
     */
    @Transactional(readOnly = true)
    public PreparateurResponse getPreparateurById(UUID id) {
        Preparateur preparateur = preparateurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Préparateur non trouvé"));
        return mapToResponse(preparateur);
    }

    /**
     * Supprime un préparateur par son ID.
     * @param id ID du préparateur.
     */
    @Transactional
    public void deletePreparateur(UUID id) {
        if (!preparateurRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Préparateur non trouvé");
        }
        preparateurRepository.deleteById(id);
    }

    /**
     * Conversion d'une entité Preparateur en DTO.
     * @param entity L'entité à convertir.
     * @return Le DTO correspondant.
     */
    private PreparateurResponse mapToResponse(Preparateur entity) {
        return PreparateurResponse.builder()
                .idPersonne(entity.getIdPersonne())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .email(entity.getEmail())
                .telephone(entity.getTelephone())
                .adresse(entity.getAdresse())
                .matricule(entity.getMatricule())
                .dateEmbauche(entity.getDateEmbauche())
                .salaire(entity.getSalaire())
                .poste(entity.getPoste().toString())
                .statutContrat(entity.getStatutContrat().toString())
                .diplome(entity.getDiplome())
                .emailPro(entity.getEmailPro())
                .build();
    }
}