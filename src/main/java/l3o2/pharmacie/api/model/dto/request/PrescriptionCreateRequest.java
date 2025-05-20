package l3o2.pharmacie.api.model.dto.request;

import lombok.Builder;
import lombok.Data;

/**
 * DTO pour la création d'une prescription médicale.
 * Contient les informations détaillées d'un médicament prescrit.
 */
@Data
@Builder
public class PrescriptionCreateRequest {
    /**
     * Le nom ou l'identifiant du médicament prescrit.
     */
    private String medicament;
    /**
     * La quantité du médicament prescrite.
     */
    private int quantitePrescrite;
    /**
     * La durée du traitement en jours.
     */
    private int duree;
    /**
     * La posologie du médicament (ex: "1 comprimé matin et soir").
     */
    private String posologie;
}
