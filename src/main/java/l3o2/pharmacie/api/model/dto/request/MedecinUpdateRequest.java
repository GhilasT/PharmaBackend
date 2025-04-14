package l3o2.pharmacie.api.model.dto.request;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedecinUpdateRequest {
    // Tous les champs du CreateRequest SAUF password
    // Aucune annotation @NotBlank
    private String nom;
    private String prenom;
    @Email private String email;
    @Pattern(regexp = "^(\\+?[0-9\\s-]{6,15})$") 
    private String telephone;
    private String adresse;
    private String rpps;
    private String adeli;
    private String civilite;
    private String profession;
    private String specialitePrincipale;
    private String specialiteSecondaire;
    private String modeExercice;
    @Pattern(regexp = "\\d{5}") private String codePostal;
    private String ville;
    private String siteWeb;
    private String secteur;
    private String conventionnement;
    private String honoraires;
    private Boolean carteVitale;
    private Boolean teleconsultation;
    private List<String> languesParlees;
    private String siret;
    private LocalDate dateMiseAJour;
}
