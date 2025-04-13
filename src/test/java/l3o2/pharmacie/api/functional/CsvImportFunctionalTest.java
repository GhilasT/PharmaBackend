package l3o2.pharmacie.api.functional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class CsvImportFunctionalTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/import";
    }

    @Test
    void testImportAllCsvFiles() {
        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/all", null, Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertNotNull(response.getBody().get("results"));
    }

    @Test
    void testImportMedicaments() {
        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/medicaments", null, Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertTrue((Integer) response.getBody().get("count") >= 0);
    }

    @Test
    void testImportPresentations() {
        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/presentations", null, Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertTrue((Integer) response.getBody().get("count") >= 0);
    }

    @Test
    void testImportStock() {
        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/stock", null, Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertTrue((Integer) response.getBody().get("count") >= 0);
    }

    // Tests supplémentaires pour les autres endpoints d'import
    @Test
    void testImportCompositions() {
        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/compositions", null, Map.class
        );
        verifyImportResponse(response);
    }

    @Test
    void testImportConditions() {
        ResponseEntity<Map> response = restTemplate.postForEntity(
            baseUrl + "/conditions", null, Map.class
        );
        verifyImportResponse(response);
    }

    private void verifyImportResponse(ResponseEntity<Map> response) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue((Boolean) response.getBody().get("success"));
        assertTrue((Integer) response.getBody().get("count") >= 0);
    }
}