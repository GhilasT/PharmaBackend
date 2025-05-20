package l3o2.pharmacie.api.model.entity.medicament;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe représentant une présentation d'un médicament dans la BDPM.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "cis_cip_bdpm")
public class CisCipBdpm {

    /**
     * Clé primaire auto-incrémentée pour cette table, afin de simplifier
     * la gestion des identifiants.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Code CIP7 (unique). Champ normal, non clé primaire mais contraint unique.
     */
    @Column(name = "code_cip7", nullable = false, unique = true)
    @NotBlank(message = "Le code CIP7 est obligatoire")
    private String codeCip7;

    /**
     * Code CIP13 (unique). Champ normal, non clé primaire mais contraint unique.
     */
    @Column(name = "code_cip13", nullable = false, unique = true)
    private String codeCip13;

    /**
     * Relation ManyToOne vers CisBdpm (via la colonne code_cis).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_cis", nullable = false)
    private CisBdpm cisBdpm;

    /** Libellé de la présentation du médicament. */
    @Column(name = "libelle_presentation")
    private String libellePresentation;

    /** Statut administratif de la présentation. */
    @Column(name = "statut_administratif")
    private String statutAdministratif;

    /** État de commercialisation de la présentation. */
    @Column(name = "etat_commercialisation")
    private String etatCommercialisation;

    /** Date de déclaration de commercialisation. */
    @Temporal(TemporalType.DATE)
    @Column(name = "date_declaration_commercialisation")
    private Date dateDeclarationCommercialisation;

    /** Agrément aux collectivités (oui, non, inconnu). */
    @Column(name = "agrement_collectivites")
    @Pattern(regexp = "oui|non|inconnu", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Valeur invalide pour l'agrément aux collectivités")
    private String agrementCollectivites;

    /** Taux de remboursement de la présentation. */
    @Column(name = "taux_remboursement")
    private String tauxRemboursement;

    /** Prix Hors Taxe (HT) de la présentation. */
    @Column(name = "prix_ht", precision = 10, scale = 2)
    private BigDecimal prixHT;

    /** Prix Toutes Taxes Comprises (TTC) de la présentation. */
    @Column(name = "prix_ttc", precision = 10, scale = 2)
    private BigDecimal prixTTC;

    /** Montant de la taxe applicable à la présentation. */
    @Column(name = "taxe", precision = 5, scale = 2)
    private BigDecimal taxe;

    /** Indications thérapeutiques ouvrant droit au remboursement. */
    @Column(name = "indications_remboursement", columnDefinition = "TEXT")
    private String indicationsRemboursement;

    /**
     * Calcule le prix d'achat unitaire avec une réduction de 30% sur le prix de vente hors taxe (HT).
     * Cette méthode n'est pas persistée en base de données grâce à l'annotation {@code @Transient}.
     * Le prix calculé correspond à 70% du prix HT.
     *
     * @return Le prix unitaire avec réduction, ou {@link BigDecimal#ZERO} si le prix HT est nul.
     */
    @Transient
    public BigDecimal getPrixUnitaireAvecReduction() {
        if (prixHT != null) {
            //le prix est donc 70% du prix HT
            return prixHT.multiply(BigDecimal.valueOf(0.70));
        }
        return BigDecimal.ZERO;
    }
}