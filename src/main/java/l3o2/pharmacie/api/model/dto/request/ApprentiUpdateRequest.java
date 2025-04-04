package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.Pattern;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.Builder;
import lombok.Data;
import java.util.Date;

@Data
@Builder
public class ApprentiUpdateRequest {
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
    private String ecole;
    private String emailPro;
    private String password;
}