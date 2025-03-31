package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * DTO pour la création d'un employé.
 * Utilisé lors de l'ajout d'un nouvel employé.
 */
@Data
@Builder
public class EmployeCreateRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    private String adresse; // Optionnel

    @NotNull(message = "La date d'embauche est obligatoire")
    private Date dateEmbauche;

    @NotNull(message = "Le salaire est obligatoire")
    private Double salaire;

    @NotBlank(message = "Le poste est obligatoire")
    private PosteEmploye poste;

    @NotBlank(message = "Le statut du contrat est obligatoire")
    private StatutContrat statutContrat;

    private String diplome; // Optionnel

    @NotBlank(message = "L'email professionnel est obligatoire")
    private String emailPro;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}