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
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PreparateurController {

    private final PreparateurService preparateurService;

    /**
     * Crée un nouveau préparateur en pharmacie.
     *
     * @param request Données pour la création du préparateur.
     * @return ResponseEntity contenant le préparateur créé et le statut HTTP OK.
     */
    @PostMapping
    public ResponseEntity<PreparateurResponse> createPreparateur(@Valid @RequestBody PreparateurCreateRequest request) {
        return ResponseEntity.ok(preparateurService.createPreparateur(request));
    }

    /**
     * Récupère la liste de tous les préparateurs.
     *
     * @return ResponseEntity contenant la liste des préparateurs et le statut HTTP OK.
     */
    @GetMapping
    public ResponseEntity<List<PreparateurResponse>> getAllPreparateurs() {
        return ResponseEntity.ok(preparateurService.getAllPreparateurs());
    }

    /**
     * Récupère un préparateur par son identifiant unique.
     *
     * @param id Identifiant UUID du préparateur.
     * @return ResponseEntity contenant le préparateur trouvé et le statut HTTP OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PreparateurResponse> getPreparateurById(@PathVariable UUID id) {
        return ResponseEntity.ok(preparateurService.getPreparateurById(id));
    }

    /**
     * Supprime un préparateur par son identifiant.
     *
     * @param id Identifiant UUID du préparateur à supprimer.
     * @return ResponseEntity avec le statut HTTP NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreparateur(@PathVariable UUID id) {
        preparateurService.deletePreparateur(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Met à jour les informations d'un préparateur existant.
     *
     * @param id Identifiant UUID du préparateur à mettre à jour.
     * @param request Données de mise à jour du préparateur.
     * @return ResponseEntity contenant le préparateur mis à jour et le statut HTTP OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PreparateurResponse> updatePreparateur(
            @PathVariable UUID id,
            @Valid @RequestBody PreparateurUpdateRequest request) {
        PreparateurResponse response = preparateurService.updatePreparateur(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Recherche des préparateurs en fonction d'un terme de recherche (nom, prénom, etc.).
     *
     * @param term Terme de recherche.
     * @return ResponseEntity contenant la liste des préparateurs correspondants et le statut HTTP OK.
     */
    @GetMapping("/search")
    public ResponseEntity<List<PreparateurResponse>> searchPreparateurs(
            @RequestParam String term) {
        List<PreparateurResponse> responses = preparateurService.searchPreparateurs(term);
        return ResponseEntity.ok(responses);
    }
}