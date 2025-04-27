package l3o2.pharmacie.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Représente une réponse pour le tableau de bord.
 * Contient des informations statistiques sur la pharmacie.
 * Author : raphaelcharoze
 */

@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    private double CA;
    private double benefices;
    private long nbEmployes;
    private long nbClients;
    private long nbMedecins;
    private long nbMedicaments;
    private long nbMedicamentsRuptureStock;
    private long nbMedicamentsPerimes;
    private long nbMedicamentsAlerte;
    private long nbMedicamentsAlerteBientotPerimee;
}
