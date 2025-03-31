package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.VenteCreateRequest;
import l3o2.pharmacie.api.model.dto.response.VenteResponse;
import l3o2.pharmacie.api.service.VenteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour gérer les ventes en pharmacie.
 * Fournit des endpoints pour la création, récupération et suppression.
 */
@RestController
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VenteController {

    private final VenteService venteService;

    /**
     * Endpoint pour récupérer toutes les ventes.
     * @return Liste des ventes enregistrées.
     */
    @GetMapping
    public ResponseEntity<List<VenteResponse>> getAllVentes() {
        return ResponseEntity.ok(venteService.getAll());
    }

    /**
     * Endpoint pour récupérer une vente par son identifiant.
     * @param idVente Identifiant de la vente.
     * @return Détails de la vente.
     */
    @GetMapping("/{idVente}")
    public ResponseEntity<VenteResponse> getVenteById(@PathVariable UUID idVente) {
        return ResponseEntity.ok(venteService.getById(idVente));
    }

    /**
     * Endpoint pour récupérer les ventes triées par date.
     * @return Liste des ventes triées par date décroissante.
     */
    @GetMapping("/sorted")
    public ResponseEntity<List<VenteResponse>> getAllVentesSortedByDate() {
        return ResponseEntity.ok(venteService.getAllOrderByDate());
    }

    /**
     * Endpoint pour créer une nouvelle vente.
     * @param request Données de la vente.
     * @return Vente créée.
     */
    @PostMapping
    public ResponseEntity<VenteResponse> createVente(@Valid @RequestBody VenteCreateRequest request) {
        return new ResponseEntity<>(venteService.createVente(request), HttpStatus.CREATED);
    }

    /**
     * Endpoint pour supprimer une vente.
     * @param idVente Identifiant de la vente.
     * @return Confirmation de suppression.
     */
    @DeleteMapping("/{idVente}")
    public ResponseEntity<Void> deleteVente(@PathVariable UUID idVente) {
        venteService.delete(idVente);
        return ResponseEntity.noContent().build();
    }
}