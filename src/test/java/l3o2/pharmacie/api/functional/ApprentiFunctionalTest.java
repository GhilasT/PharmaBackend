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

import l3o2.pharmacie.api.model.dto.request.ApprentiCreateRequest;
import l3o2.pharmacie.api.model.dto.request.ApprentiUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.ApprentiResponse;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class ApprentiFunctionalTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private static UUID apprentiId;
    private static String apprentiMatricule;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/apprentis";
    }

    @Test
    @Order(1)
    void testCreateApprenti() {
        // Préparation de la requête
        ApprentiCreateRequest createRequest = ApprentiCreateRequest.builder()
            .nom("Martin")
            .prenom("Sophie")
            .email("sophie.martin@example.com")
            .emailPro("s.martin@pharmacie.com")
            .telephone("0612345678")
            .adresse("45 Avenue des Étudiants")
            .dateEmbauche(new Date())
            .salaire(1500.0)
            .statutContrat(StatutContrat.APPRENTISSAGE)
            .poste(PosteEmploye.APPRENTI)
            .diplome("Brevet Professionnel de Préparateur")
            .ecole("École Supérieure de Pharmacie")
            .password("password123")
            .build();

        // Envoi de la requête POST
        ResponseEntity<ApprentiResponse> response = restTemplate.postForEntity(
                baseUrl, createRequest, ApprentiResponse.class);

        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getIdPersonne());
        assertNotNull(response.getBody().getMatricule());
        assertEquals("Martin", response.getBody().getNom());
        assertEquals("Sophie", response.getBody().getPrenom());
        assertEquals("s.martin@pharmacie.com", response.getBody().getEmailPro());
        assertEquals("École Supérieure de Pharmacie", response.getBody().getEcole());
        
        // Stockage de l'ID et du matricule pour les tests suivants
        apprentiId = response.getBody().getIdPersonne();
        apprentiMatricule = response.getBody().getMatricule();
    }

    @Test
    @Order(2)
    void testGetApprentiById() {
        // Vérifier que le test précédent a bien créé un apprenti
        assertNotNull(apprentiId, "Le test de création doit être exécuté avant ce test");
        
        // Récupérer l'apprenti par son ID
        ResponseEntity<ApprentiResponse> response = restTemplate.getForEntity(
                baseUrl + "/{id}", ApprentiResponse.class, apprentiId);
        
        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(apprentiId, response.getBody().getIdPersonne());
        assertEquals(apprentiMatricule, response.getBody().getMatricule());
        assertEquals("Martin", response.getBody().getNom());
        assertEquals("Sophie", response.getBody().getPrenom());
    }

    @Test
    @Order(3)
    void testGetAllApprentis() {
        // Récupérer tous les apprentis
        ResponseEntity<List<ApprentiResponse>> response = restTemplate.exchange(
                baseUrl, 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<ApprentiResponse>>() {});
        
        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
        
        // Vérifier que notre apprenti créé est dans la liste
        boolean found = response.getBody().stream()
                .anyMatch(apprenti -> apprenti.getIdPersonne().equals(apprentiId));
        assertTrue(found, "L'apprenti créé précédemment devrait être dans la liste");
    }

    @Test
    @Order(4)
    void testUpdateApprenti() {
        // Vérifier que le test de création a été exécuté
        assertNotNull(apprentiId, "Le test de création doit être exécuté avant ce test");
        
        // Préparer la requête de mise à jour
        ApprentiUpdateRequest updateRequest = ApprentiUpdateRequest.builder()
            .prenom("Sophie-Anne")
            .diplome("Brevet Professionnel de Préparateur en Pharmacie")
            .salaire(1600.0)
            .build();
        
        // Envoyer la requête PUT
        ResponseEntity<ApprentiResponse> response = restTemplate.exchange(
                baseUrl + "/{id}", 
                HttpMethod.PUT, 
                new HttpEntity<>(updateRequest), 
                ApprentiResponse.class, 
                apprentiId);
        
        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(apprentiId, response.getBody().getIdPersonne());
        assertEquals(apprentiMatricule, response.getBody().getMatricule());
        assertEquals("Sophie-Anne", response.getBody().getPrenom());
        assertEquals("Brevet Professionnel de Préparateur en Pharmacie", response.getBody().getDiplome());
        assertEquals(1600.0, response.getBody().getSalaire());
    }

    @Test
    @Order(5)
    void testSearchApprentis() {
        // Rechercher avec le nom de famille
        ResponseEntity<List<ApprentiResponse>> response = restTemplate.exchange(
                baseUrl + "/search?term={term}", 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<ApprentiResponse>>() {}, 
                "Mart");
        
        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
        
        // Rechercher avec le prénom mis à jour
        response = restTemplate.exchange(
                baseUrl + "/search?term={term}", 
                HttpMethod.GET, 
                null, 
                new ParameterizedTypeReference<List<ApprentiResponse>>() {}, 
                "Sophie-Anne");
        
        // Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    @Order(6)
    void testDeleteApprenti() {
        // Vérifier que le test de création a été exécuté
        assertNotNull(apprentiId, "Le test de création doit être exécuté avant ce test");
        
        // Envoyer la requête DELETE
        ResponseEntity<String> deleteResponse = restTemplate.exchange(
                baseUrl + "/{id}", 
                HttpMethod.DELETE, 
                null, 
                String.class, 
                apprentiId);
        
        // Vérifier que la suppression a réussi
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertEquals("Apprenti supprimé avec succès.", deleteResponse.getBody());
        
        // Vérifier que l'apprenti n'existe plus
        ResponseEntity<ApprentiResponse> getResponse = restTemplate.getForEntity(
                baseUrl + "/{id}", 
                ApprentiResponse.class, 
                apprentiId);
        
        // On s'attend à une erreur 404 Not Found
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}