package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.service.AnalyseVentesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Contrôleur REST pour l'analyse des ventes.
 * Fournit des endpoints pour obtenir des statistiques sur les ventes.
 */
@RestController
@RequestMapping("/api/analyse-ventes")
@RequiredArgsConstructor
public class AnalyseVentesController {

    private final AnalyseVentesService analyseVentesService;

    /**
     * Récupère le nombre de ventes par jour de la semaine en cours.
     * @return Une Map associant le nom du jour (en français) au nombre de ventes.
     */
    @GetMapping("/semaine")
    public Map<String, Integer> getVentesParSemaine() {
        return analyseVentesService.getVentesParJourSemaine();
    }

    /**
     * Récupère le nombre de ventes par jour du mois en cours.
     * @return Une Map associant la date (jour du mois) au nombre de ventes.
     */
    @GetMapping("/mois-jour")
    public Map<String, Integer> getVentesParJourMois() {
        return analyseVentesService.getVentesParJourMois();
    }

    /**
     * Récupère l'historique des ventes par mois sur un nombre de mois donné.
     * @param nbMois Le nombre de mois pour l'historique (par défaut 6).
     * @return Une Map associant le nom du mois (et année) au nombre de ventes.
     */
    @GetMapping("/historique")
    public Map<String, Integer> getVentesHistorique(
            @RequestParam(defaultValue = "6") int nbMois
    ) {
        return analyseVentesService.getVentesParMois(nbMois);
    }
}