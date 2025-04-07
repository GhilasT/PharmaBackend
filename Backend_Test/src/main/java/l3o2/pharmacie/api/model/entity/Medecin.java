package l3o2.pharmacie.api.model.entity;

import java.time.LocalDate;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Classe représentant un médecin dans le système.
 * Hérite de la classe Personne et ajoute des informations spécifiques aux médecins.
 */
@Entity
// Valeur qui sera stockée dans la colonne "user_type" de la table "personnes".
@DiscriminatorValue("medecins")
// Génère automatiquement les getters, setters et les méthodes toString, equals, hashCode.
@Data
// Génère un constructeur sans attributs.
@NoArgsConstructor
// Génère un constructeur avec tous les attributs.
@AllArgsConstructor
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation.
@SuperBuilder
public class Medecin extends Personne {

    @Column(nullable = false, unique = true)
    // Numéro RPPS du médecin (obligatoire et unique).
    private String rpps;

    @Column(nullable = false, unique = true)
    // Numéro ADELI du médecin (obligatoire et unique).
    private String adeli;

    @Column(nullable = false)
    // Civilité du médecin (ex: Dr, Prof.).
    private String civilite;

    @Column(nullable = false)
    // Profession médicale exercée.
    private String profession;

    @Column(nullable = false)
    // Spécialité principale du médecin.
    private String specialitePrincipale;

    @Column(nullable = true)
    // Spécialité secondaire du médecin (si applicable).
    private String specialiteSecondaire;

    @Column(nullable = false)
    // Mode d'exercice (libéral, salarié, mixte).
    private String modeExercice;

    @Column(nullable = false)
    // Code postal de l'adresse du cabinet médical.
    private String codePostal;

    @Column(nullable = false)
    // Ville du cabinet médical.
    private String ville;

    @Column(nullable = true)
    // Site web professionnel du médecin.
    private String siteWeb;

    @Column(nullable = false)
    // Secteur d'activité.
    private String secteur;

    @Column(nullable = false)
    // Type de conventionnement du médecin.
    private String conventionnement;

    @Column(nullable = false)
    // Informations sur les honoraires pratiqués.
    private String honoraires;

    @ElementCollection
    @Column(nullable = true)
    // Langues parlées par le médecin (stockées sous forme de liste).
    private List<String> languesParlees;

    @Column(nullable = false, unique = true)
    // Numéro SIRET du cabinet médical.
    private String siret;

    // Date de dernière mise à jour des informations.
    @Column(nullable = false)
    private LocalDate dateMiseAJour = LocalDate.now();

}