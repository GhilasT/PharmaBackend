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
 * Contrôleur pour l'authentification des utilisateurs.
 * Gère la connexion et l'inscription des employés.
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
     * Connecte un utilisateur.
     * @param request Les informations de connexion (email et mot de passe).
     * @return Un LoginResponse contenant le token JWT, les informations de l'utilisateur et un indicateur de succès.
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
     * Inscrit un nouvel utilisateur (employé).
     * @param request Les informations de l'employé à créer.
     * @return Une ResponseEntity indiquant le succès ou l'échec de l'inscription.
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