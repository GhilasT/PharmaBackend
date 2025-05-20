package l3o2.pharmacie.api.model.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import l3o2.pharmacie.api.model.entity.medicament.MedicamentPanier;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import lombok.*;

/**
 * Classe représentant une vente de médicaments dans le système.
 * Contient les informations relatives aux transactions effectuées en pharmacie.
 */
@Entity

@Table(name = "ventes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Vente {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, unique = true)
    /** Identifiant unique de la vente, généré automatiquement. */
    private UUID idVente;

    @Column(nullable = false)
    /** Montant total de la vente. */
    private double montantTotal;

    @Column(nullable = false)
    /** Mode de paiement utilisé pour la vente. */
    private String modePaiement;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    /** Date et heure de la vente. */
    private Date dateVente ;

    @Column(nullable = false)
    /** Montant remboursé au client (par la sécurité sociale ou mutuelle). */
    private double montantRembourse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacien_id", nullable = false)
    /** Pharmacien adjoint ayant effectué la vente. */
    private PharmacienAdjoint pharmacienAdjoint;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    /** Client concerné par la vente. */
    private Client client;

    @OneToMany(mappedBy = "vente", cascade = CascadeType.ALL, orphanRemoval = true)
    /** Liste des médicaments inclus dans cette vente (panier). */
    private List<MedicamentPanier> medicamentsPanier;

    /**
     * Retourne une représentation textuelle de l'objet Vente.
     * @return Une chaîne de caractères représentant l'objet.
     */
    @Override
    public String toString() {
        return "Vente{" +
                "idVente=" + idVente +
                ", dateVente=" + dateVente +
                ", modePaiement='" + modePaiement + '\'' +
                ", montantTotal=" + montantTotal +
                ", montantRembourse=" + montantRembourse +
                ", pharmacienAdjoint=" + (pharmacienAdjoint != null ? pharmacienAdjoint.getIdPersonne() : null) +
                ", client=" + (client != null ? client.getIdPersonne() : null) +
                ", medicamentsPanier=" + (medicamentsPanier != null ? medicamentsPanier.size() : 0) +
                '}';
    }

}