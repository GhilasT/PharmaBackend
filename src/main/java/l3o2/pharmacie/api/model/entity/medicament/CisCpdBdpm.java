package l3o2.pharmacie.api.model.entity.medicament;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

/**
 * Classe représentant les conditions de prescription et de délivrance d'un médicament
 * référencé dans la BDPM.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cis_cpd_bdpm")
public class CisCpdBdpm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identifiant unique de la condition de prescription/délivrance. */
    private Long id;

    /** Relation ManyToOne vers CisBdpm. */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_cis", nullable = false)
    private CisBdpm cisBdpm;

    /** Condition de prescription/délivrance (texte libre). */
    @Column(name = "condition_prescription", nullable = false, columnDefinition = "TEXT")
    private String conditionPrescription;
}