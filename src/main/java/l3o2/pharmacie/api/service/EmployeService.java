package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.EmployeCreateRequest;
import l3o2.pharmacie.api.model.dto.request.EmployeUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.EmployeResponse;
import l3o2.pharmacie.api.model.entity.Employe;
import l3o2.pharmacie.api.repository.EmployeRepository;
import lombok.RequiredArgsConstructor;
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
    public EmployeResponse createEmploye(EmployeCreateRequest request) {
        
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
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Employé non trouvé"));
    }

    public EmployeResponse updateEmploye(UUID id, EmployeUpdateRequest request) {
        Employe employe = employeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Employé non trouvé"));

        updateEmployeFields(employe, request);
        Employe updatedEmploye = employeRepository.save(employe);
        return mapToResponse(updatedEmploye);
    }

    public void deleteEmploye(String matricule) {
        Employe employe = employeRepository.findByMatricule(matricule)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Employé non trouvé"));
        employeRepository.delete(employe);
    }


    private void updateEmployeFields(Employe employe, EmployeUpdateRequest request) {
        if (request.getNom() != null) employe.setNom(request.getNom());
        if (request.getPrenom() != null) employe.setPrenom(request.getPrenom());
        if (request.getEmail() != null) employe.setEmail(request.getEmail());
        if (request.getEmailPro() != null) employe.setEmailPro(request.getEmailPro());
        if (request.getTelephone() != null) employe.setTelephone(request.getTelephone());
        if (request.getAdresse() != null) employe.setAdresse(request.getAdresse());
        if (request.getSalaire() != null) employe.setSalaire(request.getSalaire());
        if (request.getStatutContrat() != null) employe.setStatutContrat(request.getStatutContrat());
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