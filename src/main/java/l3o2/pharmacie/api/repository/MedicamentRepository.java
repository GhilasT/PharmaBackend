package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.medicament.Medicament;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Medicament.
 * Permet d'accéder aux données des médicaments stockés en pharmacie.
 */
@Repository
public interface MedicamentRepository extends JpaRepository<StockMedicament, Long> {

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
    Optional<Medicament> findByNumeroLot(String numeroLot);

    /**
     * Recherche tous les médicaments d'une présentation spécifique.
     * @param codeCip13 Code CIP13 de la présentation du médicament.
     * @return Liste des médicaments correspondants.
     */
    List<Medicament> findByPresentation_CodeCip13(String codeCip13);

    /**
     * Recherche tous les médicaments dont la quantité en stock est inférieure au seuil d'alerte.
     * @return Liste des médicaments nécessitant un réapprovisionnement.
     */
    List<Medicament> findByQuantiteLessThanEqual(Integer seuilAlerte);

    /**
     * Recherche les médicaments en stock par libellé de présentation ou code CIS.
     * La recherche est insensible à la casse et paginée.
     * 
     * @param searchTerm Le terme de recherche.
     * @param pageable   Les informations de pagination.
     * @return Une page de médicaments en stock correspondant aux critères de recherche.
     */
    @Query("SELECT s FROM StockMedicament s WHERE " +
            "LOWER(s.presentation.libellePresentation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.presentation.cisBdpm.codeCis) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<StockMedicament> searchByLibelleOrCodeCIS(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Recherche le médicament en stock le plus récemment mis à jour pour un code CIP13 donné.
     * 
     * @param codeCip13 Le code CIP13 de la présentation du médicament.
     * @return Un Optional contenant le médicament en stock le plus récent, ou vide s'il n'est pas trouvé.
     */
    Optional<StockMedicament> findTopByPresentation_CodeCip13OrderByDateMiseAJourDesc(String codeCip13);
}