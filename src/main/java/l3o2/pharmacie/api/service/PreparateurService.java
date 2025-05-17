package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateEmailProException;
import l3o2.pharmacie.api.exceptions.InvalidDataException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.request.PreparateurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.PreparateurUpdateRequest;
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
     * 
     * @param request Données du préparateur.
     * @return Le préparateur créé.
     */
    private final EmployeService employeService;

    @Transactional
    public PreparateurResponse createPreparateur(PreparateurCreateRequest request) {
        // Vérifier si un Preparateur avec le même email professionnel existe déjà
        if (employeService.existsByEmailPro(request.getEmailPro().trim())) {
            throw new DuplicateEmailProException(request.getEmailPro());
        }
        // Création de l'objet preparateur
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
                .permissions(List.of("COMMANDER")) //liste permissions backend
                .build();

        // Génération du matricule en fonction du poste
        String baseMatricule = preparateur.getPoste().toString(); // Exemple : "PREPARATEUR"
        preparateur.generateMatricule(baseMatricule); // Génère automatiquement le matricule

        try {
            // Sauvegarde du préparateur avec le matricule généré
            Preparateur savedPreparateur = preparateurRepository.save(preparateur);
            return mapToResponse(savedPreparateur);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Données invalides ou contraintes violées");
        }
    }

    /**
     * Récupère tous les préparateurs.
     * 
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
     * 
     * @param id ID du préparateur.
     * @return Le préparateur correspondant.
     */
    @Transactional(readOnly = true)
    public PreparateurResponse getPreparateurById(UUID id) {
        Preparateur preparateur = preparateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préparateur", "id", id));
        return mapToResponse(preparateur);
    }

    /**
     * Supprime un préparateur par son ID.
     * 
     * @param id ID du préparateur.
     */
    @Transactional
    public void deletePreparateur(UUID id) {
        if (!preparateurRepository.existsById(id)) {
            throw new ResourceNotFoundException("Préparateur", "id", id);
        }
        preparateurRepository.deleteById(id);
    }

    /**
     * Conversion d'une entité Preparateur en DTO.
     * 
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
                .poste(entity.getPoste())
                .statutContrat(entity.getStatutContrat())
                .diplome(entity.getDiplome())
                .emailPro(entity.getEmailPro())
                .build();
    }

    public PreparateurResponse updatePreparateur(UUID id, PreparateurUpdateRequest request) {
        Preparateur preparateur = preparateurRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Préparateur", "id", id));

        // Mise à jour des champs
        if (request.getNom() != null)
            preparateur.setNom(request.getNom().trim());
        if (request.getPrenom() != null)
            preparateur.setPrenom(request.getPrenom().trim());
        if (request.getEmail() != null)
            preparateur.setEmail(request.getEmail().toLowerCase().trim());
        if (request.getTelephone() != null)
            preparateur.setTelephone(request.getTelephone().replaceAll("\\s+", ""));
        if (request.getAdresse() != null)
            preparateur.setAdresse(request.getAdresse().trim());
        if (request.getPassword() != null)
            preparateur.setPassword(request.getPassword());
        if (request.getDateEmbauche() != null)
            preparateur.setDateEmbauche(request.getDateEmbauche());
        if (request.getSalaire() != null)
            preparateur.setSalaire(request.getSalaire());
        if (request.getPoste() != null)
            preparateur.setPoste(request.getPoste());
        if (request.getStatutContrat() != null)
            preparateur.setStatutContrat(request.getStatutContrat());
        if (request.getDiplome() != null)
            preparateur.setDiplome(request.getDiplome());

        if (request.getEmailPro() != null) {
            if (!preparateur.getEmailPro().equals(request.getEmailPro())
                    && employeService.existsByEmailPro(request.getEmailPro().trim())) {
                throw new DuplicateEmailProException(request.getEmailPro());
            }
            preparateur.setEmailPro(request.getEmailPro().trim());
        }

        Preparateur updated = preparateurRepository.save(preparateur);
        return mapToResponse(updated);
    }

    public List<PreparateurResponse> searchPreparateurs(String searchTerm) {
        String normalizedTerm = searchTerm.toLowerCase().trim();
        return preparateurRepository.searchByNomPrenom(normalizedTerm).stream()
                .map(this::mapToResponse)
                .toList();
    }
}