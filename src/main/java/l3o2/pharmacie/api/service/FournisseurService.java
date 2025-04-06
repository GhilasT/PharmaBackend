package l3o2.pharmacie.api.service;

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
     * 
     * @param request Données du fournisseur à créer.
     * @return FournisseurResponse contenant les informations du fournisseur créé.
     */
    public FournisseurResponse createFournisseur(FournisseurCreateRequest request) {
        // Vérification de l'unicité de la société (avec Nomsociete)
        if (fournisseurRepository.findByNomSociete(request.getNomSociete().trim()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "La société est déjà utilisée par un autre fournisseur.");
        }

        // Vérification de l'unicité de l'email
        if (fournisseurRepository.findByEmail(request.getEmail().trim().toLowerCase()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "L'email est déjà utilisé par un autre fournisseur.");
        }

        // Vérification de l'unicité du téléphone
        if (fournisseurRepository.findByTelephone(request.getTelephone().trim()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Le téléphone est déjà utilisé par un autre fournisseur.");
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Erreur lors de l'enregistrement du fournisseur.");
        }
    }

    public FournisseurResponse updateFournisseur(UUID id, FournisseurUpdateRequest request) {
        Fournisseur existing = fournisseurRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fournisseur non trouvé."));

        if (request.getNomSociete() != null) {
            String newNomSociete = request.getNomSociete().trim();
            if (!newNomSociete.equals(existing.getNomSociete())) {
                if (fournisseurRepository.findByNomSociete(newNomSociete).isPresent()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "La société est déjà utilisée par un autre fournisseur.");
                }
                existing.setNomSociete(newNomSociete);
            }
        }

        if (request.getEmail() != null) {
            String newEmail = request.getEmail().trim().toLowerCase();
            if (!newEmail.equals(existing.getEmail())) {
                if (fournisseurRepository.findByEmail(newEmail).isPresent()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "L'email est déjà utilisé par un autre fournisseur.");
                }
                existing.setEmail(newEmail);
            }
        }

        if (request.getTelephone() != null) {
            String newTelephone = request.getTelephone().replaceAll("\\s+", "");
            if (!newTelephone.equals(existing.getTelephone())) {
                if (fournisseurRepository.findByTelephone(newTelephone).isPresent()) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT,
                            "Le téléphone est déjà utilisé par un autre fournisseur.");
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
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Erreur lors de la mise à jour du fournisseur.");
        }
    }

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
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Fournisseur non trouvé."));
    }

    /**
     * Recherche un fournisseur par son email.
     * 
     * @param email Email du fournisseur.
     * @return FournisseurResponse si trouvé.
     * @throws ResponseStatusException si le fournisseur n'est pas trouvé.
     */
    public FournisseurResponse getFournisseurByEmail(String email) {
        return fournisseurRepository.findByEmail(email.trim().toLowerCase())
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Fournisseur non trouvé avec cet email."));
    }

    /**
     * Recherche un fournisseur par son téléphone.
     * 
     * @param telephone Numéro de téléphone du fournisseur.
     * @return FournisseurResponse si trouvé.
     * @throws ResponseStatusException si le fournisseur n'est pas trouvé.
     */
    public FournisseurResponse getFournisseurByTelephone(String telephone) {
        return fournisseurRepository.findByTelephone(telephone.trim())
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Fournisseur non trouvé avec ce téléphone."));
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

    // suprimer un fournisssseur
    public void deleteFournisseur(UUID id) {
        if (!fournisseurRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Fournisseur non trouvé.");
        }
        fournisseurRepository.deleteById(id);
    }

    public List<FournisseurResponse> searchFournisseurs(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le paramètre de recherche ne peut pas être vide.");
        }
        
        return fournisseurRepository.searchFournisseurs(query.trim().toLowerCase())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }
}