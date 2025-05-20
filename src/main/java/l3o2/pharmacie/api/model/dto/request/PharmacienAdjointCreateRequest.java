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
 * DTO pour la création d'un pharmacien adjoint.
 * Contient les informations nécessaires pour enregistrer un nouveau pharmacien adjoint.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacienAdjointCreateRequest {
    /**
     * Le nom du pharmacien adjoint. Obligatoire.
     */
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    /**
     * Le prénom du pharmacien adjoint. Obligatoire.
     */
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    /**
     * L'adresse email personnelle du pharmacien adjoint. Obligatoire et doit être un format d'email valide.
     */
    @NotBlank(message = "L'email personnel est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    /**
     * Le numéro de téléphone du pharmacien adjoint. Obligatoire et doit respecter un format valide.
     */
    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Format de téléphone invalide")
    private String telephone;

    /**
     * L'adresse postale du pharmacien adjoint. Optionnel.
     */
    private String adresse; 

    /**
     * Le mot de passe pour le compte du pharmacien adjoint. Obligatoire et doit contenir au moins 6 caractères.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 6, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    /**
     * La date d'embauche du pharmacien adjoint. Obligatoire.
     */
    @NotNull(message = "La date d'embauche est obligatoire")
    private Date dateEmbauche;

    /**
     * Le salaire du pharmacien adjoint. Obligatoire et doit être un nombre positif.
     */
    @NotNull(message = "Le salaire est obligatoire")
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
     * Le diplôme du pharmacien adjoint. Optionnel.
     */
    private String diplome; 

    /**
     * L'adresse email professionnelle du pharmacien adjoint. Obligatoire et doit être un format d'email valide.
     */
    @NotBlank(message = "L'email professionnel est obligatoire")
    @Email(message = "Format d'email professionnel invalide")
    private String emailPro;
    }