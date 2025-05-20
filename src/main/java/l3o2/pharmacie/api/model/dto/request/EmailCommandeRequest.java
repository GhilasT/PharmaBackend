package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO pour l'envoi d'un email à un fournisseur concernant une commande.
 * Contient les informations nécessaires pour identifier la commande.
 * @author raphaelcharoze
 * @version 1.0
 * @since 2025-03-30
 */
@Data
@Builder
@AllArgsConstructor
public class EmailCommandeRequest {
    /**
     * La référence de la commande concernée par l'email. Obligatoire.
     */
    @NotBlank(message = "Une commande est nécessaire")
    private String commandeReference;
}
