package l3o2.pharmacie.api.model.dto.response;

import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
  * Format de réponse pour une ligne de commande 
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeResponse {

    //private StockMedicament stockMedicament;
    /** L'identifiant du stock de médicament concerné par cette ligne de commande. */
    private Long stockMedicamentId;
    /** Les détails du stock de médicament (DTO) pour cette ligne de commande. */
    private StockMedicamentDTO stockMedicamentDTO;
    /** La quantité commandée pour ce médicament. */
    private int quantite;
    /** Le prix unitaire du médicament dans cette ligne de commande. */
    private BigDecimal prixUnitaire;
    /** Le montant total pour cette ligne de commande (quantité * prix unitaire). */
    private BigDecimal montantLigne;
}