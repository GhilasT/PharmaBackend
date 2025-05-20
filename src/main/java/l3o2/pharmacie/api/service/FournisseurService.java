package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateEmailException;
import l3o2.pharmacie.api.exceptions.DuplicateSocieteException;
import l3o2.pharmacie.api.exceptions.DuplicateTelephoneException;
import l3o2.pharmacie.api.exceptions.InvalidDataException;
import l3o2.pharmacie.api.exceptions.InvalidOperationException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.request.FournisseurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.FournisseurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.FournisseurResponse;
import l3o2.pharmacie.api.model.entity.Fournisseur;
import l3o2.pharmacie.api.repository.FournisseurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import l3o2.pharmacie.api.exceptions.InvalidParameterException;
import java.util.List;
import java.util.UUID;

/**
 * Service pour gérer les fournisseurs.
 * Contient la logique métier pour la création, la mise à jour, la suppression,
 * la recherche et la récupération des informations des fournisseurs.
 */
@Service
@RequiredArgsConstructor
public class FournisseurService {

    private final FournisseurRepository fournisseurRepository;

    /**
     * Création d'un nouveau fournisseur.
     * 
     * @param request Données du fournisseur à créer.
     * @return {@link FournisseurResponse} contenant les informations du fournisseur créé.
     * @throws DuplicateSocieteException si le nom de la société existe déjà.
     * @throws DuplicateEmailException si l'email existe déjà.
     * @throws DuplicateTelephoneException si le numéro de téléphone existe déjà.
     * @throws InvalidDataException en cas d'erreur lors de la persistance des données.
     */
    public FournisseurResponse createFournisseur(FournisseurCreateRequest request) {
        // Vérification de l'unicité de la société (avec Nomsociete)
        if (fournisseurRepository.findByNomSociete(request.getNomSociete().trim()).isPresent()) {
            throw new DuplicateSocieteException(request.getNomSociete().trim());
        }

        // Vérification de l'unicité de l'email
        if (fournisseurRepository.findByEmail(request.getEmail().trim().toLowerCase()).isPresent()) {
            throw new DuplicateEmailException(request.getEmail());
        }

        // Vérification de l'unicité du téléphone
        if (fournisseurRepository.findByTelephone(request.getTelephone().trim()).isPresent()) {
            throw new DuplicateTelephoneException(request.getTelephone().trim());
        }

        // Création du fournisseur
        Fournisseur fournisseur = Fournisseur.builder()
                .nomSociete(request.getNomSociete().trim())
                .sujetFonction(request.getSujetFonction() != null ? request.getSujetFonction().trim() : null)
                .fax(request.getFax() != null ? request.getFax().trim() : null)
                .email(request.getEmail().trim().toLowerCase())
                .telephone(request.getTelephone().replaceAll("\\s+", ""))
                .adresse(request.getAdresse().trim())
                .build();

        // Enregistrement du fournisseur
        try {
            Fournisseur savedFournisseur = fournisseurRepository.save(fournisseur);
            return mapToResponse(savedFournisseur);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Erreur lors de la création du fournisseur : " + e.getMessage());
        }
    }

    /**
     * Met à jour un fournisseur existant.
     *
     * @param id L'identifiant UUID du fournisseur à mettre à jour.
     * @param request Les données de mise à jour du fournisseur.
     * @return {@link FournisseurResponse} contenant les informations du fournisseur mis à jour.
     * @throws ResourceNotFoundException si le fournisseur avec l'ID spécifié n'est pas trouvé.
     * @throws DuplicateSocieteException si le nouveau nom de société existe déjà pour un autre fournisseur.
     * @throws DuplicateEmailException si le nouvel email existe déjà pour un autre fournisseur.
     * @throws DuplicateTelephoneException si le nouveau numéro de téléphone existe déjà pour un autre fournisseur.
     * @throws InvalidDataException en cas d'erreur lors de la persistance des données.
     */
    public FournisseurResponse updateFournisseur(UUID id, FournisseurUpdateRequest request) {
        Fournisseur existing = fournisseurRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Fournisseur","id",id));   

        if (request.getNomSociete() != null) {
            String newNomSociete = request.getNomSociete().trim();
            if (!newNomSociete.equals(existing.getNomSociete())) {
                if (fournisseurRepository.findByNomSociete(newNomSociete).isPresent()) {
                    throw new DuplicateSocieteException(newNomSociete);
                }
                existing.setNomSociete(newNomSociete);
            }
        }

        if (request.getEmail() != null) {
            String newEmail = request.getEmail().trim().toLowerCase();
            if (!newEmail.equals(existing.getEmail())) {
                if (fournisseurRepository.findByEmail(newEmail).isPresent()) {
                    throw new DuplicateEmailException(newEmail);
                }
                existing.setEmail(newEmail);
            }
        }

        if (request.getTelephone() != null) {
            String newTelephone = request.getTelephone().replaceAll("\\s+", "");
            if (!newTelephone.equals(existing.getTelephone())) {
                if (fournisseurRepository.findByTelephone(newTelephone).isPresent()) {
                    throw new DuplicateTelephoneException(newTelephone);
                }
                existing.setTelephone(newTelephone);
            }
        }

        if (request.getSujetFonction() != null) {
            existing.setSujetFonction(request.getSujetFonction().trim());
        }

        if (request.getFax() != null) {
            existing.setFax(request.getFax().trim());
        }

        if (request.getAdresse() != null) {
            existing.setAdresse(request.getAdresse().trim());
        }

        try {
            Fournisseur updated = fournisseurRepository.save(existing);
            return mapToResponse(updated);
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Erreur lors de la mise à jour du fournisseur : " + e.getMessage());
        }
    }

    /**
     * Vérifie si un fournisseur existe avec l'email spécifié.
     *
     * @param email L'email à vérifier.
     * @return {@code true} si un fournisseur avec cet email existe, {@code false} sinon.
     */
    public boolean existsByEmail(String email) {
        return fournisseurRepository.existsByEmail(email.trim().toLowerCase());
    }

    /**
     * Récupère tous les fournisseurs enregistrés.
     * 
     * @return Liste des fournisseurs sous forme de FournisseurResponse.
     */
    public List<FournisseurResponse> getAllFournisseurs() {
        return fournisseurRepository.findAll().stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Recherche un fournisseur par son identifiant UUID.
     * 
     * @param id Identifiant du fournisseur.
     * @return FournisseurResponse si trouvé.
     * @throws ResponseStatusException si le fournisseur n'est pas trouvé.
     */
    public FournisseurResponse getFournisseurById(UUID id) {
        return fournisseurRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Fournisseur","id",id));   
            }

    /**
     * Recherche un fournisseur par son email.
     * 
     * @param email Email du fournisseur.
     * @return {@link FournisseurResponse} si trouvé.
     * @throws DuplicateEmailException si le fournisseur n'est pas trouvé (note: l'exception semble mal nommée ici, devrait être ResourceNotFoundException).
     */
    public FournisseurResponse getFournisseurByEmail(String email) {
        return fournisseurRepository.findByEmail(email.trim().toLowerCase())
                .map(this::mapToResponse)
                .orElseThrow(() -> new DuplicateEmailException(email.trim()));
    }

    /**
     * Recherche un fournisseur par son téléphone.
     * 
     * @param telephone Numéro de téléphone du fournisseur.
     * @return {@link FournisseurResponse} si trouvé.
     * @throws DuplicateTelephoneException si le fournisseur n'est pas trouvé (note: l'exception semble mal nommée ici, devrait être ResourceNotFoundException).
     */
    public FournisseurResponse getFournisseurByTelephone(String telephone) {
        return fournisseurRepository.findByTelephone(telephone.trim())
                .map(this::mapToResponse)
                .orElseThrow(() -> new DuplicateTelephoneException(telephone.trim()));
    }

    /**
     * Convertit une entité Fournisseur en DTO FournisseurResponse.
     * 
     * @param entity L'entité fournisseur.
     * @return FournisseurResponse.
     */
    private FournisseurResponse mapToResponse(Fournisseur entity) {
        return FournisseurResponse.builder()
                .idFournisseur(entity.getIdFournisseur())
                .nomSociete(entity.getNomSociete())
                .sujetFonction(entity.getSujetFonction())
                .fax(entity.getFax())
                .email(entity.getEmail())
                .telephone(entity.getTelephone())
                .adresse(entity.getAdresse())
                .build();
    }

    /**
     * Supprime un fournisseur par son identifiant UUID.
     *
     * @param id L'identifiant UUID du fournisseur à supprimer.
     * @throws ResourceNotFoundException si le fournisseur avec l'ID spécifié n'est pas trouvé.
     */
    public void deleteFournisseur(UUID id) {
        if (!fournisseurRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fournisseur","id",id);   

        }
        fournisseurRepository.deleteById(id);
    }

    /**
     * Recherche des fournisseurs en fonction d'une requête (nom de société, email, etc.).
     *
     * @param query Le terme de recherche.
     * @return Une liste de {@link FournisseurResponse} correspondant aux critères de recherche.
     * @throws InvalidParameterException si le paramètre de recherche est vide ou nul.
     */
    public List<FournisseurResponse> searchFournisseurs(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new InvalidParameterException("Le paramètre de recherche ne peut pas être vide.");
        }
        
        return fournisseurRepository.searchFournisseurs(query.trim().toLowerCase())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}