package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Ordonnance;
import l3o2.pharmacie.api.model.entity.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrdonnanceRepository extends JpaRepository<Ordonnance, UUID> {

}