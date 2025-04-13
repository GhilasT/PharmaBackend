package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateEmailProException;
import l3o2.pharmacie.api.exceptions.InvalidDataException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointCreateRequest;
import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.PharmacienAdjointResponse;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import l3o2.pharmacie.api.repository.PharmacienAdjointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PharmacienAdjointServiceTest {

    @Mock
    private PharmacienAdjointRepository pharmacienAdjointRepository;

    @Mock
    private EmployeService employeService;

    @InjectMocks
    private PharmacienAdjointService pharmacienAdjointService;

    private PharmacienAdjointCreateRequest validRequest;
    private PharmacienAdjoint samplePharmacien;

    @BeforeEach
    void setUp() {
        // Configuration d'une requête valide
        validRequest = PharmacienAdjointCreateRequest.builder()
                .nom("Doe")
                .prenom("John")
                .email("john@example.com")
                .telephone("123456789")
                .adresse("123 Rue de la Pharmacie")
                .dateEmbauche(new Date())
                .salaire(4000.0)
                .password("password")
                .statutContrat(StatutContrat.CDI)
                .poste(PosteEmploye.PHARMACIEN_ADJOINT)
                .diplome("Diplôme de Pharmacie")
                .emailPro("john.pro@pharma.com")
                .build();

        // Configuration d'un PharmacienAdjoint mocké avec tous les champs
        samplePharmacien = PharmacienAdjoint.builder()
                .idPersonne(UUID.randomUUID())
                .nom("Doe")
                .prenom("John")
                .email("john@example.com")
                .telephone("123456789")
                .adresse("123 Rue de la Pharmacie")
                .dateEmbauche(new Date())
                .salaire(4000.0)
                .password("password")
                .statutContrat(StatutContrat.CDI)
                .poste(PosteEmploye.PHARMACIEN_ADJOINT)
                .diplome("Diplôme de Pharmacie")
                .emailPro("john.pro@pharma.com")
                .matricule("EMP-PHARM-12345")
                .build();
    }

    // TEST CAS VALIDES
    @Test
    void createPharmacienAdjoint_WithValidData_ShouldReturnResponse() {
        // Configuration des mocks
        when(employeService.existsByEmailPro(any())).thenReturn(false);
        when(pharmacienAdjointRepository.save(any())).thenReturn(samplePharmacien);

        // Exécution
        PharmacienAdjointResponse response = pharmacienAdjointService.createPharmacienAdjoint(validRequest);

        // Vérifications
        assertThat(response).isNotNull();
        assertThat(response.getNom()).isEqualTo("Doe");
        verify(pharmacienAdjointRepository).save(any());
    }

    @Test
    void getPharmacienAdjointByEmailPro_WhenExists_ShouldReturnResponse() {
        // Configuration du mock pour retourner le PharmacienAdjoint par emailPro
        when(pharmacienAdjointRepository.findByEmailPro(any())).thenReturn(Optional.of(samplePharmacien));

        // Exécution
        PharmacienAdjointResponse response = pharmacienAdjointService.findByEmailPro(samplePharmacien.getEmailPro());

        // Vérifications
        assertThat(response.getEmailPro()).isEqualTo("john.pro@pharma.com");
    }

    @Test
    void updatePharmacienAdjoint_WithInvalidEmailPro_ShouldThrow() {
        PharmacienAdjointUpdateRequest updateRequest = PharmacienAdjointUpdateRequest.builder()
                .emailPro("exist@pharma.com")
                .build();

        when(pharmacienAdjointRepository.findById(any())).thenReturn(Optional.of(samplePharmacien));
        when(employeService.existsByEmailPro("exist@pharma.com")).thenReturn(true);

        assertThatThrownBy(() -> pharmacienAdjointService.updatePharmacienAdjoint(UUID.randomUUID(), updateRequest))
                .isInstanceOf(DuplicateEmailProException.class);
    }

    @Test
    void deletePharmacienAdjoint_WhenNotExists_ShouldThrow() {
        UUID invalidId = UUID.randomUUID();
        when(pharmacienAdjointRepository.existsById(invalidId)).thenReturn(false);

        assertThatThrownBy(() -> pharmacienAdjointService.deletePharmacienAdjoint(invalidId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(invalidId.toString());
    }

    // TEST COMPORTEMENT
    @Test
    void searchPharmaciensAdjoints_ShouldUseRepositoryCorrectly() {
        String searchTerm = "pharmacie";
        when(pharmacienAdjointRepository.searchByNomPrenom(searchTerm))
                .thenReturn(List.of(samplePharmacien));

        List<PharmacienAdjointResponse> results = pharmacienAdjointService.searchPharmaciensAdjoints(searchTerm);

        assertThat(results).hasSize(1);
        verify(pharmacienAdjointRepository).searchByNomPrenom(searchTerm);
    }
}
