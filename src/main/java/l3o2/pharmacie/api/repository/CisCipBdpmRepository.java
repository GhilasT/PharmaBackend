package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.medicament.CisCipBdpm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository pour l'entité CisCipBdpm.
 * Permet d'accéder aux données des présentations (CIP) des médicaments.
 */
@Repository
public interface CisCipBdpmRepository extends JpaRepository<CisCipBdpm, Long> {
    /**
     * Recherche une présentation de médicament par son code CIP13.
     * 
     * @param codeCip13 Le code CIP13 de la présentation.
     * @return Un Optional contenant la présentation si trouvée, sinon un Optional vide.
     */
    Optional<CisCipBdpm> findByCodeCip13(String codeCip13);
    /**
     * Recherche une présentation de médicament par son identifiant unique.
     * 
     * @param id L'identifiant de la présentation.
     * @return Un Optional contenant la présentation si trouvée, sinon un Optional vide.
     */
    Optional<CisCipBdpm> findById(Long id);
    /**
     * Recherche la première présentation de médicament associée à un code CIS.
     * 
     * @param codeCis Le code CIS du médicament.
     * @return Un Optional contenant la première présentation trouvée pour ce code CIS, sinon un Optional vide.
     */
    Optional<CisCipBdpm> findFirstByCisBdpm_CodeCis(String codeCis);
}