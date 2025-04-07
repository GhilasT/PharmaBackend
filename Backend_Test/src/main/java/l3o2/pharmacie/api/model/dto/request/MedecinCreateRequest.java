package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedecinCreateRequest {

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")

    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")

    private String telephone;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;

    @NotBlank(message = "Le RPPS est obligatoire")

    private String rpps;

    @NotBlank(message = "Le numéro ADELI est obligatoire")

    private String adeli;

    @NotBlank(message = "La civilité est obligatoire")
    private String civilite;

    @NotBlank(message = "La profession est obligatoire")
    private String profession;

    @NotBlank(message = "La spécialité principale est obligatoire")
    private String specialitePrincipale;

    private String specialiteSecondaire;

    @NotBlank(message = "Le mode d'exercice est obligatoire")
    private String modeExercice;

    @Pattern(regexp = "\\d{5}", message = "Le code postal doit contenir exactement 5 chiffres")
    private String codePostal;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    private String siteWeb;

    @NotBlank(message = "Le secteur est obligatoire")
    private String secteur;

    @NotBlank(message = "Le conventionnement est obligatoire")
    private String conventionnement;

    @NotBlank(message = "Les honoraires sont obligatoires")
    private String honoraires;

    private Boolean carteVitale;
    private Boolean teleconsultation;
    private List<String> languesParlees;

    @NotBlank(message = "Le SIRET est obligatoire")

    private String siret;

    @NotNull(message = "La date de mise à jour est obligatoire")
    private LocalDate dateMiseAJour;
}