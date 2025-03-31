package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.FournisseurCreateRequest;
import l3o2.pharmacie.api.model.dto.response.FournisseurResponse;
import l3o2.pharmacie.api.service.FournisseurService;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/fournisseurs")
@RequiredArgsConstructor
public class FournisseurController {

    private final FournisseurService fournisseurService;

    @PostMapping
    public ResponseEntity<FournisseurResponse> createFournisseur(
            @Valid @RequestBody FournisseurCreateRequest request) {
        FournisseurResponse response = fournisseurService.createFournisseur(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<FournisseurResponse>> getAllFournisseurs() {
        return ResponseEntity.ok(fournisseurService.getAllFournisseurs());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FournisseurResponse> getFournisseurById(@PathVariable UUID id) {
        FournisseurResponse fournisseur = fournisseurService.getFournisseurById(id);
        return ResponseEntity.ok(fournisseur);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<FournisseurResponse> getFournisseurByEmail(@PathVariable String email) {
        FournisseurResponse fournisseur = fournisseurService.getFournisseurByEmail(email);
        return ResponseEntity.ok(fournisseur);
    }

    @GetMapping("/telephone/{telephone}")
    public ResponseEntity<FournisseurResponse> getFournisseurByTelephone(@PathVariable String telephone) {
        FournisseurResponse fournisseur = fournisseurService.getFournisseurByTelephone(telephone);
        return ResponseEntity.ok(fournisseur);
    }
}