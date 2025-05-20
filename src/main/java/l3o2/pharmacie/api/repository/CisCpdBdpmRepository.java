package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.medicament.CisCpdBdpm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité CisCpdBdpm.
 * Permet d'accéder aux données des conditions de prescription et de délivrance des médicaments.
 */
@Repository
public interface CisCpdBdpmRepository extends JpaRepository<CisCpdBdpm, Long> {
    /**
     * Vérifie si des conditions de prescription/délivrance existent pour un code CIS donné.
     * 
     * @param codeCis Le code CIS du médicament.
     * @return True si des conditions existent pour ce code CIS, sinon false.
     */
    boolean existsByCisBdpm_CodeCis(String codeCis);

}