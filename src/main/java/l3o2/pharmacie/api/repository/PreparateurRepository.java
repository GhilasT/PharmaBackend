package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Preparateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Préparateur.
 * Permet d'accéder aux données des préparateurs en pharmacie.
 */
@Repository
public interface PreparateurRepository extends JpaRepository<Preparateur, UUID> {

    /**
     * Recherche un préparateur par son matricule unique.
     * @param matricule Matricule du préparateur.
     * @return Le préparateur correspondant (s'il existe).
     */
    Optional<Preparateur> findByMatricule(String matricule);

    /**
     * Vérifie si un préparateur existe avec un matricule donné.
     * @param matricule Le matricule à vérifier.
     * @return true si un préparateur avec ce matricule existe, sinon false.
     */
    boolean existsByMatricule(String matricule);

    /**
     * Recherche un préparateur par son adresse email professionnelle.
     * @param emailPro Adresse email professionnelle du préparateur.
     * @return Le préparateur correspondant (s'il existe).
     */
    Optional<Preparateur> findByEmailPro(String emailPro);

    /**
     * Recherche tous les préparateurs enregistrés dans la pharmacie.
     * @return Liste des préparateurs enregistrés.
     */
    List<Preparateur> findAll();

    @Query("SELECT p FROM Preparateur p WHERE " +
       "LOWER(p.nom) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
       "LOWER(p.prenom) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
       "LOWER(CONCAT(p.nom, ' ', p.prenom)) LIKE LOWER(CONCAT('%', :term, '%'))")
List<Preparateur> searchByNomPrenom(@Param("term") String term);

}