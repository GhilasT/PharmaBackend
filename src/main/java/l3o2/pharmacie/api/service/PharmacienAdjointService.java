package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateEmailProException;
import l3o2.pharmacie.api.exceptions.InvalidDataException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointCreateRequest;
import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.PharmacienAdjointResponse;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import l3o2.pharmacie.api.repository.EmployeRepository;
import l3o2.pharmacie.api.repository.PharmacienAdjointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * Service gérant la logique métier des pharmaciens adjoints.
 * Il fournit des méthodes pour la création, récupération, mise à jour et suppression des
 * pharmaciens adjoints.
 */
@Service
@RequiredArgsConstructor
public class PharmacienAdjointService {

    private final PharmacienAdjointRepository pharmacienAdjointRepository;
    private final EmployeRepository employeRepository; // Ce champ n'est pas utilisé directement, EmployeService l'est.
    private final EmployeService employeService;

    /**
     * Crée un nouveau pharmacien adjoint en base de données.
     * Vérifie l'unicité de l'email professionnel et génère un matricule.
     * @param request Contient les informations du pharmacien adjoint à créer.
     * @return {@link PharmacienAdjointResponse} Le pharmacien adjoint créé sous forme de DTO.
     * @throws DuplicateEmailProException si l'email professionnel existe déjà.
     * @throws InvalidDataException en cas de données invalides ou de violation de contraintes.
     */
    public PharmacienAdjointResponse createPharmacienAdjoint(PharmacienAdjointCreateRequest request) {
        // Vérifier si un pharmacien adjoint avec le même email professionnel existe
        // déjà
        if (employeService.existsByEmailPro(request.getEmailPro().trim())) {
            throw new DuplicateEmailProException(request.getEmailPro());
        }

        // Créer un pharmacien adjoint
        PharmacienAdjoint pharmacien = PharmacienAdjoint.builder()
                .nom(request.getNom().trim())
                .prenom(request.getPrenom().trim())
                .email(request.getEmail().toLowerCase().trim())
                .telephone(request.getTelephone().replaceAll("\\s+", ""))
                .adresse(request.getAdresse().trim())
                .password(request.getPassword())
                .dateEmbauche(request.getDateEmbauche())
                .salaire(request.getSalaire())
                .poste(request.getPoste())
                .statutContrat(request.getStatutContrat())
                .emailPro(request.getEmailPro().trim())
                .permissions(List.of("VENDRE", "COMMANDER")) // liste permissions backend
                .build();

        // Générer le matricule automatiquement en fonction du poste
        String baseMatricule = pharmacien.getPoste().toString();
        pharmacien.generateMatricule(baseMatricule);

        // Sauvegarder le pharmacien adjoint dans la base de données
        try {
            PharmacienAdjoint savedPharmacien = pharmacienAdjointRepository.save(pharmacien);
            return mapToResponse(savedPharmacien);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Données invalides ou contraintes violées");
        }
    }

    /**
     * Convertit une entité {@link PharmacienAdjoint} en DTO {@link PharmacienAdjointResponse}.
     * @param entity L'entité PharmacienAdjoint.
     * @return L'objet DTO {@link PharmacienAdjointResponse} contenant les informations du pharmacien adjoint.
     */
    private PharmacienAdjointResponse mapToResponse(PharmacienAdjoint entity) {
        return PharmacienAdjointResponse.builder()
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

    /**
     * Recherche un pharmacien adjoint par son email professionnel.
     * @param emailPro L'email professionnel du pharmacien adjoint.
     * @return Le {@link PharmacienAdjointResponse} du pharmacien adjoint trouvé.
     * @throws ResourceNotFoundException si aucun pharmacien adjoint n'est trouvé avec cet email.
     */
    public PharmacienAdjointResponse findByEmailPro(String emailPro) {
        PharmacienAdjoint pharmacien = pharmacienAdjointRepository
                .findByEmailPro(emailPro.trim())
                .orElseThrow(() -> new ResourceNotFoundException("PharmacienAdjoint", "Email Pro", emailPro));

        return mapToResponse(pharmacien);
    }

    /**
     * Récupère un pharmacien adjoint par son ID.
     * @param id Identifiant du pharmacien adjoint.
     * @return {@link PharmacienAdjointResponse} Le pharmacien adjoint trouvé sous forme de DTO.
     * @throws ResponseStatusException avec le statut NOT_FOUND si aucun pharmacien adjoint n'est trouvé.
     * @author raphaelcharoze
     */
    public PharmacienAdjointResponse findById(UUID id) {
        PharmacienAdjoint pharmacien = pharmacienAdjointRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Aucun pharmacien adjoint trouvé avec l'ID: " + id));
        return mapToResponse(pharmacien);
    }

    /**
     * Récupère la liste de tous les pharmaciens adjoints.
     * @return Liste de {@link PharmacienAdjointResponse} représentant les pharmaciens adjoints.
     */
    public List<PharmacienAdjointResponse> getAllPharmaciensAdjoints() {
        return pharmacienAdjointRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Supprime un pharmacien adjoint par son identifiant.
     * @param id L'identifiant UUID du pharmacien adjoint à supprimer.
     * @throws ResourceNotFoundException si aucun pharmacien adjoint n'est trouvé avec cet ID.
     */
    public void deletePharmacienAdjoint(UUID id) {
        if (!pharmacienAdjointRepository.existsById(id)) {
            throw new ResourceNotFoundException("PharmacienAdjoint", "id", id);

        }
        pharmacienAdjointRepository.deleteById(id);
    }

    /**
     * Met à jour les informations d'un pharmacien adjoint existant.
     * @param id L'identifiant UUID du pharmacien adjoint à mettre à jour.
     * @param request Les données de mise à jour du pharmacien adjoint.
     * @return Le {@link PharmacienAdjointResponse} du pharmacien adjoint mis à jour.
     * @throws ResourceNotFoundException si aucun pharmacien adjoint n'est trouvé avec cet ID.
     * @throws DuplicateEmailProException si le nouvel email professionnel existe déjà pour un autre employé.
     */
    public PharmacienAdjointResponse updatePharmacienAdjoint(UUID id, PharmacienAdjointUpdateRequest request) {
        PharmacienAdjoint pharmacien = pharmacienAdjointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PharmacienAdjoint", "id", id));

        // Mise à jour des champs
        if (request.getNom() != null)
            pharmacien.setNom(request.getNom().trim());
        if (request.getPrenom() != null)
            pharmacien.setPrenom(request.getPrenom().trim());
        if (request.getEmail() != null)
            pharmacien.setEmail(request.getEmail().toLowerCase().trim());
        if (request.getTelephone() != null)
            pharmacien.setTelephone(request.getTelephone().replaceAll("\\s+", ""));
        if (request.getAdresse() != null)
            pharmacien.setAdresse(request.getAdresse().trim());
        if (request.getPassword() != null)
            pharmacien.setPassword(request.getPassword());
        if (request.getDateEmbauche() != null)
            pharmacien.setDateEmbauche(request.getDateEmbauche());
        if (request.getSalaire() != null)
            pharmacien.setSalaire(request.getSalaire());
        if (request.getPoste() != null)
            pharmacien.setPoste(request.getPoste());
        if (request.getStatutContrat() != null)
            pharmacien.setStatutContrat(request.getStatutContrat());
        if (request.getDiplome() != null)
            pharmacien.setDiplome(request.getDiplome());

        if (request.getEmailPro() != null) {
            if (!pharmacien.getEmailPro().equals(request.getEmailPro())
                    && employeService.existsByEmailPro(request.getEmailPro().trim())) {
                throw new DuplicateEmailProException(request.getEmailPro());
            }
            pharmacien.setEmailPro(request.getEmailPro().trim());
        }

        PharmacienAdjoint updated = pharmacienAdjointRepository.save(pharmacien);
        return mapToResponse(updated);
    }

    /**
     * Recherche des pharmaciens adjoints par leur nom ou prénom.
     * La recherche est insensible à la casse et ignore les espaces de début/fin.
     * @param searchTerm Le terme de recherche pour le nom ou prénom.
     * @return Une liste de {@link PharmacienAdjointResponse} correspondant aux critères de recherche.
     */
    public List<PharmacienAdjointResponse> searchPharmaciensAdjoints(String searchTerm) {
        String normalizedTerm = searchTerm.toLowerCase().trim();
        return pharmacienAdjointRepository.searchByNomPrenom(normalizedTerm).stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Récupère un pharmacien adjoint par son identifiant unique.
     * @param id L'identifiant UUID du pharmacien adjoint.
     * @return Le {@link PharmacienAdjointResponse} du pharmacien adjoint trouvé.
     * @throws ResourceNotFoundException si aucun pharmacien adjoint n'est trouvé avec cet ID.
     */
    public PharmacienAdjointResponse getPharmacienAdjointById(UUID id) {
        PharmacienAdjoint pharmacien = pharmacienAdjointRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("PharmacienAdjoint", "id", id));
        return mapToResponse(pharmacien);
    }
}