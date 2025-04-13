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

import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointCreateRequest;
import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.PharmacienAdjointResponse;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class PharmacienAdjointFunctionalTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private static UUID pharmacienId;
    private static String pharmacienMatricule;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/pharmaciens-adjoints";
    }

    @Test
    @Order(1)
    void testCreatePharmacienAdjoint() {
        PharmacienAdjointCreateRequest request = new PharmacienAdjointCreateRequest();
        request.setNom("Dupuis");
        request.setPrenom("Marie");
        request.setEmail("marie.dupuis@example.com");
        request.setEmailPro("m.dupuis@pharma.com");
        request.setTelephone("+33123456789");
        request.setAdresse("5 Rue des Médicaments");
        request.setPassword("pass123");
        request.setDateEmbauche(new Date());
        request.setSalaire(3200.0);
        request.setPoste(PosteEmploye.PHARMACIEN_ADJOINT);
        request.setStatutContrat(StatutContrat.CDI);
        request.setDiplome("Doctorat en pharmacie");

        ResponseEntity<PharmacienAdjointResponse> response = restTemplate.postForEntity(
                baseUrl, request, PharmacienAdjointResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        PharmacienAdjointResponse body = response.getBody();
        assertNotNull(body);
        pharmacienId = body.getIdPersonne();
        pharmacienMatricule = body.getMatricule();
    }

    @Test
    @Order(2)
    void testGetAllPharmaciensAdjoints() {
        ResponseEntity<List<PharmacienAdjointResponse>> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PharmacienAdjointResponse>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().stream().anyMatch(p -> p.getIdPersonne().equals(pharmacienId)));
    }

    @Test
    @Order(3)
    void testUpdatePharmacienAdjoint() {
        PharmacienAdjointUpdateRequest request = new PharmacienAdjointUpdateRequest();
        request.setSalaire(3500.0);
        request.setPrenom("Marie-Claire");

        ResponseEntity<PharmacienAdjointResponse> response = restTemplate.exchange(
                baseUrl + "/" + pharmacienId,
                HttpMethod.PUT,
                new HttpEntity<>(request),
                PharmacienAdjointResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Marie-Claire", response.getBody().getPrenom());
        assertEquals(3500.0, response.getBody().getSalaire());
    }

    @Test
    @Order(4)
    void testSearchPharmaciensAdjoints() {
        ResponseEntity<List<PharmacienAdjointResponse>> response = restTemplate.exchange(
                baseUrl + "/search?term=Dup",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PharmacienAdjointResponse>>() {});

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() > 0);
    }

    @Test
    @Order(5)
    void testDeletePharmacienAdjoint() {
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + pharmacienId,
                HttpMethod.DELETE,
                null,
                Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<List<PharmacienAdjointResponse>> checkResponse = restTemplate.exchange(
                baseUrl,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<PharmacienAdjointResponse>>() {});

        assertFalse(checkResponse.getBody().stream().anyMatch(p -> p.getIdPersonne().equals(pharmacienId)));
    }
}