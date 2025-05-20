package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO pour l'envoi d'un email simple (sans pièce jointe).
 * Contient les informations nécessaires pour envoyer un email.
 * @author raphaelcharoze
 * @version 1.0
 * @since 2025-03-30
 */
@Data
@Builder
@AllArgsConstructor
public class EmailSendingRequest {

    /**
     * L'adresse email du destinataire. Ne peut pas être vide.
     */
    @NotBlank(message = "Le destinataire ne peut pas etre vide")
    private String to;

    /**
     * Le sujet de l'email. Ne peut pas être vide.
     */
    @NotBlank(message = "Le sujet ne peut pas etre vide")
    private String subject;

    /**
     * Le corps du message de l'email. Optionnel.
     */
    private String text;

}
