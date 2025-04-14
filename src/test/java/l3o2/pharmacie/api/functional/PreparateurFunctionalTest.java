package l3o2.pharmacie.api.functional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
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

import l3o2.pharmacie.api.model.dto.request.PreparateurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.PreparateurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.PreparateurResponse;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class PreparateurFunctionalTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private static UUID preparateurId;
    private static String preparateurMatricule;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/preparateurs";
    }

    @Test
    @Order(1)
    void testCreatePreparateur() {
        PreparateurCreateRequest request = new PreparateurCreateRequest();
        request.setNom("Leroy");
        request.setPrenom("Paul");
        request.setEmail("paul.leroy@example.com");
        request.setEmailPro("p.leroy@pharma.com");
        request.setTelephone("0612345678");
        request.setAdresse("10 Rue des Préparations");
        request.setPassword("pass456");
        request.setDateEmbauche(new Date());
        request.setSalaire(2800.0);
        request.setStatutContrat(StatutContrat.CDD);
        request.setDiplome("CAP Pharmacie");
        request.setPoste(PosteEmploye.PREPARATEUR);

        ResponseEntity<PreparateurResponse> response = restTemplate.postForEntity(
                baseUrl, request, PreparateurResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PreparateurResponse body = response.getBody();
        assertNotNull(body);
        preparateurId = body.getIdPersonne();
        preparateurMatricule = body.getMatricule();
    }

    @Test
    @Order(2)
    void testGetPreparateurById() {
        ResponseEntity<PreparateurResponse> response = restTemplate.getForEntity(
                baseUrl + "/" + preparateurId, 
                PreparateurResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Leroy", response.getBody().getNom());
    }

    @Test
    @Order(3)
    void testGetAllPreparateurs() {
        ResponseEntity<List<PreparateurResponse>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PreparateurResponse>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    @Order(4)
    void testUpdatePreparateur() {
        PreparateurUpdateRequest request = new PreparateurUpdateRequest();
        request.setSalaire(3000.0);
        request.setAdresse("15 Avenue des Médicaments");

        ResponseEntity<PreparateurResponse> response = restTemplate.exchange(
                baseUrl + "/" + preparateurId,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                PreparateurResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(3000.0, response.getBody().getSalaire());
    }

    @Test
    @Order(5)
    void testSearchPreparateurs() {
        ResponseEntity<List<PreparateurResponse>> response = restTemplate.exchange(
                baseUrl + "/search?term=Ler",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PreparateurResponse>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    @Order(6)
    void testDeletePreparateur() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + preparateurId,
                HttpMethod.DELETE,
                null,
                Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<PreparateurResponse> checkResponse = restTemplate.getForEntity(
                baseUrl + "/" + preparateurId,
                PreparateurResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, checkResponse.getStatusCode());
    }
}