package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Apprenti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Apprenti.
 * Permet d'accéder aux données des apprentis en pharmacie.
 */
@Repository
public interface ApprentiRepository extends JpaRepository<Apprenti, UUID> {

    /**
     * Recherche un apprenti par son matricule unique.
     * @param matricule Matricule de l'apprenti.
     * @return L'apprenti correspondant (s'il existe).
     */
    Optional<Apprenti> findByMatricule(String matricule);

    /**
     * Vérifie si un apprenti existe avec un matricule donné.
     * @param matricule Le matricule à vérifier.
     * @return true si un apprenti avec ce matricule existe, sinon false.
     */
    boolean existsByMatricule(String matricule);

    /**
     * Recherche un apprenti par son adresse email professionnelle.
     * @param emailPro Adresse email professionnelle de l'apprenti.
     * @return L'apprenti correspondant (s'il existe).
     */
    Optional<Apprenti> findByEmailPro(String emailPro);

    /**
     * Recherche tous les apprentis enregistrés dans la pharmacie.
     * @return Liste des apprentis enregistrés.
     */
    List<Apprenti> findAll();

    /**
     * Recherche les apprentis par nom, prénom ou une combinaison des deux.
     * La recherche est insensible à la casse.
     * 
     * @param term Le terme de recherche.
     * @return Liste des apprentis correspondant au terme de recherche.
     */
    @Query("SELECT a FROM Apprenti a WHERE " +
       "LOWER(a.nom) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
       "LOWER(a.prenom) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
       "LOWER(CONCAT(a.nom, ' ', a.prenom)) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<Apprenti> searchByTerm(@Param("term") String term);
}