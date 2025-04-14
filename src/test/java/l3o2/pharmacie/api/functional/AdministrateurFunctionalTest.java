package l3o2.pharmacie.api.functional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import l3o2.pharmacie.api.model.dto.request.AdministrateurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.AdministrateurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.AdministrateurResponse;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class AdministrateurFunctionalTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private static UUID adminId;
    private static String adminMatricule;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/administrateurs";
    }

    @Test
    @Order(1)
    void testCreateAdministrateur() {
        // Préparation de la requête
        AdministrateurCreateRequest createRequest = new AdministrateurCreateRequest();
        createRequest.setNom("Dupont");
        createRequest.setPrenom("Jean");
        createRequest.setEmail("jean.dupont@example.com");
        createRequest.setEmailPro("j.dupont@pharmacie.com");
        createRequest.setTelephone("0123456789");
        createRequest.setAdresse("123 Rue de la Pharmacie");
        createRequest.setRole("Administrateur en chef");
        createRequest.setPassword("password123");
        createRequest.setDateEmbauche(new Date());
        createRequest.setSalaire(3500.0);
        createRequest.setStatutContrat(StatutContrat.CDI);
        createRequest.setPoste(PosteEmploye.ADMINISTRATEUR);
        createRequest.setDiplome("Master en Gestion");

        // Envoi de la requête POST
        ResponseEntity<AdministrateurResponse> response = restTemplate.postForEntity(
                baseUrl, createRequest, AdministrateurResponse.class);

        // Vérifications
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getIdPersonne());
        assertNotNull(response.getBody().getMatricule());
        assertEquals("Dupont", response.getBody().getNom());
        assertEquals("Jean", response.getBody().getPrenom());
        assertEquals("j.dupont@pharmacie.com", response.getBody().getEmailPro());
        assertEquals("Administrateur en chef", response.getBody().getRole());
        
        // Stockage de l'ID et du matricule pour les tests suivants
        adminId = response.getBody().getIdPersonne();
        adminMatricule = response.getBody().getMatricule();
    }

    @Test
    @Order(2)
    void testGetAdministrateurByMatricule() {
        // Vérifier que le test précédent a bien créé un admin
        assertNotNull(adminMatricule, "Le test de création doit être exécuté avant ce test");
        
        // Récupérer l'administrateur par son matricule
        ResponseEntity<AdministrateurResponse> response = restTemplate.getForEntity(
                baseUrl + "/{id}", AdministrateurResponse.class, adminMatricule);
        
        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(adminId, response.getBody().getIdPersonne());
        assertEquals(adminMatricule, response.getBody().getMatricule());
        assertEquals("Dupont", response.getBody().getNom());
        assertEquals("Jean", response.getBody().getPrenom());
    }

    @Test
    @Order(3)
    void testGetAllAdministrateurs() {
        // Récupérer tous les administrateurs
        ResponseEntity<List<AdministrateurResponse>> response = restTemplate.exchange(
                baseUrl, 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<AdministrateurResponse>>() {});
        
        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
        
        // Vérifier que notre administrateur créé est dans la liste
        boolean found = response.getBody().stream()
                .anyMatch(admin -> admin.getIdPersonne().equals(adminId));
        assertTrue(found, "L'administrateur créé précédemment devrait être dans la liste");
    }

    @Test
    @Order(4)
    void testUpdateAdministrateur() {
        // Vérifier que le test de création a été exécuté
        assertNotNull(adminId, "Le test de création doit être exécuté avant ce test");
        
        // Préparer la requête de mise à jour
        AdministrateurUpdateRequest updateRequest = new AdministrateurUpdateRequest();
        updateRequest.setPrenom("Jean-Pierre");
        updateRequest.setRole("Directeur administratif");
        updateRequest.setSalaire(4000.0);
        
        // Envoyer la requête PUT
        ResponseEntity<AdministrateurResponse> response = restTemplate.exchange(
                baseUrl + "/{id}", 
                HttpMethod.PUT, 
                new HttpEntity<>(updateRequest), 
                AdministrateurResponse.class, 
                adminId);
        
        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(adminId, response.getBody().getIdPersonne());
        assertEquals(adminMatricule, response.getBody().getMatricule());
        assertEquals("Jean-Pierre", response.getBody().getPrenom());
        assertEquals("Directeur administratif", response.getBody().getRole());
        assertEquals(4000.0, response.getBody().getSalaire());
    }

    @Test
    @Order(5)
    void testSearchAdministrateurs() {
        // Rechercher avec le nom de famille
        ResponseEntity<List<AdministrateurResponse>> response = restTemplate.exchange(
                baseUrl + "/search?query={query}", 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<AdministrateurResponse>>() {}, 
                "Dup");
        
        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
        
        // Rechercher avec le prénom mis à jour
        response = restTemplate.exchange(
                baseUrl + "/search?query={query}", 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<AdministrateurResponse>>() {}, 
                "Jean-Pierre");
        
        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    @Order(6)
    void testDeleteAdministrateur() {
        // Vérifier que le test de création a été exécuté
        assertNotNull(adminId, "Le test de création doit être exécuté avant ce test");
        
        // Envoyer la requête DELETE
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/{id}", 
                HttpMethod.DELETE, 
                null, 
                Void.class, 
                adminId);
        
        // Vérifier que la suppression a réussi
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        
        // Vérifier que l'administrateur n'existe plus
        ResponseEntity<AdministrateurResponse> getResponse = restTemplate.getForEntity(
                baseUrl + "/{id}", 
                AdministrateurResponse.class, 
                adminMatricule);
        
        // On s'attend à une erreur 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}