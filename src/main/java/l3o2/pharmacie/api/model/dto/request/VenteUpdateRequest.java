package l3o2.pharmacie.api.model.dto.request;

import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour la mise à jour d'une vente.
 * Contient les données nécessaires à la modification d'une vente existante.
 */
@Builder
@Data
public class VenteUpdateRequest {

    private UUID pharmacienAdjointId;
    private UUID clientId;
    private Date dateVente;
    private String modePaiement;
    private Boolean ordonnanceAjoutee;
    
    // Ajout de la liste des médicaments pour la mise à jour
    private List<MedicamentPanierRequest> medicaments;
}
