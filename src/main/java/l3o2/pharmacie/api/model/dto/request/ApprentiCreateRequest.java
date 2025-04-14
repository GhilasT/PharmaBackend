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

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String telephone;

    private String adresse;

    @NotNull(message = "La date d'embauche est obligatoire")
    private Date dateEmbauche;

    @NotNull(message = "Le salaire est obligatoire")
    private Double salaire;

    private PosteEmploye poste;

    private StatutContrat statutContrat;

    private String diplome;

    @NotBlank(message = "L'école est obligatoire")
    private String ecole;

    @NotBlank(message = "L'email professionnel est obligatoire")
    private String emailPro;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}