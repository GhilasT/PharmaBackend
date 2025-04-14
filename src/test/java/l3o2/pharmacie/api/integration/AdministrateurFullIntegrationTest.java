package l3o2.pharmacie.api.integration;

import l3o2.pharmacie.api.model.dto.request.AdministrateurCreateRequest;
import l3o2.pharmacie.api.model.dto.response.AdministrateurResponse;
import l3o2.pharmacie.api.model.entity.Administrateur;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import l3o2.pharmacie.api.repository.AdministrateurRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AdministrateurFullIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private AdministrateurRepository administrateurRepository;

    private final String BASE_URL = "/api/administrateurs";

    @Test
    void fullFlowTest() {
        // 1. Création
        AdministrateurCreateRequest request = new AdministrateurCreateRequest();
        request.setNom("Martin");
        request.setPrenom("Lucie");
        request.setEmail("lucie@test.com");
        request.setEmailPro("l.martin@pharma.com");
        request.setTelephone("0612345678");
        request.setAdresse("5 Rue des Tests");
        request.setRole("Responsable QA");
        request.setPassword("secure123");
        request.setDateEmbauche(new Date());
        request.setSalaire(4200.0);
        request.setStatutContrat(StatutContrat.CDI);
        
        ResponseEntity<AdministrateurResponse> createResponse = restTemplate.postForEntity(
            BASE_URL, 
            request, 
            AdministrateurResponse.class
        );
        
        // Vérification création
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        AdministrateurResponse createdAdmin = createResponse.getBody();
        assertThat(createdAdmin).isNotNull();
        String matricule = createdAdmin.getMatricule();
        
        // Vérification en base
        Administrateur dbAdmin = administrateurRepository.findByMatricule(matricule).orElseThrow();
        assertThat(dbAdmin.getNom()).isEqualTo("Martin");
        assertThat(dbAdmin.getEmailPro()).isEqualTo("l.martin@pharma.com");

        // 2. Récupération
        ResponseEntity<AdministrateurResponse> getResponse = restTemplate.getForEntity(
            BASE_URL + "/" + matricule, 
            AdministrateurResponse.class
        );
        
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getIdPersonne()).isEqualTo(dbAdmin.getIdPersonne());

        // 3. Mise à jour
        AdministrateurCreateRequest updateRequest = new AdministrateurCreateRequest();
        updateRequest.setSalaire(4500.0);
        updateRequest.setRole("Responsable QA Senior");
        
        ResponseEntity<AdministrateurResponse> updateResponse = restTemplate.exchange(
            BASE_URL + "/" + dbAdmin.getIdPersonne(),
            HttpMethod.PUT,
            new HttpEntity<>(updateRequest),
            AdministrateurResponse.class
        );
        
        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getSalaire()).isEqualTo(4500.0);

        // 4. Suppression
        restTemplate.delete(BASE_URL + "/" + dbAdmin.getIdPersonne());
        
        // Vérification suppression
        assertThat(administrateurRepository.existsById(dbAdmin.getIdPersonne())).isFalse();
    }
}