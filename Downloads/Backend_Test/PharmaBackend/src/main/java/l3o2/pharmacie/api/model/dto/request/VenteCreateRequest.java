package l3o2.pharmacie.api.model.dto.request;

//import l3o2.pharmacie.api.model.entity.medicament.Medicament;
import l3o2.pharmacie.api.model.entity.medicament.MedicamentPanier;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import lombok.Builder;
import lombok.Data;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour la création d'une vente.
 * Contient les données nécessaires à l'enregistrement d'une vente.
 */
@Builder
@Data
public class VenteCreateRequest {

    @NotNull(message = "L'identifiant du pharmacien adjoint est obligatoire")
    private UUID pharmacienAdjointId;

    @NotNull(message = "L'identifiant du client est obligatoire")
    private UUID clientId;

    @NotNull(message = "La liste des médicaments est obligatoire")
    private List<MedicamentPanier> medicaments;

    @NotNull(message = "La date Vente est obligatoire")
    private Date dateVente;

    @NotNull(message = "Le mode de paiement est obligatoire")
    private String modePaiement;

    @NotNull(message = "Le montant total est obligatoire")
    private double montantTotal;

    @NotNull(message = "Le montant remboursé est obligatoire")
    private double montantRembourse;
}