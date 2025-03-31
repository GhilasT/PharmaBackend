package l3o2.pharmacie.api.model.entity;

import l3o2.pharmacie.api.model.entity.medicament.Medicament;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
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
    private UUID idVente;  // Identifiant unique de la vente.

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateVente;  // Date et heure de la vente.

    @Column(nullable = false, precision = 10, scale = 2)
    // pour garantir une précision optimale des valeurs "BigDecimal"
    private BigDecimal montantTotal;  // Montant total de la vente (TTC).

    @Column(nullable = false)
    private String modePaiement;  // Mode de paiement (ex: Carte, Espèces).

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal montantRembourse;  // Montant remboursé par la mutuelle.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pharmacien_id", nullable = false)
    private PharmacienAdjoint pharmacienAdjoint;  // Pharmacien ayant effectué la vente.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;  // Client ayant effectué l'achat.


    //  "CascadeType.ALL" pour Assure que les médicaments liés à une vente sont bien persistés automatiquement.
    // "FetchType.LAZY" pour que client et phramcien ne seront chargés qu’en cas de besoin
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "ventes_medicaments",
            joinColumns = @JoinColumn(name = "vente_id"),
            inverseJoinColumns = @JoinColumn(name = "medicament_id")
    )
    private List<Medicament> medicaments;  // Liste des médicaments vendus.
}