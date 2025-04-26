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
    private int nbEmployes;
    private int nbClients;
    private int nbMedecins;
    private int nbMedicaments;
    private int nbMedicamentsRuptureStock;
    private int nbMedicamentsPerimes;
    private int nbMedicamentsAlerte;
    private int nbMedicamentsAlerteBientotPerimee;
}
