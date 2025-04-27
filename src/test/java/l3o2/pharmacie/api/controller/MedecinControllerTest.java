package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;
import l3o2.pharmacie.api.service.MedecinService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebMvcTest(MedecinController.class)
class MedecinControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedecinService medecinService;

    // Test pour créer un médecin avec des informations valides
    @Test
    void createMedecin_RequeteValide_ReturnsOk() throws Exception {
        UUID idMedecin = UUID.randomUUID();

        MedecinResponse response = MedecinResponse.builder()
                .idMedecin(idMedecin)
                .civilite("M.")
                .nomExercice("Dupont")
                .prenomExercice("Jean")
                .rppsMedecin("10003456789")
                .profession("Médecin")
                .modeExercice("Libéral")
                .qualifications("Docteur en médecine")
                .structureExercice("Hôpital de Paris")
                .fonctionActivite("Médecin traitant")
                .genreActivite("Consultation")
                .build();

        when(medecinService.createMedecin(any(MedecinCreateRequest.class))).thenReturn(response);

        String requestBody = """
        {
            "civilite": "M.",
            "nomExercice": "Dupont",
            "prenomExercice": "Jean",
            "rppsMedecin": "10003456789",
            "profession": "Médecin",
            "modeExercice": "Libéral",
            "qualifications": "Docteur en médecine",
            "structureExercice": "Hôpital de Paris",
            "fonctionActivite": "Médecin traitant",
            "genreActivite": "Consultation"
        }
        """;

        mockMvc.perform(post("/api/medecins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMedecin").value(idMedecin.toString()))
                .andExpect(jsonPath("$.nomExercice").value("Dupont"))
                .andExpect(jsonPath("$.prenomExercice").value("Jean"))
                .andExpect(jsonPath("$.rppsMedecin").value("10003456789"))
                .andExpect(jsonPath("$.profession").value("Médecin"))
                .andExpect(jsonPath("$.modeExercice").value("Libéral"));
    }

    // Test pour vérifier qu'un médecin ne peut pas être créé avec un RPPS déjà existant
    @Test
    void createMedecin_RppsExistante_ThrowsException() throws Exception {
        when(medecinService.createMedecin(any(MedecinCreateRequest.class)))
                .thenThrow(new RuntimeException("Un médecin avec ce numéro RPPS existe déjà."));

        String requestBody = """
        {
            "civilite": "M.",
            "nomExercice": "Dupont",
            "prenomExercice": "Jean",
            "rppsMedecin": "10003456789",
            "profession": "Médecin",
            "modeExercice": "Libéral",
            "qualifications": "Docteur en médecine",
            "structureExercice": "Hôpital de Paris",
            "fonctionActivite": "Médecin traitant",
            "genreActivite": "Consultation"
        }
        """;

        mockMvc.perform(post("/api/medecins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest()) // Attendu : erreur 400
                .andExpect(jsonPath("$.message").value("Un médecin avec ce numéro RPPS existe déjà."));
    }

    // Test pour récupérer la liste des médecins
    @Test
    void getAllMedecins_ReturnsListOfMedecins() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        MedecinResponse medecin1 = MedecinResponse.builder()
                .idMedecin(id1)
                .nomExercice("Dupont")
                .prenomExercice("Jean")
                .rppsMedecin("10003456789")
                .profession("Médecin")
                .modeExercice("Libéral")
                .build();

        MedecinResponse medecin2 = MedecinResponse.builder()
                .idMedecin(id2)
                .nomExercice("Martin")
                .prenomExercice("Sophie")
                .rppsMedecin("10009876543")
                .profession("Médecin")
                .modeExercice("Libéral")
                .build();

        List<MedecinResponse> medecins = Arrays.asList(medecin1, medecin2);

        when(medecinService.getAllMedecins()).thenReturn(medecins);

        mockMvc.perform(get("/api/medecins")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idMedecin").value(id1.toString()))
                .andExpect(jsonPath("$[0].nomExercice").value("Dupont"))
                .andExpect(jsonPath("$[0].prenomExercice").value("Jean"))
                .andExpect(jsonPath("$[0].rppsMedecin").value("10003456789"))
                .andExpect(jsonPath("$[1].idMedecin").value(id2.toString()))
                .andExpect(jsonPath("$[1].nomExercice").value("Martin"))
                .andExpect(jsonPath("$[1].prenomExercice").value("Sophie"))
                .andExpect(jsonPath("$[1].rppsMedecin").value("10009876543"));
    }

    // Test pour récupérer un médecin par ID
    @Test
    void getMedecinById_IdValide_ReturnsMedecin() throws Exception {
        UUID id = UUID.randomUUID();

        MedecinResponse medecin = MedecinResponse.builder()
                .idMedecin(id)
                .nomExercice("Dupont")
                .prenomExercice("Jean")
                .rppsMedecin("10003456789")
                .profession("Médecin")
                .modeExercice("Libéral")
                .build();

        when(medecinService.getMedecinById(id)).thenReturn(medecin);

        mockMvc.perform(get("/api/medecins/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMedecin").value(id.toString()))
                .andExpect(jsonPath("$.nomExercice").value("Dupont"))
                .andExpect(jsonPath("$.prenomExercice").value("Jean"))
                .andExpect(jsonPath("$.rppsMedecin").value("10003456789"))
                .andExpect(jsonPath("$.profession").value("Médecin"));
    }

    // Test pour mettre à jour un médecin
    @Test
    void updateMedecin_IdValide_ReturnsMedecin() throws Exception {
        UUID id = UUID.randomUUID();

        MedecinResponse updatedMedecin = MedecinResponse.builder()
                .idMedecin(id)
                .nomExercice("Dupont")
                .prenomExercice("Jean-Pierre")
                .rppsMedecin("10003456789")
                .profession("Médecin")
                .modeExercice("Libéral")
                .build();

        when(medecinService.updateMedecin(eq(id), any(MedecinCreateRequest.class))).thenReturn(updatedMedecin);

        String requestBody = """
        {
            "nomExercice": "Dupont",
            "prenomExercice": "Jean-Pierre",
            "rppsMedecin": "10003456789",
            "profession": "Médecin",
            "modeExercice": "Libéral"
        }
        """;

        mockMvc.perform(put("/api/medecins/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMedecin").value(id.toString()))
                .andExpect(jsonPath("$.nomExercice").value("Dupont"))
                .andExpect(jsonPath("$.prenomExercice").value("Jean-Pierre"))
                .andExpect(jsonPath("$.rppsMedecin").value("10003456789"))
                .andExpect(jsonPath("$.profession").value("Médecin"));
    }

    // Test pour supprimer un médecin
    @Test
    void deleteMedecin_IdValide_ReturnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(medecinService).deleteMedecin(id);
        mockMvc.perform(delete("/api/medecins/{id}", id))
                .andExpect(status().isNoContent());
    }


}