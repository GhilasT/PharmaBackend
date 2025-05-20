package l3o2.pharmacie.api.startup;

import l3o2.pharmacie.api.model.entity.Employe;
import l3o2.pharmacie.api.repository.EmployeRepository;
import l3o2.pharmacie.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

/**
 * Composant qui imprime un token JWT pour un pharmacien spécifique au démarrage de l'application.
 * Utile à des fins de débogage et de test.
 */
@Component
public class TokenPrinter {
    
    @Autowired
    private EmployeRepository employeRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    /**
     * Méthode exécutée après le démarrage de l'application pour imprimer un token JWT.
     * Tente de générer un token pour un pharmacien prédéfini. Si l'employé n'est pas trouvé,
     * un token de secours est généré.
     */
    @EventListener(ApplicationReadyEvent.class)
    public void printPharmacienToken() {
        String email = "pharmacien.pro@example.com";
        
        try {
            Optional<Employe> employeOptional = employeRepository.findByEmailPro(email);
            
            if (employeOptional.isPresent()) {
                Employe employe = employeOptional.get();
                // This is the correct method that should be used for authentication tokens
                String token = jwtUtil.generateToken(employe); // Modified to use employe directly
                
                System.out.println("\n\n====================================");
                System.out.println("Token pour " + email + " :");
                System.out.println(token);
                System.out.println("====================================\n\n");
            } else {
                // Alternative approach if the user isn't found in database
                UUID userId = generateMockUserId();
                // This method should be consistent with the one used above
                String token = jwtUtil.generateToken(userId.toString(), Collections.singletonList("ROLE_PHARMACIEN"));
                
                System.out.println("\n\n====================================");
                System.out.println("ATTENTION : Utilisateur " + email + " non trouvé dans la base de données.");
                System.out.println("Token de secours généré :");
                System.out.println(token);
                System.out.println("====================================\n\n");
            }
        } catch (Exception e) {
            System.out.println("\n\n====================================");
            System.out.println("Erreur lors de la génération du token pour " + email + " :");
            System.out.println(e.getMessage());
            System.out.println("====================================\n\n");
            e.printStackTrace();
        }
    }
    
    /**
     * Génère un UUID déterministe basé sur une chaîne de caractères (email).
     * Utilisé pour créer un ID utilisateur de secours lorsque l'utilisateur n'est pas trouvé dans la base de données.
     * @return L'UUID généré.
     */
    private UUID generateMockUserId() {
        // Generate a deterministic UUID based on the email string
        return UUID.nameUUIDFromBytes("pharmacien.pro@example.com".getBytes());
    }
}
