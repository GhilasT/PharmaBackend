package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.TitulaireCreateRequest;
import l3o2.pharmacie.api.model.dto.response.TitulaireResponse;
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

import java.util.Date;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TitulaireController.class)
class TitulaireControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TitulaireService titulaireService;

    // Mock de toutes les dépendances nécessaires pour le contexte Spring
    @MockBean private MedicamentRepository medicamentRepository;
    @MockBean private AdministrateurService administrateurService;
    @MockBean private PharmacienAdjointService pharmacienAdjointService;
    @MockBean private EmployeService employeService;
    @MockBean private CsvImportService csvImportService;
    @MockBean private ApprentiService apprentiService;
    @MockBean private PreparateurService preparateurService;
    @MockBean private ClientService clientService;
    @MockBean private FournisseurService fournisseurService;
    @MockBean private MedecinService medecinService;
    @MockBean private MedicamentService medicamentService;
    @MockBean private CisBdpmService cisBdpmService;
    @MockBean private CisCpdBdpmService cisCpdBdpmService;
    @MockBean private CisCipBdpmService cisCipBdpmService;
    @MockBean private VenteService venteService;
    @MockBean private CommandeService commandeService;

    private final String BASE_URL = "/api/titulaires";

    @Test
    void createTitulaire_ValidRequest_ReturnsCreated() throws Exception {
        // Given
        TitulaireResponse response = sampleResponse();
        when(titulaireService.createTitulaire(any())).thenReturn(response);

        String requestBody = """
        {
            "nom": "Dupont",
            "prenom": "Marie",
            "email": "marie@example.com",
            "telephone": "0612345678",
            "adresse": "5 Rue des Pharmaciens",
            "password": "password123",
            "emailPro": "marie.pro@pharma.com",
            "dateEmbauche": "2023-01-01",
            "salaire": 6000.0,
            "poste": "TITULAIRE",
            "statutContrat": "CDI",
            "role": "PHARMACIEN_TITULAIRE",
            "numeroRPPS": "12345678901"
        }
        """;

        // When & Then
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.matricule").value("TIT-123"))
                .andExpect(jsonPath("$.numeroRPPS").value("12345678901"));
    }

    @Test
    void getTitulaire_WhenExists_ReturnsOk() throws Exception {
        // Given
        TitulaireResponse response = sampleResponse();
        when(titulaireService.getTitulaire()).thenReturn(response);

        // When & Then
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nom").value("Dupont"))
                .andExpect(jsonPath("$.poste").value("TITULAIRE"));
    }

    @Test
    void deleteTitulaire_WhenExists_ReturnsNoContent() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        
        // When & Then
        mockMvc.perform(delete(BASE_URL + "/" + id))
                .andExpect(status().isNoContent());
    }

    private TitulaireResponse sampleResponse() {
        return TitulaireResponse.builder()
                .idPersonne(UUID.randomUUID())
                .matricule("TIT-123")
                .nom("Dupont")
                .prenom("Marie")
                .email("marie@example.com")
                .telephone("0612345678")
                .adresse("5 Rue des Pharmaciens")
                .emailPro("marie.pro@pharma.com")
                .dateEmbauche(new Date())
                .salaire(6000.0)
                .statutContrat(StatutContrat.CDI)
                .role("PHARMACIEN_TITULAIRE")
                .numeroRPPS("12345678901")
                .poste(PosteEmploye.TITULAIRE)
                .build();
    }
}