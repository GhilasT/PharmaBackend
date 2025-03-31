package l3o2.pharmacie.api.model.entity;

import java.util.List;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
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

    // Liste des commandes passées par le pharmacien adjoint + si un pharmacien adjoint est supprimé, ses commandes associées sont aussi supprimées
    @OneToMany(mappedBy = "pharmacienAdjoint", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<Commande> commandesPassees;
}