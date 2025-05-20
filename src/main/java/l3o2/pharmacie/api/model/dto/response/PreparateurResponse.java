package l3o2.pharmacie.api.model.dto.response;

import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

/**
 * DTO pour afficher les informations d'un préparateur en pharmacie.
 */
@Data
@Builder
public class PreparateurResponse {

    /** L'identifiant unique de la personne. */
    private UUID idPersonne;
    /** Le nom du préparateur. */
    private String nom;
    /** Le prénom du préparateur. */
    private String prenom;
    /** L'adresse email personnelle du préparateur. */
    private String email;
    /** Le numéro de téléphone personnel du préparateur. */
    private String telephone;
    /** L'adresse postale du préparateur. */
    private String adresse;
    /** Le matricule de l'employé préparateur. */
    private String matricule;
    /** La date d'embauche du préparateur. */
    private Date dateEmbauche;
    /** Le salaire du préparateur. */
    private Double salaire;
    /** Le poste occupé par le préparateur. */
    private PosteEmploye poste;
    /** Le statut du contrat du préparateur. */
    private StatutContrat statutContrat;
    /** Le diplôme du préparateur. */
    private String diplome;
    /** L'adresse email professionnelle du préparateur. */
    private String emailPro;
}