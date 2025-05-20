package l3o2.pharmacie.api.model.dto.request;

import lombok.*;

import java.util.UUID;

/**
 * DTO représentant un médicament dans un panier ou une vente.
 * Contient l'identifiant du médicament et la quantité souhaitée.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedicamentPanierRequest {

    /**
     * Le code CIP (Code Identifiant de Présentation) à 13 chiffres du médicament.
     */
    private String codeCip13;
    /**
     * La quantité du médicament demandée.
     */
    private int quantite;
}