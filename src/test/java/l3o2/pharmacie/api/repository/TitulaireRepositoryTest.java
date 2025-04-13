package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Titulaire;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TitulaireRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TitulaireRepository titulaireRepository;

    private Titulaire titulaire1;
    private Titulaire titulaire2;

    @BeforeEach
    void setUp() {
        // Given: Création de deux titulaires de test
        titulaire1 = Titulaire.builder()
                .nom("Doe")
                .prenom("John")
                .email("john@example.com")
                .emailPro("john.pro@pharma.com")
                .telephone("123456789")
                .adresse("123 Rue de la Pharmacie")
                .dateEmbauche(java.sql.Date.valueOf("2023-01-01"))
                .salaire(5000.0)
                .password("password")
                .statutContrat(StatutContrat.CDI)
                .poste(PosteEmploye.TITULAIRE)
                .diplome("Diplôme de Pharmacie")
                .role("ADMIN")
                .numeroRPPS("12345678901")
                .build();

        titulaire2 = Titulaire.builder()
                .nom("Smith")
                .prenom("Jane")
                .email("jane@example.com")
                .emailPro("jane.pro@pharma.com")
                .telephone("987654321")
                .adresse("456 Rue de la Pharmacie")
                .dateEmbauche(java.sql.Date.valueOf("2023-02-01"))
                .salaire(5200.0)
                .password("password")
                .statutContrat(StatutContrat.CDD)
                .poste(PosteEmploye.TITULAIRE)
                .diplome("Diplôme de Pharmacie")
                .role("ADMIN")
                .numeroRPPS("12345678902")
                .build();

        // Générer les matricules automatiquement
        titulaire1.generateMatricule(titulaire1.getPoste().toString());
        titulaire2.generateMatricule(titulaire2.getPoste().toString());

        // Persist the entities to generate the matricule automatically
        entityManager.persist(titulaire1);
        entityManager.persist(titulaire2);
        entityManager.flush();
    }

    @Test
    void findByNumeroRPPS_WhenExists_ShouldReturnTitulaire() {
        // When
        Optional<Titulaire> foundTitulaire = titulaireRepository.findByNumeroRPPS(titulaire1.getNumeroRPPS());

        // Then
        assertThat(foundTitulaire).isPresent();
        assertThat(foundTitulaire.get().getEmailPro()).isEqualTo("john.pro@pharma.com");
    }

    @Test
    void findByNumeroRPPS_WhenNotExists_ShouldReturnEmpty() {
        // When
        Optional<Titulaire> foundTitulaire = titulaireRepository.findByNumeroRPPS("INVALID");

        // Then
        assertThat(foundTitulaire).isEmpty();
    }

    @Test
    void existsByNumeroRPPS_ShouldReturnCorrectStatus() {
        // When & Then
        assertThat(titulaireRepository.existsByNumeroRPPS(titulaire1.getNumeroRPPS())).isTrue();
        assertThat(titulaireRepository.existsByNumeroRPPS("INVALID")).isFalse();
    }

    @Test
    void findByEmail_ShouldReturnTitulaire() {
        // When
        Optional<Titulaire> foundTitulaire = titulaireRepository.findByEmail("jane@example.com");

        // Then
        assertThat(foundTitulaire).isPresent();
        assertThat(foundTitulaire.get().getMatricule()).isEqualTo(titulaire2.getMatricule());
    }

    @Test
    void findByEmailPro_ShouldReturnTitulaire() {
        // When
        Optional<Titulaire> foundTitulaire = titulaireRepository.findByEmailPro("jane.pro@pharma.com");

        // Then
        assertThat(foundTitulaire).isPresent();
        assertThat(foundTitulaire.get().getMatricule()).isEqualTo(titulaire2.getMatricule());
    }

    @Test
    void existsByEmail_ShouldReturnCorrectStatus() {
        // When & Then
        assertThat(titulaireRepository.existsByEmail("jane@example.com")).isTrue();
        assertThat(titulaireRepository.existsByEmail("invalid@example.com")).isFalse();
    }

    @Test
    void existsByEmailPro_ShouldReturnCorrectStatus() {
        // When & Then
        assertThat(titulaireRepository.existsByEmailPro("jane.pro@pharma.com")).isTrue();
        assertThat(titulaireRepository.existsByEmailPro("invalid.pro@pharma.com")).isFalse();
    }

    @Test
    void existsByMatricule_ShouldReturnCorrectStatus() {
        // When & Then
        assertThat(titulaireRepository.existsByMatricule(titulaire1.getMatricule())).isTrue();
        assertThat(titulaireRepository.existsByMatricule("INVALID")).isFalse();
    }

    @Test
    void findByMatricule_WhenExists_ShouldReturnTitulaire() {
        // When
        Optional<Titulaire> foundTitulaire = titulaireRepository.findByMatricule(titulaire1.getMatricule());

        // Then
        assertThat(foundTitulaire).isPresent();
        assertThat(foundTitulaire.get().getEmailPro()).isEqualTo("john.pro@pharma.com");
    }

    @Test
    void findByMatricule_WhenNotExists_ShouldReturnEmpty() {
        // When
        Optional<Titulaire> foundTitulaire = titulaireRepository.findByMatricule("INVALID");

        // Then
        assertThat(foundTitulaire).isEmpty();
    }
}
