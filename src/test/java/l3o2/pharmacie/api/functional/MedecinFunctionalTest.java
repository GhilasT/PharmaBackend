package l3o2.pharmacie.api.functional;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
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

import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
import l3o2.pharmacie.api.model.dto.request.MedecinUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class MedecinFunctionalTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private static UUID medecinId;
    private static final String RPPS = "12345678901";
    private static final String INITIAL_NOM = "Dupont";

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/medecins";
    }

    @Test
    @Order(1)
    void testCreateMedecin() {
        MedecinCreateRequest request = MedecinCreateRequest.builder()
            .nom(INITIAL_NOM)
            .prenom("Jean")
            .email("jean.dupont@clinique.com")
            .telephone("0612345678")
            .adresse("1 Rue de la Santé")
            .rpps(RPPS)
            .adeli("123456789")
            .civilite("Dr")
            .profession("Médecin Généraliste")
            .specialitePrincipale("Médecine générale")
            .modeExercice("Libéral")
            .codePostal("75000")
            .ville("Paris")
            .secteur("Secteur 1")
            .conventionnement("Conventionné")
            .honoraires("Carte Vitale")
            .siret("12345678900001")
            .dateMiseAJour(LocalDate.now())
            .password("password123")          
            .build();

        ResponseEntity<MedecinResponse> response = restTemplate.postForEntity(
            baseUrl, request, MedecinResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getIdPersonne());
        assertEquals(INITIAL_NOM, response.getBody().getNom());
        assertEquals(RPPS, response.getBody().getRpps());
        
        medecinId = response.getBody().getIdPersonne();
    }

    @Test
    @Order(2)
    void testGetMedecinById() {
        ResponseEntity<MedecinResponse> response = restTemplate.getForEntity(
            baseUrl + "/{id}", MedecinResponse.class, medecinId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(medecinId, response.getBody().getIdPersonne());
        assertEquals("Médecin Généraliste", response.getBody().getProfession());
    }

    @Test
    @Order(3)
    void testGetAllMedecins() {
        ResponseEntity<List<MedecinResponse>> response = restTemplate.exchange(
            baseUrl,
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<MedecinResponse>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    @Order(4)
    void testUpdateMedecin() {
        MedecinUpdateRequest updateRequest = MedecinUpdateRequest.builder()
            .prenom("Jean-Marc")
            .telephone("0623456789")
            .adresse("2 Avenue des Médecins")
            .build();

        ResponseEntity<MedecinResponse> response = restTemplate.exchange(
            baseUrl + "/{id}",
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest),
            MedecinResponse.class,
            medecinId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Jean-Marc", response.getBody().getPrenom());
        assertEquals("2 Avenue des Médecins", response.getBody().getAdresse());
    }

    @Test
    @Order(5)
    void testSearchMedecins() {
        ResponseEntity<List<MedecinResponse>> response = restTemplate.exchange(
            baseUrl + "/search?q={query}",
            HttpMethod.GET,
            null,
            new ParameterizedTypeReference<List<MedecinResponse>>() {},
            "Dup");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    @Order(6)
    void testDeleteMedecin() {
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
            baseUrl + "/{id}",
            HttpMethod.DELETE,
            null,
            Void.class,
            medecinId);

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        ResponseEntity<MedecinResponse> getResponse = restTemplate.getForEntity(
            baseUrl + "/{id}",
            MedecinResponse.class,
            medecinId);

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}