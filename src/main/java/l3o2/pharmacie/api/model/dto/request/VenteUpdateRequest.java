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

    /**
     * L'identifiant unique du pharmacien adjoint associé à la vente.
     */
    private UUID pharmacienAdjointId;
    /**
     * L'identifiant unique du client associé à la vente.
     */
    private UUID clientId;
    /**
     * La date à laquelle la vente a été effectuée.
     */
    private Date dateVente;
    /**
     * Le mode de paiement utilisé pour la vente (ex: Carte, Espèces).
     */
    private String modePaiement;
    /**
     * Indique si une ordonnance a été ajoutée pour cette vente.
     */
    private Boolean ordonnanceAjoutee;
    
    /**
     * La liste des médicaments et leurs quantités pour la mise à jour de la vente.
     */
    private List<MedicamentPanierRequest> medicaments;
}
