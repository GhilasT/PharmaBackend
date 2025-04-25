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

@Repository
public interface CommandeRepository extends JpaRepository<Commande, UUID> {

    List<Commande> findByFournisseurId(UUID fourisseurId);

    List<Commande> findByPharmacienAdjointId(UUID pharmacienAdjointId);

    List<Commande> findByStatut(String statut);

    List<Commande> findByDateCommandeBetween(Date dateCommandeAfter, Date dateCommandeBefore);

}