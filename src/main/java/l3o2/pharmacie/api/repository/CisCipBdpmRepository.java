package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.medicament.CisCipBdpm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CisCipBdpmRepository extends JpaRepository<CisCipBdpm, Long> {
    Optional<CisCipBdpm> findByCodeCip13(String codeCip13);
    Optional<CisCipBdpm> findById(Long id);
    //Ajout pour rechercher par code CIS
    Optional<CisCipBdpm> findFirstByCisBdpm_CodeCis(String codeCis);
}