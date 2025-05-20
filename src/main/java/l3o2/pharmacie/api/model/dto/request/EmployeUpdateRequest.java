package l3o2.pharmacie.api.model.dto.request;

import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * DTO pour la mise à jour d'un employé.
 */
@Data
@Builder
public class EmployeUpdateRequest {

    /**
     * Le nom de l'employé.
     */
    private String nom;
    /**
     * Le prénom de l'employé.
     */
    private String prenom;
    /**
     * L'adresse email personnelle de l'employé.
     */
    private String email;
    /**
     * Le numéro de téléphone de l'employé.
     */
    private String telephone;
    /**
     * L'adresse postale de l'employé.
     */
    private String adresse;
    /**
     * La date d'embauche de l'employé.
     */
    private Date dateEmbauche;
    /**
     * Le salaire de l'employé.
     */
    private Double salaire;
    /**
     * Le poste occupé par l'employé.
     */
    private PosteEmploye poste;
    /**
     * Le statut du contrat de l'employé (ex: CDI, CDD).
     */
    private StatutContrat statutContrat;
    /**
     * Le diplôme de l'employé.
     */
    private String diplome;
    /**
     * L'adresse email professionnelle de l'employé.
     */
    private String emailPro;
}