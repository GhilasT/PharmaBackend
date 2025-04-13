package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.entity.medicament.CisBdpm;
import l3o2.pharmacie.api.model.entity.medicament.CisCipBdpm;
import l3o2.pharmacie.api.repository.CisCipBdpmRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CisCipBdpmServiceTest {

    @Mock
    private CisCipBdpmRepository cisCipBdpmRepository;

    @InjectMocks
    private CisCipBdpmService cisCipBdpmService;

    private CisCipBdpm cisCipBdpm;
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

        // Création d'un objet CisCipBdpm de test
        cisCipBdpm = new CisCipBdpm();
        cisCipBdpm.setCodeCip7("1234567");
        cisCipBdpm.setCodeCip13("1234567890123");
        cisCipBdpm.setCisBdpm(cisBdpm);
        cisCipBdpm.setLibellePresentation("Comprimé 500mg");
        cisCipBdpm.setStatutAdministratif("Autorisé");
        cisCipBdpm.setEtatCommercialisation("Commercialisé");
        cisCipBdpm.setDateDeclarationCommercialisation(new Date());
        cisCipBdpm.setAgrementCollectivites("oui");
        cisCipBdpm.setTauxRemboursement("65%");
        cisCipBdpm.setPrixHT(BigDecimal.valueOf(5.00));
        cisCipBdpm.setPrixTTC(BigDecimal.valueOf(5.50));
        cisCipBdpm.setTaxe(BigDecimal.valueOf(0.50));
        cisCipBdpm.setIndicationsRemboursement("Remboursement sous conditions");
    }

    @Test
    void saveCip_ShouldSaveCip() {
        // When
        when(cisCipBdpmRepository.save(any(CisCipBdpm.class))).thenReturn(cisCipBdpm);

        // Exécution
        CisCipBdpm savedCip = cisCipBdpmService.saveCip(cisCipBdpm);

        // Vérifications
        ArgumentCaptor<CisCipBdpm> captor = ArgumentCaptor.forClass(CisCipBdpm.class);
        verify(cisCipBdpmRepository).save(captor.capture());
        assertThat(captor.getValue()).isEqualTo(cisCipBdpm);
        assertThat(savedCip).isEqualTo(cisCipBdpm);
    }

    @Test
    void flush_ShouldFlushRepository() {
        // When
        cisCipBdpmService.flush();

        // Then
        verify(cisCipBdpmRepository).flush();
    }
}
