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

    /**
     * Le nom de l'employé. Obligatoire.
     */
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    /**
     * Le prénom de l'employé. Obligatoire.
     */
    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    /**
     * L'adresse email personnelle de l'employé. Obligatoire.
     */
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    /**
     * Le numéro de téléphone de l'employé. Obligatoire.
     */
    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    /**
     * L'adresse postale de l'employé. Optionnel.
     */
    private String adresse; 

    /**
     * La date d'embauche de l'employé. Obligatoire.
     */
    @NotNull(message = "La date d'embauche est obligatoire")
    private Date dateEmbauche;

    /**
     * Le salaire de l'employé. Obligatoire.
     */
    @NotNull(message = "Le salaire est obligatoire")
    private Double salaire;

    /**
     * Le poste de l'employé. Obligatoire.
     */
    @NotBlank(message = "Le poste est obligatoire")
    private PosteEmploye poste;

    /**
     * Le statut du contrat de l'employé. Obligatoire.
     */
    @NotBlank(message = "Le statut du contrat est obligatoire")
    private StatutContrat statutContrat;

    /**
     * Le diplôme de l'employé.
     */
    private String diplome;

    /**
     * L'adresse email professionnelle de l'employé. Obligatoire.
     */
    @NotBlank(message = "L'email professionnel est obligatoire")
    private String emailPro;

    /**
     * Le mot de passe pour le compte de l'employé. Obligatoire.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}