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

    Optional<Fournisseur> findByNomsociete(String societe);
    Optional<Fournisseur> findByEmail(String email);
    Optional<Fournisseur> findByTelephone(String telephone);
    boolean existsByEmail(String email);
    boolean existsByTelephone(String telephone);
}