package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Fournisseur;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class FournisseurRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FournisseurRepository repository;
    
    private Fournisseur fournisseur;
    
    @BeforeEach
    public void setup() {
        fournisseur = Fournisseur.builder()
                .nomSociete("Pharma Plus")
                .email("contact@pharmaplus.com")
                .telephone("0123456789")
                .sujetFonction("Médicaments génériques")
                .adresse("123 Rue de la Pharmacie")
                .commandes(new ArrayList<>())
                .build();
        
        entityManager.persist(fournisseur);
        entityManager.flush();
    }

    @Test
    public void testFindByNomSociete() {
        Optional<Fournisseur> f = repository.findByNomSociete("jane.pro@pharma.com");
        assertThat(repository.findByNomSociete("Pharma Plus")).isPresent();
        assertThat(repository.findByNomSociete("Inconnu")).isEmpty();
        
    }

    @Test
    public void testFindByEmail() {
        assertThat(repository.findByEmail("contact@pharmaplus.com")).isPresent();
        assertThat(repository.findByEmail("inconnu@exemple.com")).isEmpty();
    }

    @Test
    public void testFindByTelephone() {
        assertThat(repository.findByTelephone("0123456789")).isPresent();
        assertThat(repository.findByTelephone("9999999999")).isEmpty();
    }

    @Test
    public void testExistsByEmail() {
        assertThat(repository.existsByEmail("contact@pharmaplus.com")).isTrue();
        assertThat(repository.existsByEmail("inconnu@exemple.com")).isFalse();
    }

    @Test
    public void testExistsByTelephone() {
        assertThat(repository.existsByTelephone("0123456789")).isTrue();
        assertThat(repository.existsByTelephone("9999999999")).isFalse();
    }

    @Test
    public void testSearchFournisseurs() {
        Fournisseur fournisseur2 = Fournisseur.builder()
                .nomSociete("MediStock")
                .email("contact@medistock.com")
                .telephone("9876543210")
                .sujetFonction("Équipements médicaux")
                .adresse("456 Avenue de la Santé")
                .commandes(new ArrayList<>())
                .build();
                
        entityManager.persist(fournisseur2);
        entityManager.flush();
        
        List<Fournisseur> resultsByName = repository.searchFournisseurs("pharma");
        assertThat(resultsByName).hasSize(1);
        assertThat(resultsByName.get(0).getNomSociete()).isEqualTo("Pharma Plus");
        
        List<Fournisseur> resultsByFunction = repository.searchFournisseurs("équipements");
        assertThat(resultsByFunction).hasSize(1);
        assertThat(resultsByFunction.get(0).getNomSociete()).isEqualTo("MediStock");
        
        List<Fournisseur> noResults = repository.searchFournisseurs("introuvable");
        assertThat(noResults).isEmpty();
    }
}
