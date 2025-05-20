package l3o2.pharmacie.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;

/**
 * DTO représentant la réponse contenant les informations d'un pharmacien adjoint.
 * Utilisé pour éviter d'exposer les entités directement.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacienAdjointResponse {
    /** L'identifiant unique de la personne. */
    private UUID idPersonne;
    /** Le nom du pharmacien adjoint. */
    private String nom;
    /** Le prénom du pharmacien adjoint. */
    private String prenom;
    /** L'adresse email personnelle du pharmacien adjoint. */
    private String email;
    /** Le numéro de téléphone personnel du pharmacien adjoint. */
    private String telephone;
    /** L'adresse postale du pharmacien adjoint. */
    private String adresse;

    // Hérité de Employe
    /** Le matricule de l'employé pharmacien adjoint. */
    private String matricule;
    /** La date d'embauche du pharmacien adjoint. */
    private Date dateEmbauche; 
    /** Le salaire du pharmacien adjoint. */
    private Double salaire;
    /** Le poste occupé par le pharmacien adjoint. */
    private PosteEmploye poste;
    /** Le statut du contrat du pharmacien adjoint. */
    private StatutContrat statutContrat; 
    /** Le diplôme du pharmacien adjoint. */
    private String diplome;
    /** L'adresse email professionnelle du pharmacien adjoint. */
    private String emailPro;

}