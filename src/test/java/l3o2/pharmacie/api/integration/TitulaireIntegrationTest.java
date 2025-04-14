// TitulaireIntegrationTest.java
package l3o2.pharmacie.api.integration;

import l3o2.pharmacie.api.model.dto.request.TitulaireCreateRequest;
import l3o2.pharmacie.api.model.dto.response.TitulaireResponse;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import l3o2.pharmacie.api.model.entity.Titulaire;
import l3o2.pharmacie.api.repository.TitulaireRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TitulaireIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TitulaireRepository repository;

    private final String BASE_URL = "/api/titulaires";

    @Test
    void completeTitulaireWorkflow() {
        // Create
        TitulaireCreateRequest request = new TitulaireCreateRequest();
        request.setNom("Dupont");
        request.setPrenom("Pierre");
        request.setEmail("pierre.dupont@example.com");
        request.setTelephone("+33698765432");
        request.setAdresse("22 Rue des Titulaires");
        request.setPassword("TitulairePass123!");
        request.setEmailPro("pierre.dupont@pharma.com");
        request.setDateEmbauche(new Date());
        request.setSalaire(8000.0);
        request.setPoste(PosteEmploye.TITULAIRE);
        request.setStatutContrat(StatutContrat.CDI);
        request.setRole("PHARMACIEN_TITULAIRE");
        request.setNumeroRPPS("12345678901");

        ResponseEntity<TitulaireResponse> createResponse = restTemplate.postForEntity(
            BASE_URL, request, TitulaireResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID id = createResponse.getBody().getIdPersonne();

        // Verify creation
        Titulaire created = repository.findById(id).orElseThrow();
        assertThat(created.getNumeroRPPS()).isEqualTo("12345678901");

        // Delete
        restTemplate.delete(BASE_URL + "/" + id);
        assertThat(repository.existsById(id)).isFalse();
    }
}