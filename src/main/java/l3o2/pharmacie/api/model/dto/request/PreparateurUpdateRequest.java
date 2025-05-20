package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.*;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.Data;
import java.util.Date;

/**
 * DTO pour la mise à jour des informations d'un préparateur en pharmacie.
 * Permet de modifier les attributs d'un préparateur existant.
 */
@Data
public class PreparateurUpdateRequest {
    /**
     * Le nom du préparateur.
     */
    private String nom;
    /**
     * Le prénom du préparateur.
     */
    private String prenom;
    
    /**
     * L'adresse email personnelle du préparateur. Doit être un format d'email valide.
     */
    @Email(message = "Format d'email invalide")
    private String email;
    
    /**
     * Le numéro de téléphone du préparateur. Doit respecter un format valide.
     */
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Format de téléphone invalide")
    private String telephone;
    
    /**
     * L'adresse postale du préparateur.
     */
    private String adresse;
    
    /**
     * Le nouveau mot de passe pour le compte du préparateur. Doit contenir au moins 8 caractères.
     */
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;
    
    /**
     * La date d'embauche du préparateur.
     */
    private Date dateEmbauche;
    
    /**
     * Le salaire du préparateur. Doit être un nombre positif.
     */
    @Positive(message = "Le salaire doit être positif")
    private Double salaire;
    
    /**
     * Le poste occupé par le préparateur.
     */
    private PosteEmploye poste;
    /**
     * Le statut du contrat du préparateur (ex: CDI, CDD).
     */
    private StatutContrat statutContrat;
    /**
     * Le diplôme du préparateur.
     */
    private String diplome;
    
    /**
     * L'adresse email professionnelle du préparateur. Doit être un format d'email valide.
     */
    @Email(message = "Format d'email professionnel invalide")
    private String emailPro;
}