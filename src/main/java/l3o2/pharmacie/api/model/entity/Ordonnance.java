package l3o2.pharmacie.api.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Classe représentant une ordonnance médicale dans le système.
 * Contient les informations relatives à la prescription de médicaments par un médecin.
 */
@Entity
@Table(name = "ordonnances")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ordonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID idOrdonnance;

    @Column(nullable = false, unique = true)
    private String numeroOrd;

    @Column(nullable = false)
    private Date dateEmission;

    @Column(nullable = true)
    private Date dateExpiration;

    @Column(nullable = false)
    private String rppsMedecin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medecin_id", nullable = false)
    private Medecin medecin;


    //indique qu’une Ordonnance peut être associée à plusieurs Prescription
    // et que la gestion de cette relation est effectuée via l’attribut ordonnance
    // dans la classe Prescription
    @OneToMany(mappedBy = "ordonnance")
    private List<Prescription> prescriptions;

    @ManyToOne(fetch = FetchType.LAZY)
    // lier ordonnace avec vente
    @JoinColumn(name = "vente_id", nullable = true)
    private Vente vente;

}