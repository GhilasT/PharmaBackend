package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.medicament.CisBdpm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité CisBdpm.
 * Permet d'accéder aux données des médicaments dans la base de données publique des médicaments.
 */
@Repository
public interface CisBdpmRepository extends JpaRepository<CisBdpm, String> {
    
    /**
     * Recherche un médicament par son code CIS.
     * 
     * @param codeCis Code CIS du médicament
     * @return Le médicament correspondant (s'il existe)
     */
    Optional<CisBdpm> findByCodeCis(String codeCis);
    
    /**
     * Recherche des médicaments par leur dénomination (recherche partielle).
     * 
     * @param denomination Partie de la dénomination du médicament
     * @return Liste des médicaments correspondants
     */
    List<CisBdpm> findByDenominationContainingIgnoreCase(String denomination);
    
    /**
     * Recherche des médicaments par leur forme pharmaceutique.
     * 
     * @param formePharmaceutique Forme pharmaceutique du médicament
     * @return Liste des médicaments correspondants
     */
    List<CisBdpm> findByFormePharmaceutiqueContainingIgnoreCase(String formePharmaceutique);
    
    /**
     * Recherche des médicaments par leur titulaire (laboratoire).
     * 
     * @param titulaire Nom du titulaire/laboratoire
     * @return Liste des médicaments correspondants
     */
    List<CisBdpm> findByTitulairesContainingIgnoreCase(String titulaire);
     /**
     * Recherche des médicaments par code CIS ou dénomination.
     * 
     * @param codeCis Terme de recherche pour le code CIS
     * @param denomination Terme de recherche pour la dénomination
     * @return Liste des médicaments correspondant aux critères de recherche
     */
    List<CisBdpm> findByCodeCisContainingOrDenominationContainingIgnoreCase(String codeCis, String denomination);

}
