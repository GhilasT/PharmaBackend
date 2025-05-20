package l3o2.pharmacie.api.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Classe représentant un pharmacien titulaire dans le système.
 * Hérite de la classe Administrateur et ajoute un identifiant RPPS spécifique aux pharmaciens titulaires.
 */
@Entity
// Valeur qui sera stockée dans la colonne "type_employe" de la table "employes".
@DiscriminatorValue("TITULAIRE")
// Génère automatiquement les getters, setters et les méthodes toString, equals, hashCode.
@Data
// Génère un constructeur sans attributs.
@NoArgsConstructor
// Génère un constructeur avec tous les attributs.
@AllArgsConstructor
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation.
@SuperBuilder
public class Titulaire extends Administrateur {

    @Column(nullable = false, unique = true)
    /** Numéro RPPS du pharmacien titulaire (obligatoire et unique). */
    private String numeroRPPS;

}