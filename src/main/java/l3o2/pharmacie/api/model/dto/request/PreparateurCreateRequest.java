package l3o2.pharmacie.api.model.dto.request;

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
 * DTO pour la création d'un préparateur en pharmacie.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreparateurCreateRequest {

    /**
     * Le nom du préparateur. Obligatoire.
     */
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    /**
     * Le prénom du préparateur. Obligatoire.
     */
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    /**
     * L'adresse email personnelle du préparateur. Obligatoire.
     */
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    /**
     * Le numéro de téléphone du préparateur. Obligatoire.
     */
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String telephone;

    /**
     * L'adresse postale du préparateur.
     */
    private String adresse;

    /**
     * La date d'embauche du préparateur. Obligatoire.
     */
    @NotNull(message = "La date d'embauche est obligatoire")
    private Date dateEmbauche;

    /**
     * Le salaire du préparateur. Obligatoire.
     */
    @NotNull(message = "Le salaire est obligatoire")
    private Double salaire;

    /**
     * Le poste du préparateur.
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
     * L'adresse email professionnelle du préparateur. Obligatoire.
     */
    @NotBlank(message = "L'email professionnel est obligatoire")
    private String emailPro;

    /**
     * Le mot de passe pour le compte du préparateur. Obligatoire.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}