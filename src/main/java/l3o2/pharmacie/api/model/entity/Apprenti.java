package l3o2.pharmacie.api.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Classe représentant un apprenti en pharmacie dans le système.
 * Hérite de la classe Employe et ajoute des informations spécifiques aux apprentis.
 */
@Entity
// Valeur qui sera stockée dans la colonne "type_employe" de la table "employes".
@DiscriminatorValue("apprentis")
// Génère automatiquement les getters, setters et les méthodes toString, equals, hashCode.
@Data
// Génère un constructeur sans attributs.
@NoArgsConstructor
// Génère un constructeur avec tous les attributs.
@AllArgsConstructor
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation.
@SuperBuilder
public class Apprenti extends Employe {

    @Column(nullable = false)
    /** Nom de l'école de formation de l'apprenti. */
    private String ecole;
}