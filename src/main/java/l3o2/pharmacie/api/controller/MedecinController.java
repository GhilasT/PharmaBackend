package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
import l3o2.pharmacie.api.model.dto.request.MedecinUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;
import l3o2.pharmacie.api.service.MedecinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/medecins")
@RequiredArgsConstructor
public class MedecinController {

    private final MedecinService medecinService;

    @PostMapping
    public ResponseEntity<MedecinResponse> createMedecin(@Valid @RequestBody MedecinCreateRequest request) {
        return ResponseEntity.ok(medecinService.createMedecin(request));
    }

    @GetMapping
    public ResponseEntity<List<MedecinResponse>> getAllMedecins() {
        return ResponseEntity.ok(medecinService.getAllMedecins());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedecinResponse> getMedecinById(@PathVariable UUID id) {
        MedecinResponse medecin = medecinService.getMedecinById(id);
        return ResponseEntity.ok(medecin);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedecinResponse> updateMedecin(
            @PathVariable UUID id,
            @Valid @RequestBody MedecinUpdateRequest request) {
        MedecinResponse response = medecinService.updateMedecin(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedecin(@PathVariable UUID id) {
        medecinService.deleteMedecin(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/search")
public ResponseEntity<List<MedecinResponse>> searchMedecins(
        @RequestParam(name = "q") String query) {
    return ResponseEntity.ok(medecinService.getMedecinsByNomOuPrenom(query));
}
}