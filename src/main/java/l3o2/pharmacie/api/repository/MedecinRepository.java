package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Medecin.
 * Fournit des méthodes d'accès aux données des médecins enregistrés dans la base.
 */
@Repository
public interface MedecinRepository extends JpaRepository<Medecin, UUID> {
        /**
         * Recherche un médecin par son numéro RPPS (Répertoire Partagé des Professionnels de Santé).
         * @param rpps Numéro RPPS unique du médecin.
         * @return Médecin correspondant (s'il existe).
         */
    Optional<Medecin> findByRpps(String rpps);

    /**
     * Recherche un médecin par son numéro SIRET (identifiant de l'établissement).
     * @param siret Numéro SIRET unique du cabinet du médecin.
     * @return Médecin correspondant (s'il existe).
     */
    Optional<Medecin> findBySiret(String siret);

    /**
     * Recherche des médecins dont le nom ou le prénom contient un mot-clé (insensible à la casse).
     * @param nom Nom partiel ou complet du médecin.
     * @param prenom Prénom partiel ou complet du médecin.
     * @return Liste des médecins correspondant à la recherche.
     */
    List<Medecin> findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase(String nom, String prenom);

    /**
     * Recherche un médecin par son email professionnel.
     * @param email Adresse email professionnelle du médecin.
     * @return Médecin correspondant (s'il existe).
     */
    Optional<Medecin> findByEmail(String email);

    /**
     * Recherche les médecins exerçant dans une ville spécifique.
     * @param ville Nom de la ville.
     * @return Liste des médecins exerçant dans cette ville.
     */
    List<Medecin> findByVilleIgnoreCase(String ville);

    /**
     * Recherche les médecins par spécialité principale.
     * @param specialite Spécialité principale du médecin (ex: "Cardiologie", "Dermatologie").
     * @return Liste des médecins ayant cette spécialité.
     */
    List<Medecin> findBySpecialitePrincipaleIgnoreCase(String specialite);

    /**
     * Vérifie si un médecin existe avec un certain numéro RPPS.
     * @param rpps Numéro RPPS unique.
     * @return true si un médecin avec ce RPPS existe, sinon false.
     */
    boolean existsByRpps(String rpps);

    /**
     * Vérifie si un médecin existe avec une adresse email professionnelle donnée.
     * @param email Adresse email professionnelle.
     * @return true si un médecin avec cet email existe, sinon false.
     */
    boolean existsByEmail(String email);


    @Query("SELECT m FROM Medecin m WHERE " +
       "LOWER(CONCAT(m.prenom, ' ', m.nom)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
       "LOWER(CONCAT(m.nom, ' ', m.prenom)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
       "LOWER(m.nom) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
       "LOWER(m.prenom) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Medecin> searchByNomPrenomCombinaison(@Param("query") String query);
    
}