package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateEmailProException;
import l3o2.pharmacie.api.exceptions.InvalidDataException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.request.PreparateurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.PreparateurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.PreparateurResponse;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.Preparateur;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import l3o2.pharmacie.api.repository.PreparateurRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PreparateurServiceTest {

    @Mock
    private PreparateurRepository preparateurRepository;

    @Mock
    private EmployeService employeService;

    @InjectMocks
    private PreparateurService preparateurService;

    @Test
    void createPreparateur_WhenEmailProExists_ThrowsException() {
        // Given
        PreparateurCreateRequest request = sampleCreateRequest();
        when(employeService.existsByEmailPro(request.getEmailPro().trim())).thenReturn(true);

        // When & Then
        assertThrows(DuplicateEmailProException.class, 
            () -> preparateurService.createPreparateur(request));
    }

    @Test
    void createPreparateur_WhenValidRequest_ReturnsResponse() {
        // Given
        PreparateurCreateRequest request = sampleCreateRequest();
        Preparateur saved = samplePreparateur();
        
        when(employeService.existsByEmailPro(any())).thenReturn(false);
        when(preparateurRepository.save(any())).thenReturn(saved);

        // When
        PreparateurResponse response = preparateurService.createPreparateur(request);

        // Then
        assertNotNull(response);
        assertEquals("PREP-456", response.getMatricule());
        verify(preparateurRepository).save(any());
    }

    @Test
    void createPreparateur_WhenDataViolation_ThrowsInvalidDataException() {
        // Given
        PreparateurCreateRequest request = sampleCreateRequest();
        when(employeService.existsByEmailPro(any())).thenReturn(false);
        when(preparateurRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        // When & Then
        assertThrows(InvalidDataException.class, 
            () -> preparateurService.createPreparateur(request));
    }

    @Test
    void getPreparateurById_WhenExists_ReturnsResponse() {
        // Given
        UUID id = UUID.randomUUID();
        Preparateur preparateur = samplePreparateur();
        when(preparateurRepository.findById(id)).thenReturn(Optional.of(preparateur));

        // When
        PreparateurResponse response = preparateurService.getPreparateurById(id);

        // Then
        assertEquals(preparateur.getNom(), response.getNom());
    }

    @Test
    void getPreparateurById_WhenNotFound_ThrowsException() {
        // Given
        UUID id = UUID.randomUUID();
        when(preparateurRepository.findById(id)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, 
            () -> preparateurService.getPreparateurById(id));
    }

    @Test
    void updatePreparateur_WhenEmailProConflict_ThrowsException() {
        // Given
        UUID id = UUID.randomUUID();
        PreparateurUpdateRequest request = new PreparateurUpdateRequest();
        request.setEmailPro("new@email.com");
        
        Preparateur existing = samplePreparateur();
        when(preparateurRepository.findById(id)).thenReturn(Optional.of(existing));
        when(employeService.existsByEmailPro(request.getEmailPro().trim())).thenReturn(true);

        // When & Then
        assertThrows(DuplicateEmailProException.class, 
            () -> preparateurService.updatePreparateur(id, request));
    }

    @Test
    void deletePreparateur_WhenExists_DeletesSuccessfully() {
        // Given
        UUID id = UUID.randomUUID();
        when(preparateurRepository.existsById(id)).thenReturn(true);

        // When
        preparateurService.deletePreparateur(id);

        // Then
        verify(preparateurRepository).deleteById(id);
    }

    @Test
    void searchPreparateurs_ValidTerm_ReturnsResults() {
        // Given
        String term = "martin";
        when(preparateurRepository.searchByNomPrenom(term))
            .thenReturn(List.of(samplePreparateur()));

        // When
        List<PreparateurResponse> results = preparateurService.searchPreparateurs(term);

        // Then
        assertFalse(results.isEmpty());
        assertEquals("Martin", results.get(0).getNom());
    }

    // Helpers
    private PreparateurCreateRequest sampleCreateRequest() {
        return PreparateurCreateRequest.builder()
            .nom("Martin")
            .prenom("Lucie")
            .email("lucie@example.com")
            .telephone("0612345678")
            .dateEmbauche(new Date())
            .salaire(2800.0)
            .emailPro("lucie.pro@pharma.com")
            .password("secret123")
            .diplome("BTS Pharma")
            .statutContrat(StatutContrat.CDD)
            .build();
    }

    private Preparateur samplePreparateur() {
        return Preparateur.builder()
            .idPersonne(UUID.randomUUID())
            .matricule("PREP-456")
            .nom("Martin")
            .prenom("Lucie")
            .emailPro("lucie.pro@pharma.com")
            .poste(PosteEmploye.PREPARATEUR)
            .build();
    }
}