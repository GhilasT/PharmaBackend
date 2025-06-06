package l3o2.pharmacie.api.model.entity;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Classe abstraite représentant une personne dans le système.
 * Elle est utilisée comme classe mère pour les différentes catégories d'utilisateurs (ex: Administrateur, Client, Pharmacien).
 */
@Entity
// Definit le nom de la table associée dans la base de données.
@Table(name = "personnes")
// Strategie d'héritage : chaque sous-classe a sa propre table.
@Inheritance(strategy = InheritanceType.JOINED)
// Ajoute une colonne pour distinguer les types de personnes tel que : Client, Pharmacien...
@DiscriminatorColumn(name = "user_type")
// Génère automatiquement les getters, setters  et les toString
@Data
// Genere un constructeur sans attributs
@NoArgsConstructor
// Genere un constructeur avec tous les attributs
@AllArgsConstructor
// Permet d'utiliser le pattern Builder pour les sous-classes.
@SuperBuilder
public abstract class Personne {
    /** le @Getter est une annotation Lombok qui genere des getters pour tous les champs non statiques de la classe */
    @Getter

    @Id
    // Génèrer automatiquement un UUID comme identifiant unique
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    /** Identifiant unique de la personne. */
    private UUID idPersonne;

    @Column(nullable = false)
    /** Nom de la personne. */
    private String nom;

    @Column(nullable = false)
    /** Prénom de la personne. */
    private String prenom;

    @Column(nullable = true)
    /** Adresse email de la personne. */
    private String email;

    @Column(nullable = false)
    /** Numéro de téléphone de la personne. */
    private String telephone;

    @Column(nullable = true)
    /** Adresse postale de la personne (peut être nulle). */
    private String adresse;


}