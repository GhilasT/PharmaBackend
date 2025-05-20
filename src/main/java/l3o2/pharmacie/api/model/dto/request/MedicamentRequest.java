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

    /**
     * Le code CIP (Code Identifiant de Présentation) à 13 chiffres du médicament.
     */
    private String codeCip13;
    /**
     * Le numéro de lot du médicament.
     */
    private String numeroLot;
    /**
     * La quantité du médicament en stock.
     */
    private Integer quantite;
    /**
     * La date de péremption du médicament.
     */
    private LocalDate datePeremption;
    /**
     * La date de la dernière mise à jour des informations du médicament.
     */
    private LocalDate dateMiseAJour;
    /**
     * Le seuil d'alerte pour le stock du médicament. Valeur par défaut : 6.
     */
    private Integer seuilAlerte = 6;
    /**
     * L'emplacement de stockage du médicament dans la pharmacie.
     */
    private String emplacement;
}