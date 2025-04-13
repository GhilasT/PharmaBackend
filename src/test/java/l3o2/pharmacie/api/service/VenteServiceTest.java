package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.VenteCreateRequest;
import l3o2.pharmacie.api.model.dto.request.MedicamentPanierRequest;
import l3o2.pharmacie.api.model.dto.response.VenteResponse;
import l3o2.pharmacie.api.model.entity.Client;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import l3o2.pharmacie.api.model.entity.Vente;
import l3o2.pharmacie.api.model.entity.medicament.CisBdpm;
import l3o2.pharmacie.api.model.entity.medicament.CisCipBdpm;
import l3o2.pharmacie.api.model.entity.medicament.MedicamentPanier;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import l3o2.pharmacie.api.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import jakarta.persistence.EntityNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VenteServiceTest {

    @Mock
    private VenteRepository venteRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PharmacienAdjointRepository pharmacienAdjointRepository;

    @Mock
    private MedicamentRepository medicamentRepository;

    @Mock
    private MedicamentService medicamentService;

    @Mock
    private CisCipBdpmRepository cisCipBdpmRepository;

    @InjectMocks
    private VenteService venteService;

    private VenteCreateRequest venteCreateRequest;
    private Vente vente;
    private PharmacienAdjoint pharmacienAdjoint;
    private Client client;
    private StockMedicament stockMedicament;
    private MedicamentPanier medicamentPanier;
    private CisBdpm cisBdpm;
    private CisCipBdpm cisCipBdpm;

    @BeforeEach
    void setUp() {
        // Création des entités nécessaires
        pharmacienAdjoint = new PharmacienAdjoint();
        pharmacienAdjoint.setIdPersonne(UUID.randomUUID());

        client = new Client();
        client.setIdPersonne(UUID.randomUUID());

        cisBdpm = CisBdpm.builder()
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

        cisCipBdpm = new CisCipBdpm();
        cisCipBdpm.setCodeCip7("1234567");
        cisCipBdpm.setCodeCip13("1234567890123");
        cisCipBdpm.setCisBdpm(cisBdpm);
        cisCipBdpm.setLibellePresentation("Comprimé 500mg");
        cisCipBdpm.setStatutAdministratif("Autorisé");
        cisCipBdpm.setEtatCommercialisation("Commercialisé");
        cisCipBdpm.setDateDeclarationCommercialisation(new Date());
        cisCipBdpm.setAgrementCollectivites("oui");
        cisCipBdpm.setTauxRemboursement("65%");

        stockMedicament = new StockMedicament();
        stockMedicament.setId(1L);
        stockMedicament.setQuantite(10); // Quantité suffisante
        stockMedicament.setSeuilAlerte(5);
        stockMedicament.setPresentation(cisCipBdpm);

        medicamentPanier = new MedicamentPanier();
        medicamentPanier.setStockMedicament(stockMedicament);
        medicamentPanier.setQuantite(2);

        vente = Vente.builder()
                .idVente(UUID.randomUUID())
                .dateVente(new Date())
                .modePaiement("Carte bancaire")
                .montantTotal(50.0)
                .montantRembourse(10.0)
                .pharmacienAdjoint(pharmacienAdjoint)
                .client(client)
                .medicamentsPanier(Collections.singletonList(medicamentPanier))
                .build();

        venteCreateRequest = VenteCreateRequest.builder()
                .pharmacienAdjointId(pharmacienAdjoint.getIdPersonne())
                .clientId(client.getIdPersonne())
                .dateVente(new Date())
                .modePaiement("Carte bancaire")
                .montantTotal(50.0)
                .montantRembourse(10.0)
                .medicaments(Collections.singletonList(
                        MedicamentPanierRequest.builder()
                                .codeCip13("1234567890123")
                                .quantite(2)
                                .build()
                ))
                .build();
    }

    @Test
    void getAll_ShouldReturnAllVentes() {
        // When
        when(venteRepository.findAll(Sort.by(Sort.Direction.DESC, "dateVente"))).thenReturn(Collections.singletonList(vente));

        // Exécution
        List<VenteResponse> responses = venteService.getAllOrderByDate();

        // Vérifications
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getIdVente()).isEqualTo(vente.getIdVente());
    }

    @Test
    void getById_WhenExists_ShouldReturnVente() {
        // When
        when(venteRepository.findById(vente.getIdVente())).thenReturn(Optional.of(vente));

        // Exécution
        VenteResponse response = venteService.getById(vente.getIdVente());

        // Vérifications
        assertThat(response.getIdVente()).isEqualTo(vente.getIdVente());
    }

    @Test
    void getById_WhenNotExists_ShouldThrowEntityNotFoundException() {
        // When
        when(venteRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Exécution et vérification
        assertThatThrownBy(() -> venteService.getById(UUID.randomUUID()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Vente non trouvée");
    }


    @Test
    void deleteVente_WhenExists_ShouldDeleteVente() {
        // When
        when(venteRepository.existsById(vente.getIdVente())).thenReturn(true);

        // Exécution
        venteService.delete(vente.getIdVente());

        // Vérifications
        verify(venteRepository).deleteById(vente.getIdVente());
    }

    @Test
    void deleteVente_WhenNotExists_ShouldThrowEntityNotFoundException() {
        // When
        UUID nonExistentId = UUID.randomUUID();
        when(venteRepository.existsById(nonExistentId)).thenReturn(false);

        // Exécution et vérification
        assertThatThrownBy(() -> venteService.delete(nonExistentId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Vente non trouvée");
    }
}
