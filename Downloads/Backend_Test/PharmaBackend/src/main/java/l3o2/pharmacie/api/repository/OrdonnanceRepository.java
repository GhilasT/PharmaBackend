package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Ordonnance;
import l3o2.pharmacie.api.model.entity.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdonnanceRepository extends JpaRepository<Ordonnance, UUID> {

    /**
     * Recherche une ordonnance par son numéro d'ordonnance.
     * @param numeroOrd Numéro de l'ordonnance.
     * @return L'ordonnance correspondante.
     */
    Optional<Ordonnance> findByNumeroOrd(String numeroOrd);


    // Recherche une ordonnance par l'ID de la vente
    //Optional<Ordonnance> findByVente(UUID venteId);
    /**
     * Vérifie si une ordonnance existe avec un numéro d'ordonnance donné.
     * @param numeroOrd Numéro de l'ordonnance.
     * @return true si l'ordonnance existe, sinon false.
     */
    boolean existsByNumeroOrd(String numeroOrd);

    /**
     * Vérifie si une ordonnance existe avec un ID donné.
     * @param idOrdonnance ID de l'ordonnance.
     * @return true si l'ordonnance existe, sinon false.
     */
    boolean existsById(UUID idOrdonnance);

    /**
     * Recherche une ordonnance par son ID.
     * @param idOrdonnance ID de l'ordonnance.
     * @return L'ordonnance correspondante si elle existe.
     */
    Optional<Ordonnance> findById(UUID idOrdonnance);
}