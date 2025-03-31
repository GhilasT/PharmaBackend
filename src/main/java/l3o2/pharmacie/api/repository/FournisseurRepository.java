package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Fournisseur;
import org.springframework.data.jpa.repository.JpaRepository;
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
     * @param societe Nom de la société.
     * @return Fournisseur correspondant (s'il existe).
     */
    Optional<Fournisseur> findBySociete(String societe);

    /**
     * Recherche un fournisseur par son adresse email.
     * @param email Adresse email du fournisseur.
     * @return Fournisseur correspondant (s'il existe).
     */
    Optional<Fournisseur> findByEmail(String email);

    /**
     * Recherche un fournisseur par son numéro de téléphone.
     * @param telephone Numéro de téléphone du fournisseur.
     * @return Fournisseur correspondant (s'il existe).
     */
    Optional<Fournisseur> findByTelephone(String telephone);

    /**
     * Recherche tous les fournisseurs ayant une société dont le nom contient un mot-clé (insensible à la casse).
     * @param keyword Mot-clé à rechercher dans le nom de la société.
     * @return Liste des fournisseurs correspondant à la recherche.
     */
    List<Fournisseur> findBySocieteContainsIgnoreCase(String keyword);

    /**
     * Vérifie si un fournisseur existe avec cette adresse email.
     * @param email Adresse email du fournisseur.
     * @return true si un fournisseur avec cet email existe, sinon false.
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un fournisseur existe avec ce numéro de téléphone.
     * @param telephone Numéro de téléphone du fournisseur.
     * @return true si un fournisseur avec ce téléphone existe, sinon false.
     */
    boolean existsByTelephone(String telephone);
}