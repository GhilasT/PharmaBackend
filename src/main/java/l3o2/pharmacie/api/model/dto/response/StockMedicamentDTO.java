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
    
    /** Code CIS (Code Identifiant de Spécialité) du médicament. */
    private String codeCIS;

    
    /** Identifiant unique du stock de médicament. */
    private long id;//modifier pour test 


    /** Code CIP13 (Code Identifiant de Présentation à 13 chiffres) du médicament. */
    private String codeCip13;

    
    /** Libellé de la présentation du médicament. */
    private String libelle;
    
    /** Dénomination commune du médicament (principe actif). */
    private String denomination;
    
    /** Dosage de la substance active du médicament. */
    private String dosage;
    
    /** Référence de dosage. */
    private String reference;
    
    /** Indique si le médicament est délivré sur ordonnance ("Avec" ou "Sans"). */
    private String surOrdonnance;
    
    /** Quantité du médicament actuellement en stock. */
    private int stock;
    
    /** Prix hors taxe du médicament. */
    private BigDecimal prixHT;
    
    /** Prix toutes taxes comprises du médicament. */
    private BigDecimal prixTTC;
    
    /** Montant de la taxe appliquée au médicament. */
    private BigDecimal taxe;
    
    /** Indique si le médicament est agréé aux collectivités (oui/non/inconnu). */
    private String agrementCollectivites;
    
    /** Taux de remboursement du médicament. */
    private String tauxRemboursement;
}
