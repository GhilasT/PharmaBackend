package l3o2.pharmacie.api.integration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class MedecinIntegrationTest {
/*
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private MedecinRepository medecinRepository;

    private final String BASE_URL = "/api/medecins";

    @Test
    void fullMedecinFlowTest() {
        // Création
        MedecinCreateRequest request = MedecinCreateRequest.builder()
                .nomExercice("Dupont")
                .prenomExercice("Jean")
                .rppsMedecin("12345678901")
                .civilite("Dr")
                .profession("Médecin généraliste")
                .modeExercice("Libéral")
                .qualifications("Cardiologie")
                .structureExercice("Cabinet privé")
                .fonctionActivite("Médecin traitant")
                .genreActivite("Consultation")
                .build();

        ResponseEntity<MedecinResponse> createResponse = restTemplate.postForEntity(
                BASE_URL, request, MedecinResponse.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        MedecinResponse created = createResponse.getBody();
        UUID id = created.getIdMedecin();

        // Vérification en base
        Medecin dbEntity = medecinRepository.findById(id).orElseThrow();
        assertThat(dbEntity.getRppsMedecin()).isEqualTo("12345678901");

        // Mise à jour
        request.setPrenomExercice("Jean-Pierre");
        restTemplate.put(BASE_URL + "/" + id, request);

        // Vérification mise à jour
        Medecin updated = medecinRepository.findById(id).orElseThrow();
        assertThat(updated.getPrenomExercice()).isEqualTo("Jean-Pierre");

        // Suppression
        restTemplate.delete(BASE_URL + "/" + id);
        assertThat(medecinRepository.existsById(id)).isFalse();
    }

 */
}