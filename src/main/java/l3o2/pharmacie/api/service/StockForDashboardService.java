package l3o2.pharmacie.api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import l3o2.pharmacie.api.model.dto.response.StockDetailsDTO;
import l3o2.pharmacie.api.model.dto.response.StockForDashboardDTO;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import l3o2.pharmacie.api.repository.StockMedicamentRepository;
import lombok.AllArgsConstructor;

/**
 * Service fournissant des données de stock agrégées ou filtrées,
 * spécifiquement pour les besoins d'un tableau de bord.
 */
@Service
@AllArgsConstructor
public class StockForDashboardService{

    private StockMedicamentRepository stockMedicamentRepository;

    /**
     * Récupère tous les médicaments périmés (dont la date de péremption est antérieure à la date actuelle).
     * @return Une liste de {@link StockDetailsDTO} représentant les médicaments périmés.
     * @throws RuntimeException si aucun médicament périmé n'est trouvé.
     */
    public List<StockDetailsDTO> getMedicamentsPerimes() {
        return stockMedicamentRepository.getAllByDatePeremptionBefore(LocalDate.now())
                .orElseThrow(() -> new RuntimeException("Aucun médicament périmé trouvé"));
    }

    /**
     * Récupère tous les médicaments dont la quantité en stock est supérieure ou égale à une valeur donnée.
     * @param amount La quantité minimale requise.
     * @return Une liste de {@link StockDetailsDTO} des médicaments concernés.
     * @throws RuntimeException si aucun médicament ne correspond aux critères.
     */
    public List<StockDetailsDTO> getMedicamentsQuantiteSuperieureOuEgale(Integer amount) {
        return stockMedicamentRepository.getAllByQuantiteIsGreaterThanEqual(amount)
                .orElseThrow(() -> new RuntimeException("Aucun médicament trouvé avec une quantité supérieure ou égale à " + amount));

    }

    /**
     * Récupère tous les médicaments dont la date de péremption se situe entre une date de début et une date de fin.
     * Typiquement utilisé pour alerter sur les médicaments bientôt périmés.
     * @param date_debut La date de début de la période.
     * @param date_fin La date de fin de la période.
     * @return Une liste de {@link StockDetailsDTO} des médicaments concernés.
     * @throws RuntimeException si aucun médicament ne correspond aux critères.
     */
    public List<StockDetailsDTO> getMedicamentsAlerteBientotPerimee(LocalDate date_debut, LocalDate date_fin) {
        return stockMedicamentRepository.getAllByDatePeremptionBetween(date_debut, date_fin)
                .orElseThrow(() -> new RuntimeException("Aucun médicament trouvé avec une date de péremption entre " + LocalDate.now() + " et " + date_debut + " et " + date_fin));
    }

    /**
     * Récupère les médicaments dont la quantité en stock est inférieure ou égale au seuil d'alerte spécifié.
     * @param amount Le seuil d'alerte de quantité.
     * @return Une liste de {@link StockDetailsDTO} des médicaments en alerte de stock.
     */
    public List<StockDetailsDTO> getMedicamentsSeuilAlerte(Integer amount) {
        return stockMedicamentRepository.findByQuantiteLessThanEqual(amount);
    }

    /**
     * Compte le nombre de médicaments dont la quantité en stock est supérieure ou égale à une valeur donnée.
     * @param amount La quantité minimale.
     * @return Le nombre de médicaments correspondants.
     */
    public long countMedicamentsQuantiteSuperieureOuEgale(int amount) {
        return stockMedicamentRepository.countByQuantiteGreaterThanEqual(amount);
    }
    
    /**
     * Compte le nombre de médicaments dont la quantité en stock est inférieure ou égale à un seuil donné.
     * @param seuil Le seuil d'alerte de quantité.
     * @return Le nombre de médicaments en alerte de stock.
     */
    public long countMedicamentsSeuilAlerte(int seuil) {
        return stockMedicamentRepository.countByQuantiteLessThanEqual(seuil);
    }
    
    /**
     * Compte le nombre de médicaments périmés.
     * @return Le nombre de médicaments périmés.
     */
    public long countMedicamentsPerimes() {
        return stockMedicamentRepository.countByDatePeremptionBefore(LocalDate.now());
    }
    
    /**
     * Compte le nombre de médicaments dont la date de péremption se situe entre une date de début et une date de fin.
     * @param start La date de début de la période.
     * @param end La date de fin de la période.
     * @return Le nombre de médicaments bientôt périmés dans l'intervalle spécifié.
     */
    public long countMedicamentsAlerteBientotPerimee(LocalDate start, LocalDate end) {
        return stockMedicamentRepository.countByDatePeremptionBetween(start, end);
    }


}