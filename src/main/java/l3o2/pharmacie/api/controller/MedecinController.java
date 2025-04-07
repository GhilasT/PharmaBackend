package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;
import l3o2.pharmacie.api.service.MedecinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

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
}