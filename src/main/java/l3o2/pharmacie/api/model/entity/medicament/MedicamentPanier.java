package l3o2.pharmacie.api.model.entity.medicament;

import jakarta.persistence.*;
import l3o2.pharmacie.api.model.entity.Vente;
import lombok.*;

/**
 * Représente un médicament ajouté à un panier de vente.
 * Cette classe lie un {@link StockMedicament} à une {@link Vente} avec une quantité spécifique.
 */
@Entity
@Table(name = "medicaments_panier")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicamentPanier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identifiant unique du médicament dans le panier. */
    private Long idMedicamentPanier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vente_id", nullable = false)
    /** Vente à laquelle ce médicament du panier est associé. */
    private Vente vente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_medicament_id", referencedColumnName = "id", nullable = false)
    /** Référence au médicament en stock concerné par cet item du panier. */
    private StockMedicament stockMedicament;

    @Column(nullable = false)
    /** Quantité de ce médicament dans le panier. */
    private int quantite;

    /**
     * Construit une instance de MedicamentPanier.
     * @param med1 Le médicament en stock.
     * @param quantite La quantité du médicament.
     */
    public MedicamentPanier(StockMedicament med1, int quantite) {
        this.stockMedicament = med1;
        this.quantite = quantite;
    }

    /**
     * Vérifie si la quantité en stock du médicament est suffisante par rapport à la quantité demandée.
     * @return {@code true} si le stock est suffisant, {@code false} sinon.
     */
    public boolean isStockSuffisant() {
        return stockMedicament.getQuantite() >= quantite;
    }

    /**
     * Décrémente la quantité en stock du médicament après une vente.
     * La quantité décrémentée correspond à la quantité de ce médicament dans le panier.
     */
    public void decrementerStock() {
        stockMedicament.setQuantite(stockMedicament.getQuantite() - quantite);
    }

    /**
     * Retourne une représentation textuelle de l'objet MedicamentPanier.
     * @return Une chaîne de caractères représentant l'objet.
     */
    @Override
    public String toString() {
        return "MedicamentPanier{" +
                "idMedicamentPanier=" + idMedicamentPanier +
                ", stockMedicament=" + (stockMedicament != null ? stockMedicament.getId() : null) +
                ", quantite=" + quantite +
                ", vente=" + (vente != null ? vente.getIdVente() : null) +
                '}';
    }
}

