package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.response.DashboardResponse;
import l3o2.pharmacie.api.util.Comptabilite;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service pour gérer les opérations liées au tableau de bord.
 * Il contient des méthodes pour récupérer les données statistiques de la pharmacie.
 * Author : raphaelcharoze
 */

@Service
@RequiredArgsConstructor
public class DashboardService {

    EmployeService employeService;
    MedicamentService medicamentService;
    MedecinService medecinService;
    VenteService venteService;
    ClientService clientService;

    public DashboardResponse getDashboardStats() {
        return DashboardResponse.builder()
                .CA(Comptabilite.calculCA(venteService.getAll()))
                .benefices(0.00)
                .nbEmployes(employeService.getAllEmployes().size())
                .nbClients(clientService.getAllClients().size())
                .nbMedecins(medecinService.getAllMedecins().size())
                .nbMedicaments(0)
                .nbMedicamentsRuptureStock(0)
                .nbMedicamentsPerimes(0)
                .nbMedicamentsAlerte(0)
                .nbMedicamentsAlerteBientotPerimee(0)
                .build();
    }



}
