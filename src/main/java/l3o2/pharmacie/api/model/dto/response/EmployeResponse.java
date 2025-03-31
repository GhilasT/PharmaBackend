package l3o2.pharmacie.api.model.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;

/**
 * DTO pour la réponse contenant les informations d'un employé.
 */
@Data
@Builder
public class EmployeResponse {

    private UUID idPersonne;
    private String matricule;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private Date dateEmbauche;
    private Double salaire;
    private PosteEmploye poste;
    private StatutContrat statutContrat;
    private String diplome;
    private String emailPro;
}