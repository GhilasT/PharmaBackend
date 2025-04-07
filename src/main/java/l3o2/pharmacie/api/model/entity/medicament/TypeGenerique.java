package l3o2.pharmacie.api.model.entity.medicament;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Enumération représentant les différents types de médicaments génériques
 * Chaque type est associé à un code numérique spécifique utilisé dans la BDPM
 */
@Getter
// Génère un constructeur avec tous les paramètres
@AllArgsConstructor
public enum TypeGenerique {

    PRINCEPS(0),           // Médicament original (référence)
    GENERIQUE(1),          // Médicament générique
    COMPLEMENTARITE(2),    // Médicament de complémentarité thérapeutique
    SUBSTITUABLE(4);       // Médicament générique substituable

    // Code numérique associé à chaque type de générique
    private final int code;
}