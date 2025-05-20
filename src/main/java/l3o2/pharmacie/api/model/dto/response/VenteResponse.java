package l3o2.pharmacie.api.model.dto.response;

import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
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

    /** L'identifiant unique de la vente. */
    private UUID idVente;
    /** La date à laquelle la vente a été effectuée. */
    private Date dateVente;
    /** Le mode de paiement utilisé pour la vente. */
    private String modePaiement;
    /** Le montant total de la vente. */
    private double montantTotal;
    /** Le montant remboursé pour la vente. */
    private double montantRembourse;
    /** L'identifiant du pharmacien adjoint ayant effectué la vente. */
    private UUID pharmacienAdjointId;
    /** L'identifiant du client concerné par la vente. */
    private UUID clientId;
    /** La liste des médicaments vendus, représentés par des MedicamentResponse. */
    private List<MedicamentResponse> medicamentIds;
    /** Une notification relative à la vente. */
    private String notification;
}