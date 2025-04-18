package l3o2.pharmacie.api.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

@Getter
@Builder
public class StockDetailsDTO {
    private Integer quantite;
    private String numeroLot;
    private LocalDate datePeremption;
    private LocalDate dateMiseAJour;
    private Integer seuilAlerte;
    private String emplacement;
}