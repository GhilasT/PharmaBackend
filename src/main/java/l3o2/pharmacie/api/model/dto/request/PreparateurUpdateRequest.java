package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.*;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.Data;
import java.util.Date;

@Data
public class PreparateurUpdateRequest {
    private String nom;
    private String prenom;
    
    @Email(message = "Format d'email invalide")
    private String email;
    
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Format de téléphone invalide")
    private String telephone;
    
    private String adresse;
    
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;
    
    private Date dateEmbauche;
    
    @Positive(message = "Le salaire doit être positif")
    private Double salaire;
    
    private PosteEmploye poste;
    private StatutContrat statutContrat;
    private String diplome;
    
    @Email(message = "Format d'email professionnel invalide")
    private String emailPro;
}