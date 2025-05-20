package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateEmailProException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.request.EmployeCreateRequest;
import l3o2.pharmacie.api.model.dto.request.EmployeUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.EmployeResponse;
import l3o2.pharmacie.api.model.entity.Employe;
import l3o2.pharmacie.api.repository.EmployeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Service pour la gestion des employés.
 * Implémente {@link UserDetailsService} pour l'authentification Spring Security.
 * Fournit les opérations CRUD pour les employés, la gestion des mots de passe,
 * et la recherche d'employés.
 */
@Service("employeUserDetailsService")
@RequiredArgsConstructor
public class EmployeService implements UserDetailsService {

    @Autowired
    private EmployeRepository employeRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Vérifie si un employé existe avec l'email professionnel spécifié.
     *
     * @param emailPro L'email professionnel à vérifier.
     * @return {@code true} si un employé avec cet email professionnel existe, {@code false} sinon.
     */
    public boolean existsByEmailPro(String emailPro) {
        return employeRepository.existsByEmailPro(emailPro); // Cette méthode est déjà définie dans le repository
    }

    /**
     * Crée un nouvel employé.
     *
     * @param request Les informations de l'employé à créer.
     * @return Un {@link EmployeResponse} représentant l'employé créé.
     * @throws DuplicateEmailProException si l'email professionnel existe déjà.
     */
    public EmployeResponse createEmploye(EmployeCreateRequest request) {
        // Vérifier si un employé avec le même email professionnel existe déjà
        if (employeRepository.existsByEmailPro(request.getEmailPro())) {
            throw new DuplicateEmailProException(request.getEmailPro());
        }

        // Créer un employé
        Employe employe = Employe.builder()
                .nom(request.getNom())
                .prenom(request.getPrenom())
                .email(request.getEmail())
                .emailPro(request.getEmailPro())
                .telephone(request.getTelephone())
                .adresse(request.getAdresse())
                .password(passwordEncoder.encode(request.getPassword()))
                .dateEmbauche(request.getDateEmbauche())
                .salaire(request.getSalaire())
                .poste(request.getPoste())
                .statutContrat(request.getStatutContrat())
                .diplome(request.getDiplome())
                .build();

        // Générer le matricule en fonction du poste de l'employé
        String baseMatricule = employe.getPoste().toString();
        employe.generateMatricule(baseMatricule);

        // Enregistrer l'employé
        Employe savedEmploye = employeRepository.save(employe);
        return mapToResponse(savedEmploye);
    }

    /**
     * Récupère la liste de tous les employés.
     *
     * @return Une liste de {@link EmployeResponse}.
     */
    public List<EmployeResponse> getAllEmployes() {
        return employeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Récupère un employé par son identifiant unique (UUID).
     *
     * @param id L'identifiant UUID de l'employé.
     * @return Un {@link EmployeResponse} représentant l'employé trouvé.
     * @throws ResourceNotFoundException si aucun employé n'est trouvé pour l'ID fourni.
     */
    public EmployeResponse getEmployeById(UUID id) {
        return employeRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", "id", id));
    }

    /**
     * Met à jour les informations d'un employé existant.
     *
     * @param id L'identifiant UUID de l'employé à mettre à jour.
     * @param request Les nouvelles informations de l'employé.
     * @return Un {@link EmployeResponse} représentant l'employé mis à jour.
     * @throws ResourceNotFoundException si aucun employé n'est trouvé pour l'ID fourni.
     */
    public EmployeResponse updateEmploye(UUID id, EmployeUpdateRequest request) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", "id", id));
        updateEmployeFields(employe, request);
        Employe updatedEmploye = employeRepository.save(employe);
        return mapToResponse(updatedEmploye);
    }

    /**
     * Met à jour l'email personnel d'un employé.
     *
     * @param id L'identifiant UUID de l'employé.
     * @param email Le nouvel email personnel.
     * @return Un {@link EmployeResponse} représentant l'employé mis à jour.
     */
    public EmployeResponse updateEmployeEmail(UUID id, String email) {
        return updateEmploye(id, EmployeUpdateRequest.builder().email(email).build());
    }

    /**
     * Met à jour l'email professionnel d'un employé.
     *
     * @param id L'identifiant UUID de l'employé.
     * @param emailPro Le nouvel email professionnel.
     * @return Un {@link EmployeResponse} représentant l'employé mis à jour.
     */
    public EmployeResponse updateEmployeEmailPro(UUID id, String emailPro) {
        return updateEmploye(id, EmployeUpdateRequest.builder().email(emailPro).build());
    }

    /**
     * Met à jour le mot de passe d'un employé.
     *
     * @param id L'identifiant UUID de l'employé.
     * @param oldPwd L'ancien mot de passe.
     * @param newPwd1 Le nouveau mot de passe.
     * @param newPwd2 Confirmation du nouveau mot de passe.
     * @return Un {@link EmployeResponse} représentant l'employé avec le mot de passe mis à jour.
     * @throws ResourceNotFoundException si l'employé n'est pas trouvé.
     * @throws ResponseStatusException si l'ancien mot de passe est incorrect ou si les nouveaux mots de passe ne correspondent pas.
     */
    public EmployeResponse updateEmployePassword(UUID id, String oldPwd, String newPwd1, String newPwd2) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", "id", id));
        if (passwordEncoder.matches(oldPwd, employe.getPassword())) {
            if (newPwd1.equals(newPwd2)) {
                employe.setPassword(passwordEncoder.encode(newPwd1));
                Employe updatedEmploye = employeRepository.save(employe);
                return mapToResponse(updatedEmploye);
            } else {
                throw new ResponseStatusException(NOT_FOUND, "Problème mdp : nouveaux mdp ne matchent pas");
            }

        } else {
            throw new ResponseStatusException(NOT_FOUND, "Problème mdp : ancien mot de passe incorrect");
        }
    }

    /**
     * Recherche un employé par son email professionnel et retourne l'entité {@link Employe}.
     *
     * @param emailPro L'email professionnel de l'employé.
     * @return L'entité {@link Employe} correspondante.
     * @throws ResourceNotFoundException si aucun employé n'est trouvé pour l'email professionnel fourni.
     */
    public Employe getEmployeByEmailPro(String emailPro) {
        return employeRepository.findByEmailPro(emailPro)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", "Email Pro", emailPro));
    }

    /**
     * Récupère l'identifiant (UUID) d'un employé par son email professionnel.
     *
     * @param emailPro L'email professionnel de l'employé.
     * @return L'UUID de l'employé.
     * @throws ResourceNotFoundException si aucun employé n'est trouvé pour l'email professionnel fourni.
     */
    public UUID getEmployeIdByEmailPro(String emailPro) {
        Employe employe = employeRepository.findByEmailPro(emailPro)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", "Email Pro", emailPro));
        return employe.getIdPersonne();
    }

    /**
     * Met à jour les champs spécifiés d'une entité {@link Employe} à partir d'un {@link EmployeUpdateRequest}.
     *
     * @param employe L'entité employé à mettre à jour.
     * @param request Le DTO contenant les champs à mettre à jour.
     */
    private void updateEmployeFields(Employe employe, EmployeUpdateRequest request) {
        if (request.getNom() != null)
            employe.setNom(request.getNom());
        if (request.getPrenom() != null)
            employe.setPrenom(request.getPrenom());
        if (request.getEmail() != null)
            employe.setEmail(request.getEmail());
        if (request.getEmailPro() != null)
            employe.setEmailPro(request.getEmailPro());
        if (request.getTelephone() != null)
            employe.setTelephone(request.getTelephone());
        if (request.getAdresse() != null)
            employe.setAdresse(request.getAdresse());
        if (request.getSalaire() != null)
            employe.setSalaire(request.getSalaire());
        if (request.getStatutContrat() != null)
            employe.setStatutContrat(request.getStatutContrat());
    }

    /**
     * Supprime un employé par son matricule.
     *
     * @param matricule Le matricule de l'employé à supprimer.
     * @throws ResourceNotFoundException si aucun employé n'est trouvé pour le matricule fourni.
     */
    public void deleteEmploye(String matricule) {
        // Cherche l'employé par matricule dans toutes les sous-classes
        Employe employe = employeRepository.findByMatricule(matricule)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", "Matricule", matricule));

        // Supprime l'employé
        employeRepository.delete(employe);
        System.out.println("Employé avec matricule '" + matricule + "' supprimé avec succès.");
    }

    /**
     * Convertit une entité {@link Employe} en un DTO {@link EmployeResponse}.
     *
     * @param employe L'entité employé à convertir.
     * @return Le DTO {@link EmployeResponse} correspondant.
     */
    private EmployeResponse mapToResponse(Employe employe) {
        return EmployeResponse.builder()
                .idPersonne(employe.getIdPersonne())
                .matricule(employe.getMatricule())
                .nom(employe.getNom())
                .prenom(employe.getPrenom())
                .email(employe.getEmail())
                .emailPro(employe.getEmailPro())
                .telephone(employe.getTelephone())
                .adresse(employe.getAdresse())
                .dateEmbauche(employe.getDateEmbauche())
                .salaire(employe.getSalaire())
                .poste(employe.getPoste())
                .statutContrat(employe.getStatutContrat())
                .diplome(employe.getDiplome())
                .build();
    }

    /**
     * Compte le nombre total d'employés enregistrés.
     *
     * @return Le nombre total d'employés.
     */
    public long countAllEmployes() {
        return employeRepository.count();
    }

    /**
     * Charge un utilisateur par son nom d'utilisateur (ici, l'email professionnel).
     * Méthode requise par l'interface {@link UserDetailsService} pour Spring Security.
     *
     * @param username Le nom d'utilisateur (email professionnel) de l'employé.
     * @return Un objet {@link UserDetails} représentant l'employé.
     * @throws UsernameNotFoundException si aucun employé n'est trouvé pour le nom d'utilisateur fourni.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return employeRepository.findByEmailPro(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}