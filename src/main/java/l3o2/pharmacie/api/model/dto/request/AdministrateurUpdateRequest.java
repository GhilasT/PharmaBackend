package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.Email;

import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la mise à jour des informations d'un administrateur.
 * Permet de modifier les attributs d'un administrateur existant.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministrateurUpdateRequest {
    /**
     * Le nom de l'administrateur.
     */
    private String nom;
    /**
     * Le prénom de l'administrateur.
     */
    private String prenom;
    /**
     * L'adresse email personnelle de l'administrateur. Doit être un format d'email valide.
     */
    @Email 
    private String email;
    /**
     * Le numéro de téléphone de l'administrateur.
     */
    private String telephone;
    /**
     * L'adresse postale de l'administrateur.
     */
    private String adresse;

    /**
     * Le salaire de l'administrateur.
     */
    private Double salaire;
    /**
     * Le statut du contrat de l'administrateur.
     */
    private StatutContrat statutContrat;
    /**
     * Le diplôme de l'administrateur.
     */
    private String diplome;
    /**
     * L'adresse email professionnelle de l'administrateur.
     */
    private String emailPro;

    /**
     * Le rôle de l'administrateur.
     */
    private String role;
    
}