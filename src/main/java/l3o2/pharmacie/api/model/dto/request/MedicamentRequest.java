package l3o2.pharmacie.api.model.dto.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO utilisé pour la création ou la mise à jour d'un Medicament.
 */
@Data
@Builder
public class MedicamentRequest {


    private String codeCip13;
    private String numeroLot;
    private Integer quantite;
    private LocalDate datePeremption;
    private LocalDate dateMiseAJour;
    private Integer seuilAlerte = 6;
    private String emplacement;
}