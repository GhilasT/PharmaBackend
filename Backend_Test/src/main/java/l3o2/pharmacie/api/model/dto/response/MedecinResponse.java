package l3o2.pharmacie.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedecinResponse {
    private UUID idPersonne;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String rpps;
    private String adeli;
    private String civilite;
    private String profession;
    private String specialitePrincipale;
    private String specialiteSecondaire;
    private String modeExercice;
    private String codePostal;
    private String ville;
    private String siteWeb;
    private String secteur;
    private String conventionnement;
    private String honoraires;
    private boolean carteVitale;
    private boolean teleconsultation;
    private List<String> languesParlees;
    private String siret ;
    private LocalDate dateMiseAJour ;
}