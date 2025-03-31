package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.medicament.CisBdpm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CisBdpmRepository extends JpaRepository<CisBdpm, String> {
    // Par défaut, vous héritez de "save(CisBdpm entity)" grâce à JpaRepository
}

