package l3o2.pharmacie.api.model.dto.request;

import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO pour la création d'une ligne de commande.
 * Représente un article spécifique et sa quantité dans une commande fournisseur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeCreateRequest {

    /**
     * L'identifiant unique du stock de médicament concerné par cette ligne de commande.
     */
    private Long stockMedicamentId;
    /**
     * La quantité du médicament commandée.
     */
    private int quantite;
    /**
     * Le prix unitaire du médicament pour cette commande.
     */
    private BigDecimal prixUnitaire;

}