package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.medicament.CisBdpm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class CisBdpmRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CisBdpmRepository cisBdpmRepository;

    private CisBdpm cisBdpm1;
    private CisBdpm cisBdpm2;

    @BeforeEach
    void setUp() {
        // Given: Création de deux médicaments de test
        cisBdpm1 = CisBdpm.builder()
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

        cisBdpm2 = CisBdpm.builder()
                .codeCis("987654321")
                .denomination("Ibuprofène")
                .formePharmaceutique("Gélule")
                .voiesAdministration("Orale")
                .statutAMM("Autorisé")
                .typeProcedureAMM("Européenne")
                .etatCommercialisation("Commercialisé")
                .dateAMM(new Date())
                .statutBdm("Warning disponibilité")
                .numeroAutorisationEuropeenne("EU/1/00/002/002")
                .titulaires("Laboratoire B")
                .surveillanceRenforcee("Oui")
                .build();

        entityManager.persist(cisBdpm1);
        entityManager.persist(cisBdpm2);
        entityManager.flush();
    }

    @Test
    void findByCodeCis_WhenExists_ShouldReturnCisBdpm() {
        // When
        Optional<CisBdpm> foundCisBdpm = cisBdpmRepository.findByCodeCis(cisBdpm1.getCodeCis());

        // Then
        assertThat(foundCisBdpm).isPresent();
        assertThat(foundCisBdpm.get().getDenomination()).isEqualTo("Paracétamol");
    }

    @Test
    void findByCodeCis_WhenNotExists_ShouldReturnEmpty() {
        // When
        Optional<CisBdpm> foundCisBdpm = cisBdpmRepository.findByCodeCis("INVALID");

        // Then
        assertThat(foundCisBdpm).isEmpty();
    }

    @Test
    void findByDenominationContainingIgnoreCase_ShouldReturnMatchingCisBdpm() {
        // When
        List<CisBdpm> results = cisBdpmRepository.findByDenominationContainingIgnoreCase("paracétamol");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getDenomination()).isEqualTo("Paracétamol");
    }

    @Test
    void findByFormePharmaceutiqueContainingIgnoreCase_ShouldReturnMatchingCisBdpm() {
        // When
        List<CisBdpm> results = cisBdpmRepository.findByFormePharmaceutiqueContainingIgnoreCase("comprimé");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getFormePharmaceutique()).isEqualTo("Comprimé");
    }

    @Test
    void findByTitulairesContainingIgnoreCase_ShouldReturnMatchingCisBdpm() {
        // When
        List<CisBdpm> results = cisBdpmRepository.findByTitulairesContainingIgnoreCase("laboratoire a");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getTitulaires()).isEqualTo("Laboratoire A");
    }

    @Test
    void findByCodeCisContainingOrDenominationContainingIgnoreCase_ShouldReturnMatchingCisBdpm() {
        // When
        List<CisBdpm> results = cisBdpmRepository.findByCodeCisContainingOrDenominationContainingIgnoreCase("123456789", "ibuprofène");

        // Then
        assertThat(results).hasSize(2);
    }
}
