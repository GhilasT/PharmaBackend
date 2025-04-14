package l3o2.pharmacie.api.functional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import l3o2.pharmacie.api.model.dto.request.FournisseurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.FournisseurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.FournisseurResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class FournisseurFunctionalTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private static UUID fournisseurId;
    private static String initialNomSociete = "PharmaPlus";

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/fournisseurs";
    }

    @Test
    @Order(1)
    void testCreateFournisseur() {
        FournisseurCreateRequest request = FournisseurCreateRequest.builder()
            .nomSociete(initialNomSociete)
            .telephone("0612345678")
            .adresse("123 Rue des Pharmaciens")
            .email("contact@pharmaplus.com")
            .build();

        ResponseEntity<FournisseurResponse> response = restTemplate.postForEntity(
            baseUrl, request, FournisseurResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getIdFournisseur());
        assertEquals(initialNomSociete, response.getBody().getNomSociete());
        assertEquals("contact@pharmaplus.com", response.getBody().getEmail());
        
        fournisseurId = response.getBody().getIdFournisseur();
    }

    @Test
    @Order(2)
    void testGetFournisseurById() {
        ResponseEntity<FournisseurResponse> response = restTemplate.getForEntity(
            baseUrl + "/{id}", FournisseurResponse.class, fournisseurId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(fournisseurId, response.getBody().getIdFournisseur());
        assertEquals(initialNomSociete, response.getBody().getNomSociete());
    }

    @Test
    @Order(3)
    void testGetAllFournisseurs() {
        ResponseEntity<List<FournisseurResponse>> response = restTemplate.exchange(
            baseUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<FournisseurResponse>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
        
        boolean found = response.getBody().stream()
            .anyMatch(f -> f.getIdFournisseur().equals(fournisseurId));
        assertTrue(found);
    }

    @Test
    @Order(4)
    void testUpdateFournisseur() {
        FournisseurUpdateRequest updateRequest = FournisseurUpdateRequest.builder()
            .nomSociete("PharmaPlus International")
            .sujetFonction("Responsable Approvisionnement")
            .telephone("0623456789")
            .build();

        ResponseEntity<FournisseurResponse> response = restTemplate.exchange(
            baseUrl + "/{id}",
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest),
            FournisseurResponse.class,
            fournisseurId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("PharmaPlus International", response.getBody().getNomSociete());
        assertEquals("Responsable Approvisionnement", response.getBody().getSujetFonction());
        assertEquals("0623456789", response.getBody().getTelephone());
    }

    @Test
    @Order(5)
    void testSearchFournisseurs() {
        ResponseEntity<List<FournisseurResponse>> response = restTemplate.exchange(
            baseUrl + "/search?q={query}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<FournisseurResponse>>() {},
            "International");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
        assertTrue(response.getBody().get(0).getNomSociete().contains("International"));
    }

    @Test
    @Order(6)
    void testDeleteFournisseur() {
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
            baseUrl + "/{id}",
            HttpMethod.DELETE,
            null,
            Void.class,
            fournisseurId);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<FournisseurResponse> getResponse = restTemplate.getForEntity(
            baseUrl + "/{id}",
            FournisseurResponse.class,
            fournisseurId);

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}