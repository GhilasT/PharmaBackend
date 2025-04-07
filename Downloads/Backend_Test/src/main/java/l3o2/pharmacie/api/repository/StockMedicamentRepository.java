package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité StockMedicament.
 * Permet d'accéder aux données des médicaments stockés en pharmacie.
 */
@Repository
public interface StockMedicamentRepository extends JpaRepository<StockMedicament, Long> {

    /**
     * Recherche un médicament par son identifiant unique.
     * @param id Identifiant du médicament.
     * @return Le médicament correspondant (s'il existe).
     */
    Optional<StockMedicament> findById(Long id);

    /**
     * Recherche un médicament par son numéro de lot.
     * @param numeroLot Numéro de lot du médicament.
     * @return Le médicament correspondant (s'il existe).
     */
    Optional<StockMedicament> findByNumeroLot(String numeroLot);

    /**
     * Recherche tous les médicaments d'une présentation spécifique.
     * @param codeCip13 Code CIP13 de la présentation du médicament.
     * @return Liste des médicaments correspondants.
     */
    List<StockMedicament> findByPresentation_CodeCip13(String codeCip13);

    /**
     * Recherche tous les médicaments dont la quantité en stock est inférieure au seuil d'alerte.
     * @return Liste des médicaments nécessitant un réapprovisionnement.
     */
    List<StockMedicament> findByQuantiteLessThanEqual(Integer seuilAlerte);

    /**
     * Recherche tous les médicaments dont la présentation est liée à un code CIS spécifique.
     *
     * @param codeCis Code CIS du médicament
     * @return Liste des médicaments correspondants
     */
    List<StockMedicament> findByPresentation_CisBdpm_CodeCisContainingIgnoreCase(String codeCis);

    /**
     * Recherche tous les médicaments dont la présentation a un libellé contenant le terme spécifié.
     *
     * @param libelle Terme de recherche pour le libellé de la présentation
     * @return Liste des médicaments correspondants
     */
    List<StockMedicament> findByPresentation_LibellePresentationContainingIgnoreCase(String libelle);

    @Query("SELECT s FROM StockMedicament s WHERE " +
       "LOWER(s.presentation.libellePresentation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(s.presentation.cisBdpm.codeCis) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
       "LOWER(s.presentation.cisBdpm.denomination) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")

Page<StockMedicament> searchByLibelleOrCodeCIS(@Param("searchTerm") String searchTerm, Pageable pageable);

}
