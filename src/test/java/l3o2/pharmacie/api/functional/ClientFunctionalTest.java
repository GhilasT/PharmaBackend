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

import l3o2.pharmacie.api.model.dto.request.ClientCreateRequest;
import l3o2.pharmacie.api.model.dto.response.ClientResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class ClientFunctionalTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private static UUID clientId;
    private static final String INITIAL_TELEPHONE = "0612345678";

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/client";
    }

    @Test
    @Order(1)
    void testCreateClient() {
        ClientCreateRequest request = ClientCreateRequest.builder()
            .nom("Martin")
            .prenom("Lucie")
            .telephone(INITIAL_TELEPHONE)
            .adresse("5 Rue des Clients")
            .email("lucie.martin@example.com")
            .build();

        ResponseEntity<ClientResponse> response = restTemplate.postForEntity(
            baseUrl, request, ClientResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getIdPersonne());
        assertEquals("Martin", response.getBody().getNom());
        
        clientId = response.getBody().getIdPersonne();
    }

    @Test
    @Order(2)
    void testGetClientById() {
        ResponseEntity<ClientResponse> response = restTemplate.getForEntity(
            baseUrl + "/{id}", ClientResponse.class, clientId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientId, response.getBody().getIdPersonne());
        assertEquals("Lucie", response.getBody().getPrenom());
    }

    @Test
    @Order(3)
    void testGetAllClients() {
        ResponseEntity<List<ClientResponse>> response = restTemplate.exchange(
            baseUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<ClientResponse>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    @Order(4)
    void testGetClientByTelephone() {
        ResponseEntity<ClientResponse> response = restTemplate.getForEntity(
            baseUrl + "/telephone/{telephone}", 
            ClientResponse.class, 
            INITIAL_TELEPHONE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Martin", response.getBody().getNom());
    }

    @Test
    @Order(5)
    void testGetClientByEmail() {
        ResponseEntity<ClientResponse> response = restTemplate.getForEntity(
            baseUrl + "/email/{email}", 
            ClientResponse.class, 
            "lucie.martin@example.com");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(clientId, response.getBody().getIdPersonne());
    }

    @Test
    @Order(6)
    void testDeleteClient() {
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
            baseUrl + "/{id}",
            HttpMethod.DELETE,
            null,
            Void.class,
            clientId);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        ResponseEntity<ClientResponse> getResponse = restTemplate.getForEntity(
            baseUrl + "/{id}",
            ClientResponse.class,
            clientId);

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}