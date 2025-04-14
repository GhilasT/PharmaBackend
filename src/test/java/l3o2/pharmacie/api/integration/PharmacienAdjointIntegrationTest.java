package l3o2.pharmacie.api.integration;

import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointCreateRequest;
import l3o2.pharmacie.api.model.dto.response.PharmacienAdjointResponse;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import l3o2.pharmacie.api.repository.PharmacienAdjointRepository;
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
class PharmacienAdjointIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private PharmacienAdjointRepository repository;

    private final String BASE_URL = "/api/pharmaciens-adjoints";

    @Test
    void fullPharmacienAdjointLifecycle() {
        // Create
        PharmacienAdjointCreateRequest request = new PharmacienAdjointCreateRequest();
        request.setNom("Martin");
        request.setPrenom("Sophie");
        request.setEmail("sophie.martin@example.com");
        request.setTelephone("+33612345678");
        request.setAdresse("15 Avenue de la Santé");
        request.setDateEmbauche(new Date());
        request.setSalaire(4500.0);
        request.setPassword("SecurePass123!");
        request.setPoste(PosteEmploye.PHARMACIEN_ADJOINT);
        request.setStatutContrat(StatutContrat.CDI);
        request.setEmailPro("sophie.martin@pharma.com");
        request.setDiplome("Doctorat en Pharmacie");

        ResponseEntity<PharmacienAdjointResponse> createResponse = restTemplate.postForEntity(
            BASE_URL, request, PharmacienAdjointResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        UUID id = createResponse.getBody().getIdPersonne();

        // Verify creation
        PharmacienAdjoint created = repository.findById(id).orElseThrow();
        assertThat(created.getEmailPro()).isEqualTo("sophie.martin@pharma.com");

        // Update
        request.setSalaire(4700.0);
        request.setAdresse("17B Avenue de la Santé");
        restTemplate.put(BASE_URL + "/" + id, request);

        // Verify update
        PharmacienAdjoint updated = repository.findById(id).orElseThrow();
        assertThat(updated.getSalaire()).isEqualTo(4700.0);
        assertThat(updated.getAdresse()).isEqualTo("17B Avenue de la Santé");

        // Delete
        restTemplate.delete(BASE_URL + "/" + id);
        assertThat(repository.existsById(id)).isFalse();
    }
}