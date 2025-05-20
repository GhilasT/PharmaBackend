package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Commande;
import l3o2.pharmacie.api.model.entity.Fournisseur;
import l3o2.pharmacie.api.model.entity.PharmacienAdjoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Repository pour l'entité Commande.
 * Permet d'accéder aux données des commandes passées aux fournisseurs.
 */
@Repository
public interface CommandeRepository extends JpaRepository<Commande, UUID> {

    /**
     * Recherche les commandes associées à un fournisseur spécifique.
     * 
     * @param fourisseurId Identifiant du fournisseur.
     * @return Liste des commandes passées à ce fournisseur.
     */
    List<Commande> findByFournisseurId(UUID fourisseurId);

    /**
     * Recherche les commandes passées par un pharmacien adjoint spécifique.
     * 
     * @param pharmacienAdjointId Identifiant du pharmacien adjoint.
     * @return Liste des commandes passées par ce pharmacien adjoint.
     */
    List<Commande> findByPharmacienAdjointId(UUID pharmacienAdjointId);

    /**
     * Recherche les commandes par leur statut (ex: "En attente", "Livrée").
     * 
     * @param statut Statut de la commande.
     * @return Liste des commandes correspondant à ce statut.
     */
    List<Commande> findByStatut(String statut);

    /**
     * Recherche les commandes effectuées dans une période donnée.
     * 
     * @param dateCommandeAfter Date de début de la période.
     * @param dateCommandeBefore Date de fin de la période.
     * @return Liste des commandes effectuées dans l'intervalle de dates spécifié.
     */
    List<Commande> findByDateCommandeBetween(Date dateCommandeAfter, Date dateCommandeBefore);

    /**
     * Récupère toutes les commandes avec leurs lignes de commande, les médicaments en stock associés et leurs présentations.
     * Utilise FETCH JOIN pour charger avidement les associations et éviter les problèmes N+1.
     * 
     * @return Liste de toutes les commandes avec les détails associés.
     */
    @Query("SELECT DISTINCT c FROM Commande c " +
       "LEFT JOIN FETCH c.ligneCommandes l " +
       "LEFT JOIN FETCH l.stockMedicament sm " +
       "LEFT JOIN FETCH sm.presentation")
    List<Commande> findAllWithLigneCommandesAndStockMedicament();

}