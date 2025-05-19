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

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VenteController {

    private final VenteService venteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VenteResponse createVente(@RequestBody VenteCreateRequest request) {
        System.out.println("Vente reçue : " + request);
        request.getMedicaments().forEach(m -> System.out
                .println("📦 Médicament reçu - CodeCIS: " + m.getCodeCip13() + ", Quantité: " + m.getQuantite()));
        return venteService.createVente(request);
    }

    @GetMapping("/{id}")
    public VenteResponse getVente(@PathVariable UUID id) {
        return venteService.getById(id);
    }

    @GetMapping
    public List<VenteResponse> getAllVentes() {
        return venteService.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVente(@PathVariable UUID id) {
        venteService.delete(id);
    }

    @GetMapping("/pharmacien/{pharmacienId}")
    public List<VenteResponse> getVentesByPharmacienId(@PathVariable UUID pharmacienId) {
        return venteService.getByPharmacienId(pharmacienId);
    }

    @GetMapping("/client/{clientId}")
    public List<VenteResponse> getVentesByClientId(@PathVariable UUID clientId) {
        return venteService.getByClientId(clientId);
    }

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