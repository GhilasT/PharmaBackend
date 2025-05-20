package l3o2.pharmacie.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * DTO représentant une réponse pour le tableau de bord.
 * Contient des informations statistiques sur la pharmacie.
 * Auteur : raphaelcharoze
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class DashboardResponse {

    /** Le chiffre d'affaires. */
    private double CA;
    /** Les bénéfices réalisés. */
    private double benefices;
    /** Le nombre total d'employés. */
    private long nbEmployes;
    /** Le nombre total de clients. */
    private long nbClients;
    /** Le nombre total de médecins enregistrés. */
    private long nbMedecins;
    /** Le nombre total de types de médicaments en stock. */
    private long nbMedicaments;
    /** Le nombre de médicaments en rupture de stock. */
    private long nbMedicamentsRuptureStock;
    /** Le nombre de médicaments périmés. */
    private long nbMedicamentsPerimes;
    /** Le nombre de médicaments dont le stock est en alerte (seuil bas atteint). */
    private long nbMedicamentsAlerte;
    /** Le nombre de médicaments dont la date de péremption est proche (alerte). */
    private long nbMedicamentsAlerteBientotPerimee;
}
