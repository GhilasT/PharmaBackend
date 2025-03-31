package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
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
     * @param pharmacienAdjointId Identifiant du pharmacien adjoint.
     * @return Liste des ventes effectuées par ce pharmacien adjoint.
     */
    List<Vente> findByPharmacienAdjoint_IdPersonne(UUID pharmacienAdjointId);

    /**
     * Recherche les ventes effectuées par un client spécifique.
     * @param clientId Identifiant du client.
     * @return Liste des ventes associées à ce client.
     */
    List<Vente> findByClient_IdPersonne(UUID clientId);

    /**
     * Recherche les ventes effectuées dans une période donnée.
     * @param dateDebut Date de début de la période.
     * @param dateFin Date de fin de la période.
     * @return Liste des ventes réalisées entre ces dates.
     */
    List<Vente> findByDateVenteBetween(Date dateDebut, Date dateFin);

    /**
     * Recherche les ventes par mode de paiement (ex: Espèces, Carte bancaire).
     * @param modePaiement Mode de paiement utilisé.
     * @return Liste des ventes correspondant au mode de paiement.
     */
    List<Vente> findByModePaiement(String modePaiement);
}