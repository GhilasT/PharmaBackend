package l3o2.pharmacie.api.model.entity.medicament;

import jakarta.persistence.*;
import l3o2.pharmacie.api.model.entity.Vente;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "medicaments_panier")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicamentPanier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMedicamentPanier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vente_id", nullable = false)
    private Vente vente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_medicament_id", referencedColumnName = "id", nullable = false)
    private StockMedicament stockMedicament;

    @Column(nullable = false)
    private int quantite;

    public MedicamentPanier(StockMedicament med1, int quantite) {
        this.stockMedicament = med1;
        this.quantite = quantite;
    }

    // verifier le stock  si il est suffisant
    public boolean isStockSuffisant() {
        return stockMedicament.getQuantite() >= quantite;
    }

    //  decrementer le stock après la vente
    public void decrementerStock() {
        stockMedicament.setQuantite(stockMedicament.getQuantite() - quantite);
    }
}

