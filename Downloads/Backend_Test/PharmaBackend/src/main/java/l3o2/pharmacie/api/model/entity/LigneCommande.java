package l3o2.pharmacie.api.model.entity;

import jakarta.persistence.*;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Classe représentant une ligne de commande dans le système.
 * Contient les informations détaillées sur un médicament commandé auprès d'un fournisseur.
 */
@Entity
// Définit le nom de la table associée dans la base de données.
@Table(name = "lignes_commandes")
// Génère automatiquement les getters, setters et les méthodes toString, equals, hashCode.
@Data
// Génère un constructeur sans attributs.
@NoArgsConstructor
// Génère un constructeur avec tous les attributs.
@AllArgsConstructor
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation.
@Builder

public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commande_id", nullable = false)
    private Commande commande;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_medicament_id", referencedColumnName = "id", nullable = false)
    private StockMedicament stockMedicament;

    @Column(nullable = false)
    private int quantite;

    @Column(nullable = false)
    private BigDecimal prixUnitaire;

    @Column(nullable = false)
    private BigDecimal montantLigne;

    //cette methode sera executee avant l'insertion de la ligne de commande dans la base de donnees grace au : @PrePersist
    @PrePersist

    public void calculerMontantLigneAvantSauvegarde() {


            // Calculer le prix unitaire basé sur 70% du prix HT
            this.prixUnitaire = stockMedicament.getPresentation().getPrixUnitaireAvecReduction();

            // Calculer le montant total de la ligne de commande
            this.montantLigne = prixUnitaire.multiply(BigDecimal.valueOf(quantite));

    }
}

