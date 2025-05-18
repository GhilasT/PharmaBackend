package l3o2.pharmacie.api.functional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import l3o2.pharmacie.api.model.dto.response.LoginResponse;
import l3o2.pharmacie.api.model.entity.Employe;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import l3o2.pharmacie.api.repository.EmployeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class AuthFunctionalTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private EmployeRepository employeRepository;

    private String baseUrl;
    private final String validEmail = "auth.test@pharma.com";
    private final String validPassword = "securePassword123";
    private UUID employeId;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/auth";
        initializeTestData();
    }

    private void initializeTestData() {
        // Nettoyer les données existantes
        employeRepository.deleteAll();

        // Créer un employé de test
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Employe employe = Employe.builder()
                .nom("AuthTest")
                .prenom("User")
                .emailPro(validEmail)
                .password(encoder.encode(validPassword))
                .poste(PosteEmploye.PHARMACIEN_ADJOINT)
                .dateEmbauche(new Date())
                .telephone("0123456789")
                .adresse("123 Rue de la Pharmacie")
                .matricule("EMP-123456")
                .salaire(3000.0)
                .statutContrat(StatutContrat.CDI)
                .build();
        
        Employe saved = employeRepository.save(employe);
        employeId = saved.getIdPersonne();
    }

    @Test
    @Order(1)
    void testSuccessfulLogin() {
        // Given
        var credentials = Map.of(
            "email", validEmail,
            "password", validPassword
        );

        // When
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
            baseUrl + "/login",
            new HttpEntity<>(credentials),
            LoginResponse.class
        );

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        LoginResponse body = response.getBody();
        assertNotNull(body);
        assertTrue(body.isSuccess());
        assertEquals("AuthTest", body.getNom());
        assertEquals("User", body.getPrenom());
        assertEquals(employeId, body.getId());
        assertNotNull(body.getToken());
        assertTrue(body.getToken().startsWith("eyJ"));
        // Token should contain email as subject
        assertTrue(body.getToken().contains("eyJhbGciOiJIUzI1NiJ9"));
    }

    @Test
    @Order(2)
    void testInvalidEmail() {
        // Given
        var credentials = Map.of(
            "email", "invalid@pharma.com",
            "password", validPassword
        );

        // When
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
            baseUrl + "/login",
            new HttpEntity<>(credentials),
            LoginResponse.class
        );

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }

    @Test
    @Order(3)
    void testInvalidPassword() {
        // Given
        var credentials = Map.of(
            "email", validEmail,
            "password", "wrongPassword"
        );

        // When
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
            baseUrl + "/login",
            new HttpEntity<>(credentials),
            LoginResponse.class
        );

        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertFalse(response.getBody().isSuccess());
    }
}