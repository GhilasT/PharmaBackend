package l3o2.pharmacie.api.model.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
/**
 * DTO pour la réponse de l'API concernant les médicaments.
 * Contient les informations nécessaires pour l'affichage dans le frontend.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockMedicamentDTO {
    
    // Code CIS du médicament
    private String codeCIS;

    
    private long id;//modifier pour test 


    // Code CIP13 du médicament
    private String codeCip13;

    
    // Libellé de la présentation
    private String libelle;
    
    // Dénomination du médicament (à partir de CisBdpm)
    private String denomination;
    
    // Dosage de la substance
    private String dosage;
    
    // Référence de dosage
    private String reference;
    
    // Indique si le médicament est sur ordonnance ("Avec" ou "Sans")
    private String surOrdonnance;
    
    // Quantité en stock
    private int stock;
    
    // Prix hors taxe
    private BigDecimal prixHT;
    
    // Prix TTC
    private BigDecimal prixTTC;
    
    // Montant de la taxe
    private BigDecimal taxe;
    
    // Agrément aux collectivités (oui/non/inconnu)
    private String agrementCollectivites;
    
    // Taux de remboursement
    private String tauxRemboursement;
}
