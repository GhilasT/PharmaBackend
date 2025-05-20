package l3o2.pharmacie.api.model.entity.medicament;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

/**
 * Classe représentant un médicament stocké en pharmacie.
 * Contient les informations sur l’identification, la quantité en stock et les caractéristiques du médicament.
 */
@Getter
@Setter
// Génère automatiquement un constructeur sans paramètres.
@NoArgsConstructor
// Génère automatiquement un constructeur avec tous les paramètres.
@AllArgsConstructor
// Indique que cette classe est une entité JPA.
@Entity
// Définit le nom de la table associée dans la base de données.
@Table(name = "stock_medicament")
public class StockMedicament {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identifiant unique du médicament dans la pharmacie. */
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_cip13", referencedColumnName = "code_cip13", nullable = false)
    /** Relation avec la présentation du médicament via son code CIP13. */
    private CisCipBdpm presentation;

    @PositiveOrZero(message = "La quantité ne peut pas être négative")
    @Column(name = "quantite", nullable = false)
    /** Quantité du médicament en stock. */
    private Integer quantite;

    @Column(name = "numero_lot", length = 50)
    /** Numéro de lot du médicament. */
    private String numeroLot;

    @Column(name = "date_peremption")
    /** Date de péremption du médicament (doit être dans le futur). */
    private LocalDate datePeremption;

    @Column(name = "date_mise_a_jour", nullable = false)
    /** Date de la dernière mise à jour de l'enregistrement. */
    private LocalDate dateMiseAJour = LocalDate.now();

    @Column(name = "seuil_alerte")
    @PositiveOrZero
    /** Seuil d'alerte pour le stock du médicament. La valeur par défaut est 6. */
    private Integer seuilAlerte = 6;

    @Column(name = "emplacement", length = 100)
    /** Emplacement physique du médicament dans la pharmacie. */
    private String emplacement;

}