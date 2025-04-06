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
 * Contrôleur REST pour gérer les titulaires en pharmacie.
 * Fournit des endpoints pour la création, récupération et suppression.
 */
@RestController
@RequestMapping("/api/titulaires")
@RequiredArgsConstructor
public class TitulaireController {

    private final TitulaireService titulaireService;

    /**
     * Endpoint pour créer un nouveau titulaire.
     * @param request Données de création du titulaire.
     * @return TitulaireResponse contenant les détails du titulaire créé.
     */
    @PostMapping
    public ResponseEntity<TitulaireResponse> createTitulaire(@RequestBody TitulaireCreateRequest request) {
        return new ResponseEntity<>(titulaireService.createTitulaire(request), HttpStatus.CREATED);
    }

    /**
     * Endpoint pour récupérer le titulaire en pharmacie.
     * @return Détails du titulaire.
     */
    @GetMapping
    public ResponseEntity<TitulaireResponse> getTitulaire() {
        return ResponseEntity.ok(titulaireService.getTitulaire());
    }

    /**
     * Endpoint pour supprimer un titulaire.
     * @param id Identifiant du titulaire.
     * @return Confirmation de suppression.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTitulaire(@PathVariable UUID id) {
        titulaireService.deleteTitulaire(id);
        return ResponseEntity.noContent().build();
    }
}