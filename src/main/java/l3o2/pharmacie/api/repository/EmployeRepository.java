package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Employe.
 * Fournit des méthodes d'accès aux données des employés.
 */
@Repository
public interface EmployeRepository extends JpaRepository<Employe, UUID> {

    /**
     * Recherche un employé par son matricule unique.
     * @param matricule Matricule de l'employé.
     * @return L'employé correspondant s'il existe.
     */
    Optional<Employe> findByMatricule(String matricule);

    /**
     * Vérifie si un employé existe avec ce matricule.
     * @param matricule Matricule à vérifier.
     * @return True si un employé avec ce matricule existe, sinon false.
     */
    boolean existsByMatricule(String matricule);

    /**
     * Vérifie si un matricule existe déjà en base de données.
     * @param matricule Matricule à vérifier.
     * @return True si le matricule est déjà attribué, sinon false.
     */
    default boolean checkMatriculeExists(String matricule) {
        return existsByMatricule(matricule);
    }
}