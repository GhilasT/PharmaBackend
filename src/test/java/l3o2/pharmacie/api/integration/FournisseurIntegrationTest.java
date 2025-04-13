// FournisseurIntegrationTest.java
package l3o2.pharmacie.api.integration;

import l3o2.pharmacie.api.model.dto.request.FournisseurCreateRequest;
import l3o2.pharmacie.api.model.dto.response.FournisseurResponse;
import l3o2.pharmacie.api.model.entity.Fournisseur;
import l3o2.pharmacie.api.repository.FournisseurRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FournisseurIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FournisseurRepository fournisseurRepository;

    private final String BASE_URL = "/api/fournisseurs";

    @Test
    void fullFournisseurFlowTest() {
        // Création
        FournisseurCreateRequest request = new FournisseurCreateRequest();
        request.setNomSociete("PharmaPlus");
        request.setEmail("contact@pharmaplus.fr");
        request.setTelephone("0612345678");
        request.setAdresse("123 Rue des Fournisseurs, Paris");

        ResponseEntity<FournisseurResponse> createResponse = restTemplate.postForEntity(
            BASE_URL, request, FournisseurResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        FournisseurResponse created = createResponse.getBody();
        UUID id = created.getIdFournisseur();

        // Vérification en base
        Fournisseur dbEntity = fournisseurRepository.findById(id).orElseThrow();
        assertThat(dbEntity.getNomSociete()).isEqualTo("PharmaPlus");

        // Mise à jour
        request.setNomSociete("PharmaPlus Updated");
        request.setEmail("nouveau@pharmaplus.fr");
        restTemplate.put(BASE_URL + "/" + id, request);

        // Vérification mise à jour
        Fournisseur updated = fournisseurRepository.findById(id).orElseThrow();
        assertThat(updated.getNomSociete()).isEqualTo("PharmaPlus Updated");

        // Suppression
        restTemplate.delete(BASE_URL + "/" + id);
        assertThat(fournisseurRepository.existsById(id)).isFalse();
    }
}