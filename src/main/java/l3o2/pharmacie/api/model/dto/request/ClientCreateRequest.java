package l3o2.pharmacie.api.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO pour la création d'un client.
 * Contient les informations nécessaires pour enregistrer un nouveau client.
 */
@Getter
@Setter
@Builder
public class ClientCreateRequest {

    /**
     * Le nom du client. Ne peut pas être vide.
     */
    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    /**
     * Le prénom du client. Ne peut pas être vide.
     */
    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String prenom;

    /**
     * L'adresse email du client. Optionnel.
     */
    private String email;

    /**
     * Le numéro de téléphone du client. Ne peut pas être vide.
     */
    @NotBlank(message = "Le numéro de téléphone ne peut pas être vide")
    private String telephone;

    /**
     * L'adresse postale du client. Optionnel.
     */
    private String adresse;

    /**
     * Le mot de passe pour le compte du client. Optionnel.
     */
    private String password;

    /**
     * Le numéro de sécurité sociale du client. Optionnel.
     */
    private String numeroSecu;

    /**
     * Le nom de la mutuelle du client. Optionnel.
     */
    private String mutuelle;
}
