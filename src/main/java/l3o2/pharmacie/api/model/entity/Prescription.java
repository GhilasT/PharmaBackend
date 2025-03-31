package l3o2.pharmacie.api.model.entity;

import l3o2.pharmacie.api.model.entity.medicament.Medicament;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    @GeneratedValue(strategy = GenerationType.UUID)
    // Identifiant unique de la prescription.
    private UUID idPrescription;

    @Column(nullable = false)
    // Posologie indiquée pour le médicament prescrit.
    private String posologie;

    @Column(nullable = false)
    // Quantité prescrite du médicament.
    private int quantitePrescrite;

    @Column(nullable = false)
    // Durée du traitement en jours.
    private int duree;

    @OneToOne
    // Médicament associé à la prescription.
    private Medicament medicament;
    @ManyToOne
    @JoinColumn(name = "id_ordonnance")
    private Ordonnance ordonnance;


}