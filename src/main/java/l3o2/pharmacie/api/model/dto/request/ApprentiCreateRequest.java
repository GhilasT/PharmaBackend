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
 * DTO pour la création d'un apprenti en pharmacie.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprentiCreateRequest {

    /**
     * Le nom de l'apprenti. Obligatoire.
     */
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    /**
     * Le prénom de l'apprenti. Obligatoire.
     */
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    /**
     * L'adresse email personnelle de l'apprenti. Obligatoire.
     */
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    /**
     * Le numéro de téléphone de l'apprenti. Obligatoire.
     */
    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String telephone;

    /**
     * L'adresse postale de l'apprenti.
     */
    private String adresse;

    /**
     * La date d'embauche de l'apprenti. Obligatoire.
     */
    @NotNull(message = "La date d'embauche est obligatoire")
    private Date dateEmbauche;

    /**
     * Le salaire de l'apprenti. Obligatoire.
     */
    @NotNull(message = "Le salaire est obligatoire")
    private Double salaire;

    /**
     * Le poste de l'apprenti.
     */
    private PosteEmploye poste;

    /**
     * Le statut du contrat de l'apprenti.
     */
    private StatutContrat statutContrat;

    /**
     * Le diplôme préparé ou obtenu par l'apprenti.
     */
    private String diplome;

    /**
     * L'école ou l'établissement de formation de l'apprenti. Obligatoire.
     */
    @NotBlank(message = "L'école est obligatoire")
    private String ecole;

    /**
     * L'adresse email professionnelle de l'apprenti. Obligatoire.
     */
    @NotBlank(message = "L'email professionnel est obligatoire")
    private String emailPro;

    /**
     * Le mot de passe pour le compte de l'apprenti. Obligatoire.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}