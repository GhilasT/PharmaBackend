package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.repository.MedecinRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import l3o2.pharmacie.api.exceptions.*;
import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;
import l3o2.pharmacie.api.model.entity.Medecin;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private List<String> languesParlees;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        languesParlees = Arrays.asList("Français", "Anglais");

        // Création d'un objet médecin pour tester
        medecin = Medecin.builder()
                .idMedecin(id)
                .civilite("Dr")
                .nomExercice("Dupont")
                .prenomExercice("Jean")
                .rppsMedecin("12345678901")
                .profession("Médecin généraliste")
                .modeExercice("Libéral")
                .qualifications("Docteur en médecine")
                .structureExercice("Hôpital de Paris")
                .fonctionActivite("Médecin traitant")
                .genreActivite("Consultation")
                .build();

        // Requête de création avec les bons attributs
        createRequest = new MedecinCreateRequest();
        createRequest.setNomExercice("Dupont");
        createRequest.setPrenomExercice("Jean");
        createRequest.setRppsMedecin("12345678901");
        createRequest.setCivilite("Dr");
        createRequest.setProfession("Médecin généraliste");
        createRequest.setModeExercice("Libéral");
        createRequest.setQualifications("Docteur en médecine");
        createRequest.setStructureExercice("Hôpital de Paris");
        createRequest.setFonctionActivite("Médecin traitant");
        createRequest.setGenreActivite("Consultation");
        createRequest.setCategorieProfessionnelle("Civil");
        // Ajoutez les autres attributs selon votre besoin
    }

    @Test
    void createMedecin_ValideDonnee() {
        when(repository.findByRppsMedecin(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Medecin.class))).thenReturn(medecin);

        MedecinResponse result = service.createMedecin(createRequest);

        // Assertions pour vérifier que le médecin a été créé correctement
        assertNotNull(result);
        assertEquals("Dupont", result.getNomExercice());
        assertEquals("12345678901", result.getRppsMedecin());
        assertEquals("Médecin généraliste", result.getProfession());
        assertEquals("Libéral", result.getModeExercice());
        assertEquals("Hôpital de Paris", result.getStructureExercice());
        verify(repository).save(any(Medecin.class));
    }

    @Test
    void createMedecin_DuplicEmail_ThrowsException() {

        assertThrows(DuplicateEmailException.class, () -> service.createMedecin(createRequest));
        verify(repository, never()).save(any(Medecin.class));
    }

    @Test
    void createMedecin_DuplicRpps_ThrowsException() {
        when(repository.findByRppsMedecin(anyString())).thenReturn(Optional.of(medecin));

        assertThrows(DuplicateRPPSException.class, () -> service.createMedecin(createRequest));
        verify(repository, never()).save(any(Medecin.class));
    }

    @Test
    void updateMedecin_ValideDonnee() {
        Medecin updatedMedecin = Medecin.builder()
                .idMedecin(id)
                .nomExercice("Dupont-Martin")
                .prenomExercice("Jean-Pierre")
                .rppsMedecin(medecin.getRppsMedecin())
                .profession(medecin.getProfession())
                .modeExercice(medecin.getModeExercice())
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(medecin));
        when(repository.save(any(Medecin.class))).thenReturn(updatedMedecin);

        MedecinResponse result = service.updateMedecin(id, createRequest);

        // Assertions pour vérifier que le médecin a bien été mis à jour
        assertEquals("Dupont-Martin", result.getNomExercice());
        assertEquals("Jean-Pierre", result.getPrenomExercice());
        assertEquals("Médecin généraliste", result.getProfession());
        assertEquals("Libéral", result.getModeExercice());
        assertEquals("Médecin traitant", result.getFonctionActivite());
        verify(repository).save(any(Medecin.class));
    }

    @Test
    void updateMedecin_NonValideDonnee_ThrowsException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.updateMedecin(id, createRequest));
        verify(repository, never()).save(any(Medecin.class));
    }

    @Test
    void getAllMedecins_ValideDonne() {
        Medecin medecin2 = Medecin.builder()
                .idMedecin(UUID.randomUUID())
                .nomExercice("Martin")
                .prenomExercice("Sophie")
                .rppsMedecin("98765432109")
                .profession("Chirurgien")
                .build();

        when(repository.findAll()).thenReturn(Arrays.asList(medecin, medecin2));

        List<MedecinResponse> result = service.getAllMedecins();

        // Vérification de la taille et des noms
        assertEquals(2, result.size());
        assertEquals("Dupont", result.get(0).getNomExercice());
        assertEquals("Martin", result.get(1).getNomExercice());
    }

    @Test
    void getMedecinById_ValideDonne() {
        when(repository.findById(id)).thenReturn(Optional.of(medecin));

        MedecinResponse result = service.getMedecinById(id);

        // Vérification des valeurs
        assertEquals("Dupont", result.getNomExercice());
        assertEquals("12345678901", result.getRppsMedecin());
    }

    @Test
    void getMedecinById_NotFound_ThrowsException() {
        when(repository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.getMedecinById(id));
    }


}