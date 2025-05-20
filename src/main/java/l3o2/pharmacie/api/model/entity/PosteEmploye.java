package l3o2.pharmacie.api.model.entity;

/**
 * Enumération représentant les différents postes qu'un employé peut occuper.
 */
public enum PosteEmploye {
    /** Poste de Pharmacien Titulaire. */
    TITULAIRE,
    /** Poste de Pharmacien Adjoint. */
    PHARMACIEN_ADJOINT,
    /** Poste de Préparateur en pharmacie. */
    PREPARATEUR,
    /** Poste d'Apprenti. */
    APPRENTI,
    /** Poste d'Administrateur du système. */
    ADMINISTRATEUR
}