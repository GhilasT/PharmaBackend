package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Administrateur;
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
@ActiveProfiles("test") // Activation explicite du profil "test"s
class AdministrateurRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private AdministrateurRepository administrateurRepository;

    private Administrateur admin1;
    private Administrateur admin2;

    @BeforeEach
    void setUp() {
        // Given: Création de deux administrateurs de test
        admin1 = Administrateur.builder()
                .nom("Doe")
                .prenom("John")
                .email("john@example.com")
                .emailPro("john.pro@pharma.com")
                .matricule("ADMIN-123")
                .role("SUPER_ADMIN")
                .telephone("123456789")
                .adresse("123 Rue de la Pharmacie")
                .dateEmbauche(java.sql.Date.valueOf("2023-01-01"))
                .salaire(5000.0)
                .password("password")
                .statutContrat(StatutContrat.CDI)
                .poste(PosteEmploye.ADMINISTRATEUR)
                .build();

        admin2 = Administrateur.builder()
                .nom("Smith")
                .prenom("Jane")
                .email("jane@example.com")
                .emailPro("jane.admin@pharma.com")
                .matricule("ADMIN-456")
                .telephone("012345678")
                .adresse("123 Rue de la Pharmacie")
                .dateEmbauche(java.sql.Date.valueOf("2023-01-01"))
                .salaire(5000.0)
                .statutContrat(StatutContrat.CDI)
                .poste(PosteEmploye.ADMINISTRATEUR)
                .role("GESTIONNAIRE")
                .poste(PosteEmploye.ADMINISTRATEUR)
                .password("123456")
                .build();

        entityManager.persist(admin1);
        entityManager.persist(admin2);
        entityManager.flush();
    }

    @Test
    void findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase_ShouldReturnMatchingAdmins() {
        // When: Recherche par nom ou prénom (insensible à la casse)
        List<Administrateur> result1 = administrateurRepository
                .findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase("doe", null);
        
        List<Administrateur> result2 = administrateurRepository
                .findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase(null, "JOH");
        
        List<Administrateur> result3 = administrateurRepository
                .findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase("mit", "xyz");

        // Then
        assertThat(result1).containsExactly(admin1);
        assertThat(result2).containsExactly(admin1);
        assertThat(result3).containsExactly(admin2);
    }

    @Test
    void findByRole_ShouldReturnAdminsWithSpecifiedRole() {
        // When
        List<Administrateur> superAdmins = administrateurRepository.findByRole("SUPER_ADMIN");
        List<Administrateur> gestionnaires = administrateurRepository.findByRole("GESTIONNAIRE");

        // Then
        assertThat(superAdmins).containsExactly(admin1);
        assertThat(gestionnaires).containsExactly(admin2);
    }

    @Test
    void findByMatricule_WhenExists_ShouldReturnAdmin() {
        // When
        Optional<Administrateur> foundAdmin = administrateurRepository.findByMatricule("ADMIN-123");

        // Then
        assertThat(foundAdmin).isPresent();
        assertThat(foundAdmin.get().getEmailPro()).isEqualTo("john.pro@pharma.com");
    }

    @Test
    void findByMatricule_WhenNotExists_ShouldReturnEmpty() {
        // When
        Optional<Administrateur> foundAdmin = administrateurRepository.findByMatricule("INVALID");

        // Then
        assertThat(foundAdmin).isEmpty();
    }

    @Test
    void findByEmailPro_ShouldReturnAdmin() {
        // When
        Optional<Administrateur> foundAdmin = administrateurRepository.findByEmailPro("jane.admin@pharma.com");

        // Then
        assertThat(foundAdmin).isPresent();
        assertThat(foundAdmin.get().getMatricule()).isEqualTo("ADMIN-456");
    }

    @Test
    void existsByEmailPro_ShouldReturnTrueWhenExists() {
        // When & Then
        assertThat(administrateurRepository.existsByEmailPro("john.pro@pharma.com")).isTrue();
        assertThat(administrateurRepository.existsByEmailPro("unknown@test.com")).isFalse();
    }

    @Test
    void existsByEmail_ShouldReturnCorrectStatus() {
        // When & Then
        assertThat(administrateurRepository.existsByEmail("john@example.com")).isTrue();
        assertThat(administrateurRepository.existsByEmail("invalid@test.com")).isFalse();
    }

    @Test
    void existsByMatricule_ShouldReturnCorrectStatus() {
        // When & Then
        assertThat(administrateurRepository.existsByMatricule("ADMIN-123")).isTrue();
        assertThat(administrateurRepository.existsByMatricule("INVALID")).isFalse();
    }

    @Test
    void deleteByMatricule_ShouldRemoveAdmin() {
        // When
        administrateurRepository.deleteByMatricule("ADMIN-123");
        
        // Then
        assertThat(administrateurRepository.existsByMatricule("ADMIN-123")).isFalse();
        assertThat(administrateurRepository.findAll()).hasSize(1);
    }
}