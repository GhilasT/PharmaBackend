package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.medicament.CisBdpm;
import l3o2.pharmacie.api.model.entity.medicament.CisCipBdpm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CisCipBdpmRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CisCipBdpmRepository cisCipBdpmRepository;

    private CisCipBdpm cisCipBdpm1;
    private CisCipBdpm cisCipBdpm2;
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

        entityManager.persist(cisBdpm);

        // Création des objets CisCipBdpm de test
        cisCipBdpm1 = new CisCipBdpm();
        cisCipBdpm1.setCodeCip7("1234567");
        cisCipBdpm1.setCodeCip13("1234567890123");
        cisCipBdpm1.setCisBdpm(cisBdpm);
        cisCipBdpm1.setLibellePresentation("Comprimé 500mg");
        cisCipBdpm1.setStatutAdministratif("Autorisé");
        cisCipBdpm1.setEtatCommercialisation("Commercialisé");
        cisCipBdpm1.setDateDeclarationCommercialisation(new Date());
        cisCipBdpm1.setAgrementCollectivites("oui");
        cisCipBdpm1.setTauxRemboursement("65%");
        cisCipBdpm1.setPrixHT(BigDecimal.valueOf(5.00));
        cisCipBdpm1.setPrixTTC(BigDecimal.valueOf(5.50));
        cisCipBdpm1.setTaxe(BigDecimal.valueOf(0.50));
        cisCipBdpm1.setIndicationsRemboursement("Remboursement sous conditions");

        cisCipBdpm2 = new CisCipBdpm();
        cisCipBdpm2.setCodeCip7("7654321");
        cisCipBdpm2.setCodeCip13("7654321098765");
        cisCipBdpm2.setCisBdpm(cisBdpm);
        cisCipBdpm2.setLibellePresentation("Gélule 200mg");
        cisCipBdpm2.setStatutAdministratif("Autorisé");
        cisCipBdpm2.setEtatCommercialisation("Commercialisé");
        cisCipBdpm2.setDateDeclarationCommercialisation(new Date());
        cisCipBdpm2.setAgrementCollectivites("non");
        cisCipBdpm2.setTauxRemboursement("30%");
        cisCipBdpm2.setPrixHT(BigDecimal.valueOf(3.00));
        cisCipBdpm2.setPrixTTC(BigDecimal.valueOf(3.30));
        cisCipBdpm2.setTaxe(BigDecimal.valueOf(0.30));
        cisCipBdpm2.setIndicationsRemboursement("Remboursement sous conditions");

        entityManager.persist(cisCipBdpm1);
        entityManager.persist(cisCipBdpm2);
        entityManager.flush();
    }

    @Test
    void findByCodeCip13_WhenExists_ShouldReturnCisCipBdpm() {
        // When
        Optional<CisCipBdpm> foundCisCipBdpm = cisCipBdpmRepository.findByCodeCip13(cisCipBdpm1.getCodeCip13());

        // Then
        assertThat(foundCisCipBdpm).isPresent();
        assertThat(foundCisCipBdpm.get().getLibellePresentation()).isEqualTo("Comprimé 500mg");
    }

    @Test
    void findByCodeCip13_WhenNotExists_ShouldReturnEmpty() {
        // When
        Optional<CisCipBdpm> foundCisCipBdpm = cisCipBdpmRepository.findByCodeCip13("INVALID");

        // Then
        assertThat(foundCisCipBdpm).isEmpty();
    }

    @Test
    void findById_WhenExists_ShouldReturnCisCipBdpm() {
        // When
        Optional<CisCipBdpm> foundCisCipBdpm = cisCipBdpmRepository.findById(cisCipBdpm1.getId());

        // Then
        assertThat(foundCisCipBdpm).isPresent();
        assertThat(foundCisCipBdpm.get().getLibellePresentation()).isEqualTo("Comprimé 500mg");
    }

    @Test
    void findById_WhenNotExists_ShouldReturnEmpty() {
        // When
        Optional<CisCipBdpm> foundCisCipBdpm = cisCipBdpmRepository.findById(999L);

        // Then
        assertThat(foundCisCipBdpm).isEmpty();
    }

    @Test
    void findFirstByCisBdpm_CodeCis_WhenExists_ShouldReturnCisCipBdpm() {
        // When
        Optional<CisCipBdpm> foundCisCipBdpm = cisCipBdpmRepository.findFirstByCisBdpm_CodeCis(cisBdpm.getCodeCis());

        // Then
        assertThat(foundCisCipBdpm).isPresent();
        assertThat(foundCisCipBdpm.get().getCisBdpm().getCodeCis()).isEqualTo("123456789");
    }

    @Test
    void findFirstByCisBdpm_CodeCis_WhenNotExists_ShouldReturnEmpty() {
        // When
        Optional<CisCipBdpm> foundCisCipBdpm = cisCipBdpmRepository.findFirstByCisBdpm_CodeCis("INVALID");

        // Then
        assertThat(foundCisCipBdpm).isEmpty();
    }
}
