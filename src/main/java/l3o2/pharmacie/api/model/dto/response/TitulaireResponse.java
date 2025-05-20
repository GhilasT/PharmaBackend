package l3o2.pharmacie.api.model.dto.response;

import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

/**
 * DTO représentant la réponse contenant les informations d'un titulaire.
 * Utilisé pour éviter d'exposer les entités directement.
 */
@Data
@Builder
public class TitulaireResponse {

    /** L'identifiant unique de la personne. */
    private UUID idPersonne;
    /** Le nom du titulaire. */
    private String nom;
    /** Le prénom du titulaire. */
    private String prenom;
    /** L'adresse email personnelle du titulaire. */
    private String email;
    /** Le numéro de téléphone personnel du titulaire. */
    private String telephone;
    /** L'adresse postale du titulaire. */
    private String adresse;
    /** L'adresse email professionnelle du titulaire. */
    private String emailPro;
    /** Le matricule de l'employé titulaire. */
    private String matricule;
    /** La date d'embauche du titulaire. */
    private Date dateEmbauche;
    /** Le salaire du titulaire. */
    private Double salaire;
    /** Le poste occupé par le titulaire. */
    private PosteEmploye poste;
    /** Le statut du contrat du titulaire. */
    private StatutContrat statutContrat;
    /** Le diplôme du titulaire. */
    private String diplome;
    /** Le rôle du titulaire (spécifique au titulaire). */
    private String role;
    /** Le numéro RPPS (Répertoire Partagé des Professionnels de Santé) du titulaire. */
    private String numeroRPPS;
}