package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Vente;
import l3o2.pharmacie.api.model.entity.Client;
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
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class VenteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VenteRepository venteRepository;

    private Vente vente1;
    private Vente vente2;
    private PharmacienAdjoint pharmacienAdjoint;
    private Client client;

    @BeforeEach
    void setUp() {
        // Given: Création des entités nécessaires
        pharmacienAdjoint = PharmacienAdjoint.builder()
                .nom("Doe")
                .prenom("John")
                .email("john@example.com")
                .emailPro("john.pro@pharma.com")
                .telephone("123456789")
                .adresse("123 Rue de la Pharmacie")
                .dateEmbauche(java.sql.Date.valueOf("2023-01-01"))
                .salaire(4000.0)
                .password("password")
                .statutContrat(StatutContrat.CDI)
                .poste(PosteEmploye.PHARMACIEN_ADJOINT)
                .diplome("Diplôme de Pharmacie")
                .build();

        // Générer le matricule pour le pharmacien adjoint
        pharmacienAdjoint.generateMatricule(pharmacienAdjoint.getPoste().toString());

        client = Client.builder()
                .nom("Smith")
                .prenom("Jane")
                .email("jane@example.com")
                .telephone("987654321")
                .adresse("456 Rue de la Pharmacie")
                .build();

        entityManager.persist(pharmacienAdjoint);
        entityManager.persist(client);

        // Création des ventes de test
        vente1 = Vente.builder()
                .pharmacienAdjoint(pharmacienAdjoint)
                .client(client)
                .dateVente(new Date())
                .modePaiement("Carte bancaire")
                .montantTotal(50.0)
                .montantRembourse(10.0)
                .build();

        vente2 = Vente.builder()
                .pharmacienAdjoint(pharmacienAdjoint)
                .client(client)
                .dateVente(new Date())
                .modePaiement("Espèces")
                .montantTotal(30.0)
                .montantRembourse(5.0)
                .build();

        entityManager.persist(vente1);
        entityManager.persist(vente2);
        entityManager.flush();
    }

    @Test
    void findByPharmacienAdjoint_IdPersonne_ShouldReturnVentes() {
        // When
        List<Vente> ventes = venteRepository.findByPharmacienAdjoint_IdPersonne(pharmacienAdjoint.getIdPersonne());

        // Then
        assertThat(ventes).hasSize(2);
        assertThat(ventes).extracting(Vente::getPharmacienAdjoint).containsOnly(pharmacienAdjoint);
    }

    @Test
    void findByClient_IdPersonne_ShouldReturnVentes() {
        // When
        List<Vente> ventes = venteRepository.findByClient_IdPersonne(client.getIdPersonne());

        // Then
        assertThat(ventes).hasSize(2);
        assertThat(ventes).extracting(Vente::getClient).containsOnly(client);
    }

    @Test
    void findByDateVenteBetween_ShouldReturnVentes() {
        // When
        List<Vente> ventes = venteRepository.findByDateVenteBetween(new Date(System.currentTimeMillis() - 10000), new Date(System.currentTimeMillis() + 10000));

        // Then
        assertThat(ventes).hasSize(2);
    }

    @Test
    void findByModePaiement_ShouldReturnVentes() {
        // When
        List<Vente> ventes = venteRepository.findByModePaiement("Carte bancaire");

        // Then
        assertThat(ventes).hasSize(1);
        assertThat(ventes.get(0).getModePaiement()).isEqualTo("Carte bancaire");
    }
}
