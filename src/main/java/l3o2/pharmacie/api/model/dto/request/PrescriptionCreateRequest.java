package l3o2.pharmacie.api.model.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PrescriptionCreateRequest {
    private String medicament;
    private int quantitePrescrite;
    private int duree;
    private String posologie;
}
