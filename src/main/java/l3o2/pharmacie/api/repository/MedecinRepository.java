package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Medecin.
 * Permet d'accéder aux données des médecins.
 */
@Repository
public interface MedecinRepository extends JpaRepository<Medecin, UUID> {

    // Recherche un médecin par son numéro RPPS
    Optional<Medecin> findByRppsMedecin(String rppsMedecin);



    // Recherche un médecin par nom ou prénom, insensible à la casse
    @Query("SELECT m FROM Medecin m WHERE LOWER(m.nomExercice) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(m.prenomExercice) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Medecin> findByNomExerciceContainsIgnoreCaseOrPrenomExerciceContainsIgnoreCase(String query);

    // Recherche par combinaison nom/prénom
    @Query("SELECT m FROM Medecin m WHERE LOWER(m.nomExercice) LIKE LOWER(CONCAT('%', :query, '%')) " +
            "OR LOWER(m.prenomExercice) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Medecin> searchByNomPrenomCombinaison(String query);
}