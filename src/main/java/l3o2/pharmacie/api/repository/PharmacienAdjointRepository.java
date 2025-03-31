package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité PharmacienAdjoint.
 * Fournit des méthodes d'accès aux données des pharmaciens adjoints.
 */
@Repository
public interface PharmacienAdjointRepository extends JpaRepository<PharmacienAdjoint, UUID> {

    /**
     * Recherche un pharmacien adjoint par son numéro d'employé (matricule).
     * @param matricule Le matricule unique du pharmacien adjoint.
     * @return Un pharmacien adjoint correspondant s'il existe.
     */
    Optional<PharmacienAdjoint> findByMatricule(String matricule);

    /**
     * Vérifie si un pharmacien adjoint existe avec ce numéro d'employé.
     * @param matricule Le matricule à vérifier.
     * @return true si un pharmacien avec ce matricule existe, sinon false.
     */
    boolean existsByMatricule(String matricule);

    /**
     * Recherche un pharmacien adjoint par adresse email professionnelle.
     * @param emailPro L'email professionnel du pharmacien.
     * @return Un pharmacien adjoint correspondant s'il existe.
     */
    Optional<PharmacienAdjoint> findByEmailPro(String emailPro);

    /**
     * Récupère la liste des pharmaciens adjoints ayant passé au moins une commande.
     * @return Liste des pharmaciens adjoints ayant passé des commandes.
     */
    List<PharmacienAdjoint> findByCommandesPasseesIsNotEmpty();



}