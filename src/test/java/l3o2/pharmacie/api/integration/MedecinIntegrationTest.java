// MedecinIntegrationTest.java
package l3o2.pharmacie.api.integration;

import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;
import l3o2.pharmacie.api.model.entity.Medecin;
import l3o2.pharmacie.api.repository.MedecinRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MedecinIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MedecinRepository medecinRepository;

    private final String BASE_URL = "/api/medecins";

    @Test
    void fullMedecinFlowTest() {
        // Création
        MedecinCreateRequest request = new MedecinCreateRequest();
        request.setNom("Dupont");
        request.setPrenom("Jean");
        request.setEmail("jean.dupont@medecin.fr");
        request.setTelephone("0612345678");
        request.setAdresse("15 Rue de la Santé");
        request.setPassword("Password123!");
        request.setRpps("12345678901");
        request.setAdeli("123456789");
        request.setCivilite("Dr");
        request.setProfession("Médecin généraliste");
        request.setSpecialitePrincipale("Cardiologie");
        request.setSpecialiteSecondaire("Nutrition");
        request.setModeExercice("Libéral"); 
        request.setCodePostal("75001"); 
        request.setVille("Paris"); 
        request.setSiteWeb("www.drdupont.fr");
        request.setSecteur("Secteur 1"); 
        request.setConventionnement("Conventionné"); 
        request.setHonoraires("Tarif conventionnel"); 
        request.setCarteVitale(true); 
        request.setTeleconsultation(true); 
        request.setLanguesParlees(Arrays.asList("Français", "Anglais"));
        request.setSiret("12345678901234"); 
        request.setDateMiseAJour(LocalDate.now());
        
        ResponseEntity<MedecinResponse> createResponse = restTemplate.postForEntity(
            BASE_URL, request, MedecinResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        MedecinResponse created = createResponse.getBody();
        UUID id = created.getIdPersonne();

        // Vérification en base
        Medecin dbEntity = medecinRepository.findById(id).orElseThrow();
        assertThat(dbEntity.getRpps()).isEqualTo("12345678901");

        // Mise à jour
        request.setPrenom("Jean-Pierre");
        request.setEmail("jeanpierre.dupont@medecin.fr");
        restTemplate.put(BASE_URL + "/" + id, request);

        // Vérification mise à jour
        Medecin updated = medecinRepository.findById(id).orElseThrow();
        assertThat(updated.getPrenom()).isEqualTo("Jean-Pierre");

        // Suppression
        restTemplate.delete(BASE_URL + "/" + id);
        assertThat(medecinRepository.existsById(id)).isFalse();
    }
}