package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Fournisseur.
 * Fournit des méthodes d'accès aux données des fournisseurs enregistrés dans la pharmacie.
 */
@Repository
public interface FournisseurRepository extends JpaRepository<Fournisseur, UUID> {

    /**
     * Recherche un fournisseur par le nom de sa société.
     * 
     * @param societe Le nom de la société du fournisseur.
     * @return Un Optional contenant le fournisseur s'il est trouvé, sinon un Optional vide.
     */
    Optional<Fournisseur> findByNomSociete(String societe);

    /**
     * Recherche un fournisseur par son adresse email.
     * 
     * @param email L'adresse email du fournisseur.
     * @return Un Optional contenant le fournisseur s'il est trouvé, sinon un Optional vide.
     */
    Optional<Fournisseur> findByEmail(String email);

    /**
     * Recherche un fournisseur par son numéro de téléphone.
     * 
     * @param telephone Le numéro de téléphone du fournisseur.
     * @return Un Optional contenant le fournisseur s'il est trouvé, sinon un Optional vide.
     */
    Optional<Fournisseur> findByTelephone(String telephone);

    /**
     * Vérifie si un fournisseur existe avec l'adresse email donnée.
     * 
     * @param email L'adresse email à vérifier.
     * @return True si un fournisseur avec cet email existe, sinon false.
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un fournisseur existe avec le numéro de téléphone donné.
     * 
     * @param telephone Le numéro de téléphone à vérifier.
     * @return True si un fournisseur avec ce numéro de téléphone existe, sinon false.
     */
    boolean existsByTelephone(String telephone);

    /**
     * Recherche des fournisseurs par nom de société ou sujet/fonction.
     * La recherche est insensible à la casse.
     * 
     * @param query Le terme de recherche.
     * @return Liste des fournisseurs correspondant au terme de recherche.
     */
    @Query("SELECT f FROM Fournisseur f WHERE " +
       "LOWER(f.nomSociete) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
       "LOWER(f.sujetFonction) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Fournisseur> searchFournisseurs(@Param("query") String query);
}