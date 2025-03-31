package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointCreateRequest;
import l3o2.pharmacie.api.model.dto.response.PharmacienAdjointResponse;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import l3o2.pharmacie.api.repository.PharmacienAdjointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Service gérant la logique métier des pharmaciens adjoints.
 * Il fournit des méthodes pour la création, récupération et gestion des pharmaciens adjoints.
 */
@Service
@RequiredArgsConstructor
public class PharmacienAdjointService {

    private final PharmacienAdjointRepository pharmacienAdjointRepository;

    /**
     * Création d'un pharmacien adjoint en base de données.
     * @param request Contient les informations du pharmacien adjoint à créer.
     * @return Pharmacien adjoint créé sous forme de DTO.
     */
    public PharmacienAdjointResponse createPharmacienAdjoint(PharmacienAdjointCreateRequest request) {
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
                .diplome(request.getDiplome())
                .emailPro(request.getEmailPro().trim())
                .matricule(request.getMatricule().trim())
                .build();

        try {
            PharmacienAdjoint savedPharmacien = pharmacienAdjointRepository.save(pharmacien);
            return mapToResponse(savedPharmacien);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données dupliquées ou invalides");
        }
    }

    /**
     * Convertit une entité PharmacienAdjoint en DTO PharmacienAdjointResponse.
     * @param entity L'entité PharmacienAdjoint.
     * @return L'objet DTO contenant les informations du pharmacien adjoint.
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
     * Récupère la liste de tous les pharmaciens adjoints.
     * @return Liste de DTOs représentant les pharmaciens adjoints.
     */
    public List<PharmacienAdjointResponse> getAllPharmaciensAdjoints() {
        return pharmacienAdjointRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }
}