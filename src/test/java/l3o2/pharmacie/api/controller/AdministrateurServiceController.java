package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.AdministrateurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.AdministrateurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.AdministrateurResponse;
import l3o2.pharmacie.api.service.AdministrateurService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdministrateurController.class)
class AdministrateurControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdministrateurService administrateurService;

    private final String BASE_URL = "/api/administrateurs";

    @Test
    void createAdministrateur_ValidRequest_ReturnsCreated() throws Exception {
        // Given
        AdministrateurResponse response = sampleResponse();
        when(administrateurService.createAdministrateur(any())).thenReturn(response);

        String requestBody = """
            {
                "nom": "Doe",
                "prenom": "John",
                "email": "john@example.com",
                "emailPro": "john.pro@pharma.com",
                "password": "password",
                "role": "SUPER_ADMIN",
                "dateEmbauche": "2023-01-01",
                "salaire": 5000.0
            }
            """;

        // When & Then
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.matricule").value("ADMIN-123"));
    }

    @Test
    void getAdministrateurByMatricule_WhenExists_ReturnsOk() throws Exception {
        // Given
        AdministrateurResponse response = sampleResponse();
        when(administrateurService.getAdministrateurByMatricule("ADMIN-123")).thenReturn(response);

        // When & Then
        mockMvc.perform(get(BASE_URL + "/ADMIN-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matricule").value("ADMIN-123"));
    }

    @Test
    void updateAdministrateur_ValidRequest_ReturnsOk() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        AdministrateurResponse response = sampleResponse();
        when(administrateurService.updateAdministrateur(eq(id), any())).thenReturn(response);

        String requestBody = """
            {
                "nom": "NouveauNom",
                "salaire": 6000.0
            }
            """;

        // When & Then
        mockMvc.perform(put(BASE_URL + "/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.matricule").value("ADMIN-123"));
    }

    @Test
    void deleteAdministrateur_WhenExists_ReturnsNoContent() throws Exception {
        // Given
        UUID id = UUID.randomUUID();
        doNothing().when(administrateurService).deleteAdministrateur(id);

        // When & Then
        mockMvc.perform(delete(BASE_URL + "/" + id))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllAdministrateurs_ReturnsList() throws Exception {
        // Given
        when(administrateurService.getAllAdministrateurs()).thenReturn(List.of(sampleResponse()));

        // When & Then
        mockMvc.perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].matricule").value("ADMIN-123"));
    }

    // Helper
    private AdministrateurResponse sampleResponse() {
        return AdministrateurResponse.builder()
                .matricule("ADMIN-123")
                .nom("Doe")
                .prenom("John")
                .email("john@example.com")
                .role("SUPER_ADMIN")
                .build();
    }
}