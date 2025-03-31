package l3o2.pharmacie.api.model.entity.medicament;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Classe représentant une présentation d'un médicament dans la BDPM.
 * Contient les informations relatives à l'identification, la commercialisation et le remboursement d'un médicament.
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
@Table(name = "cis_cip_bdpm")
public class CisCipBdpm {

    @Id
    @Column(name = "code_cip7", nullable = false, unique = true)
    @NotBlank(message = "Le code CIP7 est obligatoire")
    // Code unique d'identification de la présentation du médicament (CIP7).
    private String codeCip7;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_cis", nullable = false)
    // Relation avec la table principale des médicaments (CIS).
    private CisBdpm cisBdpm;

    @Column(name = "libelle_presentation")
    // Libellé de la présentation du médicament (ex: "Boîte de 30 comprimés").
    private String libellePresentation;

    @Column(name = "statut_administratif")
    // Statut administratif du médicament (ex: autorisé, en attente).
    private String statutAdministratif;

    @Column(name = "etat_commercialisation")
    // Indique si la présentation est commercialisée ou non.
    private String etatCommercialisation;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_declaration_commercialisation")
    // Date de déclaration de commercialisation (format JJ/MM/AAAA).
    private Date dateDeclarationCommercialisation;

    @Column(name = "code_cip13", nullable = false, unique = true)
    // Code CIP13, identifiant unique de la présentation.
    private String codeCip13;

    @Column(name = "agrement_collectivites")
    @Pattern(regexp = "oui|non|inconnu", flags = Pattern.Flag.CASE_INSENSITIVE,
            message = "Valeur invalide pour l'agrément aux collectivités")
    // Indique si la présentation est agréée pour les collectivités (Oui/Non/Inconnu).
    private String agrementCollectivites;

    @Column(name = "taux_remboursement")
    // Taux de remboursement appliqué à la présentation (ex: "65%; 30%").
    private String tauxRemboursement;

    @Column(name = "prix_ht", precision = 10, scale = 2)
    // Prix Hors Taxes (HT) de la présentation.
    private BigDecimal prixHT;

    @Column(name = "prix_ttc", precision = 10, scale = 2)
    // Prix Toutes Taxes Comprises (TTC) de la présentation.
    private BigDecimal prixTTC;

    @Column(name = "taxe", precision = 5, scale = 2)
    // Montant de la taxe appliquée à la présentation.
    private BigDecimal taxe;

    @Column(name = "indications_remboursement", columnDefinition = "TEXT")
    // Indications pour le remboursement du médicament.
    private String indicationsRemboursement;
}