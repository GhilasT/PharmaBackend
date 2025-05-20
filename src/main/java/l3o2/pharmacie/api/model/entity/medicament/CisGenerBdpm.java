package l3o2.pharmacie.api.model.entity.medicament;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

/**
 * Classe représentant un groupe générique d'un médicament référencé dans la BDPM.
 * Contient les informations sur l'appartenance d'un médicament à un groupe générique.
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
@Table(name = "cis_gener_bdpm")
public class CisGenerBdpm {

    @Id
    @Column(name = "identifiant_groupe_generique", nullable = false)
    /** Identifiant unique du groupe générique auquel appartient le médicament. */
    private String identifiantGroupeGenerique;

    @Column(name = "libelle_groupe_generique")
    /** Libellé du groupe générique (ex: "Paracétamol et ses dérivés"). */
    private String libelleGroupeGenerique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_cis", nullable = false)
    /** Référence au médicament principal (CIS). */
    private CisBdpm cisBdpm;

    @Enumerated(EnumType.ORDINAL) // Stocke la valeur numérique (0,1,2,4).
    @Column(name = "type_generique", nullable = false)
    /** Type de générique (ex: groupe homogène, assimilable). */
    private TypeGenerique typeGenerique;

    @Column(name = "numero_tri")
    /** Numéro de tri du médicament au sein du groupe générique. */
    private Integer numeroTri;
}