package l3o2.pharmacie.api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import l3o2.pharmacie.api.model.dto.response.StockDetailsDTO;
import l3o2.pharmacie.api.model.dto.response.StockForDashboardDTO;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import l3o2.pharmacie.api.repository.StockMedicamentRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class StockForDashboardService{

    private StockMedicamentRepository stockMedicamentRepository;

    /**
     * Récupère tous les médicaments périmés.
     * @return Liste des médicaments périmés.
     */
    public List<StockDetailsDTO> getMedicamentsPerimes() {
        return stockMedicamentRepository.getAllByDatePeremptionBefore(LocalDate.now())
                .orElseThrow(() -> new RuntimeException("Aucun médicament périmé trouvé"));
    }

    /**
     * Récupère tous les médicaments dont la quantité est supérieure ou égale à une valeur donnée.
     * @param amount Valeur minimale de la quantité.
     * @return Liste des médicaments avec quantité supérieure ou égale à amount.
     */
    public List<StockDetailsDTO> getMedicamentsQuantiteSuperieureOuEgale(Integer amount) {
        return stockMedicamentRepository.getAllByQuantiteIsGreaterThanEqual(amount)
                .orElseThrow(() -> new RuntimeException("Aucun médicament trouvé avec une quantité supérieure ou égale à " + amount));

    }

    /**
     * Recupère tous les médicaments dont la date limite est entre auj et une date donnee
     * @return liste de medicament
     */
    public List<StockDetailsDTO> getMedicamentsAlerteBientotPerimee(LocalDate date_debut, LocalDate date_fin) {
        return stockMedicamentRepository.getAllByDatePeremptionBetween(date_debut, date_fin)
                .orElseThrow(() -> new RuntimeException("Aucun médicament trouvé avec une date de péremption entre " + LocalDate.now() + " et " + date_debut + " et " + date_fin));
    }

    public List<StockDetailsDTO> getMedicamentsSeuilAlerte(Integer amount) {
        return stockMedicamentRepository.findByQuantiteLessThanEqual(amount);
    }

    public long countMedicamentsQuantiteSuperieureOuEgale(int amount) {
        return stockMedicamentRepository.countByQuantiteGreaterThanEqual(amount);
    }
    
    public long countMedicamentsSeuilAlerte(int seuil) {
        return stockMedicamentRepository.countByQuantiteLessThanEqual(seuil);
    }
    
    public long countMedicamentsPerimes() {
        return stockMedicamentRepository.countByDatePeremptionBefore(LocalDate.now());
    }
    
    public long countMedicamentsAlerteBientotPerimee(LocalDate start, LocalDate end) {
        return stockMedicamentRepository.countByDatePeremptionBetween(start, end);
    }


}