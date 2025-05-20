package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO pour la création d'un fournisseur.
 * Contient les informations nécessaires pour enregistrer un nouveau fournisseur.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurCreateRequest {

    /**
     * Le nom de la société du fournisseur. Obligatoire.
     */
    @NotBlank(message = "Le nom de la société est obligatoire")
    private String nomSociete;

    /**
     * Le sujet ou la fonction du contact chez le fournisseur.
     */
    private String sujetFonction;

    /**
     * Le numéro de téléphone du fournisseur. Obligatoire.
     */
    @NotBlank(message = "Le téléphone est obligatoire")
    private String telephone;

    /**
     * Le numéro de fax du fournisseur.
     */
    private String fax;

    /**
     * L'adresse postale du fournisseur. Obligatoire.
     */
    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    /**
     * L'adresse email du fournisseur. Obligatoire et doit être un format d'email valide.
     */
    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "L'email doit être valide")
    private String email;
}