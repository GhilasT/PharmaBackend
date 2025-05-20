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
    /**
     * L'adresse email de l'utilisateur pour la connexion. Obligatoire.
     */
    @NotBlank(message = "L'email est obligatoire")
    private String email;
    /**
     * Le mot de passe de l'utilisateur pour la connexion. Obligatoire.
     */
    @NotBlank(message = "Le mot de passe est obligatoire")
    private String password;
}
