package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.OrdonnanceCreateRequest;
import l3o2.pharmacie.api.model.dto.response.OrdonnanceResponse;
import l3o2.pharmacie.api.model.entity.Ordonnance;
import l3o2.pharmacie.api.model.entity.Vente;
import l3o2.pharmacie.api.repository.VenteRepository;
import l3o2.pharmacie.api.repository.OrdonnanceRepository;
import l3o2.pharmacie.api.service.OrdonnanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ordonnances")
@RequiredArgsConstructor
public class OrdonnanceController {
/*
    private final OrdonnanceService ordonnanceService;
    private final VenteRepository venteRepository; // Injecte le repository pour les ventes
    private final OrdonnanceRepository ordonnanceRepository; // Injecte le repository pour les ordonnances


     //Crée une nouvelle ordonnance.

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrdonnanceResponse createOrdonnance(@RequestBody OrdonnanceCreateRequest request) {
        return ordonnanceService.createOrdonnance(request);
    }


     // Récupère une ordonnance par son ID.

    @GetMapping("/{idOrdonnance}")
    public OrdonnanceResponse getOrdonnanceById(@PathVariable UUID idOrdonnance) {
        return ordonnanceService.getOrdonnanceById(idOrdonnance);
    }


     //Récupère toutes les ordonnances.

    @GetMapping
    public List<OrdonnanceResponse> getAllOrdonnances() {
        return ordonnanceService.getAllOrdonnances();
    }


     // Récupère une ordonnance par vente.


    @GetMapping("/vente/{venteId}")
    public OrdonnanceResponse getOrdonnanceByVente(@PathVariable UUID venteId) {
        // Récupérer la vente via son ID
        Vente vente = venteRepository.findById(venteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vente non trouvée"));

        // Récupérer l'ordonnance associée à la vente
        Ordonnance ordonnance = ordonnanceRepository.findByVente(venteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ordonnance non trouvée pour cette vente"));

        // Retourner l'ordonnance sous forme de DTO
        return ordonnanceService.mapToResponse(ordonnance);
    }
     */
}