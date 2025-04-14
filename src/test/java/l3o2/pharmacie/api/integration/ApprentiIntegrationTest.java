package l3o2.pharmacie.api.integration;

import l3o2.pharmacie.api.model.dto.request.ApprentiCreateRequest;
import l3o2.pharmacie.api.model.dto.response.ApprentiResponse;
import l3o2.pharmacie.api.model.entity.Apprenti;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import l3o2.pharmacie.api.repository.ApprentiRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ApprentiIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ApprentiRepository apprentiRepository;

    private final String BASE_URL = "/api/apprentis";

    @Test
    void fullApprentiFlowTest() {
        // Création
        ApprentiCreateRequest request = new ApprentiCreateRequest();
        request.setNom("Dupuis");
        request.setPrenom("Marie");
        request.setEmail("marie@test.com");
        request.setEmailPro("m.dupuis@pharma.com");
        request.setTelephone("0611223344");
        request.setAdresse("7 Rue des Apprentis");
        request.setDateEmbauche(new Date());
        request.setSalaire(1450.0);
        request.setStatutContrat(StatutContrat.APPRENTISSAGE);
        request.setEcole("École de Pharmacie Lyon");
        request.setPassword("pass123");

        ResponseEntity<ApprentiResponse> createResponse = restTemplate.postForEntity(
            BASE_URL, request, ApprentiResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        ApprentiResponse created = createResponse.getBody();
        String matricule = created.getMatricule();

        // Vérification en base
        Apprenti dbEntity = apprentiRepository.findByMatricule(matricule).orElseThrow();
        assertThat(dbEntity.getEcole()).isEqualTo("École de Pharmacie Lyon");

        // Mise à jour
        request.setSalaire(1550.0);
        restTemplate.put(BASE_URL + "/" + created.getIdPersonne(), request);

        // Vérification mise à jour
        Apprenti updated = apprentiRepository.findByMatricule(matricule).orElseThrow();
        assertThat(updated.getSalaire()).isEqualTo(1550.0);

        // Suppression
        restTemplate.delete(BASE_URL + "/" + created.getIdPersonne());
        assertThat(apprentiRepository.existsByMatricule(matricule)).isFalse();
    }
}