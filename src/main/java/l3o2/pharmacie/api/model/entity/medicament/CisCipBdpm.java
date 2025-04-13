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

    @Column(name = "libelle_presentation")
    private String libellePresentation;

    @Column(name = "statut_administratif")
    private String statutAdministratif;

    @Column(name = "etat_commercialisation")
    private String etatCommercialisation;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_declaration_commercialisation")
    private Date dateDeclarationCommercialisation;

    @Column(name = "agrement_collectivites")
    @Pattern(regexp = "oui|non|inconnu", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Valeur invalide pour l'agrément aux collectivités")
    private String agrementCollectivites;

    @Column(name = "taux_remboursement")
    private String tauxRemboursement;

    @Column(name = "prix_ht", precision = 10, scale = 2)
    private BigDecimal prixHT;

    @Column(name = "prix_ttc", precision = 10, scale = 2)
    private BigDecimal prixTTC;

    @Column(name = "taxe", precision = 5, scale = 2)
    private BigDecimal taxe;

    @Column(name = "indications_remboursement", columnDefinition = "TEXT")
    private String indicationsRemboursement;


    // methode pour calculer le prix d'achat avec une reduction de 30% du prix vendu hors HT
    // info : cette methode n'est pas stocker dans la base de donnee de cis_cip_bdpm garce au  @Transient
    @Transient
    public BigDecimal getPrixUnitaireAvecReduction() {
        if (prixHT != null) {
            //le prix est donc 70% du prix HT
            return prixHT.multiply(BigDecimal.valueOf(0.70));
        }
        return BigDecimal.ZERO;
    }
}