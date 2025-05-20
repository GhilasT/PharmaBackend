package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.FournisseurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.FournisseurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.FournisseurResponse;
import l3o2.pharmacie.api.service.FournisseurService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Contrôleur REST pour la gestion des fournisseurs.
 * Fournit des endpoints pour créer, récupérer, mettre à jour, supprimer et rechercher des fournisseurs.
 */
@RestController
@RequestMapping("/api/fournisseurs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class FournisseurController {

    private final FournisseurService fournisseurService;

    /**
     * Crée un nouveau fournisseur.
     *
     * @param request Données pour la création du fournisseur.
     * @return ResponseEntity contenant le fournisseur créé et le statut HTTP CREATED.
     */
    @PostMapping
    public ResponseEntity<FournisseurResponse> createFournisseur(
            @Valid @RequestBody FournisseurCreateRequest request) {
        FournisseurResponse response = fournisseurService.createFournisseur(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupère la liste de tous les fournisseurs.
     *
     * @return ResponseEntity contenant la liste des fournisseurs et le statut HTTP OK.
     */
    @GetMapping
    public ResponseEntity<List<FournisseurResponse>> getAllFournisseurs() {
        return ResponseEntity.ok(fournisseurService.getAllFournisseurs());
    }

    /**
     * Récupère un fournisseur par son identifiant unique.
     *
     * @param id Identifiant UUID du fournisseur.
     * @return ResponseEntity contenant le fournisseur trouvé et le statut HTTP OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FournisseurResponse> getFournisseurById(@PathVariable UUID id) {
        FournisseurResponse fournisseur = fournisseurService.getFournisseurById(id);
        return ResponseEntity.ok(fournisseur);
    }

    /**
     * Récupère un fournisseur par son adresse e-mail.
     *
     * @param email Adresse e-mail du fournisseur.
     * @return ResponseEntity contenant le fournisseur trouvé et le statut HTTP OK.
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<FournisseurResponse> getFournisseurByEmail(@PathVariable String email) {
        FournisseurResponse fournisseur = fournisseurService.getFournisseurByEmail(email);
        return ResponseEntity.ok(fournisseur);
    }

    /**
     * Récupère un fournisseur par son numéro de téléphone.
     *
     * @param telephone Numéro de téléphone du fournisseur.
     * @return ResponseEntity contenant le fournisseur trouvé et le statut HTTP OK.
     */
    @GetMapping("/telephone/{telephone}")
    public ResponseEntity<FournisseurResponse> getFournisseurByTelephone(@PathVariable String telephone) {
        FournisseurResponse fournisseur = fournisseurService.getFournisseurByTelephone(telephone);
        return ResponseEntity.ok(fournisseur);
    }

    /**
     * Met à jour les informations d'un fournisseur existant.
     *
     * @param id Identifiant UUID du fournisseur à mettre à jour.
     * @param request Données de mise à jour du fournisseur.
     * @return ResponseEntity contenant le fournisseur mis à jour et le statut HTTP OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FournisseurResponse> updateFournisseur(
            @PathVariable UUID id,
            @Valid @RequestBody FournisseurUpdateRequest request) {
        FournisseurResponse response = fournisseurService.updateFournisseur(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Supprime un fournisseur par son identifiant.
     *
     * @param id Identifiant UUID du fournisseur à supprimer.
     * @return ResponseEntity avec le statut HTTP NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFournisseur(@PathVariable UUID id) {
        fournisseurService.deleteFournisseur(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Recherche des fournisseurs en fonction d'un critère (nom, email, etc.).
     *
     * @param query Critère de recherche.
     * @return ResponseEntity contenant la liste des fournisseurs correspondants et le statut HTTP OK.
     */
    @GetMapping("/search")
    public ResponseEntity<List<FournisseurResponse>> searchFournisseurs(
            @RequestParam(name = "q") String query) {
        return ResponseEntity.ok(fournisseurService.searchFournisseurs(query));
    }
}