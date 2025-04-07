package l3o2.pharmacie.api.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Classe représentant un client dans le système.
 * Hérite de la classe Personne et ajoute des attributs spécifiques aux clients.
 */
@Entity
// Valeur qui sera stockée dans la colonne "user_type" de la table "personnes".
@DiscriminatorValue("clients")
// Génère automatiquement les getters, setters et les méthodes toString, equals, hashCode.
@Data
// Génère un constructeur sans attributs.
@NoArgsConstructor
// Génère un constructeur avec tous les attributs.
@AllArgsConstructor
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation.
@SuperBuilder
public class Client extends Personne {

    @Column(nullable = true)
    // Numéro de sécurité sociale du client (optionnel).
    private String numeroSecu;

    @Column(nullable = true)
    // Mutuelle associée au client (optionnel).
    private String mutuelle;


}