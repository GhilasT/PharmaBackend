package l3o2.pharmacie.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import l3o2.pharmacie.api.exceptions.*;
import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
import l3o2.pharmacie.api.model.dto.request.MedecinUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;
import l3o2.pharmacie.api.model.entity.Medecin;
import l3o2.pharmacie.api.repository.MedecinRepository;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedecinServiceTest {

    @Mock
    private MedecinRepository repository;

    @InjectMocks
    private MedecinService service;

    private UUID id;
    private Medecin medecin;
    private MedecinCreateRequest createRequest;
    private MedecinUpdateRequest updateRequest;
    private List<String> languesParlees;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        LocalDate dateMiseAJour = LocalDate.now();
        languesParlees = Arrays.asList("Français", "Anglais");
        
        medecin = Medecin.builder()
                .idPersonne(id)
                .nom("Dupont")
                .prenom("Jean")
                .email("jean.dupont@medecin.fr")
                .telephone("0612345678")
                .adresse("123 Rue de la Santé")
                .rpps("12345678901")
                .adeli("123456789")
                .civilite("Dr")
                .profession("Médecin généraliste")
                .specialitePrincipale("Médecine générale")
                .specialiteSecondaire("Nutrition")
                .modeExercice("Libéral")
                .codePostal("75001")
                .ville("Paris")
                .siteWeb("www.drdupont.fr")
                .secteur("Secteur 1")
                .conventionnement("Conventionné")
                .honoraires("Tarif conventionnel")
                .languesParlees(languesParlees)
                .siret("12345678901234")
                .dateMiseAJour(dateMiseAJour)
                .build();
        
        createRequest = new MedecinCreateRequest();
        createRequest.setNom("Dupont");
        createRequest.setPrenom("Jean");
        createRequest.setEmail("jean.dupont@medecin.fr");
        createRequest.setTelephone("0612345678");
        createRequest.setAdresse("123 Rue de la Santé");
        createRequest.setPassword("password123");
        createRequest.setRpps("12345678901");
        createRequest.setAdeli("123456789");
        createRequest.setCivilite("Dr");
        createRequest.setProfession("Médecin généraliste");
        createRequest.setSpecialitePrincipale("Médecine générale");
        createRequest.setSpecialiteSecondaire("Nutrition");
        createRequest.setModeExercice("Libéral");
        createRequest.setCodePostal("75001");
        createRequest.setVille("Paris");
        createRequest.setSiteWeb("www.drdupont.fr");
        createRequest.setSecteur("Secteur 1");
        createRequest.setConventionnement("Conventionné");
        createRequest.setHonoraires("Tarif conventionnel");
        createRequest.setCarteVitale(true);
        createRequest.setTeleconsultation(true);
        createRequest.setLanguesParlees(languesParlees);
        createRequest.setSiret("12345678901234");
        createRequest.setDateMiseAJour(dateMiseAJour);
        
        updateRequest = new MedecinUpdateRequest();
        updateRequest.setNom("Dupont-Martin");
        updateRequest.setPrenom("Jean-Pierre");
        updateRequest.setEmail("jeanpierre.dupont@medecin.fr");
        updateRequest.setTelephone("0687654321");
        updateRequest.setAdresse("456 Avenue de la Médecine");
    }

    @Test
    void createMedecin_ValideDonne() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.findByRpps(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Medecin.class))).thenReturn(medecin);
        
        MedecinResponse result = service.createMedecin(createRequest);
        
        assertNotNull(result);
        assertEquals("Dupont", result.getNom());
        assertEquals("12345678901", result.getRpps());
        verify(repository).save(any(Medecin.class));
    }

    @Test
    void createMedecin_DuplicEmail_ThrowsException() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(medecin));
        
        assertThrows(DuplicateEmailException.class, () -> service.createMedecin(createRequest));
        verify(repository, never()).save(any(Medecin.class));
    }

    @Test
    void createMedecin_DuplicRpps_ThrowsException() {
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.findByRpps(anyString())).thenReturn(Optional.of(medecin));
        
        assertThrows(DuplicateRPPSException.class, () -> service.createMedecin(createRequest));
        verify(repository, never()).save(any(Medecin.class));
    }

    @Test
    void updateMedecin_ValideDonne() {
        Medecin updatedMedecin = Medecin.builder()
                .idPersonne(id)
                .nom("Dupont-Martin")
                .prenom("Jean-Pierre")
                .email("jeanpierre.dupont@medecin.fr")
                .telephone("0687654321")
                .adresse("456 Avenue de la Médecine")
                .rpps(medecin.getRpps())
                .adeli(medecin.getAdeli())
                .civilite(medecin.getCivilite())
                .profession(medecin.getProfession())
                .specialitePrincipale(medecin.getSpecialitePrincipale())
                .specialiteSecondaire(medecin.getSpecialiteSecondaire())
                .modeExercice(medecin.getModeExercice())
                .codePostal(medecin.getCodePostal())
                .ville(medecin.getVille())
                .siteWeb(medecin.getSiteWeb())
                .secteur(medecin.getSecteur())
                .conventionnement(medecin.getConventionnement())
                .honoraires(medecin.getHonoraires())
                .languesParlees(medecin.getLanguesParlees())
                .siret(medecin.getSiret())
                .dateMiseAJour(medecin.getDateMiseAJour())
                .build();
        
        when(repository.findById(id)).thenReturn(Optional.of(medecin));
        when(repository.save(any(Medecin.class))).thenReturn(updatedMedecin);
        
        MedecinResponse result = service.updateMedecin(id, updateRequest);
        
        assertEquals("Dupont-Martin", result.getNom());
        assertEquals("Jean-Pierre", result.getPrenom());
        assertEquals("jeanpierre.dupont@medecin.fr", result.getEmail());
        verify(repository).save(any(Medecin.class));
    }

    @Test
    void updateMedecin_NonValideDonnee_ThrowsException() {
        when(repository.findById(id)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> service.updateMedecin(id, updateRequest));
        verify(repository, never()).save(any(Medecin.class));
    }

    @Test
    void getAllMedecins_ValideDonne() {
        Medecin medecin2 = Medecin.builder()
                .idPersonne(UUID.randomUUID())
                .nom("Martin")
                .prenom("Sophie")
                .email("sophie.martin@medecin.fr")
                .rpps("98765432109")
                .build();
        
        when(repository.findAll()).thenReturn(Arrays.asList(medecin, medecin2));
        
        List<MedecinResponse> result = service.getAllMedecins();
        
        assertEquals(2, result.size());
        assertEquals("Dupont", result.get(0).getNom());
        assertEquals("Martin", result.get(1).getNom());
    }

    @Test
    void getMedecinById_ValideDonne() {
        when(repository.findById(id)).thenReturn(Optional.of(medecin));
        
        MedecinResponse result = service.getMedecinById(id);
        
        assertEquals("Dupont", result.getNom());
        assertEquals("12345678901", result.getRpps());
    }

    @Test
    void getMedecinById_NotFound_ThrowsException() {
        when(repository.findById(id)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> service.getMedecinById(id));
    }

    @Test
    void getMedecinByRpps_ValideDonne() {
        when(repository.findByRpps("12345678901")).thenReturn(Optional.of(medecin));
        
        MedecinResponse result = service.getMedecinByRpps("12345678901");
        
        assertEquals("Dupont", result.getNom());
        assertEquals("12345678901", result.getRpps());
    }

    @Test
    void getMedecinByRpps_NotFound_ThrowsException() {
        when(repository.findByRpps("12345678901")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> service.getMedecinByRpps("12345678901"));
    }

    @Test
    void getMedecinBySiret_ValideDonne() {
        when(repository.findBySiret("12345678901234")).thenReturn(Optional.of(medecin));
        
        MedecinResponse result = service.getMedecinBySiret("12345678901234");
        
        assertEquals("12345678901234", result.getSiret());
    }

    @Test
    void getMedecinBySiret_NotFound_ThrowsException() {
        when(repository.findBySiret("12345678901234")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> service.getMedecinBySiret("12345678901234"));
    }

    @Test
    void deleteMedecin_ValideDonne() {
        when(repository.existsById(id)).thenReturn(true);
        
        service.deleteMedecin(id);
        
        verify(repository).deleteById(id);
    }

    @Test
    void deleteMedecin_NotFound_ThrowsException() {
        when(repository.existsById(id)).thenReturn(false);
        
        assertThrows(ResourceNotFoundException.class, () -> service.deleteMedecin(id));
        verify(repository, never()).deleteById(any());
    }

    @Test
    void getMedecinsByNomOuPrenom_Donnee() {
        String query = "Dupont";
        when(repository.searchByNomPrenomCombinaison(query.toLowerCase())).thenReturn(Arrays.asList(medecin));
        
        List<MedecinResponse> result = service.getMedecinsByNomOuPrenom(query);
        
        assertEquals(1, result.size());
        assertEquals("Dupont", result.get(0).getNom());
    }

    @Test
    void getMedecinsByNomOuPrenom_EmptyQuery_ThrowsException() {
        assertThrows(InvalidParameterException.class, () -> service.getMedecinsByNomOuPrenom(""));
        assertThrows(InvalidParameterException.class, () -> service.getMedecinsByNomOuPrenom("   "));
        assertThrows(InvalidParameterException.class, () -> service.getMedecinsByNomOuPrenom(null));
        
        verify(repository, never()).searchByNomPrenomCombinaison(any());
    }

    @Test
    void existsByEmail() {
        when(repository.existsByEmail("jean.dupont@medecin.fr")).thenReturn(true);
        when(repository.existsByEmail("inconnu@medecin.fr")).thenReturn(false);
        
        assertTrue(service.existsByEmail("jean.dupont@medecin.fr"));
        assertFalse(service.existsByEmail("inconnu@medecin.fr"));
    }
}
