package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO pour l'envoi d'un email avec une pièce jointe spécifiée par son chemin.
 * Contient les informations nécessaires pour envoyer un email avec une pièce jointe.
 * @author raphaelcharoze
 * @version 1.0
 * @since 2025-04-05
 */
@Data
@Builder
@AllArgsConstructor
public class EmailWithAttachmentPathSendingRequest {

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
     * Le chemin d'accès au fichier à joindre. Ne peut pas être vide.
     */
    @NotBlank(message = "Le fichier ne peut pas etre vide")
    private String filePath;

    /**
     * Le corps du message de l'email. Optionnel.
     */
    private String text;
}

