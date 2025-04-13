package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateEmailProException;
import l3o2.pharmacie.api.exceptions.InvalidDataException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.request.ApprentiCreateRequest;
import l3o2.pharmacie.api.model.dto.request.ApprentiUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.ApprentiResponse;
import l3o2.pharmacie.api.model.entity.Apprenti;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import l3o2.pharmacie.api.repository.ApprentiRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApprentiServiceTest {

    @Mock
    private ApprentiRepository apprentiRepository;

    @Mock
    private EmployeService employeService;

    @InjectMocks
    private ApprentiService apprentiService;

    private ApprentiCreateRequest validRequest;
    private Apprenti sampleApprenti;

    @BeforeEach
    void setUp() {
        // Configuration d'une requête valide
        validRequest = ApprentiCreateRequest.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean@example.com")
                .telephone("0123456789")
                .dateEmbauche(new Date())
                .salaire(1500.0)
                .ecole("École de Pharmacie")
                .emailPro("jean@pharma.fr")
                .password("password123")
                .statutContrat(StatutContrat.CDI)
                .build();

        // Configuration d'un Apprenti mocké
        sampleApprenti = Apprenti.builder()
                .idPersonne(UUID.randomUUID())
                .nom("Dupont")
                .emailPro("jean@pharma.fr")
                .matricule("EMP-APPR-12345")
                .statutContrat(StatutContrat.CDI)
                .ecole("École de Pharmacie")
                .build();
    }

    // TEST CAS VALIDES
    @Test
    void createApprenti_WithValidData_ShouldReturnResponse() {
        // Configuration des mocks
        when(employeService.existsByEmailPro(any())).thenReturn(false);
        when(apprentiRepository.save(any())).thenReturn(sampleApprenti);

        // Exécution
        ApprentiResponse response = apprentiService.createApprenti(validRequest);

        // Vérifications
        assertThat(response).isNotNull();
        assertThat(response.getNom()).isEqualTo("Dupont");
        verify(apprentiRepository).save(any());
    }

    @Test
    void getApprentiById_WhenExists_ShouldReturnResponse() {
        when(apprentiRepository.findById(any())).thenReturn(Optional.of(sampleApprenti));

        ApprentiResponse response = apprentiService.getApprentiById(UUID.randomUUID());

        assertThat(response.getEmailPro()).isEqualTo("jean@pharma.fr");
    }


    @Test
    void updateApprenti_WithInvalidEmailPro_ShouldThrow() {
        ApprentiUpdateRequest updateRequest = ApprentiUpdateRequest.builder()
                .emailPro("exist@pharma.fr")
                .build();

        when(apprentiRepository.findById(any())).thenReturn(Optional.of(sampleApprenti));
        when(employeService.existsByEmailPro("exist@pharma.fr")).thenReturn(true);

        assertThatThrownBy(() -> apprentiService.updateApprenti(UUID.randomUUID(), updateRequest))
                .isInstanceOf(DuplicateEmailProException.class);
    }

    @Test
    void deleteApprenti_WhenNotExists_ShouldThrow() {
        UUID invalidId = UUID.randomUUID();
        when(apprentiRepository.existsById(invalidId)).thenReturn(false);

        assertThatThrownBy(() -> apprentiService.deleteApprenti(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(invalidId.toString());
    }

    // TEST COMPORTEMENT
    @Test
    void searchApprentis_ShouldUseRepositoryCorrectly() {
        String searchTerm = "pharma";
        when(apprentiRepository.searchByTerm("%" + searchTerm + "%"))
                .thenReturn(List.of(sampleApprenti));

        List<ApprentiResponse> results = apprentiService.searchApprentis(searchTerm);

        assertThat(results).hasSize(1);
        verify(apprentiRepository).searchByTerm("%pharma%");
    }
}