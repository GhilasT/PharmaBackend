package l3o2.pharmacie.api.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * DTO représentant les détails complets d'un médicament, y compris ses informations de stock.
 * Utilisé pour fournir une vue détaillée d'un médicament spécifique.
 */
@Getter
@Builder
public class MedicamentDetailsDTO {
    /** La dénomination commune du médicament. */
    private String denomination;
    /** La forme pharmaceutique du médicament (ex: comprimé, sirop). */
    private String formePharmaceutique;
    /** Les voies d'administration du médicament (ex: orale, cutanée). */
    private String voiesAdministration;
    /** Le libellé de la présentation du médicament. */
    private String libellePresentation;
    /** Le taux de remboursement du médicament. */
    private String tauxRemboursement;
    /** Le prix hors taxe du médicament. */
    private BigDecimal prixHT;
    /** Le prix toutes taxes comprises du médicament. */
    private BigDecimal prixTTC;
    /** Le montant de la taxe appliquée au médicament. */
    private BigDecimal taxe;
    /** Les indications ouvrant droit au remboursement. */
    private String indicationsRemboursement;
    /** La liste des détails des différents stocks pour ce médicament. */
    private List<StockDetailsDTO> stocks;
}