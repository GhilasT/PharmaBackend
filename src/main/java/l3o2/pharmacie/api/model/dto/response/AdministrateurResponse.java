package l3o2.pharmacie.api.model.dto.response;

import l3o2.pharmacie.api.model.entity.PosteEmploye;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import l3o2.pharmacie.api.model.entity.StatutContrat;

/**
 * DTO représentant la réponse contenant les informations d'un administrateur.
 * Utilisé pour éviter d'exposer les entités directement et pour structurer les données de l'administrateur.
 * Un administrateur est une personne et un employé avec des droits spécifiques.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministrateurResponse {
    /** L'identifiant unique de la personne (administrateur). */
    private UUID idPersonne;
    /** Le nom de l'administrateur. */
    private String nom;
    /** Le prénom de l'administrateur. */
    private String prenom;
    /** L'adresse email personnelle de l'administrateur. Doit être un format email valide. */
    @Email 
    private String email;
    /** Le numéro de téléphone personnel de l'administrateur. */
    private String telephone;
    /** L'adresse postale de l'administrateur. */
    private String adresse;

    // Employe
    /** Le matricule de l'employé administrateur. */
    private String matricule;
    /** La date d'embauche de l'administrateur. */
    private Date dateEmbauche;
    /** Le salaire de l'administrateur. */
    private Double salaire;
    /** Le poste occupé par l'administrateur. */
    private PosteEmploye poste;
    /** Le statut du contrat de l'administrateur. */
    private StatutContrat statutContrat;
    /** Le diplôme de l'administrateur. */
    private String diplome;
    /** L'adresse email professionnelle de l'administrateur. */
    private String emailPro;

    // Administrateur
    /** Le rôle spécifique de l'administrateur (ex: Admin_Systeme, Admin_Pharma). */
    private String role;
}