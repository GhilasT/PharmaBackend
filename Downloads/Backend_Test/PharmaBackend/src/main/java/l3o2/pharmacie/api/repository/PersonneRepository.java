package l3o2.pharmacie.api.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import l3o2.pharmacie.api.model.entity.Personne;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Personne.
 * Permet l'accès aux données des utilisateurs (clients, employés, fournisseurs...).
 */
@Repository
public interface PersonneRepository extends JpaRepository<Personne, UUID> {

    /**
     * Recherche une personne par email et mot de passe.
     * Utilisé pour l'authentification.
     *
     *  Il est préférable d'encoder les mots de passe dans la base de données
     * et d'utiliser un service de validation sécurisé plutôt qu'une requête SQL directe.
     *
     * @param email L'adresse email de la personne.
     * @param password Le mot de passe non sécurisé (devrait être hashé).
     * @return La personne correspondante si elle existe.
     */
    @Query("SELECT p FROM Personne p WHERE p.email = :email AND p.password = :password")
    Optional<Personne> findByEmailAndPassword(
            @Param("email") String email,
            @Param("password") String password
    );

    /**
     * Recherche une personne par son adresse email.
     * @param email L'adresse email à rechercher.
     * @return La personne correspondante si elle existe.
     */
    Optional<Personne> findByEmail(String email);

    /**
     * Recherche une personne par son numéro de téléphone.
     * @param telephone Le numéro de téléphone à rechercher.
     * @return La personne correspondante si elle existe.
     */
    Optional<Personne> findByTelephone(String telephone);

    /**
     * Vérifie si une personne avec cet email existe.
     * Utile pour éviter les doublons lors de l'inscription.
     * @param email L'adresse email à vérifier.
     * @return true si une personne avec cet email existe, sinon false.
     */
    boolean existsByEmail(String email);
}