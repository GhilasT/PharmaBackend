package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.response.DashboardResponse;
import l3o2.pharmacie.api.model.dto.response.EmployeResponse;
import l3o2.pharmacie.api.util.Comptabilite;
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
@RequiredArgsConstructor
public class DashboardService {

    EmployeService employeService;
    StockMedicamentService stockMedicamentService;
    MedecinService medecinService;
    VenteService venteService;
    ClientService clientService;

    public DashboardResponse getDashboardStats() {

        DashboardResponse response = DashboardResponse.builder()
            .CA(0.00)
            .benefices(0.00)
            .nbEmployes(0)
            .nbClients(0)
            .nbMedecins(0)
            .nbMedicaments(0)
            .nbMedicamentsRuptureStock(0)
            .nbMedicamentsPerimes(0)
            .nbMedicamentsAlerte(0)
            .nbMedicamentsAlerteBientotPerimee(0)
            .build();

            /*
             * DashboardResponse response = DashboardResponse.builder()
            .CA(Comptabilite.calculCA(venteService.getAll()))
            .benefices(0.00)
            .nbEmployes(employeService.getAllEmployes().size())
            .nbClients(clientService.getAllClients().size())
            .nbMedecins(medecinService.getAllMedecins().size())
            .nbMedicaments(stockMedicamentService.getMedicamentsQuantiteSuperieureOuEgale(1).size())
            .nbMedicamentsRuptureStock(stockMedicamentService.getMedicamentsSeuilAlerte(0).size())
            .nbMedicamentsPerimes(stockMedicamentService.getMedicamentsPerimes().size())
            .nbMedicamentsAlerte(stockMedicamentService.getMedicamentsSeuilAlerte(10).size())
            .nbMedicamentsAlerteBientotPerimee(stockMedicamentService.getMedicamentsAlerteBientotPerimee(LocalDate.now(),LocalDate.now().plusDays(30)).size())
            .build();
             */
 

        return response;
    }
}
