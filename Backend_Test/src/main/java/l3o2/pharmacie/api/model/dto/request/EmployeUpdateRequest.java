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