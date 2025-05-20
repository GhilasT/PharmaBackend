package l3o2.pharmacie.api.model.entity;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Classe représentant un pharmacien adjoint dans le système.
 * Hérite de la classe Employe et ajoute des informations spécifiques aux pharmaciens adjoints.
 */
@Entity
// Valeur qui sera stockée dans la colonne "type_employe" de la table "employes".
@DiscriminatorValue("pharmacien_adjoint")
// Génère automatiquement les getters, setters et les méthodes toString, equals, hashCode.
@Data
// Génère un constructeur sans attributs.
@NoArgsConstructor
// Génère un constructeur avec tous les attributs.
@AllArgsConstructor
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation.
@SuperBuilder
public class PharmacienAdjoint extends Employe {

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacien_adjoint_id")
    /** Liste des commandes passées par ce pharmacien adjoint. */
    private List<Commande> commandesPassees;
}