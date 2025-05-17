package l3o2.pharmacie.api.model.dto.response;

import lombok.Builder;
import lombok.Data;

/**
 * Login response
 * @author raphaelcharoze
 */

@Data
@Builder
public class LoginResponse {
    private boolean success;
    private String nom;
    private String prenom;
    private String role;
    private String id;
    private String token;
}
