package l3o2.pharmacie.api.controller;

import jakarta.validation.Valid;
import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
import l3o2.pharmacie.api.model.dto.response.DashboardResponse;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;
import l3o2.pharmacie.api.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour gérer les requêtes liées au tableau de bord.
 * Il est responsable de la gestion des données affichées sur le tableau de bord.
 * Il utilise le service DashboardService pour interagir avec les données.
 * @author raphaelcharoze
 */

@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    /**
     * Récupère les statistiques du tableau de bord.
     * @return Une ResponseEntity contenant les données du tableau de bord et le statut HTTP OK.
     */
    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard() {
        DashboardResponse dashboardResponse = dashboardService.getDashboardStats();
        return new ResponseEntity<>(dashboardResponse, HttpStatus.OK);
    }


}
