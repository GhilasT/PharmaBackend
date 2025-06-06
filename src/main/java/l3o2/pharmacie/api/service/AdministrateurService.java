package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateEmailProException;
import l3o2.pharmacie.api.exceptions.InvalidDataException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.request.AdministrateurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.AdministrateurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.AdministrateurResponse;
import l3o2.pharmacie.api.model.entity.Administrateur;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.repository.AdministrateurRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.UUID;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service pour gérer les administrateurs
 * Contient la logique métier pour la gestion des administrateurs dans
 * l'application.
 */
@Service
@RequiredArgsConstructor
public class AdministrateurService {

    private final AdministrateurRepository administrateurRepository;

    /**
     * Création d'un nouvel administrateur.
     * 
     * @param request Contient les informations de l'administrateur à créer.
     * @return L'administrateur créé sous forme de réponse DTO.
     */
    private final EmployeService employeService;

    public AdministrateurResponse createAdministrateur(AdministrateurCreateRequest request) {
        // Utiliser EmployeService pour vérifier l'email
        if (employeService.existsByEmailPro(request.getEmailPro())) {
            throw new DuplicateEmailProException(request.getEmailPro());
        }
        // Créer un administrateur
        Administrateur admin = Administrateur.builder()
                .nom(request.getNom().trim())
                .prenom(request.getPrenom().trim())
                .email(request.getEmail().toLowerCase().trim()) // Email personnel
                .emailPro(request.getEmailPro().trim()) // Email professionnel
                .telephone(request.getTelephone().trim())
                .adresse(request.getAdresse().trim())
                .password(request.getPassword().trim())
                .role(request.getRole().trim())
                .dateEmbauche(request.getDateEmbauche())
                .salaire(request.getSalaire())
                .statutContrat(request.getStatutContrat())
                .poste(PosteEmploye.ADMINISTRATEUR) // Poste spécifique
                .diplome(request.getDiplome()) // Diplôme éventuel
                .permissions(List.of("ADMINISTRER","COMMANDER")) //liste permissions backend
                .build();

        // Générer le matricule automatiquement en fonction du poste
        String baseMatricule = admin.getPoste().toString(); // Exemple : "ADMIN"
        admin.generateMatricule(baseMatricule); // Générer un matricule automatique

        // Sauvegarder l'administrateur dans la base de données
        try {
            Administrateur savedAdmin = administrateurRepository.save(admin);
            return mapToResponse(savedAdmin);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Données invalides ou contraintes violées");
        }
    }

    /**
     * Récupère tous les administrateurs.
     * 
     * @return Liste des administrateurs sous forme de DTOs.
     */
    public List<AdministrateurResponse> getAllAdministrateurs() {
        return administrateurRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Recherche des administrateurs par nom ou prénom (insensible à la casse).
     * 
     * @param query Terme de recherche pour le nom ou prénom.
     * @return Liste des administrateurs correspondant à la recherche.
     */
    public List<AdministrateurResponse> getAdministrateursByNomOuPrenom(String query) {
        return administrateurRepository.findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase(query, query)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Récupère un administrateur par son matricule (au lieu de UUID).
     * 
     * @param matricule Matricule unique de l'administrateur.
     * @return L'administrateur sous forme de DTO.
     * @throws ResponseStatusException si l'administrateur n'est pas trouvé.
     */
    public AdministrateurResponse getAdministrateurByMatricule(String matricule) {
        return administrateurRepository.findByMatricule(matricule)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Administrateur", "id", matricule));

    }

    /**
     * Met à jour un administrateur existant.
     * 
     * @param id Matricule de l'administrateur à mettre à jour.
     * @param request   Contient les champs à modifier.
     * @return L'administrateur mis à jour sous forme de DTO.
     * @throws ResponseStatusException si l'administrateur n'est pas trouvé.
     */
    public AdministrateurResponse updateAdministrateur(UUID id, AdministrateurUpdateRequest request) {
        Administrateur admin = administrateurRepository.findById(id) // Recherche par UUID
                .orElseThrow(() -> new ResourceNotFoundException("Administrateur", "id", id));

        if (request.getNom() != null)
            admin.setNom(request.getNom().trim());
        if (request.getPrenom() != null)
            admin.setPrenom(request.getPrenom().trim());
        if (request.getEmail() != null)
            admin.setEmail(request.getEmail().trim());
        if (request.getEmailPro() != null)
            admin.setEmailPro(request.getEmailPro().trim());
        if (request.getTelephone() != null)
            admin.setTelephone(request.getTelephone().trim());
        if (request.getAdresse() != null)
            admin.setAdresse(request.getAdresse().trim());
        if (request.getRole() != null)
            admin.setRole(request.getRole().trim());
        if (request.getSalaire() != null)
            admin.setSalaire(request.getSalaire());
        if (request.getStatutContrat() != null)
            admin.setStatutContrat(request.getStatutContrat());
        if (request.getDiplome() != null)
            admin.setDiplome(request.getDiplome());

        return mapToResponse(administrateurRepository.save(admin));
    }

    /**
     * Supprime un administrateur par son matricule.
     * 
     * @param id Matricule unique de l'administrateur à supprimer.
     * @throws ResponseStatusException si l'administrateur n'existe pas.
     */
    public void deleteAdministrateur(UUID id) {
        if (!administrateurRepository.existsById(id)) { // Vérification par UUID
            throw new ResourceNotFoundException("Administrateur", "id", id);
        }
        administrateurRepository.deleteById(id); // Suppression par UUID
    }

    /**
     * Convertit une entité Administrateur en DTO AdministrateurResponse.
     * 
     * @param entity L'entité administrateur.
     * @return L'objet DTO contenant les informations de l'administrateur.
     */
    private AdministrateurResponse mapToResponse(Administrateur entity) {
        return AdministrateurResponse.builder()
                .idPersonne(entity.getIdPersonne()) // UUID de la personne
                .matricule(entity.getMatricule()) // Matricule unique
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .email(entity.getEmail()) // Email personnel
                .emailPro(entity.getEmailPro()) // Email professionnel
                .telephone(entity.getTelephone())
                .adresse(entity.getAdresse())
                .role(entity.getRole())
                .salaire(entity.getSalaire())
                .statutContrat(entity.getStatutContrat())
                .diplome(entity.getDiplome())
                .dateEmbauche(entity.getDateEmbauche())
                .poste(entity.getPoste()) // Poste spécifique
                .build();
    }
}