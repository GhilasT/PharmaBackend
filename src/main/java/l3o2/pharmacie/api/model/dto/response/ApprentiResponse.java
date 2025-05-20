package l3o2.pharmacie.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.UUID;

import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;

/**
 * DTO pour afficher les informations d'un apprenti en pharmacie.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApprentiResponse {

    /** L'identifiant unique de la personne (apprenti). */
    private UUID idPersonne;
    /** Le nom de l'apprenti. */
    private String nom;
    /** Le prénom de l'apprenti. */
    private String prenom;
    /** L'adresse email personnelle de l'apprenti. */
    private String email;
    /** Le numéro de téléphone personnel de l'apprenti. */
    private String telephone;
    /** L'adresse postale de l'apprenti. */
    private String adresse;
    /** Le matricule de l'employé apprenti. */
    private String matricule;
    /** La date d'embauche de l'apprenti. */
    private Date dateEmbauche;
    /** Le salaire de l'apprenti. */
    private Double salaire;
    /** Le poste occupé par l'apprenti. */
    private PosteEmploye poste;
    /** Le statut du contrat de l'apprenti. */
    private StatutContrat statutContrat;
    /** Le diplôme préparé ou obtenu par l'apprenti. */
    private String diplome;
    /** L'école ou l'établissement de formation de l'apprenti. */
    private String ecole;
    /** L'adresse email professionnelle de l'apprenti. */
    private String emailPro;
}