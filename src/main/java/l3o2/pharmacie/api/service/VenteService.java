package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.VenteCreateRequest;
import l3o2.pharmacie.api.model.dto.response.VenteResponse;
import l3o2.pharmacie.api.model.entity.Client;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import l3o2.pharmacie.api.model.entity.Vente;
import l3o2.pharmacie.api.model.entity.medicament.Medicament;
import l3o2.pharmacie.api.repository.VenteRepository;
import l3o2.pharmacie.api.repository.ClientRepository;
import l3o2.pharmacie.api.repository.PharmacienAdjointRepository;
import l3o2.pharmacie.api.repository.MedicamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VenteService {
    private final VenteRepository venteRepository;
    private final ClientRepository clientRepository;
    private final PharmacienAdjointRepository pharmacienAdjointRepository;
private final MedicamentRepository medicamentRepository;

/**
 * Récupère toutes les ventes.
 * @return Liste des ventes sous forme de DTOs.
 */
@Transactional(readOnly = true)
public List<VenteResponse> getAll() {
    return venteRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    /**
     * Récupère une vente par son identifiant.
     * @param idVente Identifiant de la vente.
     * @return DTO de la vente.
     * @throws EntityNotFoundException si la vente n'existe pas.
     */
    @Transactional(readOnly = true)
    public VenteResponse getById(UUID idVente) {
        return venteRepository.findById(idVente)
        .map(this::mapToResponse)
        .orElseThrow(() -> new EntityNotFoundException("Vente non trouvée"));
    }
    
    /**
     * Récupère toutes les ventes triées par date décroissante.
     * @return Liste des ventes triées.
     */
    @Transactional(readOnly = true)
    public List<VenteResponse> getAllOrderByDate() {
        return venteRepository.findAll(Sort.by(Sort.Direction.DESC, "dateVente")).stream()
        .map(this::mapToResponse)
        .collect(Collectors.toList());
    }
    
    /**
     * Crée une nouvelle vente avec gestion des relations et validation.
     * @param request Données de la vente.
     * @return DTO de la vente créée.
     */
    @Transactional
    public VenteResponse createVente(VenteCreateRequest request) {
        // Vérification de l'existence du pharmacien adjoint
        PharmacienAdjoint pharmacienAdjoint = pharmacienAdjointRepository.findById(request.getPharmacienAdjointId())
        .orElseThrow(() -> new EntityNotFoundException("Pharmacien adjoint non trouvé"));
        
        // Vérification de l'existence du client
        Client client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client non trouvé"));

                // Vérification des médicaments vendus
                List<Medicament> medicaments = new ArrayList<>();//medicamentRepository.findAllById(request.getMedicamentIds());
                if (medicaments.isEmpty()) {
                    throw new IllegalArgumentException("Aucun médicament valide trouvé pour la vente");
                }
                
                Vente vente = Vente.builder()
                .dateVente(new Date())
                .modePaiement(request.getModePaiement())
                //.montantTotal(request.getMontantTotal())
                //.montantRembourse(request.getMontantRembourse())
                .pharmacienAdjoint(pharmacienAdjoint)
                .client(client)
                .medicaments(medicaments)
                .build();
                
                Vente savedVente = venteRepository.save(vente);
                return mapToResponse(savedVente);
            }
            
            /**
             * Supprime une vente.
             * @param idVente Identifiant de la vente.
             */
            @Transactional
            public void delete(UUID idVente) {
        if (!venteRepository.existsById(idVente)) {
            throw new EntityNotFoundException("Vente non trouvée");
        }
        venteRepository.deleteById(idVente);
    }
    
    /**
     * Convertit une entité Vente en DTO VenteResponse.
     * @param vente L'entité Vente.
     * @return DTO de la vente.
     */
    private VenteResponse mapToResponse(Vente vente) {
        return VenteResponse.builder()
        .idVente(vente.getIdVente())
        .dateVente(vente.getDateVente())
        .modePaiement(vente.getModePaiement())
        //.montantTotal(vente.getMontantTotal())
        //.montantRembourse(vente.getMontantRembourse())
        .pharmacienAdjointId(vente.getPharmacienAdjoint().getIdPersonne())
        .clientId(vente.getClient().getIdPersonne())
        //.medicamentIds(vente.getMedicaments().stream().map(Medicament::getId).collect(Collectors.toList()))
        .build();
    }
}