package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.PreparateurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.PreparateurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.PreparateurResponse;
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

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PreparateurController.class)
class PreparateurControllerTest {

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

    private final String BASE_URL = "/api/preparateurs";

    @Test
    void createPreparateur_ValidRequest_ReturnsCreated() throws Exception {
        PreparateurResponse response = sampleResponse();
        when(preparateurService.createPreparateur(any())).thenReturn(response);

        String requestBody = """
        {
            "nom": "Martin",
            "prenom": "Lucie",
            "email": "lucie@example.com",
            "telephone": "0612345678",
            "dateEmbauche": "2023-01-15",
            "salaire": 2800.0,
            "emailPro": "lucie.pro@pharma.com",
            "password": "secret123",
            "diplome": "BTS Pharma",
            "statutContrat": "CDD"
        }
        """;

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matricule").value("PREP-456"));
    }

    @Test
    void getPreparateurById_WhenExists_ReturnsOk() throws Exception {
        UUID id = UUID.randomUUID();
        PreparateurResponse response = sampleResponse();
        when(preparateurService.getPreparateurById(id)).thenReturn(response);

        mockMvc.perform(get(BASE_URL + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matricule").value("PREP-456"));
    }

    @Test
    void updatePreparateur_ValidRequest_ReturnsOk() throws Exception {
        UUID id = UUID.randomUUID();
        PreparateurResponse response = sampleResponse();
        when(preparateurService.updatePreparateur(eq(id), any())).thenReturn(response);

        String requestBody = """
        {
            "nom": "Martin",
            "salaire": 3000.0,
            "diplome": "DEUST Pharma"
        }
        """;

        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matricule").value("PREP-456"));
    }

    @Test
    void deletePreparateur_WhenExists_ReturnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(preparateurService).deletePreparateur(id);

        mockMvc.perform(delete(BASE_URL + "/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllPreparateurs_ReturnsList() throws Exception {
        when(preparateurService.getAllPreparateurs()).thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].matricule").value("PREP-456"));
    }

    @Test
    void searchPreparateurs_ValidTerm_ReturnsResults() throws Exception {
        when(preparateurService.searchPreparateurs("martin"))
            .thenReturn(List.of(sampleResponse()));

        mockMvc.perform(get(BASE_URL + "/search?term=martin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Martin"));
    }

    private PreparateurResponse sampleResponse() {
        return PreparateurResponse.builder()
                .idPersonne(UUID.randomUUID())
                .matricule("PREP-456")
                .nom("Martin")
                .prenom("Lucie")
                .email("lucie@example.com")
                .telephone("0612345678")
                .dateEmbauche(new Date())
                .salaire(2800.0)
                .diplome("BTS Pharma")
                .emailPro("lucie.pro@pharma.com")
                .build();
    }
}