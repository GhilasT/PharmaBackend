package l3o2.pharmacie.api.controller;

import jakarta.validation.Valid;
import l3o2.pharmacie.api.model.dto.request.AdministrateurCreateRequest;
import l3o2.pharmacie.api.model.dto.response.AdministrateurResponse;
import l3o2.pharmacie.api.model.dto.response.AnalyseVenteResponse;
import l3o2.pharmacie.api.service.AnalyseVenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Classe pour le controller de la page Analyse des ventes
 * @author raphaelcharoze
 */
@RestController
@RequestMapping("/api/analyse-ventes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AnalyseVenteController {

    private final AnalyseVenteService analyseVenteService;

    @GetMapping
    public ResponseEntity<AnalyseVenteResponse> getAnalyseVentes() {
        AnalyseVenteResponse response = analyseVenteService.getAnalyseVente();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
