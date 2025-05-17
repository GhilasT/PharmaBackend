package l3o2.pharmacie.api.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
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

    @Column(nullable = false)
    private Date dateEmission;

    @Column(nullable = false)
    private String rppsMedecin;
    
     @Column(name="numeroord", nullable=true)
    private String numeroOrd;

    /** Relation vers le client */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /**
     * Chaque ordonnance peut comprendre plusieurs prescriptions.
     * Relation unidirectionnelle, la FK est dans la table prescriptions.
     * @Singular pour que Lombok gère la liste sans initialisation explicite.
     */
    @Builder.Default      // ← garantit qu’on initialise même via builder()
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "ordonnance_id", nullable = false)
    private List<Prescription> prescriptions= new ArrayList<>();;
}
