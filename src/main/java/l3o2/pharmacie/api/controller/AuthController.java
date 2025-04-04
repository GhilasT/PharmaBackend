package l3o2.pharmacie.api.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import l3o2.pharmacie.api.model.dto.response.LoginResponse;
import l3o2.pharmacie.api.model.entity.Employe;
import l3o2.pharmacie.api.service.AuthService;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody Map<String, String> credentials) {
        String emailPro = credentials.get("email"); // Supposant que "email" correspond à emailPro
        String password = credentials.get("password");
        
        Optional<Employe> employeOpt = authService.authenticate(emailPro, password);
        if (employeOpt.isPresent()) {
            Employe employe = employeOpt.get();
            LoginResponse response = new LoginResponse(
                true,
                employe.getNom(),
                employe.getPrenom(),
                employe.getPoste().name(),
                employe.getIdPersonne()
            );
            return ResponseEntity.ok(response);
        } else {
            LoginResponse response = new LoginResponse(false, "Aucun", "Aucun", "Aucun", null);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}