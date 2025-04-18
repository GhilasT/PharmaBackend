package l3o2.pharmacie.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

/**
 * DTO renvoyé lors de la lecture (ou la création) d'un Medicament.
 */
@Data
@Builder
// constructeur public pour accéder depuis d'autres packages
@AllArgsConstructor
public class MedicamentResponse {

    private Long id;
    private String codeCip13;
    private String denomination;
    private String numeroLot;
    private Integer quantite;
    private LocalDate datePeremption;
    private LocalDate dateMiseAJour;
    private Integer seuilAlerte;
    private String emplacement;
}