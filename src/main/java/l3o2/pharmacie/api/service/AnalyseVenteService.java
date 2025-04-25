package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.response.AnalyseVenteResponse;
import l3o2.pharmacie.api.model.dto.response.VenteResponse;
import l3o2.pharmacie.api.repository.VenteRepository;
import l3o2.pharmacie.api.util.Comptabilite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe pour la logique de la page Analyse des ventes
 * @author raphaelcharoze
 */
@Service
@RequiredArgsConstructor
public class AnalyseVenteService {

    private VenteService venteService;
    private CommandeService commandeService;

    public AnalyseVenteResponse getAnalyseVente() {

        Map<LocalDate, Integer> ventes = new HashMap<>();
        Map<LocalDate, Integer> commandes = new HashMap<>();
        Map<LocalDate, Double> CA = new HashMap<>();

        LocalDate date = LocalDate.from(LocalDate.now().atTime(12,0));

        for (int i = 0; i < 30; i++){
            date = date.minusDays(1);

            List<VenteResponse> listeVentes = venteService.getAllOrderByDate(date);

            ventes.putIfAbsent(date, listeVentes.size());
            commandes.putIfAbsent(date, commandeService.getAllOrderByDate(date).size());
            CA.putIfAbsent(date, Comptabilite.calculCA(listeVentes));
        }

        return AnalyseVenteResponse.builder()
                .ventesJournalier(ventes)
                .CAJournalier(CA)
                .commandesJournalier(commandes)

                .build();
    }
}
