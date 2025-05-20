package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.response.DashboardResponse;
import l3o2.pharmacie.api.model.dto.response.EmployeResponse;
import l3o2.pharmacie.api.repository.VenteRepository;
import l3o2.pharmacie.api.util.Comptabilite;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

/**
 * Service pour gérer les opérations liées au tableau de bord.
 * Il contient des méthodes pour récupérer les données statistiques agrégées de la pharmacie,
 * telles que le chiffre d'affaires, le nombre d'employés, de clients, de médecins,
 * et diverses statistiques sur les médicaments (en stock, en rupture, périmés, en alerte).
 * @author raphaelcharoze
 */

@Service
@AllArgsConstructor
public class DashboardService {

    EmployeService employeService;
    StockForDashboardService stockMedicamentService;
    MedecinService medecinService;
    VenteService venteService;
    ClientService clientService;
    VenteRepository venteRepository;

    /**
     * Récupère les statistiques globales pour le tableau de bord.
     * Calcule le chiffre d'affaires total, le nombre d'employés, de clients, de médecins,
     * ainsi que des indicateurs sur l'état du stock de médicaments (quantité, rupture, péremption, alertes).
     *
     * @return Un objet {@link DashboardResponse} contenant toutes les statistiques agrégées.
     */
    public DashboardResponse getDashboardStats() {
        double ca = venteRepository.sumTotalCA();
        long nbEmployes = employeService.countAllEmployes();
        long nbClients = clientService.countAllClients();
        long nbMedecins = medecinService.countAllMedecins();
        long nbMedicaments = stockMedicamentService.countMedicamentsQuantiteSuperieureOuEgale(1);
        long nbMedicamentsRuptureStock = stockMedicamentService.countMedicamentsSeuilAlerte(0);
        long nbMedicamentsPerimes = stockMedicamentService.countMedicamentsPerimes();
        long nbMedicamentsAlerte = stockMedicamentService.countMedicamentsSeuilAlerte(10);
        long nbMedicamentsAlerteBientotPerimee = stockMedicamentService.countMedicamentsAlerteBientotPerimee(
            LocalDate.now(), 
            LocalDate.now().plusDays(30)
        );
    
        return DashboardResponse.builder()
            .CA(ca)
            .nbEmployes(nbEmployes)
            .nbClients(nbClients)
            .nbMedecins(nbMedecins)
            .nbMedicaments(nbMedicaments)
            .nbMedicamentsRuptureStock(nbMedicamentsRuptureStock)
            .nbMedicamentsPerimes(nbMedicamentsPerimes)
            .nbMedicamentsAlerte(nbMedicamentsAlerte)
            .nbMedicamentsAlerteBientotPerimee(nbMedicamentsAlerteBientotPerimee)
            .build();
    }
}
