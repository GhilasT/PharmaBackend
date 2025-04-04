package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.PreparateurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.PreparateurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.PreparateurResponse;
import l3o2.pharmacie.api.service.PreparateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des préparateurs en pharmacie.
 */
@RestController
@RequestMapping("/api/preparateurs")
@RequiredArgsConstructor
public class PreparateurController {

    private final PreparateurService preparateurService;

    /**
     * Création d'un préparateur.
     * @param request Données du préparateur.
     * @return Le préparateur créé.
     */
    @PostMapping
    public ResponseEntity<PreparateurResponse> createPreparateur(@Valid @RequestBody PreparateurCreateRequest request) {
        return ResponseEntity.ok(preparateurService.createPreparateur(request));
    }

    /**
     * Récupère tous les préparateurs.
     * @return Liste des préparateurs.
     */
    @GetMapping
    public ResponseEntity<List<PreparateurResponse>> getAllPreparateurs() {
        return ResponseEntity.ok(preparateurService.getAllPreparateurs());
    }

    /**
     * Récupère un préparateur par son ID.
     * @param id ID du préparateur.
     * @return Le préparateur trouvé.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PreparateurResponse> getPreparateurById(@PathVariable UUID id) {
        return ResponseEntity.ok(preparateurService.getPreparateurById(id));
    }

    /**
     * Suppression d'un préparateur.
     * @param id ID du préparateur.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreparateur(@PathVariable UUID id) {
        preparateurService.deletePreparateur(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
public ResponseEntity<PreparateurResponse> updatePreparateur(
        @PathVariable UUID id,
        @Valid @RequestBody PreparateurUpdateRequest request) {
    PreparateurResponse response = preparateurService.updatePreparateur(id, request);
    return ResponseEntity.ok(response);
}

@GetMapping("/search")
public ResponseEntity<List<PreparateurResponse>> searchPreparateurs(
        @RequestParam String term) {
    List<PreparateurResponse> responses = preparateurService.searchPreparateurs(term);
    return ResponseEntity.ok(responses);
}
}