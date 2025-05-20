package l3o2.pharmacie.api.model.entity;

import jakarta.persistence.*;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe représentant une prescription médicale dans le système.
 * Contient les informations liées à la prescription d'un médicament à un patient.
 */
@Entity
// Définit le nom de la table associée dans la base de données.
@Table(name = "prescriptions")
// Génère automatiquement les getters, setters et les méthodes toString, equals, hashCode.
@Data
// Génère un constructeur sans attributs.
@NoArgsConstructor
// Génère un constructeur avec tous les attributs.
@AllArgsConstructor
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation.
@Builder
public class Prescription {

    @Id
    /** Identifiant unique de la Prescription (généré automatiquement). */
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, unique = true)
    private UUID idPrescription;

    @Column(nullable = false)
    /** Posologie indiquée pour le médicament prescrit (ou commentaire). */
    private String posologie;

    @Column(nullable = false)
    /** Quantité prescrite du médicament. */
    private int quantitePrescrite;

    @Column(nullable = true)
    /** Durée du traitement en jours. */
    private int duree;

    @Column(nullable = false, name = "medicament")
    /** Nom ou identifiant du médicament prescrit. */
    private String medicament;

}