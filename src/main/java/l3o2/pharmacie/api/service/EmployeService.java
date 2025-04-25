package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateEmailProException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.request.EmployeCreateRequest;
import l3o2.pharmacie.api.model.dto.request.EmployeUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.EmployeResponse;
import l3o2.pharmacie.api.model.entity.Employe;
import l3o2.pharmacie.api.repository.EmployeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor
public class EmployeService {

    private final EmployeRepository employeRepository;
    private final PasswordEncoder passwordEncoder;

    // Ajoutez cette méthode pour vérifier si un employé existe avec cet email
    // professionnel
    public boolean existsByEmailPro(String emailPro) {
        return employeRepository.existsByEmailPro(emailPro); // Cette méthode est déjà définie dans le repository
    }

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

    public List<EmployeResponse> getAllEmployes() {
        return employeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public EmployeResponse getEmployeById(UUID id) {
        return employeRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", "id", id));
    }

    public EmployeResponse updateEmploye(UUID id, EmployeUpdateRequest request) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", "id", id));
        updateEmployeFields(employe, request);
        Employe updatedEmploye = employeRepository.save(employe);
        return mapToResponse(updatedEmploye);
    }

    public EmployeResponse updateEmployeEmail(UUID id,String email) {
        return updateEmploye(id, EmployeUpdateRequest.builder().email(email).build());
    }

    public EmployeResponse updateEmployeEmailPro(UUID id,String emailPro) {
        return updateEmploye(id, EmployeUpdateRequest.builder().email(emailPro).build());
    }

    public EmployeResponse updateEmployePassword(UUID id,String oldPwd, String newPwd1, String newPwd2) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", "id", id));
        if (passwordEncoder.matches(oldPwd, employe.getPassword())) {
            if (newPwd1.equals(newPwd2)) {
                employe.setPassword(passwordEncoder.encode(newPwd1));
                Employe updatedEmploye = employeRepository.save(employe);
                return mapToResponse(updatedEmploye);
            }
            else{
                throw new ResponseStatusException(NOT_FOUND, "Problème mdp : nouveaux mdp ne matchent pas");
            }

        }
        else{
            throw new ResponseStatusException(NOT_FOUND, "Problème mdp : ancien mot de passe incorrect");
        }
    }

    /**
     * Recherche un employé par son email professionnel et retourne l'employe
     */
    public Employe getEmployeByEmailPro(String emailPro) {
        return employeRepository.findByEmailPro(emailPro)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", "Email Pro", emailPro));
    }

    // et ici je retourne que le ID de l'employe

    public UUID getEmployeIdByEmailPro(String emailPro) {
        Employe employe = employeRepository.findByEmailPro(emailPro)
                .orElseThrow(() -> new ResourceNotFoundException("Employé", "Email Pro", emailPro));
        return employe.getIdPersonne();
    }

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

    public void deleteEmploye(String matricule) {
        // Cherche l'employé par matricule dans toutes les sous-classes
        Employe employe = employeRepository.findByMatricule(matricule)
        .orElseThrow(() -> new ResourceNotFoundException("Employé","Matricule",matricule));   


        // Supprime l'employé
        employeRepository.delete(employe);
        System.out.println("Employé avec matricule '" + matricule + "' supprimé avec succès.");
    }

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
}