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

@Component
public class TokenPrinter {
    
    @Autowired
    private EmployeRepository employeRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
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
                System.out.println("Token for " + email + ":");
                System.out.println(token);
                System.out.println("====================================\n\n");
            } else {
                // Alternative approach if the user isn't found in database
                UUID userId = generateMockUserId();
                // This method should be consistent with the one used above
                String token = jwtUtil.generateToken(userId.toString(), Collections.singletonList("ROLE_PHARMACIEN"));
                
                System.out.println("\n\n====================================");
                System.out.println("WARNING: User " + email + " not found in database.");
                System.out.println("Generated mock token instead:");
                System.out.println(token);
                System.out.println("====================================\n\n");
            }
        } catch (Exception e) {
            System.out.println("\n\n====================================");
            System.out.println("Error generating token for " + email + ":");
            System.out.println(e.getMessage());
            System.out.println("====================================\n\n");
            e.printStackTrace();
        }
    }
    
    private UUID generateMockUserId() {
        // Generate a deterministic UUID based on the email string
        return UUID.nameUUIDFromBytes("pharmacien.pro@example.com".getBytes());
    }
}
