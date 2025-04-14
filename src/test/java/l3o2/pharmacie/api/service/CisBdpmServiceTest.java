package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.entity.medicament.CisBdpm;
import l3o2.pharmacie.api.repository.CisBdpmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CisBdpmServiceTest {

    @Mock
    private CisBdpmRepository cisBdpmRepository;

    @InjectMocks
    private CisBdpmService cisBdpmService;

    private CisBdpm cisBdpm;

    @BeforeEach
    void setUp() {
        // Création d'un objet CisBdpm de test
        cisBdpm = CisBdpm.builder()
                .codeCis("123456789")
                .denomination("Paracétamol")
                .formePharmaceutique("Comprimé")
                .voiesAdministration("Orale")
                .statutAMM("Autorisé")
                .typeProcedureAMM("Nationale")
                .etatCommercialisation("Commercialisé")
                .dateAMM(new Date())
                .statutBdm("Alerte")
                .numeroAutorisationEuropeenne("EU/1/00/001/001")
                .titulaires("Laboratoire A")
                .surveillanceRenforcee("Non")
                .build();
    }

    @Test
    void saveCisBdpm_ShouldSaveCisBdpm() {
        // When
        when(cisBdpmRepository.save(any(CisBdpm.class))).thenReturn(cisBdpm);

        // Exécution
        CisBdpm savedCisBdpm = cisBdpmService.saveCisBdpm(cisBdpm);

        // Vérifications
        ArgumentCaptor<CisBdpm> captor = ArgumentCaptor.forClass(CisBdpm.class);
        verify(cisBdpmRepository).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(cisBdpm);
        assertThat(savedCisBdpm).isEqualTo(cisBdpm);
    }
}
