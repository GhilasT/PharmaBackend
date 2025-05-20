package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.*;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * DTO pour la mise à jour des informations d'un pharmacien adjoint.
 * Permet de modifier les attributs d'un pharmacien adjoint existant.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PharmacienAdjointUpdateRequest {
    /**
     * Le nom du pharmacien adjoint.
     */
    private String nom;
    /**
     * Le prénom du pharmacien adjoint.
     */
    private String prenom;
    
    /**
     * L'adresse email personnelle du pharmacien adjoint. Doit être un format d'email valide.
     */
    @Email(message = "Format d'email invalide")
    private String email;
    
    /**
     * Le numéro de téléphone du pharmacien adjoint. Doit respecter un format valide.
     */
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Format de téléphone invalide")
    private String telephone;
    
    /**
     * L'adresse postale du pharmacien adjoint.
     */
    private String adresse;
    
    /**
     * Le nouveau mot de passe pour le compte du pharmacien adjoint. Doit contenir au moins 8 caractères.
     */
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;
    
    /**
     * La date d'embauche du pharmacien adjoint.
     */
    private Date dateEmbauche;
    
    /**
     * Le salaire du pharmacien adjoint. Doit être un nombre positif.
     */
    @Positive(message = "Le salaire doit être positif")
    private Double salaire;
    
    /**
     * Le poste occupé par le pharmacien adjoint.
     */
    private PosteEmploye poste;
    /**
     * Le statut du contrat du pharmacien adjoint (ex: CDI, CDD).
     */
    private StatutContrat statutContrat;
    /**
     * Le diplôme du pharmacien adjoint.
     */
    private String diplome;
    
    /**
     * L'adresse email professionnelle du pharmacien adjoint. Doit être un format d'email valide.
     */
    @Email(message = "Format d'email professionnel invalide")
    private String emailPro;
}