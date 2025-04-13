package l3o2.pharmacie.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import l3o2.pharmacie.api.model.dto.request.FournisseurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.FournisseurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.FournisseurResponse;
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

@WebMvcTest(FournisseurController.class)
class FournisseurControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    private ApprentiService apprentiService;
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

    
    @Test
    void createFournisseur_RequeteValide_ReturnsCreated() throws Exception {
        UUID id = UUID.randomUUID();
        FournisseurCreateRequest request = new FournisseurCreateRequest();
        request.setNomSociete("Pharma Plus");
        request.setEmail("contact@pharmaplus.fr");
        request.setTelephone("0612345678");
        request.setAdresse("123 Rue de la Pharmacie, 75000 Paris");

        FournisseurResponse response = FournisseurResponse.builder()
                .idFournisseur(id)
                .nomSociete("Pharma Plus")
                .email("contact@pharmaplus.fr")
                .telephone("0612345678")
                .adresse("123 Rue de la Pharmacie, 75000 Paris")
                .build();
                
        when(fournisseurService.createFournisseur(any())).thenReturn(response);

        mockMvc.perform(post("/api/fournisseurs")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idFournisseur").value(id.toString()))
                .andExpect(jsonPath("$.nomSociete").value("Pharma Plus"))
                .andExpect(jsonPath("$.email").value("contact@pharmaplus.fr"))
                .andExpect(jsonPath("$.telephone").value("0612345678"))
                .andExpect(jsonPath("$.adresse").value("123 Rue de la Pharmacie, 75000 Paris"));
}
    
    @Test
    void getAllFournisseurs_ReturnsListOfFournisseurs() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        
        FournisseurResponse fournisseur1 = FournisseurResponse.builder()
                .idFournisseur(id1)
                .nomSociete("Pharma Plus")
                .email("contact@pharmaplus.fr")
                .build();
                
        FournisseurResponse fournisseur2 = FournisseurResponse.builder()
                .idFournisseur(id2)
                .nomSociete("MediStock")
                .email("info@medistock.fr")
                .build();
        
        List<FournisseurResponse> fournisseurs = Arrays.asList(fournisseur1, fournisseur2);
        
        when(fournisseurService.getAllFournisseurs()).thenReturn(fournisseurs);
        
        mockMvc.perform(get("/api/fournisseurs")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idFournisseur").value(id1.toString()))
                .andExpect(jsonPath("$[0].nomSociete").value("Pharma Plus"))
                .andExpect(jsonPath("$[1].idFournisseur").value(id2.toString()))
                .andExpect(jsonPath("$[1].nomSociete").value("MediStock"));
    }
    
    @Test
    void getFournisseurById_IdValide_ReturnsFournisseur() throws Exception {
        UUID id = UUID.randomUUID();
        
        FournisseurResponse response = FournisseurResponse.builder()
                .idFournisseur(id)
                .nomSociete("Pharma Plus")
                .email("contact@pharmaplus.fr")
                .telephone("0612345678")
                .build();
        
        when(fournisseurService.getFournisseurById(id)).thenReturn(response);
        
        mockMvc.perform(get("/api/fournisseurs/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFournisseur").value(id.toString()))
                .andExpect(jsonPath("$.nomSociete").value("Pharma Plus"))
                .andExpect(jsonPath("$.email").value("contact@pharmaplus.fr"));
    }
    
    @Test
    void getFournisseurByEmail_EmailValide_ReturnsFournisseur() throws Exception {
        UUID id = UUID.randomUUID();
        String email = "contact@pharmaplus.fr";
        
        FournisseurResponse response = FournisseurResponse.builder()
                .idFournisseur(id)
                .nomSociete("Pharma Plus")
                .email(email)
                .build();
        
        when(fournisseurService.getFournisseurByEmail(email)).thenReturn(response);
        
        mockMvc.perform(get("/api/fournisseurs/email/{email}", email)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFournisseur").value(id.toString()))
                .andExpect(jsonPath("$.nomSociete").value("Pharma Plus"))
                .andExpect(jsonPath("$.email").value(email));
    }
    
    @Test
    void getFournisseurByTelephone_TelephoneValide_ReturnsFournisseur() throws Exception {
        UUID id = UUID.randomUUID();
        String telephone = "0612345678";
        
        FournisseurResponse response = FournisseurResponse.builder()
                .idFournisseur(id)
                .nomSociete("Pharma Plus")
                .telephone(telephone)
                .build();
        
        when(fournisseurService.getFournisseurByTelephone(telephone)).thenReturn(response);
        
        mockMvc.perform(get("/api/fournisseurs/telephone/{telephone}", telephone)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFournisseur").value(id.toString()))
                .andExpect(jsonPath("$.nomSociete").value("Pharma Plus"))
                .andExpect(jsonPath("$.telephone").value(telephone));
    }
    
    @Test
    void updateFournisseur_IdValide_ReturnsFournisseur() throws Exception {
        UUID id = UUID.randomUUID();
        
        FournisseurUpdateRequest request = new FournisseurUpdateRequest();
        request.setNomSociete("Pharma Plus Updated");
        request.setEmail("nouveau@pharmaplus.fr");
        
        FournisseurResponse response = FournisseurResponse.builder()
                .idFournisseur(id)
                .nomSociete("Pharma Plus Updated")
                .email("nouveau@pharmaplus.fr")
                .build();
        
        when(fournisseurService.updateFournisseur(eq(id), any())).thenReturn(response);
        
        mockMvc.perform(put("/api/fournisseurs/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idFournisseur").value(id.toString()))
                .andExpect(jsonPath("$.nomSociete").value("Pharma Plus Updated"))
                .andExpect(jsonPath("$.email").value("nouveau@pharmaplus.fr"));
    }
    
    @Test
    void deleteFournisseur_IdValide_ReturnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(fournisseurService).deleteFournisseur(id);
        
        mockMvc.perform(delete("/api/fournisseurs/{id}", id))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void searchFournisseurs_TermeDeRecherche_ReturnsFournisseurs() throws Exception {
        UUID id = UUID.randomUUID();
        String query = "pharma";
        
        FournisseurResponse response = FournisseurResponse.builder()
                .idFournisseur(id)
                .nomSociete("Pharma Plus")
                .email("contact@pharmaplus.fr")
                .build();
        
        List<FournisseurResponse> fournisseurs = List.of(response);
        
        when(fournisseurService.searchFournisseurs(query)).thenReturn(fournisseurs);
        
        mockMvc.perform(get("/api/fournisseurs/search")
                .param("q", query)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idFournisseur").value(id.toString()))
                .andExpect(jsonPath("$[0].nomSociete").value("Pharma Plus"));
    }
}
