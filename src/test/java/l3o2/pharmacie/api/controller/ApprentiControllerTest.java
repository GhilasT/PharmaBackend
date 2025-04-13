package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.ApprentiCreateRequest;
import l3o2.pharmacie.api.model.dto.response.ApprentiResponse;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApprentiController.class)
class ApprentiControllerTest {

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
void createApprenti_ValidRequest_ReturnsOk() throws Exception {
    // Créer une instance de ApprentiCreateRequest avec les valeurs nécessaires
    ApprentiCreateRequest request = ApprentiCreateRequest.builder()
            .nom("Dupont")
            .prenom("Jean")
            .email("jean.dupont@example.com")
            .telephone("0123456789")
            .adresse("123 Rue de la Pharmacie")
            .dateEmbauche(new Date())
            .salaire(1500.00)
            .poste(PosteEmploye.APPRENTI)
            .statutContrat(StatutContrat.CDI)
            .diplome("Bac+2")
            .ecole("École de Pharmacie")
            .emailPro("jean.dupont@pharmacie.com")
            .password("securePassword123")
            .build();

    ApprentiResponse response = new ApprentiResponse();
    response.setNom("Dupont");

    given(apprentiService.createApprenti(any())).willReturn(response);

    mockMvc.perform(post("/api/apprentis")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nom").value("Dupont"));
}


    @Test
    void getApprentiById_WhenExists_ReturnsApprenti() throws Exception {
        ApprentiResponse response = new ApprentiResponse();
        response.setIdPersonne(TEST_UUID);
        
        given(apprentiService.getApprentiById(TEST_UUID)).willReturn(response);
        
        mockMvc.perform(get("/api/apprentis/" + TEST_UUID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPersonne").value(TEST_UUID.toString()));
    }

    @Test
    void searchApprentis_WithTerm_ReturnsList() throws Exception {
        ApprentiResponse response = new ApprentiResponse();
        response.setNom("Dupont");
        
        given(apprentiService.searchApprentis(any())).willReturn(Collections.singletonList(response));
        
        mockMvc.perform(get("/api/apprentis/search")
                .param("term", "dup"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nom").value("Dupont"));
    }
}