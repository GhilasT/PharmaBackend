package l3o2.pharmacie.api.model.dto.response;

import l3o2.pharmacie.api.model.entity.Fournisseur;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO représentant la réponse contenant les informations d'une commande fournisseur.
 * Utilisé pour éviter d'exposer les entités directement.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeResponse {

    /** La référence unique de la commande. */
    private UUID reference;
    /** La date à laquelle la commande a été passée. */
    private Date dateCommande;
    /** Le montant total de la commande. */
    private BigDecimal montantTotal;
    /** Le statut actuel de la commande (ex: En cours, Livrée, Annulée). */
    private String statut;
    /** L'identifiant du fournisseur associé à la commande. */
    private UUID fournisseurId;
    /** L'identifiant du pharmacien adjoint ayant passé la commande. */
    private UUID pharmacienAdjointId;
    /** La liste des lignes de commande incluses dans cette commande. */
    private List<LigneCommandeResponse> ligneCommandes;
}