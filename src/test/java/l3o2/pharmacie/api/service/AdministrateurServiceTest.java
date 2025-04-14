package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateEmailProException;
import l3o2.pharmacie.api.exceptions.InvalidDataException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.request.AdministrateurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.AdministrateurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.AdministrateurResponse;
import l3o2.pharmacie.api.model.entity.Administrateur;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import l3o2.pharmacie.api.repository.AdministrateurRepository;
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
class AdministrateurServiceTest {

    @Mock
    private AdministrateurRepository administrateurRepository;

    @Mock
    private EmployeService employeService;

    @InjectMocks
    private AdministrateurService administrateurService;

    @Test
    void createAdministrateur_WhenEmailProExists_ThrowsException() {
        // Given
        AdministrateurCreateRequest request = sampleCreateRequest();
        when(employeService.existsByEmailPro(request.getEmailPro())).thenReturn(true);

        // When & Then
        assertThrows(DuplicateEmailProException.class, () -> administrateurService.createAdministrateur(request));
    }

    @Test
    void createAdministrateur_WhenValidRequest_ReturnsResponse() {
        // Given
        AdministrateurCreateRequest request = sampleCreateRequest();
        Administrateur savedAdmin = sampleAdministrateur();

        when(employeService.existsByEmailPro(any())).thenReturn(false);
        when(administrateurRepository.save(any())).thenReturn(savedAdmin);

        // When
        AdministrateurResponse response = administrateurService.createAdministrateur(request);

        // Then
        assertNotNull(response);
        assertEquals(savedAdmin.getMatricule(), response.getMatricule());
        verify(administrateurRepository).save(any());
    }

    @Test
    void createAdministrateur_WhenDataIntegrityViolation_ThrowsException() {
        // Given
        AdministrateurCreateRequest request = sampleCreateRequest();
        when(employeService.existsByEmailPro(any())).thenReturn(false);
        when(administrateurRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        // When & Then
        assertThrows(InvalidDataException.class, () -> administrateurService.createAdministrateur(request));
    }

    @Test
    void getAdministrateurByMatricule_WhenExists_ReturnsResponse() {
        // Given
        Administrateur admin = sampleAdministrateur();
        when(administrateurRepository.findByMatricule(admin.getMatricule()))
                .thenReturn(Optional.of(admin));

        // When
        AdministrateurResponse response = administrateurService.getAdministrateurByMatricule("ADMIN-123");

        // Then
        assertNotNull(response);
        assertEquals(admin.getMatricule(), response.getMatricule());
    }

    @Test
    void getAdministrateurByMatricule_WhenNotExists_ThrowsException() {
        // Given
        String matricule = "INVALID";
        when(administrateurRepository.findByMatricule(matricule)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> administrateurService.getAdministrateurByMatricule(matricule));
    }

    @Test
    void updateAdministrateur_WhenValidRequest_UpdatesFields() {
        // Given
        UUID id = UUID.randomUUID();
        AdministrateurUpdateRequest request = sampleUpdateRequest();
        Administrateur admin = sampleAdministrateur();

        when(administrateurRepository.findById(id)).thenReturn(Optional.of(admin));
        when(administrateurRepository.save(any())).thenReturn(admin);

        // When
        AdministrateurResponse response = administrateurService.updateAdministrateur(id, request);

        // Then
        assertEquals("NouveauNom", response.getNom());
        verify(administrateurRepository).save(admin);
    }

    @Test
    void deleteAdministrateur_WhenExists_DeletesSuccessfully() {
        // Given
        UUID id = UUID.randomUUID();
        when(administrateurRepository.existsById(id)).thenReturn(true);

        // When
        administrateurService.deleteAdministrateur(id);

        // Then
        verify(administrateurRepository).deleteById(id);
    }

    // Helpers
    private AdministrateurCreateRequest sampleCreateRequest() {
        return AdministrateurCreateRequest.builder()
                .nom("Doe")
                .prenom("John")
                .email("john@example.com")
                .emailPro("john.pro@pharma.com")
                .password("password")
                .role("SUPER_ADMIN")
                .dateEmbauche(new Date())
                .salaire(5000.0)
                .adresse("123 Rue de la Pharmacie")
                .statutContrat(StatutContrat.CDI)
                .telephone("123456789")
                .build();
    }

    private Administrateur sampleAdministrateur() {
        return Administrateur.builder()
                .idPersonne(UUID.randomUUID())
                .matricule("ADMIN-123")
                .nom("Doe")
                .prenom("John")
                .email("john@example.com")
                .poste(PosteEmploye.ADMINISTRATEUR)
                .adresse("123 Rue de la Pharmacie")
                .build();
    }

    private AdministrateurUpdateRequest sampleUpdateRequest() {
        return AdministrateurUpdateRequest.builder()
                .nom("NouveauNom")
                .salaire(6000.0)
                .build();
    }
}