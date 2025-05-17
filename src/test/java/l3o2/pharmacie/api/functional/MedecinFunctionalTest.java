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

import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
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
                .nomExercice(INITIAL_NOM)
                .prenomExercice("Jean")
                .rppsMedecin(RPPS)
                .civilite("Dr")
                .profession("Médecin Généraliste")
                .modeExercice("Libéral")
                .qualifications("Médecine générale")
                .structureExercice("Cabinet privé")
                .fonctionActivite("Médecin traitant")
                .genreActivite("Consultation")
                .build();

        ResponseEntity<MedecinResponse> response = restTemplate.postForEntity(
                baseUrl, request, MedecinResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getIdMedecin());  // Utilisation correcte de l'attribut idMedecin
        assertEquals(INITIAL_NOM, response.getBody().getNomExercice());  // Vérification du nom
        assertEquals(RPPS, response.getBody().getRppsMedecin());  // Vérification du RPPS

        medecinId = response.getBody().getIdMedecin();  // Sauvegarde de l'id du médecin pour les tests suivants
    }

    @Test
    @Order(2)
    void testGetMedecinById() {
        ResponseEntity<MedecinResponse> response = restTemplate.getForEntity(
                baseUrl + "/{id}", MedecinResponse.class, medecinId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(medecinId, response.getBody().getIdMedecin());  // Utilisation correcte de l'attribut idMedecin
        assertEquals("Médecin Généraliste", response.getBody().getProfession());  // Vérification de la profession
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
        assertTrue(response.getBody().size() > 0);  // Vérification que la liste n'est pas vide
    }

    @Test
    @Order(4)
    void testUpdateMedecin() {

        MedecinCreateRequest updateRequest = MedecinCreateRequest.builder()
                .prenomExercice("Jean-Marc")
                .build();

        ResponseEntity<MedecinResponse> response = restTemplate.exchange(
                baseUrl + "/{id}",
                HttpMethod.PUT,
                new HttpEntity<>(updateRequest),
                MedecinResponse.class,
                medecinId
        );

        // Vérification du statut HTTP
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Vérification que le prénom a bien été mis à jour
        assertEquals("Jean-Marc", response.getBody().getPrenomExercice());  // Vérification du prénom mis à jour
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
        assertTrue(response.getBody().size() > 0);  // Vérification qu'au moins un médecin est trouvé
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

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());  // Vérification qu'après suppression, le médecin n'est plus trouvé
    }


}