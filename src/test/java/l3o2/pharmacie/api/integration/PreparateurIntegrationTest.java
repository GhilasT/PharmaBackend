package l3o2.pharmacie.api.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import l3o2.pharmacie.api.model.dto.request.PreparateurCreateRequest;
import l3o2.pharmacie.api.model.dto.response.PreparateurResponse;
import l3o2.pharmacie.api.repository.EmployeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD) // Correction ici
class PreparateurIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private EmployeRepository employeRepository;
    
    @BeforeEach
    void setup() {
        employeRepository.deleteAll();
    }

    @Test
    void shouldCreatePreparateur() {
        PreparateurCreateRequest request = new PreparateurCreateRequest();
        request.setNom("Test");
        request.setPrenom("User");
        
        String uniqueEmail = "test_" + System.currentTimeMillis() + "@example.com";
        request.setEmail(uniqueEmail);
        request.setEmailPro(uniqueEmail); // ⚠️ Assurez-vous que email et emailPro sont uniques
        
        request.setTelephone("0123456789");
        request.setDateEmbauche(new Date());
        request.setSalaire(2500.0);
        request.setPassword("password123");
        
        ResponseEntity<PreparateurResponse> response = restTemplate.postForEntity(
                "/api/preparateurs", request, PreparateurResponse.class);
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}