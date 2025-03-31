package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.FournisseurCreateRequest;
import l3o2.pharmacie.api.model.dto.response.FournisseurResponse;
import l3o2.pharmacie.api.model.entity.Fournisseur;
import l3o2.pharmacie.api.repository.FournisseurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

/**
 * Service pour gérer les fournisseurs.
 * Contient la logique métier pour la gestion des fournisseurs.
 */
@Service
@RequiredArgsConstructor
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;

    /**
     * Création d'un nouveau fournisseur.
     * @param request Données du fournisseur à créer.
     * @return FournisseurResponse contenant les informations du fournisseur créé.
     */
    public FournisseurResponse createFournisseur(FournisseurCreateRequest request) {
        // Vérification de l'unicité de l'email
        if (fournisseurRepository.findByEmail(request.getEmail().trim().toLowerCase()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "L'email est déjà utilisé par un autre fournisseur.");
        }

        // Vérification de l'unicité du téléphone
        if (fournisseurRepository.findByTelephone(request.getTelephone().trim()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Le téléphone est déjà utilisé par un autre fournisseur.");
        }

        // Création de l'objet Fournisseur (hérite de Personne)
        Fournisseur fournisseur = Fournisseur.builder()
                .societe(request.getSociete().trim())
                .sujetFonction(request.getSujetFonction() != null ? request.getSujetFonction().trim() : null)
                .fax(request.getFax() != null ? request.getFax().trim() : null)
                .nom(request.getNom().trim())
                .prenom(request.getPrenom().trim())
                .email(request.getEmail().trim().toLowerCase())
                .telephone(request.getTelephone().replaceAll("\\s+", ""))
                .adresse(request.getAdresse().trim())
                .build();

        try {
            Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);
            return mapToResponse(savedFournisseur);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur lors de l'enregistrement du fournisseur.");
        }
    }

    /**
     * Récupère tous les fournisseurs enregistrés.
     * @return Liste des fournisseurs sous forme de FournisseurResponse.
     */
    public List<FournisseurResponse> getAllFournisseurs() {
        return fournisseurRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Recherche un fournisseur par son identifiant UUID.
     * @param id Identifiant du fournisseur.
     * @return FournisseurResponse si trouvé.
     * @throws ResponseStatusException si le fournisseur n'est pas trouvé.
     */
    public FournisseurResponse getFournisseurById(UUID id) {
        return fournisseurRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fournisseur non trouvé."));
    }

    /**
     * Recherche un fournisseur par son email.
     * @param email Email du fournisseur.
     * @return FournisseurResponse si trouvé.
     * @throws ResponseStatusException si le fournisseur n'est pas trouvé.
     */
    public FournisseurResponse getFournisseurByEmail(String email) {
        return fournisseurRepository.findByEmail(email.trim().toLowerCase())
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fournisseur non trouvé avec cet email."));
    }

    /**
     * Recherche un fournisseur par son téléphone.
     * @param telephone Numéro de téléphone du fournisseur.
     * @return FournisseurResponse si trouvé.
     * @throws ResponseStatusException si le fournisseur n'est pas trouvé.
     */
    public FournisseurResponse getFournisseurByTelephone(String telephone) {
        return fournisseurRepository.findByTelephone(telephone.trim())
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fournisseur non trouvé avec ce téléphone."));
    }

    /**
     * Convertit une entité Fournisseur en DTO FournisseurResponse.
     * @param entity L'entité fournisseur.
     * @return FournisseurResponse.
     */
    private FournisseurResponse mapToResponse(Fournisseur entity) {
        return FournisseurResponse.builder()
                .idPersonne(entity.getIdPersonne())
                .societe(entity.getSociete())
                .sujetFonction(entity.getSujetFonction())
                .fax(entity.getFax())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .email(entity.getEmail())
                .telephone(entity.getTelephone())
                .adresse(entity.getAdresse())
                .build();
    }
}