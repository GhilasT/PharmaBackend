package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointCreateRequest;
import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.PharmacienAdjointResponse;
import l3o2.pharmacie.api.service.PharmacienAdjointService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

/**
 * Contrôleur REST pour la gestion des pharmaciens adjoints.
 */
@RestController
@RequestMapping("/api/pharmaciens-adjoints")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PharmacienAdjointController {

    private final PharmacienAdjointService pharmacienAdjointService;

    /**
     * Crée un nouveau pharmacien adjoint.
     *
     * @param request Données pour la création du pharmacien adjoint.
     * @return ResponseEntity contenant le pharmacien adjoint créé et le statut HTTP CREATED.
     */
    @PostMapping
    public ResponseEntity<PharmacienAdjointResponse> createPharmacienAdjoint(
            @Valid @RequestBody PharmacienAdjointCreateRequest request) {
        PharmacienAdjointResponse response = pharmacienAdjointService.createPharmacienAdjoint(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Récupère la liste de tous les pharmaciens adjoints.
     *
     * @return ResponseEntity contenant la liste des pharmaciens adjoints et le statut HTTP OK.
     */
    @GetMapping
    public ResponseEntity<List<PharmacienAdjointResponse>> getAllPharmaciensAdjoints() {
        List<PharmacienAdjointResponse> responses = pharmacienAdjointService.getAllPharmaciensAdjoints();
        return ResponseEntity.ok(responses);
    }

    /**
     * Supprime un pharmacien adjoint par son identifiant.
     *
     * @param id Identifiant UUID du pharmacien adjoint à supprimer.
     * @return ResponseEntity avec le statut HTTP NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePharmacienAdjoint(@PathVariable UUID id) {
        pharmacienAdjointService.deletePharmacienAdjoint(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Met à jour les informations d'un pharmacien adjoint existant.
     *
     * @param id Identifiant UUID du pharmacien adjoint à mettre à jour.
     * @param request Données de mise à jour du pharmacien adjoint.
     * @return ResponseEntity contenant le pharmacien adjoint mis à jour et le statut HTTP OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PharmacienAdjointResponse> updatePharmacienAdjoint(
            @PathVariable UUID id,
            @Valid @RequestBody PharmacienAdjointUpdateRequest request) {
        PharmacienAdjointResponse response = pharmacienAdjointService.updatePharmacienAdjoint(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Recherche des pharmaciens adjoints en fonction d'un terme de recherche.
     *
     * @param term Terme de recherche.
     * @return ResponseEntity contenant la liste des pharmaciens adjoints correspondants et le statut HTTP OK.
     */
    @GetMapping("/search")
    public ResponseEntity<List<PharmacienAdjointResponse>> searchPharmaciensAdjoints(
            @RequestParam String term) {
        List<PharmacienAdjointResponse> responses = pharmacienAdjointService.searchPharmaciensAdjoints(term);
        return ResponseEntity.ok(responses);
    }

    /**
     * Récupère un pharmacien adjoint par son identifiant unique.
     *
     * @param id Identifiant UUID du pharmacien adjoint.
     * @return ResponseEntity contenant le pharmacien adjoint trouvé et le statut HTTP OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PharmacienAdjointResponse> getPharmacienAdjointById(@PathVariable UUID id) {
        PharmacienAdjointResponse response = pharmacienAdjointService.getPharmacienAdjointById(id);
        return ResponseEntity.ok(response);
    }

}