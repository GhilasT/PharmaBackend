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

    /** L'identifiant unique du médicament (ou du stock spécifique). */
    private Long id;
    /** Le code CIP13 (Code Identifiant de Présentation à 13 chiffres) du médicament. */
    private String codeCip13;
    /** La dénomination commune du médicament. */
    private String denomination;
    /** Le numéro de lot du médicament. */
    private String numeroLot;
    /** La quantité disponible de ce médicament/lot. */
    private Integer quantite;
    /** La date de péremption du médicament/lot. */
    private LocalDate datePeremption;
    /** La date de dernière mise à jour des informations de ce médicament/lot. */
    private LocalDate dateMiseAJour;
    /** Le seuil d'alerte pour ce médicament/lot, indiquant une faible quantité. */
    private Integer seuilAlerte;
    /** L'emplacement de stockage de ce médicament/lot. */
    private String emplacement;
}