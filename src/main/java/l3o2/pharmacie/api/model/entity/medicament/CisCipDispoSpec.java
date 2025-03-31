package l3o2.pharmacie.api.model.entity.medicament;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.util.Date;

/**
 * Classe représentant la disponibilité spécifique d'un médicament.
 * Contient des informations sur l'état de commercialisation et les mises à jour de disponibilité.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
// Définit le nom de la table associée dans la base de données.
@Table(name = "cis_cip_dispo_spec")
public class CisCipDispoSpec {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Identifiant unique auto-généré.
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_cis", nullable = false)
    // Relation avec le médicament principal (CIS).
    private CisBdpm cisBdpm;

    @Column(name = "code_cip13")
    // Code CIP13 du médicament.
    private String codeCip13;

    @Column(name = "code_statut", nullable = false)
    @Min(1)
    @Max(4)
    // Code représentant le statut de disponibilité (ex: 1 = disponible, 4 = en rupture).
    private Integer codeStatut;

    @Column(name = "libelle_statut", nullable = false)
    // Libellé du statut de disponibilité (ex: "Disponible", "Rupture de stock").
    private String libelleStatut;

    // Dates au format JJ/MM/AAAA.
    @Temporal(TemporalType.DATE)
    @Column(name = "date_debut", nullable = false)
    // Date de début de la disponibilité ou indisponibilité.
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_mise_a_jour", nullable = false)
    // Date de la dernière mise à jour du statut.
    private Date dateMiseAJour;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_remise_disposition")
    // Date prévue de remise en disposition (si applicable).
    private Date dateRemiseDisposition;

    @Column(name = "lien_ansm", columnDefinition = "TEXT")
    // Lien vers la page officielle ANSM pour plus d'informations.
    private String lienANSM;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicament_id", referencedColumnName = "id")
    private StockMedicament stockMedicament;
}