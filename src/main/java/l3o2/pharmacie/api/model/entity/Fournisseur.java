package l3o2.pharmacie.api.model.entity;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Classe représentant un fournisseur dans le système.
 * Hérite de la classe Personne et ajoute des informations spécifiques aux fournisseurs.
 */
@Entity
// Valeur qui sera stockée dans la colonne "user_type" de la table "personnes".
@DiscriminatorValue("fournisseurs")
// Génère automatiquement les getters, setters et les méthodes toString, equals, hashCode.
@Data
// Génère un constructeur sans attributs.
@NoArgsConstructor
// Génère un constructeur avec tous les attributs.
@AllArgsConstructor
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation.
@SuperBuilder
public class Fournisseur extends Personne {

    @Column(nullable = true)
    // Nom de la société du fournisseur.
    private String societe;

    @Column(nullable = true)
    // Fonction ou rôle du fournisseur.
    private String sujetFonction;

    @Column(nullable = true)
    // Numéro de fax du fournisseur (optionnel).
    private String fax;

    @OneToMany(mappedBy = "fournisseur")
    // Liste des commandes associées au fournisseur.
    private List<Commande> commandes;

    /**
     * Ajoute une commande à la liste des commandes du fournisseur.
     * @param commande La commande à ajouter.
     */
    public void ajouterCommande(Commande commande) {
        commandes.add(commande);
    }
}