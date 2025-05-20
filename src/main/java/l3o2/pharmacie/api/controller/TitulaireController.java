package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.TitulaireCreateRequest;
import l3o2.pharmacie.api.model.dto.response.TitulaireResponse;
import l3o2.pharmacie.api.service.TitulaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Contrôleur REST pour gérer le titulaire de la pharmacie.
 * Fournit des endpoints pour la création, la récupération et la suppression du titulaire.
 */
@RestController
@RequestMapping("/api/titulaires")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TitulaireController {

    private final TitulaireService titulaireService;

    /**
     * Crée un nouveau titulaire.
     *
     * @param request Données pour la création du titulaire.
     * @return ResponseEntity contenant le titulaire créé et le statut HTTP CREATED.
     */
    @PostMapping
    public ResponseEntity<TitulaireResponse> createTitulaire(@RequestBody TitulaireCreateRequest request) {
        return new ResponseEntity<>(titulaireService.createTitulaire(request), HttpStatus.CREATED);
    }

    /**
     * Récupère le titulaire actuel de la pharmacie.
     *
     * @return ResponseEntity contenant les détails du titulaire et le statut HTTP OK.
     */
    @GetMapping
    public ResponseEntity<TitulaireResponse> getTitulaire() {
        return ResponseEntity.ok(titulaireService.getTitulaire());
    }

    /**
     * Supprime un titulaire par son identifiant.
     *
     * @param id Identifiant du titulaire à supprimer.
     * @return ResponseEntity avec le statut HTTP NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTitulaire(@PathVariable UUID id) {
        titulaireService.deleteTitulaire(id);
        return ResponseEntity.noContent().build();
    }
}