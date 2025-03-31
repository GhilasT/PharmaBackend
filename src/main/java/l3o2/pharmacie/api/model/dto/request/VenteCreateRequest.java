package l3o2.pharmacie.api.model.dto.request;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour la création d'une vente.
 * Contient les données nécessaires à l'enregistrement d'une vente.
 */
@Data
public class VenteCreateRequest {

    @NotNull(message = "L'identifiant du pharmacien adjoint est obligatoire")
    private UUID pharmacienAdjointId;

    @NotNull(message = "L'identifiant du client est obligatoire")
    private UUID clientId;

    @NotNull(message = "La liste des médicaments est obligatoire")
    private List<UUID> medicamentIds;

    @NotNull(message = "Le mode de paiement est obligatoire")
    private String modePaiement;

    @NotNull(message = "Le montant total est obligatoire")
    private Float montantTotal;

    @NotNull(message = "Le montant remboursé est obligatoire")
    private Float montantRembourse;
}