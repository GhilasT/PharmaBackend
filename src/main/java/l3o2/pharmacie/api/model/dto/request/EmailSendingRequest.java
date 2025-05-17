package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for sending an email
 * Contains the information necessary to send an email
 * @author raphaelcharoze
 * @version 1.0
 * @since 2025-03-30
 */
@Data
@Builder
@AllArgsConstructor
public class EmailSendingRequest {

    @NotBlank(message = "Le destinataire ne peut pas etre vide")
    private String to;

    @NotBlank(message = "Le sujet ne peut pas etre vide")
    private String subject;

    private String text;

}
