package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Medecin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

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
        // Création du premier médecin (medecin1)
        medecin1 = new Medecin();
        medecin1.setCivilite("Dr");
        medecin1.setNomExercice("Dupont");
        medecin1.setPrenomExercice("Jean");
        medecin1.setRppsMedecin("10001234567");
        medecin1.setCategorieProfessionnelle("Civil");
        medecin1.setProfession("Médecin");
        medecin1.setModeExercice("Libéral");
        medecin1.setQualifications("Cardiologie");
        medecin1.setStructureExercice("Hôpital de Paris");
        medecin1.setFonctionActivite("Médecin traitant");
        medecin1.setGenreActivite("Consultation");

        // Création du second médecin (medecin2)
        medecin2 = new Medecin();
        medecin2.setCivilite("Dr");
        medecin2.setNomExercice("Martin");
        medecin2.setPrenomExercice("Sophie");
        medecin2.setRppsMedecin("10009876543");
        medecin2.setCategorieProfessionnelle("Civil");
        medecin2.setProfession("Médecin");
        medecin2.setModeExercice("Libéral");
        medecin2.setQualifications("Dermatologie");
        medecin2.setStructureExercice("Hôpital de Lyon");
        medecin2.setFonctionActivite("Médecin spécialiste");
        medecin2.setGenreActivite("Chirurgie");

        // Persistance des deux médecins
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
        Optional<Medecin> found = medecinRepository.findByRppsMedecin("10001234567");
        assertThat(found).isPresent();
        assertThat(found.get().getNomExercice()).isEqualTo("Dupont");

        Optional<Medecin> notFound = medecinRepository.findByRppsMedecin("99999999999");
        assertThat(notFound).isEmpty();
    }

    @Test
    public void testFindByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase() {
        // Recherche par nom insensible à la casse
        List<Medecin> foundByNom = medecinRepository.findByNomExerciceContainsIgnoreCaseOrPrenomExerciceContainsIgnoreCase("Dupont");
        assertThat(foundByNom).hasSize(1); // Il doit y avoir 1 médecin avec le nom "Dupont"
        assertThat(foundByNom.get(0).getNomExercice()).isEqualTo("Dupont");

        // Recherche par prénom insensible à la casse
        List<Medecin> foundByPrenom = medecinRepository.findByNomExerciceContainsIgnoreCaseOrPrenomExerciceContainsIgnoreCase("Sophie");
        assertThat(foundByPrenom).hasSize(1); // Il doit y avoir 1 médecin avec le prénom "Sophie"
        assertThat(foundByPrenom.get(0).getPrenomExercice()).isEqualTo("Sophie");

        // Recherche avec des valeurs qui n'existent pas dans la base
        List<Medecin> notFound = medecinRepository.findByNomExerciceContainsIgnoreCaseOrPrenomExerciceContainsIgnoreCase("inconnu");
        assertThat(notFound).isEmpty(); // Aucun médecin ne doit être trouvé pour "inconnu"
    }


    @Test
    public void testSearchByNomPrenomCombinaison() {
        List<Medecin> foundByNom = medecinRepository.searchByNomPrenomCombinaison("Dupont");
        assertThat(foundByNom).hasSize(1);
        assertThat(foundByNom.get(0).getNomExercice()).isEqualTo("Dupont");

        List<Medecin> foundByPrenom = medecinRepository.searchByNomPrenomCombinaison("Sophie");
        assertThat(foundByPrenom).hasSize(1);
        assertThat(foundByPrenom.get(0).getPrenomExercice()).isEqualTo("Sophie");

        List<Medecin> foundByFullName = medecinRepository.searchByNomPrenomCombinaison("Jean Dupont");
        assertThat(foundByFullName).hasSize(1);
        assertThat(foundByFullName.get(0).getNomExercice()).isEqualTo("Dupont");

        List<Medecin> foundByReversedName = medecinRepository.searchByNomPrenomCombinaison("Dupont Jean");
        assertThat(foundByReversedName).hasSize(1);
        assertThat(foundByReversedName.get(0).getNomExercice()).isEqualTo("Dupont");

        List<Medecin> notFound = medecinRepository.searchByNomPrenomCombinaison("inconnu");
        assertThat(notFound).isEmpty();
    }

    @Test
    public void testFindAll() {
        List<Medecin> allMedecins = medecinRepository.findAll();
        assertThat(allMedecins).hasSize(2);
    }


}