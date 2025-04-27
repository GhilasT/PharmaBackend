package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.response.DashboardResponse;
import l3o2.pharmacie.api.model.dto.response.EmployeResponse;
import l3o2.pharmacie.api.util.Comptabilite;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Service pour gérer les opérations liées au tableau de bord.
 * Il contient des méthodes pour récupérer les données statistiques de la pharmacie.
 * Author : raphaelcharoze
 */

@Service
@AllArgsConstructor
public class DashboardService {

    EmployeService employeService;
    StockForDashboardService stockMedicamentService;
    MedecinService medecinService;
    VenteService venteService;
    ClientService clientService;

    public DashboardResponse getDashboardStats() {
         
        double ca = Comptabilite.calculCA(venteService.getAll());
        int nbEmployes = employeService.getAllEmployes().size();
        int nbClients = clientService.getAllClients().size();
        int nbMedecins = medecinService.getAllMedecins().size();
        int nbMedicaments = stockMedicamentService.getMedicamentsQuantiteSuperieureOuEgale(1).size();
        int nbMedicamentsRuptureStock = stockMedicamentService.getMedicamentsSeuilAlerte(0).size();
        int nbMedicamentsPerimes = stockMedicamentService.getMedicamentsPerimes().size();
        int nbMedicamentsAlerte = stockMedicamentService.getMedicamentsSeuilAlerte(10).size();
        int nbMedicamentsAlerteBientotPerimee = stockMedicamentService.getMedicamentsAlerteBientotPerimee(LocalDate.now(),LocalDate.now().plusDays(30)).size();

        DashboardResponse response = DashboardResponse.builder()
            .CA(ca)
            .benefices(0.00)
            .nbEmployes(nbEmployes)
            .nbClients(nbClients)
            .nbMedecins(nbMedecins)
            .nbMedicaments(nbMedicaments)
            .nbMedicamentsRuptureStock(nbMedicamentsRuptureStock)
            .nbMedicamentsPerimes(nbMedicamentsPerimes)
            .nbMedicamentsAlerte(nbMedicamentsAlerte)
            .nbMedicamentsAlerteBientotPerimee(nbMedicamentsAlerteBientotPerimee)
            .build();
            
        return response;
    }
}
