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

@RestController
@RequestMapping("/api/administrateurs")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdministrateurController {

    private final AdministrateurService administrateurService;

    @PostMapping
    public ResponseEntity<AdministrateurResponse> createAdministrateur(
            @Valid @RequestBody AdministrateurCreateRequest request) {
        AdministrateurResponse response = administrateurService.createAdministrateur(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdministrateurResponse> getAdministrateurByMatricule(@PathVariable String id) {
        AdministrateurResponse admin = administrateurService.getAdministrateurByMatricule(id);
        return ResponseEntity.ok(admin);
    }

    @GetMapping
    public ResponseEntity<List<AdministrateurResponse>> getAllAdministrateurs() {
        return ResponseEntity.ok(administrateurService.getAllAdministrateurs());
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<AdministrateurResponse> updateAdministrateur(
        @PathVariable UUID id,
        @Valid @RequestBody AdministrateurUpdateRequest request) {
        AdministrateurResponse updatedAdmin = administrateurService.updateAdministrateur(id, request);
        return ResponseEntity.ok(updatedAdmin);
    }
    

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdministrateur(@PathVariable("id") UUID id) {
        administrateurService.deleteAdministrateur(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
    public ResponseEntity<List<AdministrateurResponse>> getAdministrateursByNomOuPrenom(@RequestParam String query) {
        return ResponseEntity.ok(administrateurService.getAdministrateursByNomOuPrenom(query));
    }
}