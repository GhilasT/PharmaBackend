package l3o2.pharmacie.api.model.entity;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe représentant une ordonnance médicale dans le système.
 * Contient les informations relatives à la prescription de médicaments par un médecin.
 */
@Entity
// Définit le nom de la table associée dans la base de données.
@Table(name = "ordonnances")
// Génère automatiquement les getters, setters et les méthodes toString, equals, hashCode.
@Data
// Génère un constructeur sans attributs.
@NoArgsConstructor
// Génère un constructeur avec tous les attributs.
@AllArgsConstructor
// Permet d'utiliser le pattern Builder pour faciliter l'instanciation.
@Builder
public class Ordonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    // Identifiant unique de l'ordonnance.
    @Column(nullable = false, updatable = false, unique = true)
    private UUID idOrdonnance;

    @Column(nullable = false, unique = true)
    // Numéro unique de l'ordonnance.
    private String numeroOrd;

    @Column(nullable = false)
    // Date d'émission de l'ordonnance.
    private Date dateEmission;

    @Column(nullable = true)
    // Date d'expiration de l'ordonnance.
    private Date dateExpiration;

    @Column(nullable = false)
    // Numéro RPPS du médecin ayant prescrit l'ordonnance.
    private String rppsMedecin;

    @ManyToOne
    // Client (patient) associé à l'ordonnance.
    private Client client;

    @ManyToOne
    // Médecin ayant prescrit l'ordonnance.
    private Medecin medecin;

    @OneToMany(mappedBy = "ordonnance")
    // Liste des prescriptions associées à l'ordonnance.
    private List<Prescription> prescriptions;

}