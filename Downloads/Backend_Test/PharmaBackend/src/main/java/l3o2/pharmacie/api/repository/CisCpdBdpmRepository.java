package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.medicament.CisCpdBdpm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CisCpdBdpmRepository extends JpaRepository<CisCpdBdpm, Long> {
    boolean existsByCisBdpm_CodeCis(String codeCis);

}