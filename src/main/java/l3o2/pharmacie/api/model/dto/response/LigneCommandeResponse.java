package l3o2.pharmacie.api.model.dto.response;

import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeResponse {

    private StockMedicament stockMedicament;
    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal montantLigne;
}