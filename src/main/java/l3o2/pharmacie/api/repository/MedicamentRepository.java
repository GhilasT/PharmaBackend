package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentRepository extends JpaRepository<StockMedicament, Long> {

    Optional<StockMedicament> findByNumeroLot(String numeroLot);

    List<StockMedicament> findByPresentation_CodeCip13(String codeCip13);

    List<StockMedicament> findByQuantiteLessThanEqual(Integer seuilAlerte);
}