package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Medecin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class MedecinRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MedecinRepository medecinRepository;

    private Medecin medecin1;
    private Medecin medecin2;

    @BeforeEach
    public void setup() {
        medecin1 = new Medecin();
        medecin1.setNom("Dupont");
        medecin1.setPrenom("Jean");
        medecin1.setEmail("jean.dupont@medecin.fr");
        medecin1.setTelephone("0123456789");
        medecin1.setAdresse("15 rue de la Santé");
        medecin1.setRpps("10001234567");
        medecin1.setAdeli("123456789");
        medecin1.setCivilite("Dr");
        medecin1.setProfession("Médecin");
        medecin1.setSpecialitePrincipale("Cardiologie");
        medecin1.setModeExercice("Libéral");
        medecin1.setCodePostal("75013");
        medecin1.setVille("Paris");
        medecin1.setSecteur("Secteur 1");
        medecin1.setConventionnement("Conventionné");
        medecin1.setHonoraires("Tarif conventionné");
        medecin1.setSiret("12345678901234");
        medecin1.setDateMiseAJour(LocalDate.now());

        medecin2 = new Medecin();
        medecin2.setNom("Martin");
        medecin2.setPrenom("Sophie");
        medecin2.setEmail("sophie.martin@medecin.fr");
        medecin2.setTelephone("0987654321");
        medecin2.setAdresse("8 avenue des Soins");
        medecin2.setRpps("10009876543");
        medecin2.setAdeli("987654321");
        medecin2.setCivilite("Dr");
        medecin2.setProfession("Médecin");
        medecin2.setSpecialitePrincipale("Dermatologie");
        medecin2.setModeExercice("Libéral");
        medecin2.setCodePostal("69002");
        medecin2.setVille("Lyon");
        medecin2.setSecteur("Secteur 2");
        medecin2.setConventionnement("Conventionné avec dépassements");
        medecin2.setHonoraires("Dépassements d'honoraires");
        medecin2.setSiret("98765432109876");
        medecin2.setDateMiseAJour(LocalDate.now());

        entityManager.persist(medecin1);
        entityManager.persist(medecin2);
        entityManager.flush();
    }

    @Test
    public void testRepositoryIsNotNull() {
        assertThat(medecinRepository).isNotNull();
    }

    @Test
    public void testFindByRpps() {
        Optional<Medecin> found = medecinRepository.findByRpps("10001234567");
        assertThat(found).isPresent();
        assertThat(found.get().getNom()).isEqualTo("Dupont");
        
        Optional<Medecin> notFound = medecinRepository.findByRpps("99999999999");
        assertThat(notFound).isEmpty();
    }

    @Test
    public void testFindBySiret() {
        Optional<Medecin> found = medecinRepository.findBySiret("12345678901234");
        assertThat(found).isPresent();
        assertThat(found.get().getNom()).isEqualTo("Dupont");
        
        Optional<Medecin> notFound = medecinRepository.findBySiret("99999999999999");
        assertThat(notFound).isEmpty();
    }

    @Test
    public void testFindByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase() {
        List<Medecin> foundByNom = medecinRepository.findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase("Dupont", null);
        assertThat(foundByNom).hasSize(1);
        assertThat(foundByNom.get(0).getNom()).isEqualTo("Dupont");
        
        List<Medecin> foundByPrenom = medecinRepository.findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase(null, "Sophie");
        assertThat(foundByPrenom).hasSize(1);
        assertThat(foundByPrenom.get(0).getPrenom()).isEqualTo("Sophie");
        
        List<Medecin> notFound = medecinRepository.findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase("inconnu", "inconnu");
        assertThat(notFound).isEmpty();
    }
    
    @Test
    public void testFindByEmail() {
        Optional<Medecin> found = medecinRepository.findByEmail("jean.dupont@medecin.fr");
        assertThat(found).isPresent();
        assertThat(found.get().getNom()).isEqualTo("Dupont");
        
        Optional<Medecin> notFound = medecinRepository.findByEmail("inconnu@medecin.fr");
        assertThat(notFound).isEmpty();
    }

    @Test
    public void testFindByVilleIgnoreCase() {
        List<Medecin> foundParis = medecinRepository.findByVilleIgnoreCase("paris");
        assertThat(foundParis).hasSize(1);
        assertThat(foundParis.get(0).getNom()).isEqualTo("Dupont");
        
        List<Medecin> foundLyon = medecinRepository.findByVilleIgnoreCase("LYON");
        assertThat(foundLyon).hasSize(1);
        assertThat(foundLyon.get(0).getNom()).isEqualTo("Martin");
        
        List<Medecin> notFound = medecinRepository.findByVilleIgnoreCase("Marseille");
        assertThat(notFound).isEmpty();
    }

    @Test
    public void testFindBySpecialitePrincipaleIgnoreCase() {
        List<Medecin> foundCardio = medecinRepository.findBySpecialitePrincipaleIgnoreCase("cardiologie");
        assertThat(foundCardio).hasSize(1);
        assertThat(foundCardio.get(0).getNom()).isEqualTo("Dupont");
        
        List<Medecin> foundDermo = medecinRepository.findBySpecialitePrincipaleIgnoreCase("DERMATOLOGIE");
        assertThat(foundDermo).hasSize(1);
        assertThat(foundDermo.get(0).getNom()).isEqualTo("Martin");
        
        List<Medecin> notFound = medecinRepository.findBySpecialitePrincipaleIgnoreCase("Neurologie");
        assertThat(notFound).isEmpty();
    }

    @Test
    public void testExistsByRpps() {
        assertThat(medecinRepository.existsByRpps("10001234567")).isTrue();
        
        assertThat(medecinRepository.existsByRpps("99999999999")).isFalse();
    }

    @Test
    public void testExistsByEmail() {
        assertThat(medecinRepository.existsByEmail("jean.dupont@medecin.fr")).isTrue();
        
        assertThat(medecinRepository.existsByEmail("inconnu@medecin.fr")).isFalse();
    }

    @Test
    public void testSearchByNomPrenomCombinaison() {
        List<Medecin> foundByNom = medecinRepository.searchByNomPrenomCombinaison("Dupont");
        assertThat(foundByNom).hasSize(1);
        assertThat(foundByNom.get(0).getNom()).isEqualTo("Dupont");
        
        List<Medecin> foundByPrenom = medecinRepository.searchByNomPrenomCombinaison("Sophie");
        assertThat(foundByPrenom).hasSize(1);
        assertThat(foundByPrenom.get(0).getPrenom()).isEqualTo("Sophie");
        
        List<Medecin> foundByFullName = medecinRepository.searchByNomPrenomCombinaison("Jean Dupont");
        assertThat(foundByFullName).hasSize(1);
        assertThat(foundByFullName.get(0).getNom()).isEqualTo("Dupont");
        
        List<Medecin> foundByReversedName = medecinRepository.searchByNomPrenomCombinaison("Dupont Jean");
        assertThat(foundByReversedName).hasSize(1);
        assertThat(foundByReversedName.get(0).getNom()).isEqualTo("Dupont");
        
        List<Medecin> notFound = medecinRepository.searchByNomPrenomCombinaison("inconnu");
        assertThat(notFound).isEmpty();
    }

    @Test
    public void testFindAll() {
        List<Medecin> allMedecins = medecinRepository.findAll();
        assertThat(allMedecins).hasSize(2);
    }
}
