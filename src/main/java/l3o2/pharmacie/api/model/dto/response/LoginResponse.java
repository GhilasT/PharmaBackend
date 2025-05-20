package l3o2.pharmacie.api.model.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * DTO représentant la réponse suite à une tentative de connexion.
 * @author raphaelcharoze
 */
@Data
@Builder
public class LoginResponse {
    /** Indique si la connexion a réussi. */
    private boolean success;
    /** Le nom de l'utilisateur connecté. */
    private String nom;
    /** Le prénom de l'utilisateur connecté. */
    private String prenom;
    /** Le rôle de l'utilisateur connecté. */
    private String role;
    /** L'identifiant de l'utilisateur connecté. */
    private String id;
    /** Le jeton d'authentification (token) pour l'utilisateur connecté. */
    private String token;
}
