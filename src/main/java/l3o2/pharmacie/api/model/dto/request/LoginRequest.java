package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * DTO pour la requête de connexion.
 * Utilisé lors de l'authentification d'un utilisateur.
 * @author raphaelcharoze
 */

@Data
@Builder
public class LoginRequest {
    @NotBlank(message = "L'email est obligatoire")
    private String email;
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}
