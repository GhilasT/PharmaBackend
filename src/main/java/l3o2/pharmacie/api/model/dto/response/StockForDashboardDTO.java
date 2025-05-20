package l3o2.pharmacie.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO représentant les informations de stock d'un médicament pour l'affichage sur le tableau de bord.
 * Contient le libellé et la quantité en stock.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockForDashboardDTO {

    /** Le libellé du médicament. */
    private String libelle;
    /** La quantité en stock du médicament. */
    private int stock;
    
    
}
