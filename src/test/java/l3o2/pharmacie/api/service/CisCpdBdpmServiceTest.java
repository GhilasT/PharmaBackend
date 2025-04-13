package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.entity.medicament.CisBdpm;
import l3o2.pharmacie.api.model.entity.medicament.CisCpdBdpm;
import l3o2.pharmacie.api.repository.CisCpdBdpmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
class CisCpdBdpmServiceTest {

    @Mock
    private CisCpdBdpmRepository cisCpdBdpmRepository;

    @InjectMocks
    private CisCpdBdpmService cisCpdBdpmService;

    private CisCpdBdpm cisCpdBdpm;
    private CisBdpm cisBdpm;

    @BeforeEach
    void setUp() {
        // Création d'un objet CisBdpm pour l'association
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

        // Création d'un objet CisCpdBdpm de test
        cisCpdBdpm = new CisCpdBdpm();
        cisCpdBdpm.setCisBdpm(cisBdpm);
        cisCpdBdpm.setConditionPrescription("Condition de prescription libre");
    }

    @Test
    void saveCondition_ShouldSaveCisCpdBdpm() {
        // When
        when(cisCpdBdpmRepository.save(any(CisCpdBdpm.class))).thenReturn(cisCpdBdpm);

        // Exécution
        CisCpdBdpm savedCondition = cisCpdBdpmService.saveCondition(cisCpdBdpm);

        // Vérifications
        ArgumentCaptor<CisCpdBdpm> captor = ArgumentCaptor.forClass(CisCpdBdpm.class);
        verify(cisCpdBdpmRepository).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(cisCpdBdpm);
        assertThat(savedCondition).isEqualTo(cisCpdBdpm);
    }
}
