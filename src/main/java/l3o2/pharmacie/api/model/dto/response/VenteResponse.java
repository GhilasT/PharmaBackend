package l3o2.pharmacie.api.model.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO représentant la réponse contenant les informations d'une vente.
 * Utilisé pour éviter d'exposer l'entité directement.
 */
@Data
@Builder
public class VenteResponse {

    private UUID idVente;
    private Date dateVente;
    private String modePaiement;
    private Float montantTotal;
    private Float montantRembourse;
    private UUID pharmacienAdjointId;
    private UUID clientId;
    private List<UUID> medicamentIds;
}