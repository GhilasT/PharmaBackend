package l3o2.pharmacie.api.model.entity;

import l3o2.pharmacie.api.model.entity.medicament.Medicament;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe représentant une ligne de commande dans le système.
 * Contient les informations détaillées sur les médicaments commandés auprès d'un fournisseur.
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Identifiant unique de la ligne de commande.
    private Long idLigneCommande;

    @Column(nullable = false)
    // Quantité commandée du médicament.
    private int quantite;

    @Column(nullable = false)
    // Prix unitaire du médicament commandé.
    private Double prixUnitaire;

    @ManyToOne
    // Médicament associé à cette ligne de commande.
    private Medicament medicament;

    @ManyToOne
    // Commande associée à cette ligne de commande.
    private Commande commande;

}