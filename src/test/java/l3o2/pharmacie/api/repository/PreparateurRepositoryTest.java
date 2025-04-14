package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Preparateur;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class PreparateurRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PreparateurRepository preparateurRepository;

    private Preparateur preparateur1;
    private Preparateur preparateur2;

    @BeforeEach
    void setUp() {
        // Given: Création de deux préparateurs de test
        preparateur1 = Preparateur.builder()
                .nom("Doe")
                .prenom("John")
                .email("john@example.com")
                .emailPro("john.pro@pharma.com")
                .telephone("123456789")
                .adresse("123 Rue de la Pharmacie")
                .dateEmbauche(java.sql.Date.valueOf("2023-01-01"))
                .salaire(3000.0)
                .password("password")
                .statutContrat(StatutContrat.CDI)
                .poste(PosteEmploye.PREPARATEUR)
                .diplome("Diplôme de Préparateur")
                .build();

        preparateur2 = Preparateur.builder()
                .nom("Smith")
                .prenom("Jane")
                .email("jane@example.com")
                .emailPro("jane.pro@pharma.com")
                .telephone("987654321")
                .adresse("456 Rue de la Pharmacie")
                .dateEmbauche(java.sql.Date.valueOf("2023-02-01"))
                .salaire(3200.0)
                .password("password")
                .statutContrat(StatutContrat.CDD)
                .poste(PosteEmploye.PREPARATEUR)
                .diplome("Diplôme de Préparateur")
                .build();

        // Générer les matricules automatiquement
        preparateur1.generateMatricule(preparateur1.getPoste().toString());
        preparateur2.generateMatricule(preparateur2.getPoste().toString());

        // Persist the entities to generate the matricule automatically
        entityManager.persist(preparateur1);
        entityManager.persist(preparateur2);
        entityManager.flush();
    }

    @Test
    void findByMatricule_WhenExists_ShouldReturnPreparateur() {
        // When
        Optional<Preparateur> foundPreparateur = preparateurRepository.findByMatricule(preparateur1.getMatricule());

        // Then
        assertThat(foundPreparateur).isPresent();
        assertThat(foundPreparateur.get().getEmailPro()).isEqualTo("john.pro@pharma.com");
    }

    @Test
    void findByMatricule_WhenNotExists_ShouldReturnEmpty() {
        // When
        Optional<Preparateur> foundPreparateur = preparateurRepository.findByMatricule("INVALID");

        // Then
        assertThat(foundPreparateur).isEmpty();
    }

    @Test
    void existsByMatricule_ShouldReturnCorrectStatus() {
        // When & Then
        assertThat(preparateurRepository.existsByMatricule(preparateur1.getMatricule())).isTrue();
        assertThat(preparateurRepository.existsByMatricule("INVALID")).isFalse();
    }

    @Test
    void findByEmailPro_ShouldReturnPreparateur() {
        // When
        Optional<Preparateur> foundPreparateur = preparateurRepository.findByEmailPro("jane.pro@pharma.com");

        // Then
        assertThat(foundPreparateur).isPresent();
        assertThat(foundPreparateur.get().getMatricule()).isEqualTo(preparateur2.getMatricule());
    }

    @Test
    void findAll_ShouldReturnAllPreparateurs() {
        // When
        List<Preparateur> preparateurs = preparateurRepository.findAll();

        // Then
        assertThat(preparateurs).hasSize(2);
    }

    @Test
    void searchByNomPrenom_ShouldReturnMatchingPreparateurs() {
        // When
        List<Preparateur> results = preparateurRepository.searchByNomPrenom("john");

        // Then
        assertThat(results).hasSize(1);
        assertThat(results.get(0).getNom()).isEqualTo("Doe");
    }
}
