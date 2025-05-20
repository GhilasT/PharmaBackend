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

    /**
     * L'identifiant unique du pharmacien adjoint effectuant la vente. Obligatoire.
     */
    @NotNull(message = "L'identifiant du pharmacien adjoint est obligatoire")
    private UUID pharmacienAdjointId;

    /**
     * L'identifiant unique du client pour lequel la vente est effectuée. Obligatoire.
     */
    @NotNull(message = "L'identifiant du client est obligatoire")
    private UUID clientId;

    /**
     * La liste des médicaments inclus dans la vente, avec leurs quantités. Obligatoire.
     */
    @NotNull(message = "La liste des médicaments est obligatoire")
    private List<MedicamentPanierRequest> medicaments;

    /**
     * La date à laquelle la vente est effectuée. Obligatoire.
     */
    @NotNull(message = "La date Vente est obligatoire")
    private Date dateVente;

    /**
     * Le mode de paiement utilisé pour la vente. Obligatoire.
     */
    @NotNull(message = "Le mode de paiement est obligatoire")
    private String modePaiement;

    /**
     * Le montant total de la vente. Obligatoire.
     */
    @NotNull(message = "Le montant total est obligatoire")
    private double montantTotal;

    /**
     * Le montant remboursé pour cette vente (par exemple, par une assurance). Obligatoire.
     */
    @NotNull(message = "Le montant remboursé est obligatoire")
    private double montantRembourse;

    /**
     * Indique si une ordonnance a été associée à cette vente.
     */
    private boolean ordonnanceAjoutee;
}