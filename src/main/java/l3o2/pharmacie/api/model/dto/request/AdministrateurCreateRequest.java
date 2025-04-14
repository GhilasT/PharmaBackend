package l3o2.pharmacie.api.model.dto.request;

import java.util.Date;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création d'un administrateur
 * Contient les informations nécessaires pour créer un nouvel administrateur
 */
@Data
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation
@Builder
// Génère un constructeur sans paramètres
@NoArgsConstructor
// Génère un constructeur avec tous les paramètres
@AllArgsConstructor
public class AdministrateurCreateRequest {
    // Champs de Personne
    @NotBlank 
    private String nom;
    @NotBlank 
    private String prenom;
    @NotBlank
    @Email 
    private String email;
    @NotBlank
    private String telephone;
    private String adresse;

    // Champs spécifiques à Employe
    @NotNull 
    private Date dateEmbauche;
    @NotNull
    private Double salaire;

    private PosteEmploye poste;

    private StatutContrat statutContrat;
    private String diplome;
    @NotBlank 
    @Email 
    private String emailPro;
    
    // Champs spécifiques à Administrateur
    @NotBlank 
    private String role;
    @NotBlank 
    private String password;
}