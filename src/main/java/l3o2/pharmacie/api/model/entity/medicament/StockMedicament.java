package l3o2.pharmacie.api.model.entity.medicament;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stock_medicament")
public class StockMedicament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation avec la présentation via CIP13
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_cip13", referencedColumnName = "code_cip13")
    private CisCipBdpm presentation;

    @PositiveOrZero(message = "La quantité ne peut pas être négative")
    @Column(name = "quantite", nullable = false)
    private Integer quantite;

    @Column(name = "numero_lot", length = 50)
    private String numeroLot;

    @Future(message = "La date de péremption doit être dans le futur")
    @Column(name = "date_peremption")
    private LocalDate datePeremption;

    @Column(name = "date_mise_a_jour", nullable = false)
    private LocalDate dateMiseAJour = LocalDate.now();

    @Column(name = "seuil_alerte")
    @PositiveOrZero
    private Integer seuilAlerte;

    @Column(name = "emplacement", length = 100)
    private String emplacement;

}