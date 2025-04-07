package l3o2.pharmacie.api.model.entity;

//import l3o2.pharmacie.api.model.entity.medicament.Medicament;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import l3o2.pharmacie.api.model.entity.medicament.MedicamentPanier;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import lombok.*;

/**
 * Classe représentant une vente de médicaments dans le système.
 * Contient les informations relatives aux transactions effectuées en pharmacie.
 */
@Entity
@Table(name = "ventes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Vente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID idVente;

    @Column(nullable = false)

    private double montantTotal;

    @Column(nullable = false)
    private String modePaiement;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateVente ;

    @Column(nullable = false)
    private double montantRembourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacien_id", nullable = false)
    private PharmacienAdjoint pharmacienAdjoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;



    @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MedicamentPanier> medicamentsPanier;



}