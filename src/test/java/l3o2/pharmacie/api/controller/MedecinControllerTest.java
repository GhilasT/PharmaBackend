package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.MedecinUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;
import l3o2.pharmacie.api.repository.MedicamentRepository;
import l3o2.pharmacie.api.service.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MedecinController.class)
class MedecinControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

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
    void createMedecin_RequeteValide_ReturnsOk() throws Exception {
        UUID idPersonne = UUID.randomUUID();
        
        MedecinResponse response = MedecinResponse.builder()
                .idPersonne(idPersonne)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .telephone("0123456789")
                .adresse("15 rue de la Santé")
                .rpps("10003456789")
                .adeli("123456789")
                .civilite("M.")
                .profession("Médecin")
                .specialitePrincipale("Cardiologie")
                .specialiteSecondaire("Médecine du sport")
                .modeExercice("Libéral")
                .codePostal("75014")
                .ville("Paris")
                .siteWeb("www.docteur-dupont.fr")
                .secteur("Secteur 1")
                .conventionnement("Conventionné")
                .honoraires("Tarif conventionnel")
                .carteVitale(true)
                .teleconsultation(true)
                .languesParlees(Arrays.asList("Français", "Anglais"))
                .siret("12345678901234")
                .dateMiseAJour(LocalDate.of(2023, 5, 15))
                .build();
                
        when(medecinService.createMedecin(any())).thenReturn(response);

        String requestBody = """
        {
            "nom": "Dupont",
            "prenom": "Jean",
            "email": "jean.dupont@example.com",
            "telephone": "0123456789",
            "adresse": "15 rue de la Santé",
            "password": "MotDePasse123!",
            "rpps": "10003456789",
            "adeli": "123456789",
            "civilite": "M.",
            "profession": "Médecin",
            "specialitePrincipale": "Cardiologie",
            "specialiteSecondaire": "Médecine du sport",
            "modeExercice": "Libéral",
            "codePostal": "75014",
            "ville": "Paris",
            "siteWeb": "www.docteur-dupont.fr",
            "secteur": "Secteur 1",
            "conventionnement": "Conventionné",
            "honoraires": "Tarif conventionnel",
            "carteVitale": true,
            "teleconsultation": true,
            "languesParlees": ["Français", "Anglais"],
            "siret": "12345678901234",
            "dateMiseAJour": "2023-05-15"
        }
        """;

        // When & Then
        mockMvc.perform(post("/api/medecins")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPersonne").value(idPersonne.toString()))
                .andExpect(jsonPath("$.nom").value("Dupont"))
                .andExpect(jsonPath("$.prenom").value("Jean"))
                .andExpect(jsonPath("$.email").value("jean.dupont@example.com"))
                .andExpect(jsonPath("$.rpps").value("10003456789"))
                .andExpect(jsonPath("$.specialitePrincipale").value("Cardiologie"));
    }

    @Test
    void getAllMedecins_ReturnsListOfMedecins() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        
        MedecinResponse medecin1 = MedecinResponse.builder()
                .idPersonne(id1)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .telephone("0123456789")
                .adresse("15 rue de la Santé")
                .rpps("10003456789")
                .adeli("123456789")
                .civilite("M.")
                .profession("Médecin")
                .specialitePrincipale("Cardiologie")
                .build();
                
        MedecinResponse medecin2 = MedecinResponse.builder()
                .idPersonne(id2)
                .nom("Martin")
                .prenom("Sophie")
                .email("sophie.martin@example.com")
                .telephone("0987654321")
                .adresse("25 avenue de la République")
                .rpps("10009876543")
                .adeli("987654321")
                .civilite("Mme")
                .profession("Médecin")
                .specialitePrincipale("Pédiatrie")
                .build();
        
        List<MedecinResponse> medecins = Arrays.asList(medecin1, medecin2);
        
        when(medecinService.getAllMedecins()).thenReturn(medecins);
        
        mockMvc.perform(get("/api/medecins")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPersonne").value(id1.toString()))
                .andExpect(jsonPath("$[0].nom").value("Dupont"))
                .andExpect(jsonPath("$[0].prenom").value("Jean"))
                .andExpect(jsonPath("$[0].specialitePrincipale").value("Cardiologie"))
                .andExpect(jsonPath("$[1].idPersonne").value(id2.toString()))
                .andExpect(jsonPath("$[1].nom").value("Martin"))
                .andExpect(jsonPath("$[1].prenom").value("Sophie"))
                .andExpect(jsonPath("$[1].specialitePrincipale").value("Pédiatrie"));
    }
    
    @Test
    void getMedecinById_IdValide_ReturnsMedecin() throws Exception {
        UUID id = UUID.randomUUID();
        
        MedecinResponse medecin = MedecinResponse.builder()
                .idPersonne(id)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .telephone("0123456789")
                .adresse("15 rue de la Santé")
                .rpps("10003456789")
                .adeli("123456789")
                .civilite("M.")
                .profession("Médecin")
                .specialitePrincipale("Cardiologie")
                .specialiteSecondaire("Médecine du sport")
                .modeExercice("Libéral")
                .codePostal("75014")
                .ville("Paris")
                .build();
        
        when(medecinService.getMedecinById(id)).thenReturn(medecin);
        
        mockMvc.perform(get("/api/medecins/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPersonne").value(id.toString()))
                .andExpect(jsonPath("$.nom").value("Dupont"))
                .andExpect(jsonPath("$.prenom").value("Jean"))
                .andExpect(jsonPath("$.email").value("jean.dupont@example.com"))
                .andExpect(jsonPath("$.specialitePrincipale").value("Cardiologie"));
    }
    
    @Test
    void updateMedecin_IdValide_ReturnsMedecin() throws Exception {
        UUID id = UUID.randomUUID();
        MedecinResponse updatedMedecin = MedecinResponse.builder()
                .idPersonne(id)
                .nom("Dupont")
                .prenom("Jean-Pierre")
                .email("jeanpierre.dupont@example.com")
                .telephone("0123456789")
                .adresse("20 rue de la Paix")
                .rpps("10003456789")
                .adeli("123456789")
                .civilite("M.")
                .profession("Médecin")
                .specialitePrincipale("Cardiologie")
                .specialiteSecondaire("Médecine du sport")
                .build();
        
        when(medecinService.updateMedecin(eq(id), any(MedecinUpdateRequest.class))).thenReturn(updatedMedecin);
        
        String requestBody = """
        {
            "nom": "Dupont",
            "prenom": "Jean-Pierre",
            "email": "jeanpierre.dupont@example.com",
            "telephone": "0123456789",
            "adresse": "20 rue de la Paix",
            "password": "MotDePasse123!",
            "codeCIS": "12345678",
            "denomination": "Médicament test",
            "formePharmaceutique": "Comprimé",
            "voiesAdministration": "Orale",
            "statutAdministratifAMM": "Autorisé",
            "typeProcedureAMM": "Nationale",
            "etatCommercialisation": "Commercialisé",
            "titulaire": "Laboratoire Test",
            "prix": 10.5,
            "codeCIP": "1234567",
            "indication": "Traitement test",
            "posologie": "1 comprimé par jour",
            "generique": "Non"
        }
        """;
        
        mockMvc.perform(put("/api/medecins/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idPersonne").value(id.toString()))
                .andExpect(jsonPath("$.nom").value("Dupont"))
                .andExpect(jsonPath("$.prenom").value("Jean-Pierre"))
                .andExpect(jsonPath("$.email").value("jeanpierre.dupont@example.com"))
                .andExpect(jsonPath("$.adresse").value("20 rue de la Paix"));
    }
    
    @Test
    void deleteMedecin_IdValide_ReturnsNoContent() throws Exception {
        UUID id = UUID.randomUUID();
        doNothing().when(medecinService).deleteMedecin(id);
        mockMvc.perform(delete("/api/medecins/{id}", id))
                .andExpect(status().isNoContent());
    }
    
    @Test
    void searchMedecins_TermeDeRecherche_ReturnsMatchingMedecins() throws Exception {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        
        MedecinResponse medecin1 = MedecinResponse.builder()
                .idPersonne(id1)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .specialitePrincipale("Cardiologie")
                .build();
                
        MedecinResponse medecin2 = MedecinResponse.builder()
                .idPersonne(id2)
                .nom("Duponteil")
                .prenom("Marie")
                .email("marie.duponteil@example.com")
                .specialitePrincipale("Dermatologie")
                .build();
        
        List<MedecinResponse> medecins = Arrays.asList(medecin1, medecin2);
        
        when(medecinService.getMedecinsByNomOuPrenom("Dupont")).thenReturn(medecins);
        
        mockMvc.perform(get("/api/medecins/search")
                .param("q", "Dupont")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].idPersonne").value(id1.toString()))
                .andExpect(jsonPath("$[0].nom").value("Dupont"))
                .andExpect(jsonPath("$[1].idPersonne").value(id2.toString()))
                .andExpect(jsonPath("$[1].nom").value("Duponteil"));
    }
}
