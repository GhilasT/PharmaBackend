package l3o2.pharmacie.api.model.dto.request;

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

import java.util.Date;

/**
 * DTO pour la création d'un titulaire en pharmacie.
 * Contient les champs nécessaires à l'enregistrement d'un titulaire.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TitulaireCreateRequest {

    /**
     * Le nom du titulaire. Obligatoire.
     */
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    /**
     * Le prénom du titulaire. Obligatoire.
     */
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    /**
     * L'adresse email personnelle du titulaire. Doit être valide et est obligatoire.
     */
    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    /**
     * Le numéro de téléphone du titulaire. Obligatoire.
     */
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String telephone;

    /**
     * L'adresse postale du titulaire.
     */
    private String adresse;

    /**
     * Le mot de passe pour le compte du titulaire. Obligatoire.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    /**
     * L'adresse email professionnelle du titulaire. Obligatoire.
     */
    @NotBlank(message = "L'email professionnel est obligatoire")
    private String emailPro;

    /**
     * La date d'embauche du titulaire. Obligatoire.
     */
    @NotNull(message = "La date d'embauche est obligatoire")
    private Date dateEmbauche;

    /**
     * Le salaire du titulaire. Obligatoire.
     */
    @NotNull(message = "Le salaire est obligatoire")
    private Double salaire;

    /**
     * Le poste du titulaire, doit être "TITULAIRE". Obligatoire.
     */
    @NotBlank(message = "Le poste est obligatoire")
    @Pattern(regexp = "TITULAIRE", message = "Poste invalide pour un TITULAIRE")
    private PosteEmploye poste;

    /**
     * Le statut du contrat du titulaire (ex: CDI, CDD). Obligatoire.
     */
    @NotBlank(message = "Le type de contrat est obligatoire")
    private StatutContrat statutContrat;

    /**
     * Le diplôme du titulaire.
     */
    private String diplome;

    /**
     * Le rôle administratif du titulaire. Obligatoire.
     */
    @NotBlank(message = "Le rôle de l'administrateur est obligatoire")
    private String role;

    /**
     * Le numéro RPPS (Répertoire Partagé des Professionnels de Santé) du titulaire.
     * Doit contenir exactement 11 chiffres. Obligatoire.
     */
    @NotBlank(message = "Le numéro RPPS est obligatoire")
    @Pattern(regexp = "\\d{11}", message = "Le numéro RPPS doit contenir exactement 11 chiffres")
    private String numeroRPPS;
}