package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.AdministrateurCreateRequest;
import l3o2.pharmacie.api.model.dto.request.AdministrateurUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.AdministrateurResponse;
import l3o2.pharmacie.api.service.AdministrateurService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

/**
 * Contrôleur REST pour la gestion des administrateurs.
 * Permet de créer, récupérer, mettre à jour et supprimer des administrateurs.
 */
@RestController
@RequestMapping("/api/administrateurs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdministrateurController {

    private final AdministrateurService administrateurService;

    /**
     * Crée un nouvel administrateur.
     * @param request Les informations de l'administrateur à créer.
     * @return Une ResponseEntity contenant l'AdministrateurResponse créé et le statut HTTP CREATED.
     */
    @PostMapping
    public ResponseEntity<AdministrateurResponse> createAdministrateur(
            @Valid @RequestBody AdministrateurCreateRequest request) {
        AdministrateurResponse response = administrateurService.createAdministrateur(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupère un administrateur par son matricule (identifiant).
     * @param id Le matricule de l'administrateur.
     * @return Une ResponseEntity contenant l'AdministrateurResponse trouvé et le statut HTTP OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdministrateurResponse> getAdministrateurByMatricule(@PathVariable String id) {
        AdministrateurResponse admin = administrateurService.getAdministrateurByMatricule(id);
        return ResponseEntity.ok(admin);
    }

    /**
     * Récupère la liste de tous les administrateurs.
     * @return Une ResponseEntity contenant une liste d'AdministrateurResponse et le statut HTTP OK.
     */
    @GetMapping
    public ResponseEntity<List<AdministrateurResponse>> getAllAdministrateurs() {
        return ResponseEntity.ok(administrateurService.getAllAdministrateurs());
    }
    
    /**
     * Met à jour un administrateur existant.
     * @param id L'identifiant UUID de l'administrateur à mettre à jour.
     * @param request Les données de mise à jour de l'administrateur.
     * @return Une ResponseEntity contenant l'AdministrateurResponse mis à jour et le statut HTTP OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdministrateurResponse> updateAdministrateur(
        @PathVariable UUID id,
        @Valid @RequestBody AdministrateurUpdateRequest request) {
        AdministrateurResponse updatedAdmin = administrateurService.updateAdministrateur(id, request);
        return ResponseEntity.ok(updatedAdmin);
    }
    

    /**
     * Supprime un administrateur par son ID.
     * @param id L'identifiant UUID de l'administrateur à supprimer.
     * @return Une ResponseEntity avec le statut HTTP NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrateur(@PathVariable("id") UUID id) {
        administrateurService.deleteAdministrateur(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Recherche des administrateurs par nom ou prénom.
     * @param query Le terme de recherche (nom ou prénom).
     * @return Une ResponseEntity contenant une liste d'AdministrateurResponse correspondant à la recherche et le statut HTTP OK.
     */
    @GetMapping("/search")
    public ResponseEntity<List<AdministrateurResponse>> getAdministrateursByNomOuPrenom(@RequestParam String query) {
        return ResponseEntity.ok(administrateurService.getAdministrateursByNomOuPrenom(query));
    }
}