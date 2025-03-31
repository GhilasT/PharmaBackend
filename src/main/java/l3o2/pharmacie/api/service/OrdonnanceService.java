package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.OrdonnanceCreateRequest;
import l3o2.pharmacie.api.model.dto.response.OrdonnanceResponse;
import l3o2.pharmacie.api.model.entity.Ordonnance;
import l3o2.pharmacie.api.model.entity.Vente;
import l3o2.pharmacie.api.repository.OrdonnanceRepository;
import l3o2.pharmacie.api.repository.VenteRepository; // Ajout de VenteRepository
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrdonnanceService {
/*
    private final OrdonnanceRepository ordonnanceRepository;
    private final VenteRepository venteRepository;

    @Transactional
    public OrdonnanceResponse createOrdonnance(OrdonnanceCreateRequest request) {
        // Vérification si l'ordonnance existe déjà
        if (ordonnanceRepository.existsById(request.getIdOrdonnance())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Une ordonnance avec cet id existe déjà.");
        }

        // Recherche de la vente par l'ID
        Vente vente = venteRepository.findById(request.getVenteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vente non trouvée"));

        // Création de l'ordonnance
        Ordonnance ordonnance = Ordonnance.builder()
                .idOrdonnance(request.getIdOrdonnance())
                .dateEmission(request.getDateEmission())
                .dateExpiration(request.getDateExpiration())
                .rppsMedecin(request.getRppsMedecin())
                .client(request.getClient())
                .medecin(request.getMedecin())
                .prescriptions(request.getPrescriptions())
                .vente(vente)
                .build();


        return mapToResponse(ordonnanceRepository.save(ordonnance));
    }

    public List<OrdonnanceResponse> getAllOrdonnances() {
        // Récupérer toutes les ordonnances et les transformer en réponses
        return ordonnanceRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public OrdonnanceResponse getOrdonnanceById(UUID idOrdonnance) {
        // Récupérer une ordonnance par son ID et retourner la réponse
        Ordonnance ordonnance = ordonnanceRepository.findById(idOrdonnance)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordonnance non trouvée"));
        return mapToResponse(ordonnance);
    }
    /*
    public OrdonnanceResponse getOrdonnanceByVente(UUID venteId) {
        // Récupérer une ordonnance par la vente associée
        return ordonnanceRepository.findByVente(venteId)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordonnance non trouvée pour cette vente"));
    }



    // Convertir Ordonnance en OrdonnanceResponse
    public OrdonnanceResponse mapToResponse(Ordonnance ordonnance) {
        return OrdonnanceResponse.builder()
                .idOrdonnance(ordonnance.getIdOrdonnance())
                .numeroOrd(ordonnance.getNumeroOrd())
                .dateEmission(ordonnance.getDateEmission())
                .dateExpiration(ordonnance.getDateExpiration())
                .rppsMedecin(ordonnance.getRppsMedecin())
                .client(ordonnance.getClient())
                .medecin(ordonnance.getMedecin())
                .prescriptions(ordonnance.getPrescriptions())
                .build();
    }

    // Convertir OrdonnanceCreateRequest en Ordonnance
    public Ordonnance mapToEntity(OrdonnanceCreateRequest request) {
        Vente vente = venteRepository.findById(request.getVenteId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vente non trouvée"));

        return Ordonnance.builder()

                .idOrdonnance(request.getIdOrdonnance())
                .dateEmission(request.getDateEmission())
                .dateExpiration(request.getDateExpiration())
                .rppsMedecin(request.getRppsMedecin())
                .client(request.getClient())
                .medecin(request.getMedecin())
                .prescriptions(request.getPrescriptions())
                .vente(vente)
                .build();
    }

 */
}