package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.*;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacienAdjointCreateRequest {
    // Champs de Personne
    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email personnel est obligatoire")
    @Email(message = "Format d'email invalide")
    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Format de téléphone invalide")
    private String telephone;

    private String adresse; // Optionnel

    // Champs de Employe
    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;

    @NotNull(message = "La date d'embauche est obligatoire")
    private Date dateEmbauche;

    @NotNull(message = "Le salaire est obligatoire")
    @Positive(message = "Le salaire doit être positif")
    private Double salaire;

    @NotBlank(message = "Le poste est obligatoire")
    @Pattern(regexp = "PHARMACIEN_ADJOINT", message = "Poste invalide pour un pharmacien adjoint")
    private PosteEmploye poste;

    @NotBlank(message = "Le statut du contrat est obligatoire")
    private StatutContrat statutContrat;

    private String diplome; // Optionnel

    @NotBlank(message = "L'email professionnel est obligatoire")
    @Email(message = "Format d'email professionnel invalide")
    private String emailPro;
    
    //TODO: Ajouter une méthode pour la génération automatique de matricule
    private String matricule;
}