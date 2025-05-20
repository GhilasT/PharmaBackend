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

    /** Médicament original (référence) */
    PRINCEPS(0),
    /** Médicament générique */
    GENERIQUE(1),
    /** Médicament de complémentarité thérapeutique */
    COMPLEMENTARITE(2),
    /** Médicament générique substituable */
    SUBSTITUABLE(4);

    /** Code numérique associé à chaque type de générique. */
    private final int code;
}