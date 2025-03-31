package l3o2.pharmacie.api.model.entity;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
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
public class Fournisseur {
    @Getter

    @Id
    // Génèrer automatiquement un UUID comme identifiant unique
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)

    private UUID idFournisseur;


    @Column(nullable = false)
    // Nom de la société du fournisseur.
    private String nomsociete;

    @Column(nullable = true)
    // Fonction ou rôle du fournisseur.
    private String sujetFonction;

    @Column(nullable = true)
    // Numéro de fax du fournisseur (optionnel).
    private String fax;

    @Column(nullable = false)
    // Adresse email de la personne
    private String email;

    @Column(nullable = false)
    // Numéro de téléphone
    private String telephone;

    @Column(nullable = false)

    private String adresse;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "fournisseur_id")
    private List<Commande> commandes;


}