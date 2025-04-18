package l3o2.pharmacie.api.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class MedicamentDetailsDTO {
    private String denomination;
    private String formePharmaceutique;
    private String voiesAdministration;
    private String libellePresentation;
    private String tauxRemboursement;
    private BigDecimal prixHT;
    private BigDecimal prixTTC;
    private BigDecimal taxe;
    private String indicationsRemboursement;
    private List<StockDetailsDTO> stocks;
}