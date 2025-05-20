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
    /** Identifiant unique de l'ordonnance, généré automatiquement. */
    private UUID idOrdonnance;

    @Column(nullable = false)
    /** Date d'émission de l'ordonnance. */
    private Date dateEmission;

    @Column(nullable = false)
    /** Numéro RPPS du médecin prescripteur. */
    private String rppsMedecin;
    
    @Column(name="numeroord", nullable=true)
    /** Numéro unique de l'ordonnance (peut être fourni par le système externe ou généré). */
    private String numeroOrd;

    /** Relation vers le client pour lequel l'ordonnance a été émise. */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    /**
     * Chaque ordonnance peut comprendre plusieurs prescriptions.
     * Relation unidirectionnelle, la clé étrangère (FK) est dans la table des prescriptions.
     * L'annotation {@code @Builder.Default} garantit l'initialisation de la liste même via le builder.
     */
    @Builder.Default
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JoinColumn(name = "ordonnance_id", nullable = false)
    private List<Prescription> prescriptions= new ArrayList<>();;
}
