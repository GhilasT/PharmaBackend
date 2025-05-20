package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la mise à jour des informations d'un fournisseur.
 * Permet de modifier les attributs d'un fournisseur existant.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurUpdateRequest {
    /**
     * Le nom de la société du fournisseur.
     */
    private String nomSociete;
    /**
     * Le sujet ou la fonction du contact chez le fournisseur.
     */
    private String sujetFonction;
    
    /**
     * Le numéro de téléphone du fournisseur. Doit contenir entre 6 et 15 chiffres.
     */
    @Pattern(regexp = "^(\\+?[0-9\\s-]{6,15})$", message = "Le numéro de téléphone doit contenir entre 6 et 15 chiffres")
    private String telephone;
    
    /**
     * Le numéro de fax du fournisseur.
     */
    private String fax;
    /**
     * L'adresse postale du fournisseur.
     */
    private String adresse;
    
    /**
     * L'adresse email du fournisseur. Doit être un format d'email valide.
     */
    @Email(message = "L'email doit être valide")
    private String email;
}