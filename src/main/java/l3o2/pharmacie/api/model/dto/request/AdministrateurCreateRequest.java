package l3o2.pharmacie.api.model.dto.request;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création d'un administrateur
 * Contient les informations nécessaires pour créer un nouvel administrateur
 */
@Data
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation
@Builder
// Génère un constructeur sans paramètres
@NoArgsConstructor
// Génère un constructeur avec tous les paramètres
@AllArgsConstructor
public class AdministrateurCreateRequest {
    /**
     * Le nom de l'administrateur. Obligatoire.
     */
    @NotBlank 
    private String nom;
    /**
     * Le prénom de l'administrateur. Obligatoire.
     */
    @NotBlank 
    private String prenom;
    /**
     * L'adresse email personnelle de l'administrateur. Obligatoire et doit être un format d'email valide.
     */
    @NotBlank
    @Email 
    private String email;
    /**
     * Le numéro de téléphone de l'administrateur. Obligatoire.
     */
    @NotBlank
    private String telephone;
    /**
     * L'adresse postale de l'administrateur.
     */
    private String adresse;

    /**
     * La date d'embauche de l'administrateur. Obligatoire.
     */
    @NotNull 
    private Date dateEmbauche;
    /**
     * Le salaire de l'administrateur. Obligatoire.
     */
    @NotNull
    private Double salaire;

    /**
     * Le poste de l'administrateur.
     */
    private PosteEmploye poste;

    /**
     * Le statut du contrat de l'administrateur.
     */
    private StatutContrat statutContrat;
    /**
     * Le diplôme de l'administrateur.
     */
    private String diplome;
    /**
     * L'adresse email professionnelle de l'administrateur. Obligatoire et doit être un format d'email valide.
     */
    @NotBlank 
    @Email 
    private String emailPro;
    
    /**
     * Le rôle de l'administrateur. Obligatoire.
     */
    @NotBlank 
    private String role;
    /**
     * Le mot de passe pour le compte de l'administrateur. Obligatoire.
     */
    @NotBlank 
    private String password;
}