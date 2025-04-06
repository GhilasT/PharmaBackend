package l3o2.pharmacie.api.model.dto.request;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicamentPanierRequest {

    private Long id;
    private int quantite;
}