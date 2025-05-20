package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.Pattern;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

/**
 * DTO pour la mise à jour des informations d'un apprenti en pharmacie.
 * Permet de modifier les attributs d'un apprenti existant.
 */
@Data
@Builder
public class ApprentiUpdateRequest {
    /**
     * Le nom de l'apprenti.
     */
    private String nom;
    /**
     * Le prénom de l'apprenti.
     */
    private String prenom;
    /**
     * L'adresse email personnelle de l'apprenti.
     */
    private String email;
    /**
     * Le numéro de téléphone de l'apprenti.
     */
    private String telephone;
    /**
     * L'adresse postale de l'apprenti.
     */
    private String adresse;
    /**
     * La date d'embauche de l'apprenti.
     */
    private Date dateEmbauche;
    /**
     * Le salaire de l'apprenti.
     */
    private Double salaire;
    /**
     * Le poste occupé par l'apprenti.
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
     * L'école ou l'établissement de formation de l'apprenti.
     */
    private String ecole;
    /**
     * L'adresse email professionnelle de l'apprenti.
     */
    private String emailPro;
    /**
     * Le mot de passe pour le compte de l'apprenti.
     */
    private String password;
}