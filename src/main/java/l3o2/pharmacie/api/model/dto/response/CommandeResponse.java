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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommandeResponse {

    private UUID reference;
    private Date dateCommande;
    private BigDecimal montantTotal;
    private String statut;
    private UUID fournisseurId;
    private UUID pharmacienAdjointId;
    private List<LigneCommandeResponse> ligneCommandes;
}