package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Vente;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.Date;

/**
 * Repository pour l'entité Vente.
 * Permet d'accéder aux données des ventes effectuées en pharmacie.
 */
@Repository
public interface VenteRepository extends JpaRepository<Vente, UUID> {

    /**
     * Recherche les ventes effectuées par un pharmacien adjoint spécifique.
     * 
     * @param pharmacienAdjointId Identifiant du pharmacien adjoint.
     * @return Liste des ventes effectuées par ce pharmacien adjoint.
     */
    @EntityGraph(attributePaths = {
            "pharmacienAdjoint",
            "client",
            "medicamentsPanier",
            "medicamentsPanier.stockMedicament",
            "medicamentsPanier.stockMedicament.presentation",
            "medicamentsPanier.stockMedicament.presentation.cisBdpm"
    })
    List<Vente> findByPharmacienAdjoint_IdPersonne(UUID pharmacienAdjointId);

    /**
     * Recherche les ventes effectuées par un client spécifique.
     * 
     * @param clientId Identifiant du client.
     * @return Liste des ventes associées à ce client.
     */
    @EntityGraph(attributePaths = {
            "pharmacienAdjoint",
            "client",
            "medicamentsPanier",
            "medicamentsPanier.stockMedicament",
            "medicamentsPanier.stockMedicament.presentation",
            "medicamentsPanier.stockMedicament.presentation.cisBdpm"
    })
    List<Vente> findByClient_IdPersonne(UUID clientId);

    /**
     * Recherche les ventes effectuées dans une période donnée.
     * 
     * @param start Date de début de la période.
     * @param end Date de fin de la période.
     * @return Liste des ventes effectuées dans l'intervalle de dates spécifié.
     */
    @EntityGraph(attributePaths = {
            "pharmacienAdjoint",
            "client",
            "medicamentsPanier",
            "medicamentsPanier.stockMedicament",
            "medicamentsPanier.stockMedicament.presentation",
            "medicamentsPanier.stockMedicament.presentation.cisBdpm"
    })
    List<Vente> findByDateVenteBetween(Date start, Date end);

    /**
     * Recherche les ventes par mode de paiement (ex: Espèces, Carte bancaire).
     * 
     * @param modePaiement Mode de paiement utilisé.
     * @return Liste des ventes correspondant au mode de paiement.
     */
    List<Vente> findByModePaiement(String modePaiement);

    /**
     * Récupère toutes les ventes enregistrées.
     * Les entités associées (pharmacienAdjoint, client, medicamentsPanier, etc.) sont chargées avidement.
     * 
     * @return Liste de toutes les ventes.
     */
    @Override
    @EntityGraph(attributePaths = {
            "pharmacienAdjoint",
            "client",
            "medicamentsPanier",
            "medicamentsPanier.stockMedicament",
            "medicamentsPanier.stockMedicament.presentation",
            "medicamentsPanier.stockMedicament.presentation.cisBdpm"
    })
    List<Vente> findAll();

    /**
     * Recherche une vente par son identifiant unique.
     * Les entités associées (pharmacienAdjoint, client, medicamentsPanier, etc.) sont chargées avidement.
     * 
     * @param idVente Identifiant de la vente.
     * @return Un Optional contenant la vente si trouvée, sinon Optional vide.
     */
    @EntityGraph(attributePaths = {
            "pharmacienAdjoint",
            "client",
            "medicamentsPanier",
            "medicamentsPanier.stockMedicament.presentation.cisBdpm"
    })
    Optional<Vente> findById(UUID idVente);

    /**
     * Calcule la somme totale du chiffre d'affaires de toutes les ventes.
     * 
     * @return Le montant total du chiffre d'affaires.
     */
    @Query("SELECT COALESCE(SUM(v.montantTotal), 0) FROM Vente v")
    double sumTotalCA();
}