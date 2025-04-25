package l3o2.pharmacie.api.controller;

import jakarta.validation.Valid;
import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
import l3o2.pharmacie.api.model.dto.response.DashboardResponse;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;
import l3o2.pharmacie.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller pour gérer les requêtes liées au tableau de bord.
 * Il est responsable de la gestion des données affichées sur le tableau de bord.
 * Il utilise le service DashboardService pour interagir avec les données.
 * Author : raphaelcharoze
 */

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }


}
