package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.MedicamentPanierRequest;
import l3o2.pharmacie.api.model.dto.request.VenteCreateRequest;
import l3o2.pharmacie.api.model.dto.response.VenteResponse;
import l3o2.pharmacie.api.repository.MedicamentRepository;
import l3o2.pharmacie.api.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.text.SimpleDateFormat;
import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(controllers = VenteController.class)
public class VenteControllerTest {

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

    private VenteCreateRequest venteCreateRequest;
    private VenteResponse venteResponse;
    private UUID testUuid;

    @BeforeEach
    void setUp() {
        // Création d'un code CIP-13 valide (13 chiffres)
        String codeCip13 = String.format("%013d", new Random().nextLong()).substring(0, 13);

        MedicamentPanierRequest panierRequest = MedicamentPanierRequest.builder()
                .codeCip13(codeCip13)
                .quantite(5)
                .build();

        venteCreateRequest = VenteCreateRequest.builder()
                .pharmacienAdjointId(UUID.randomUUID())
                .clientId(UUID.randomUUID())
                .medicaments(Collections.singletonList(panierRequest))
                .dateVente(new Date())
                .modePaiement("Carte bancaire")
                .montantTotal(50.0)
                .montantRembourse(10.0)
                .build();

        venteResponse = VenteResponse.builder()
                .idVente(UUID.randomUUID())
                .dateVente(new Date())
                .modePaiement("Carte bancaire")
                .montantTotal(50.0)
                .montantRembourse(10.0)
                .build();
    }

    @Test
    void createVente_ShouldReturnCreated() throws Exception {
        when(venteService.createVente(any(VenteCreateRequest.class))).thenReturn(venteResponse);

        mockMvc.perform(post("/api/ventes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(venteCreateRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idVente").value(venteResponse.getIdVente().toString()))
                .andExpect(jsonPath("$.modePaiement").value(venteResponse.getModePaiement()))
                .andExpect(jsonPath("$.montantTotal").value(venteResponse.getMontantTotal()))
                .andExpect(jsonPath("$.montantRembourse").value(venteResponse.getMontantRembourse()));
    }


    @Test
    void deleteVente_ShouldReturnNoContent() throws Exception {
        UUID venteId = UUID.randomUUID();
        doNothing().when(venteService).delete(venteId);

        mockMvc.perform(delete("/api/ventes/{id}", venteId))
                .andExpect(status().isNoContent());
    }

    private String asJsonString(final Object obj) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
            mapper.setTimeZone(TimeZone.getTimeZone("UTC"));
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}