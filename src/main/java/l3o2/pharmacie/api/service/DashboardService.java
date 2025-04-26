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
    StockMedicamentService stockMedicamentService;
    MedecinService medecinService;
    VenteService venteService;
    ClientService clientService;

    public DashboardResponse getDashboardStats() {


        System.out.println("yeah");

        DashboardResponse response2 = DashboardResponse.builder()
            .CA(0)
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

         
        double ca = Comptabilite.calculCA(venteService.getAll());
        //double ca = Comptabilite.calculCA(List.of());
        System.out.println("CA : " + ca);

        int nbEmployes = employeService.getAllEmployes().size();
        System.out.println("nb employes : " + nbEmployes);

        int nbClients = clientService.getAllClients().size();
        System.out.println("nb clients : " + nbClients);

        int nbMedecins = medecinService.getAllMedecins().size();
        System.out.println("nb medecins : " + nbMedecins);

        int nbMedicaments = stockMedicamentService.getMedicamentsQuantiteSuperieureOuEgale(1).size();
        System.out.println("nb medicaments : " + nbMedicaments);

        int nbMedicamentsRuptureStock = stockMedicamentService.getMedicamentsSeuilAlerte(0).size();
        System.out.println("nb medicaments rupture : " + nbMedicamentsRuptureStock);
        int nbMedicamentsPerimes = stockMedicamentService.getMedicamentsPerimes().size();
        System.out.println("nb medicaments perimees : " + nbMedicamentsPerimes);
        int nbMedicamentsAlerte = stockMedicamentService.getMedicamentsSeuilAlerte(10).size();
        System.out.println("nb medicaments alerte: " + nbMedicamentsAlerte);
        int nbMedicamentsAlerteBientotPerimee = stockMedicamentService.getMedicamentsAlerteBientotPerimee(LocalDate.now(),LocalDate.now().plusDays(30)).size();
        System.out.println("nb medicaments bientot perim : " + nbMedicamentsAlerteBientotPerimee);

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
