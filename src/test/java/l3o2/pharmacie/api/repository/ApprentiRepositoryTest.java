package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Apprenti;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class ApprentiRepositoryTest {

    @Autowired
    private ApprentiRepository apprentiRepository;

    private Apprenti createSampleApprenti() {
        return Apprenti.builder()
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@example.com")
                .matricule("EMP-APPR-12345")
                .telephone("0123456789")
                .adresse("123 Rue de la Pharmacie")
                .dateEmbauche(new Date())
                .salaire(1500.0)
                .poste(PosteEmploye.APPRENTI)
                .statutContrat(StatutContrat.CDI)
                .ecole("École de Pharmacie Paris")
                .emailPro("jean.dupont@pharmacie.fr")
                .password("password123")
                .build();
    }

    @Test
    void findByMatricule_ShouldReturnApprenti() {
        Apprenti saved = apprentiRepository.save(createSampleApprenti());
        
        Optional<Apprenti> found = apprentiRepository.findByMatricule(saved.getMatricule());
        
        assertThat(found).isPresent();
        assertThat(found.get().getMatricule()).isEqualTo(saved.getMatricule());
    }

    @Test
    void existsByMatricule_ShouldReturnTrue() {
        Apprenti saved = apprentiRepository.save(createSampleApprenti());
        
        boolean exists = apprentiRepository.existsByMatricule(saved.getMatricule());
        
        assertThat(exists).isTrue();
    }

    @Test
    void searchByTerm_ShouldReturnMatchingApprentis() {
        Apprenti apprenti = createSampleApprenti();
        apprentiRepository.save(apprenti);
        
        List<Apprenti> results = apprentiRepository.searchByTerm("dup");
        
        assertThat(results)
            .hasSize(1)
            .allMatch(a -> a.getNom().contains("Dupont") || a.getPrenom().contains("Jean"));
    }
}