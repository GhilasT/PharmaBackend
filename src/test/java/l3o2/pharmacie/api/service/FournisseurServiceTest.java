package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.*;
import l3o2.pharmacie.api.model.dto.request.FournisseurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.FournisseurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.FournisseurResponse;
import l3o2.pharmacie.api.model.entity.Fournisseur;
import l3o2.pharmacie.api.repository.FournisseurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FournisseurServiceTest {

    @Mock
    private FournisseurRepository repository;

    @InjectMocks
    private FournisseurService service;

    private UUID id;
    private Fournisseur fournisseur;
    private FournisseurCreateRequest createRequest;
    private FournisseurUpdateRequest updateRequest;
    private Fournisseur updatedFournisseur;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        
        fournisseur = Fournisseur.builder()
                .idFournisseur(id)
                .nomSociete("Pharma Plus")
                .email("contact@pharmaplus.fr")
                .telephone("0612345678")
                .adresse("123 Rue de la Pharmacie, 75000 Paris")
                .build();
        
        createRequest = new FournisseurCreateRequest();
        createRequest.setNomSociete("Pharma Plus");
        createRequest.setEmail("contact@pharmaplus.fr");
        createRequest.setTelephone("0612345678");
        createRequest.setAdresse("123 Rue de la Pharmacie, 75000 Paris");
        
        updateRequest = new FournisseurUpdateRequest();
        updateRequest.setNomSociete("Pharma Plus Updated");
        updateRequest.setEmail("updated@pharmaplus.fr");
        updateRequest.setTelephone("0687654321");
        updateRequest.setAdresse("456 Avenue de la Santé, 75001 Paris");
        
        updatedFournisseur = Fournisseur.builder()
                .idFournisseur(id)
                .nomSociete("Pharma Plus Updated")
                .email("updated@pharmaplus.fr")
                .telephone("0687654321")
                .adresse("456 Avenue de la Santé, 75001 Paris")
                .build();
    }

    @Test
    void createFournisseur_Success() {
        when(repository.findByNomSociete(anyString())).thenReturn(Optional.empty());
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.findByTelephone(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Fournisseur.class))).thenReturn(fournisseur);
        
        FournisseurResponse result = service.createFournisseur(createRequest);
        
        assertNotNull(result);
        assertEquals("Pharma Plus", result.getNomSociete());
        verify(repository).save(any(Fournisseur.class));
    }

    @Test
    void createFournisseur_ThrowsExceptions() {
        when(repository.findByNomSociete(anyString())).thenReturn(Optional.of(fournisseur));
        assertThrows(DuplicateSocieteException.class, () -> service.createFournisseur(createRequest));
        
        when(repository.findByNomSociete(anyString())).thenReturn(Optional.empty());
        when(repository.findByEmail(anyString())).thenReturn(Optional.of(fournisseur));
        assertThrows(DuplicateEmailException.class, () -> service.createFournisseur(createRequest));
        
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.findByTelephone(anyString())).thenReturn(Optional.of(fournisseur));
        assertThrows(DuplicateTelephoneException.class, () -> service.createFournisseur(createRequest));
        
        when(repository.findByTelephone(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Fournisseur.class))).thenThrow(DataIntegrityViolationException.class);
        assertThrows(InvalidDataException.class, () -> service.createFournisseur(createRequest));
    }

    @Test
    void updateFournisseur_Success() {
        when(repository.findById(id)).thenReturn(Optional.of(fournisseur));
        when(repository.findByNomSociete(anyString())).thenReturn(Optional.empty());
        when(repository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(repository.findByTelephone(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(Fournisseur.class))).thenReturn(updatedFournisseur);
        
        FournisseurResponse result = service.updateFournisseur(id, updateRequest);
        
        assertEquals("Pharma Plus Updated", result.getNomSociete());
        assertEquals("updated@pharmaplus.fr", result.getEmail());
        verify(repository).save(any(Fournisseur.class));
    }

    @Test
    void updateFournisseur_NotFound() {
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.updateFournisseur(id, updateRequest));
        verify(repository, never()).save(any());
    }

    @Test
    void getAllFournisseurs() {
        Fournisseur fournisseur2 = Fournisseur.builder()
                .idFournisseur(UUID.randomUUID())
                .nomSociete("Autre Pharma")
                .build();
        when(repository.findAll()).thenReturn(Arrays.asList(fournisseur, fournisseur2));
        
        assertEquals(2, service.getAllFournisseurs().size());
    }

    @Test
    void getFournisseurById() {
        when(repository.findById(id)).thenReturn(Optional.of(fournisseur));
        assertNotNull(service.getFournisseurById(id));
        
        when(repository.findById(id)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.getFournisseurById(id));
    }

    @Test
    void deleteFournisseur() {
        when(repository.existsById(id)).thenReturn(true);
        service.deleteFournisseur(id);
        verify(repository).deleteById(id);
        
        when(repository.existsById(id)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.deleteFournisseur(id));
        verify(repository, times(1)).deleteById(any());
    }

    @Test
    void searchFournisseurs() {
        String query = "pharma";
        when(repository.searchFournisseurs(query)).thenReturn(Arrays.asList(fournisseur));
        assertEquals(1, service.searchFournisseurs(query).size());
        
        assertThrows(InvalidParameterException.class, () -> service.searchFournisseurs(""));
        assertThrows(InvalidParameterException.class, () -> service.searchFournisseurs("   "));
        assertThrows(InvalidParameterException.class, () -> service.searchFournisseurs(null));
    }
}
