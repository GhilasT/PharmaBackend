package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.File;

/**
 * DTO for sending an email with attachment
 * Contains the information necessary to send an email
 * @author raphaelcharoze
 * @version 1.0
 * @since 2025-04-05
 */
@Data
@Builder
@AllArgsConstructor
public class EmailWithAttachmentFileSendingRequest {

    @NotBlank(message = "Le destinataire ne peut pas etre vide")
    private String to;

    @NotBlank(message = "Le sujet ne peut pas etre vide")
    private String subject;

    @NotBlank(message = "Le fichier ne peut pas etre vide")
    private File file;

    private String text;

}
