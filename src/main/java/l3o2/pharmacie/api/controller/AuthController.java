package l3o2.pharmacie.api.controller;

import jakarta.validation.Valid;
import l3o2.pharmacie.api.model.dto.request.EmployeCreateRequest;
import l3o2.pharmacie.api.model.dto.request.LoginRequest;
import l3o2.pharmacie.api.model.dto.response.LoginResponse;
import l3o2.pharmacie.api.model.entity.Employe;
import l3o2.pharmacie.api.service.AuthService;
import l3o2.pharmacie.api.service.EmployeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Controller for authentication
 * @author raphaelcharoze
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final EmployeService employeService;

    /**
     * Login a user
     * @return ResponseEntity validating the login
     * @author raphaelcharoze
     */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {

        String token = authService.authenticate(request);
        if (token != null) {
            Employe user = employeService.getEmployeByEmailPro(request.getEmail());
            return LoginResponse.builder()
                    .success(true)
                    .nom(user.getNom())
                    .prenom(user.getPrenom())
                    .role(user.getPoste().toString())
                    .id(user.getIdPersonne().toString())
                    .token(token)
                    .build();
        }
        else {
            return LoginResponse.builder()
                    .success(false)
                    .nom("Aucun")
                    .prenom("Aucun")
                    .role("Aucun")
                    .id(null)
                    .token(null)
                    .build();
        }
    }

    /**
     * Register a new user
     * @param request : EmployeCreateRequest
     * @return ResponseEntity validating the registration
     * @author raphaelcharoze
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody EmployeCreateRequest request) {
        try {
            employeService.createEmploye(request);
            return ResponseEntity.ok("User registered");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(412).body("User already exists");
        }
    }

}