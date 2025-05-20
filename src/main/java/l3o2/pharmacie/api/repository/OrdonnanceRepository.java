package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Ordonnance;
import l3o2.pharmacie.api.model.entity.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Ordonnance.
 * Permet d'accéder aux données des ordonnances médicales.
 */
@Repository
public interface OrdonnanceRepository extends JpaRepository<Ordonnance, UUID> {
    /**
     * Vérifie si une ordonnance existe avec un numéro d'ordonnance donné.
     * 
     * @param numeroOrd Le numéro de l'ordonnance à vérifier.
     * @return True si une ordonnance avec ce numéro existe, sinon false.
     */
    boolean existsByNumeroOrd(String numeroOrd);
}
