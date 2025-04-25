package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.service.AnalyseVentesService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/analyse-ventes")
@RequiredArgsConstructor
public class AnalyseVentesController {

    private final AnalyseVentesService analyseVentesService;

    @GetMapping("/semaine")
    public Map<String, Integer> getVentesParSemaine() {
        return analyseVentesService.getVentesParJourSemaine();
    }

    @GetMapping("/mois-jour")
    public Map<String, Integer> getVentesParJourMois() {
        return analyseVentesService.getVentesParJourMois();
    }

    @GetMapping("/historique")
    public Map<String, Integer> getVentesHistorique(
            @RequestParam(defaultValue = "6") int nbMois
    ) {
        return analyseVentesService.getVentesParMois(nbMois);
    }
}