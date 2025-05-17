package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for sending an email to a fournisseur with a commande
 * Contains the information necessary to send an email
 * @author raphaelcharoze
 * @version 1.0
 * @since 2025-03-30
 */
@Data
@Builder
@AllArgsConstructor
public class EmailCommandeRequest {
    @NotBlank(message = "Une commande est nécessaire")
    private String commandeReference;
}
