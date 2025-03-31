package l3o2.pharmacie.api.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Classe représentant un administrateur dans le système.
 * Hérite de la classe Employe et ajoute un attribut spécifique pour différencier les types d'administrateurs.
 */
@Entity
// Valeur qui sera stockée dans la colonne "type_employe" de la table "employes".
@DiscriminatorValue("administrateurs")
// Génère automatiquement les getters, setters et les méthodes toString, equals, hashCode.
@Data
// Génère un constructeur sans attributs.
@NoArgsConstructor
// Génère un constructeur avec tous les attributs.
@AllArgsConstructor
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation.
@SuperBuilder
public class Administrateur extends Employe {

    @Column(nullable = false)
    // Rôle de l'administrateur pour différencier le gestionnaire et le super-admin.
    private String role;

}