package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateRPPSException;
import l3o2.pharmacie.api.exceptions.InvalidDataException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.request.TitulaireCreateRequest;
import l3o2.pharmacie.api.model.dto.response.TitulaireResponse;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import l3o2.pharmacie.api.model.entity.Titulaire;
import l3o2.pharmacie.api.repository.TitulaireRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TitulaireServiceTest {

    @Mock
    private TitulaireRepository titulaireRepository;

    @Mock
    private EmployeService employeService;

    @InjectMocks
    private TitulaireService titulaireService;

    @Test
    void createTitulaire_WhenRPPSExists_ThrowsException() {
        // Given
        TitulaireCreateRequest request = sampleCreateRequest();
        when(titulaireRepository.existsByNumeroRPPS(request.getNumeroRPPS())).thenReturn(true);

        // When & Then
        assertThrows(DuplicateRPPSException.class,
                () -> titulaireService.createTitulaire(request));
    }

    @Test
    void createTitulaire_WhenValidRequest_ReturnsResponse() {
        // Given
        TitulaireCreateRequest request = sampleCreateRequest();
        Titulaire savedTitulaire = sampleTitulaire();

        when(titulaireRepository.existsByNumeroRPPS(any())).thenReturn(false);
        when(titulaireRepository.save(any())).thenReturn(savedTitulaire);

        // When
        TitulaireResponse response = titulaireService.createTitulaire(request);

        // Then
        assertNotNull(response);
        assertEquals("TIT-123", response.getMatricule());
        assertEquals("12345678901", response.getNumeroRPPS());
        verify(titulaireRepository).save(any());
    }

    @Test
    void createTitulaire_WhenDataViolation_ThrowsInvalidDataException() {
        // Given
        TitulaireCreateRequest request = sampleCreateRequest();
        when(titulaireRepository.existsByNumeroRPPS(any())).thenReturn(false);
        when(titulaireRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        // When & Then
        assertThrows(InvalidDataException.class,
                () -> titulaireService.createTitulaire(request));
    }

    @Test
    void getTitulaire_WhenNoneExists_ThrowsException() {
        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> titulaireService.getTitulaire());
    }

    @Test
    void deleteTitulaire_WhenExists_DeletesSuccessfully() {
        // Given
        UUID id = UUID.randomUUID();
        when(titulaireRepository.existsById(id)).thenReturn(true);

        // When
        titulaireService.deleteTitulaire(id);

        // Then
        verify(titulaireRepository).deleteById(id);
    }

    @Test
    void deleteTitulaire_WhenNotExists_ThrowsException() {
        // Given
        UUID id = UUID.randomUUID();
        when(titulaireRepository.existsById(id)).thenReturn(false);

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> titulaireService.deleteTitulaire(id));
    }

    // Helpers
    private TitulaireCreateRequest sampleCreateRequest() {
        return TitulaireCreateRequest.builder()
                .nom("Dupont")
                .prenom("Marie")
                .email("marie@example.com")
                .telephone("0612345678")
                .adresse("5 Rue des Pharmaciens")
                .password("password123")
                .emailPro("marie.pro@pharma.com")
                .dateEmbauche(new Date())
                .salaire(6000.0)
                .poste(PosteEmploye.TITULAIRE)
                .statutContrat(StatutContrat.CDI)
                .role("PHARMACIEN_TITULAIRE")
                .numeroRPPS("12345678901")
                .build();
    }

    private Titulaire sampleTitulaire() {
        return Titulaire.builder()
                .idPersonne(UUID.randomUUID())
                .matricule("TIT-123")
                .nom("Dupont")
                .prenom("Marie")
                .emailPro("marie.pro@pharma.com")
                .numeroRPPS("12345678901")
                .poste(PosteEmploye.TITULAIRE)
                .build();
    }
}