package l3o2.pharmacie.api.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * Classe représentant un préparateur en pharmacie dans le système.
 * Hérite de la classe Employe et ne nécessite pas d'attributs supplémentaires,
 * car les informations spécifiques (matricule, diplome, etc.) sont déjà présentes dans Employe.
 */
@Entity
// Valeur qui sera stockée dans la colonne "type_employe" de la table "employes".
@DiscriminatorValue("preparateurs")
// Génère automatiquement les getters, setters et les méthodes toString, equals, hashCode.
@Data
@AllArgsConstructor
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation.
@SuperBuilder
public class Preparateur extends Employe {
}