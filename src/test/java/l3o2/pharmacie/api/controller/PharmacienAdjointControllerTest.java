package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointCreateRequest;
import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.PharmacienAdjointResponse;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import l3o2.pharmacie.api.repository.MedicamentRepository;
import l3o2.pharmacie.api.service.AdministrateurService;
import l3o2.pharmacie.api.service.ApprentiService;
import l3o2.pharmacie.api.service.CisBdpmService;
import l3o2.pharmacie.api.service.CisCipBdpmService;
import l3o2.pharmacie.api.service.CisCpdBdpmService;
import l3o2.pharmacie.api.service.ClientService;
import l3o2.pharmacie.api.service.CommandeService;
import l3o2.pharmacie.api.service.CsvImportService;
import l3o2.pharmacie.api.service.EmployeService;
import l3o2.pharmacie.api.service.FournisseurService;
import l3o2.pharmacie.api.service.MedecinService;
import l3o2.pharmacie.api.service.MedicamentService;
import l3o2.pharmacie.api.service.PharmacienAdjointService;
import l3o2.pharmacie.api.service.PreparateurService;
import l3o2.pharmacie.api.service.TitulaireService;
import l3o2.pharmacie.api.service.VenteService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PharmacienAdjointController.class)
class PharmacienAdjointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ApprentiService apprentiService;
    @MockBean
    private MedicamentRepository medicamentRepository;

    @MockBean
    private AdministrateurService administrateurService;

   @MockBean
    private PharmacienAdjointService pharmacienAdjointService;
    @MockBean
    private EmployeService employeService;
    @MockBean
    private CsvImportService csvImportService;
    @MockBean
    private PreparateurService preparateurService;
    @MockBean
    private TitulaireService titulaireService;
    @MockBean
    private ClientService clientService;
    @MockBean
    private FournisseurService fournisseurService;
    @MockBean
    private MedecinService medecinService;
    @MockBean
    private MedicamentService medicamentService;
    @MockBean
    private CisBdpmService cisBdpmService;
    @MockBean
    private CisCpdBdpmService cisCpdBdpmService;
    @MockBean
    private CisCipBdpmService cisCipBdpmService;
    @MockBean
    private VenteService venteService;
    @MockBean
    private CommandeService commandeService;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID TEST_UUID = UUID.randomUUID();

    @Test
    void createPharmacienAdjoint_ValidRequest_ReturnsCreated() throws Exception {
        // Créer une instance de PharmacienAdjointCreateRequest avec les valeurs nécessaires
        PharmacienAdjointCreateRequest request = PharmacienAdjointCreateRequest.builder()
                .nom("Doe")
                .prenom("John")
                .email("john.doe@example.com")
                .telephone("0456789123")
                .adresse("123 Rue de la Pharmacie")
                .dateEmbauche(new Date())
                .salaire(4000.00)
                .poste(PosteEmploye.PHARMACIEN_ADJOINT)
                .statutContrat(StatutContrat.CDI)
                .diplome("Diplôme de Pharmacie")
                .emailPro("john.doe@pharmacie.com")
                .password("securePassword123")
                .build();

        PharmacienAdjointResponse response = new PharmacienAdjointResponse();
        response.setNom("Doe");

        given(pharmacienAdjointService.createPharmacienAdjoint(request)).willReturn(response);

        mockMvc.perform(post("/api/pharmaciens-adjoints")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nom").value("Doe"));
    }

    @Test
    void getAllPharmaciensAdjoints_ReturnsList() throws Exception {
        PharmacienAdjointResponse response = new PharmacienAdjointResponse();
        response.setNom("Doe");

        given(pharmacienAdjointService.getAllPharmaciensAdjoints()).willReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/pharmaciens-adjoints"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Doe"));
    }

    @Test
    void deletePharmacienAdjoint_WhenExists_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/pharmaciens-adjoints/" + TEST_UUID))
                .andExpect(status().isNoContent());
    }

    @Test
    void updatePharmacienAdjoint_ValidRequest_ReturnsOk() throws Exception {
        PharmacienAdjointUpdateRequest request = PharmacienAdjointUpdateRequest.builder()
                .nom("UpdatedDoe")
                .build();

        PharmacienAdjointResponse response = new PharmacienAdjointResponse();
        response.setNom("UpdatedDoe");

        given(pharmacienAdjointService.updatePharmacienAdjoint(any(), any())).willReturn(response);

        mockMvc.perform(put("/api/pharmaciens-adjoints/" + TEST_UUID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("UpdatedDoe"));
    }

    @Test
    void searchPharmaciensAdjoints_WithTerm_ReturnsList() throws Exception {
        PharmacienAdjointResponse response = new PharmacienAdjointResponse();
        response.setNom("Doe");

        given(pharmacienAdjointService.searchPharmaciensAdjoints(any())).willReturn(Collections.singletonList(response));

        mockMvc.perform(get("/api/pharmaciens-adjoints/search")
                .param("term", "doe"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Doe"));
    }
}
