package l3o2.pharmacie.api.model.entity.medicament;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cis_gener_bdpm")
public class CisGenerBdpm {

    @Id
    @Column(name = "identifiant_groupe_generique", nullable = false, length = 1000)
    private String identifiantGroupeGenerique;

    @Column(name = "libelle_groupe_generique", length = 1000)
    private String libelleGroupeGenerique;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_cis", nullable = false)
    private CisBdpm cisBdpm;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "type_generique", nullable = false)
    private TypeGenerique typeGenerique;

    @Column(name = "numero_tri")
    private Integer numeroTri;
}
