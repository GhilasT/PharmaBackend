package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.VenteCreateRequest;
import l3o2.pharmacie.api.model.dto.request.VenteUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.VenteResponse;
import l3o2.pharmacie.api.service.VenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur REST pour gérer les opérations CRUD sur les ventes.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VenteController {

    private final VenteService venteService;

    /**
     * Crée une nouvelle vente.
     *
     * @param request Données de la vente à créer.
     * @return La vente créée.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VenteResponse createVente(@RequestBody VenteCreateRequest request) {
        System.out.println("Vente reçue : " + request);
        request.getMedicaments().forEach(m -> System.out
                .println("📦 Médicament reçu - CodeCIS: " + m.getCodeCip13() + ", Quantité: " + m.getQuantite()));
        return venteService.createVente(request);
    }

    /**
     * Récupère une vente par son identifiant.
     *
     * @param id Identifiant de la vente.
     * @return La vente correspondante.
     */
    @GetMapping("/{id}")
    public VenteResponse getVente(@PathVariable UUID id) {
        return venteService.getById(id);
    }

    /**
     * Récupère toutes les ventes.
     *
     * @return Liste de toutes les ventes.
     */
    @GetMapping
    public List<VenteResponse> getAllVentes() {
        return venteService.getAll();
    }

    /**
     * Supprime une vente par son identifiant.
     *
     * @param id Identifiant de la vente à supprimer.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVente(@PathVariable UUID id) {
        venteService.delete(id);
    }

    /**
     * Récupère les ventes associées à un pharmacien spécifique.
     *
     * @param pharmacienId Identifiant du pharmacien.
     * @return Liste des ventes du pharmacien.
     */
    @GetMapping("/pharmacien/{pharmacienId}")
    public List<VenteResponse> getVentesByPharmacienId(@PathVariable UUID pharmacienId) {
        return venteService.getByPharmacienId(pharmacienId);
    }

    /**
     * Récupère les ventes associées à un client spécifique.
     *
     * @param clientId Identifiant du client.
     * @return Liste des ventes du client.
     */
    @GetMapping("/client/{clientId}")
    public List<VenteResponse> getVentesByClientId(@PathVariable UUID clientId) {
        return venteService.getByClientId(clientId);
    }

    /**
     * Met à jour une vente existante.
     *
     * @param id Identifiant de la vente à mettre à jour.
     * @param request Données de mise à jour de la vente.
     * @return ResponseEntity contenant la vente mise à jour ou un message d'erreur.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVente(@PathVariable UUID id, @RequestBody VenteUpdateRequest request) {
        try {
            VenteResponse response = venteService.updateVente(id, request);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode())
                    .body(Map.of("error", e.getReason()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Erreur serveur lors de la mise à jour de la vente: " + e.getMessage()));
        }
    }

}