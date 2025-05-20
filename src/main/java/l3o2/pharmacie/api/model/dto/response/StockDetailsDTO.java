package l3o2.pharmacie.api.model.dto.response;

import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;

/**
 * DTO représentant les détails d'un stock spécifique de médicament.
 * Contient des informations telles que la quantité, le numéro de lot, la date de péremption, etc.
 */
@Getter
@Builder
public class StockDetailsDTO {
    /** La quantité de ce lot de médicament en stock. */
    private Integer quantite;
    /** Le numéro de lot du médicament. */
    private String numeroLot;
    /** La date de péremption du lot de médicament. */
    private LocalDate datePeremption;
    /** La date de dernière mise à jour des informations de ce stock. */
    private LocalDate dateMiseAJour;
    /** Le seuil d'alerte pour ce stock, indiquant une faible quantité. */
    private Integer seuilAlerte;
    /** L'emplacement de stockage de ce lot de médicament. */
    private String emplacement;
}