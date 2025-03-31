package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * DTO pour la création d'un titulaire en pharmacie.
 * Contient les champs nécessaires à l'enregistrement d'un titulaire.
 */
@Data
@Builder
public class TitulaireCreateRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    private String email;

    @NotBlank(message = "Le numéro de téléphone est obligatoire")
    private String telephone;

    private String adresse;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @NotBlank(message = "L'email professionnel est obligatoire")
    private String emailPro;

    @NotBlank(message = "Le matricule est obligatoire")
    private String matricule;

    @NotNull(message = "La date d'embauche est obligatoire")
    private Date dateEmbauche;

    @NotNull(message = "Le salaire est obligatoire")
    private Double salaire;

    @NotBlank(message = "Le poste est obligatoire")
    private String poste;

    @NotBlank(message = "Le type de contrat est obligatoire")
    private StatutContrat statutContrat;

    private String diplome;

    @NotBlank(message = "Le rôle de l'administrateur est obligatoire")
    private String role;

    @NotBlank(message = "Le numéro RPPS est obligatoire")
    @Pattern(regexp = "\\d{11}", message = "Le numéro RPPS doit contenir exactement 11 chiffres")
    private String numeroRPPS;
}