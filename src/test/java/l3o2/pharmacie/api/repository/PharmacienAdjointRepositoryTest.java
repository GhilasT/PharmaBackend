package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
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
class PharmacienAdjointRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PharmacienAdjointRepository pharmacienAdjointRepository;

    private PharmacienAdjoint pharmacien1;
    private PharmacienAdjoint pharmacien2;

    @BeforeEach
    void setUp() {
        // Given: Création de deux pharmaciens adjoints de test
        pharmacien1 = PharmacienAdjoint.builder()
                .nom("Doe")
                .prenom("John")
                .email("john@example.com")
                .emailPro("john.pro@pharma.com")
                .telephone("123456789")
                .adresse("123 Rue de la Pharmacie")
                .dateEmbauche(new Date())
                .salaire(4000.0)
                .password("password")
                .statutContrat(StatutContrat.CDI)
                .poste(PosteEmploye.PHARMACIEN_ADJOINT)
                .diplome("Diplôme de Pharmacie")
                .build();

        pharmacien2 = PharmacienAdjoint.builder()
                .nom("Smith")
                .prenom("Jane")
                .email("jane@example.com")
                .emailPro("jane.pro@pharma.com")
                .telephone("987654321")
                .adresse("456 Rue de la Pharmacie")
                .dateEmbauche(new Date())
                .salaire(4200.0)
                .password("password")
                .statutContrat(StatutContrat.CDD)
                .poste(PosteEmploye.PHARMACIEN_ADJOINT)
                .diplome("Diplôme de Pharmacie")
                .build();

        // Générer les matricules automatiquement
        pharmacien1.generateMatricule(pharmacien1.getPoste().toString());
        pharmacien2.generateMatricule(pharmacien2.getPoste().toString());

        // Persist the entities to generate the matricule automatically
        entityManager.persist(pharmacien1);
        entityManager.persist(pharmacien2);
        entityManager.flush();
    }

    @Test
    void findByMatricule_WhenExists_ShouldReturnPharmacienAdjoint() {
        // When
        Optional<PharmacienAdjoint> foundPharmacien = pharmacienAdjointRepository.findByMatricule(pharmacien1.getMatricule());

        // Then
        assertThat(foundPharmacien).isPresent();
        assertThat(foundPharmacien.get().getEmailPro()).isEqualTo("john.pro@pharma.com");
    }

    @Test
    void findByMatricule_WhenNotExists_ShouldReturnEmpty() {
        // When
        Optional<PharmacienAdjoint> foundPharmacien = pharmacienAdjointRepository.findByMatricule("INVALID");

        // Then
        assertThat(foundPharmacien).isEmpty();
    }

    @Test
    void existsByMatricule_ShouldReturnCorrectStatus() {
        // When & Then
        assertThat(pharmacienAdjointRepository.existsByMatricule(pharmacien1.getMatricule())).isTrue();
        assertThat(pharmacienAdjointRepository.existsByMatricule("INVALID")).isFalse();
    }

    @Test
    void findByEmailPro_ShouldReturnPharmacienAdjoint() {
        // When
        Optional<PharmacienAdjoint> foundPharmacien = pharmacienAdjointRepository.findByEmailPro("jane.pro@pharma.com");

        // Then
        assertThat(foundPharmacien).isPresent();
        assertThat(foundPharmacien.get().getMatricule()).isEqualTo(pharmacien2.getMatricule());
    }

}
