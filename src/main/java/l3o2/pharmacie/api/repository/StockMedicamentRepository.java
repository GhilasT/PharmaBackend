package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.dto.response.StockDetailsDTO;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
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
     * 
     * @param id Identifiant du médicament.
     * @return Le médicament correspondant (s'il existe).
     */
    Optional<StockMedicament> findById(Long id);

    /**
     * Recherche tous les médicaments dont la quantité est non nulle.
     * 
     * @return Liste des médicaments avec quantité non nulle.
     */
    Optional<List<StockDetailsDTO>> getAllByQuantiteIsGreaterThanEqual(Integer amount);

    /**
     * Recherche tous les médicaments dont la date de péremption est comprise entre
     * deux dates.
     * 
     * @param from
     * @param to
     * @return
     */
    Optional<List<StockDetailsDTO>> getAllByDatePeremptionBetween(LocalDate from, LocalDate to);

    /**
     * Recherche tous les médicaments dont la date de péremption est inférieure ou
     * égale à la date actuelle.
     * 
     * @param date Date actuelle.
     * @return Liste des médicaments périmés.
     */
    Optional<List<StockDetailsDTO>> getAllByDatePeremptionBefore(LocalDate date);

    /**
     * Recherche un médicament par son numéro de lot.
     * 
     * @param numeroLot Numéro de lot du médicament.
     * @return Le médicament correspondant (s'il existe).
     */
    Optional<StockMedicament> findByNumeroLot(String numeroLot);

    /**
     * Recherche tous les médicaments d'une présentation spécifique.
     * 
     * @param codeCip13 Code CIP13 de la présentation du médicament.
     * @return Liste des médicaments correspondants.
     */
    List<StockMedicament> findByPresentation_CodeCip13(String codeCip13);

    /**
     * Recherche tous les médicaments dont la quantité en stock est inférieure au
     * seuil d'alerte.
     * 
     * @return Liste des médicaments nécessitant un réapprovisionnement.
     */
    List<StockDetailsDTO> findByQuantiteLessThanEqual(Integer seuilAlerte);

    /**
     * Recherche tous les médicaments dont la présentation est liée à un code CIS
     * spécifique.
     * 
     * @param codeCis Code CIS du médicament
     * @return Liste des médicaments correspondants
     */
    List<StockMedicament> findByPresentation_CisBdpm_CodeCisContainingIgnoreCase(String codeCis);

    /**
     * Recherche tous les médicaments dont la présentation a un libellé contenant le
     * terme spécifié.
     * 
     * @param libelle Terme de recherche pour le libellé de la présentation
     * @return Liste des médicaments correspondants
     */
    List<StockMedicament> findByPresentation_LibellePresentationContainingIgnoreCase(String libelle);

    /**
     * Recherche les médicaments en stock par libellé de présentation, code CIS ou dénomination du médicament.
     * La recherche est insensible à la casse et paginée.
     * 
     * @param searchTerm Le terme de recherche.
     * @param pageable   Les informations de pagination.
     * @return Une page de médicaments en stock correspondant aux critères de recherche.
     */
    @Query("SELECT s FROM StockMedicament s WHERE " +
            "LOWER(s.presentation.libellePresentation) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.presentation.cisBdpm.codeCis) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
            "LOWER(s.presentation.cisBdpm.denomination) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    Page<StockMedicament> searchByLibelleOrCodeCIS(@Param("searchTerm") String searchTerm, Pageable pageable);

    /**
     * Compte le nombre de médicaments en stock dont la quantité est supérieure ou égale à un montant donné.
     * 
     * @param amount La quantité minimale.
     * @return Le nombre de médicaments en stock correspondants.
     */
    @Query("SELECT COUNT(s) FROM StockMedicament s WHERE s.quantite >= :amount")
    long countByQuantiteGreaterThanEqual(@Param("amount") int amount);
    
    /**
     * Compte le nombre de médicaments en stock dont la quantité est inférieure ou égale à un seuil donné.
     * 
     * @param seuil Le seuil de quantité.
     * @return Le nombre de médicaments en stock correspondants.
     */
    @Query("SELECT COUNT(s) FROM StockMedicament s WHERE s.quantite <= :seuil")
    long countByQuantiteLessThanEqual(@Param("seuil") int seuil);

    /**
     * Compte le nombre de médicaments en stock dont la date de péremption est antérieure à une date donnée.
     * 
     * @param date La date de référence.
     * @return Le nombre de médicaments périmés.
     */
    long countByDatePeremptionBefore(LocalDate date);

    /**
     * Compte le nombre de médicaments en stock dont la date de péremption est comprise entre deux dates.
     * 
     * @param start Date de début de la période.
     * @param end   Date de fin de la période.
     * @return Le nombre de médicaments dont la date de péremption est dans l'intervalle spécifié.
     */
    long countByDatePeremptionBetween(LocalDate start, LocalDate end);
}
