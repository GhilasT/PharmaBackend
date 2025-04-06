package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.ApprentiCreateRequest;
import l3o2.pharmacie.api.model.dto.response.ApprentiResponse;
import l3o2.pharmacie.api.service.ApprentiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des apprentis en pharmacie.
 * Permet de créer, récupérer et supprimer des apprentis.
 */
@RestController
@RequestMapping("/api/apprentis")
@RequiredArgsConstructor
public class ApprentiController {

    private final ApprentiService apprentiService;

    /**
     * Crée un nouvel apprenti.
     * @param request Données du nouvel apprenti.
     * @return Réponse contenant les informations de l'apprenti créé.
     */
    @PostMapping
    public ResponseEntity<ApprentiResponse> createApprenti(@Valid @RequestBody ApprentiCreateRequest request) {
        ApprentiResponse apprenti = apprentiService.createApprenti(request);
        return ResponseEntity.ok(apprenti);
    }

    /**
     * Récupère la liste de tous les apprentis.
     * @return Liste des apprentis enregistrés.
     */
    @GetMapping
    public ResponseEntity<List<ApprentiResponse>> getAllApprentis() {
        List<ApprentiResponse> apprentis = apprentiService.getAllApprentis();
        return ResponseEntity.ok(apprentis);
    }

    /**
     * Récupère un apprenti par son ID.
     * @param id Identifiant UUID de l'apprenti.
     * @return Apprenti correspondant à l'ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApprentiResponse> getApprentiById(@PathVariable UUID id) {
        ApprentiResponse apprenti = apprentiService.getApprentiById(id);
        return ResponseEntity.ok(apprenti);
    }

    /**
     * Supprime un apprenti par son ID.
     * @param id Identifiant UUID de l'apprenti à supprimer.
     * @return Message de confirmation.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteApprenti(@PathVariable UUID id) {
        apprentiService.deleteApprenti(id);
        return ResponseEntity.ok("Apprenti supprimé avec succès.");
    }
}