package l3o2.pharmacie.api.model.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Classe représentant une commande de médicaments dans le système.
 * Contient les informations relatives aux achats effectués par la pharmacie auprès des fournisseurs.
 */
@Entity
@Table(name = "commandes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false, unique = true, updatable = false)
    /** Référence unique de la commande. */
    private UUID reference;

    @Column(nullable = false)
    /** Date de la commande. */
    private Date dateCommande;

    @Column(nullable = false)
    /** Montant total de la commande. */
    private BigDecimal montantTotal;

    @Column(nullable = false)
    /** Statut de la commande (ex: En attente, Validée, Expédiée). */
    private String statut;

    @Column(name = "fournisseur_id", nullable = false)
    /** Identifiant du fournisseur associé à cette commande. */
    private UUID fournisseurId;

    @Column(name = "pharmacien_adjoint_id", nullable = false)
    /** Identifiant du pharmacien adjoint ayant passé cette commande. */
    private UUID pharmacienAdjointId;


    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    /** Liste des lignes de commande associées à cette commande. */
    private List<LigneCommande> ligneCommandes;


}