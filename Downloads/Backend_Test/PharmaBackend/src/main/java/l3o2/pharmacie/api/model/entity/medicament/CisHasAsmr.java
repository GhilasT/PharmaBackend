package l3o2.pharmacie.api.model.entity.medicament;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.Date;

/**
 * Classe représentant un avis de l'Amélioration du Service Médical Rendu (ASMR) dans la BDPM.
 * Contient les informations sur l'évaluation d'un médicament par la Haute Autorité de Santé (HAS).
 */
@Getter
@Setter
// Génère automatiquement un constructeur sans paramètres.
@NoArgsConstructor
// Génère automatiquement un constructeur avec tous les paramètres.
@AllArgsConstructor
// Indique que cette classe est une entité JPA.
@Entity
// Définit le nom de la table associée dans la base de données.
@Table(name = "cis_has_asmr")
public class CisHasAsmr {

    @Id
    @Column(name = "code_dossier_has", nullable = false, unique = true)
    @NotBlank(message = "Le code dossier HAS est obligatoire")
    // Code unique d'identification du dossier HAS pour l'ASMR.
    private String codeDossierHas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_cis", nullable = false)
    // Référence au médicament principal (CIS).
    private CisBdpm cisBdpm;

    @Column(name = "motif_evaluation")
    // Motif de l'évaluation du médicament (ex: nouvelle indication, réévaluation).
    private String motifEvaluation;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_avis")
    // Date de l'avis HAS sur l'ASMR.
    private Date dateAvis;

    @Column(name = "valeur_asmr")
    // Valeur de l'ASMR (ex: "ASMR I", "ASMR V").
    private String valeurAsmr;

    @Column(name = "libelle_asmr", columnDefinition = "TEXT")
    // Libellé détaillant le niveau d'amélioration du service médical rendu.
    private String libelleAsmr;

    @OneToOne
    @JoinColumn(name = "code_dossier_has", referencedColumnName = "code_dossier_has")
    // Lien vers la page de la Commission de la Transparence (CT) associée à l'ASMR.
    private HasLiensPageCT lienPageCT;
}