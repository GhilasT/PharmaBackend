package l3o2.pharmacie.api.model.dto.request;

import l3o2.pharmacie.api.model.entity.Fournisseur;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeCreateRequest {

    private UUID fournisseurId;
    private UUID pharmacienAdjointId;
    private List<LigneCommandeCreateRequest> ligneCommandes;
}