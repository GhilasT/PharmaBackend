package l3o2.pharmacie.api.functional;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
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
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import l3o2.pharmacie.api.model.dto.request.TitulaireCreateRequest;
import l3o2.pharmacie.api.model.dto.response.TitulaireResponse;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestMethodOrder(OrderAnnotation.class)
public class TitulaireFunctionalTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private static UUID titulaireId;
    private static String numeroRPPS;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/titulaires";
    }

    @Test
    @Order(1)
    void testCreateTitulaire() {
        TitulaireCreateRequest request = new TitulaireCreateRequest();
        request.setNom("Martin");
        request.setPrenom("Sophie");
        request.setEmail("sophie.martin@example.com");
        request.setTelephone("0612345678");
        request.setAdresse("45 Avenue des Médicaments");
        request.setPassword("titulairePass");
        request.setEmailPro("s.martin@pharma.com");
        request.setDateEmbauche(new Date());
        request.setSalaire(6000.0);
        request.setPoste(PosteEmploye.TITULAIRE);
        request.setStatutContrat(StatutContrat.CDI);
        request.setDiplome("Doctorat en Pharmacie");
        request.setRole("Responsable Principal");
        request.setNumeroRPPS("98765432109"); // 11 chiffres

        ResponseEntity<TitulaireResponse> response = restTemplate.postForEntity(
                baseUrl, 
                request, 
                TitulaireResponse.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        
        titulaireId = response.getBody().getIdPersonne();
        numeroRPPS = response.getBody().getNumeroRPPS();
    }

    @Test
    @Order(2)
    void testGetTitulaire() {
        ResponseEntity<TitulaireResponse> response = restTemplate.getForEntity(
                baseUrl, 
                TitulaireResponse.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        TitulaireResponse body = response.getBody();
        assertNotNull(body);
        assertEquals("Martin", body.getNom());
        assertEquals(numeroRPPS, body.getNumeroRPPS());
    }

    @Test
    @Order(3)
    void testDeleteTitulaire() {
        // Suppression
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + titulaireId,
                HttpMethod.DELETE,
                null,
                Void.class
        );
        
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Vérification de la suppression
        ResponseEntity<TitulaireResponse> getResponse = restTemplate.getForEntity(
                baseUrl, 
                TitulaireResponse.class
        );
        
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }
}