package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Administrateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Administrateur.
 * Fournit des méthodes d'accès aux données pour les administrateurs.
 */
@Repository
public interface AdministrateurRepository extends JpaRepository<Administrateur, UUID> {


    /**
     * Recherche des administrateurs par nom ou prénom (insensible à la casse).
     * @param nom Nom partiel ou complet de l'administrateur.
     * @param prenom Prénom partiel ou complet de l'administrateur.
     * @return Liste des administrateurs correspondant à la recherche.
     */
    List<Administrateur> findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase(String nom, String prenom);

    /**
     * Recherche des administrateurs par rôle (ex: "gestionnaire", "super-admin").
     * @param role Rôle de l'administrateur.
     * @return Liste des administrateurs ayant ce rôle.
     */
    List<Administrateur> findByRole(String role);

    /**
     * Recherche un administrateur par son matricule unique.
     * @param matricule Matricule de l'administrateur.
     * @return Un administrateur correspondant au matricule (s'il existe).
     */
    Optional<Administrateur> findByMatricule(String matricule);

    /**
     * Recherche un administrateur par son email professionnel.
     * @param emailPro Email professionnel de l'administrateur.
     * @return Un administrateur correspondant à l'email professionnel (s'il existe).
     */
    Optional<Administrateur> findByEmailPro(String emailPro);

    /**
     * Vérifie si un administrateur existe déjà avec cet email professionnel.
     * @param emailPro Email professionnel de l'administrateur.
     * @return true si un administrateur avec cet email existe, sinon false.
     */



    boolean existsByEmail(String email);
    boolean existsByMatricule(String matricule);
    void deleteByMatricule(String matricule);
}