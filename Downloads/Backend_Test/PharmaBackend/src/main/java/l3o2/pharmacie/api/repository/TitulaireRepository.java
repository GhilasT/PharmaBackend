package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Titulaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Titulaire.
 * Permet d'effectuer des opérations CRUD et des recherches spécifiques.
 */
@Repository
public interface TitulaireRepository extends JpaRepository<Titulaire, UUID> {

    /**
     * Recherche un pharmacien titulaire par son numéro RPPS.
     * @param numeroRPPS Numéro RPPS du titulaire.
     * @return Le titulaire correspondant (s'il existe).
     */
    Optional<Titulaire> findByNumeroRPPS(String numeroRPPS);

    /**
     * Vérifie si un titulaire existe avec un numéro RPPS donné.
     * @param numeroRPPS Numéro RPPS à vérifier.
     * @return True si un titulaire existe avec ce RPPS, sinon false.
     */
    boolean existsByNumeroRPPS(String numeroRPPS);

    /**
     * Recherche un titulaire par son email personnel (hérité de Personne).
     * @param email Email personnel du titulaire.
     * @return Le titulaire correspondant (s'il existe).
     */
    Optional<Titulaire> findByEmail(String email);

    /**
     * Recherche un titulaire par son email professionnel (hérité de Employe).
     * @param emailPro Email professionnel du titulaire.
     * @return Le titulaire correspondant (s'il existe).
     */
    Optional<Titulaire> findByEmailPro(String emailPro);

    /**
     * Vérifie si un titulaire existe avec un email personnel donné.
     * @param email Email personnel à vérifier.
     * @return True si un titulaire avec cet email existe, sinon false.
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un titulaire existe avec un email professionnel donné.
     * @param emailPro Email professionnel à vérifier.
     * @return True si un titulaire avec cet email professionnel existe, sinon false.
     */
    boolean existsByEmailPro(String emailPro);

    /**
     * Vérifie si un titulaire existe avec un matricule donné.
     * @param matricule Matricule à vérifier.
     * @return True si un titulaire avec ce matricule existe, sinon false.
     */
    boolean existsByMatricule(String matricule);

    /**
     * Recherche un titulaire par son matricule unique.
     * @param matricule Matricule du titulaire.
     * @return Le titulaire correspondant s'il existe.
     */
    Optional<Titulaire> findByMatricule(String matricule);
}