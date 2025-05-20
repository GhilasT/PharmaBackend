package l3o2.pharmacie.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

/**
 * DTO représentant la réponse contenant les informations d'un fournisseur.
 * Utilisé pour éviter d'exposer les entités directement.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurResponse {
    /** L'identifiant unique du fournisseur. */
    private UUID idFournisseur;
    /** Le nom de la société du fournisseur. */
    private String nomSociete;
    /** Le sujet ou la fonction du contact chez le fournisseur. */
    private String sujetFonction;
    /** Le numéro de téléphone du fournisseur. */
    private String telephone;
    /** Le numéro de fax du fournisseur. */
    private String fax;
    /** L'adresse postale du fournisseur. */
    private String adresse;
    /** L'adresse email du fournisseur. */
    private String email;
}