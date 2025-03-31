package l3o2.pharmacie.api.repository;

import l3o2.pharmacie.api.model.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository pour l'entité Client.
 * Fournit des méthodes d'accès aux données pour les clients de la pharmacie.
 */
@Repository
public interface ClientRepository extends JpaRepository<Client, UUID> {

    /**
     * Recherche un client par son adresse email.
     * @param email Adresse email du client.
     * @return Un client correspondant à l'email (s'il existe).
     */
    Optional<Client> findByEmail(String email);

    /**
     * Recherche un client par son numéro de téléphone.
     * @param telephone Numéro de téléphone du client.
     * @return Un client correspondant au numéro de téléphone (s'il existe).
     */
    Optional<Client> findByTelephone(String telephone);

    /**
     * Recherche un client par son identifiant unique (UUID).
     * @param id Identifiant du client.
     * @return Un client correspondant à cet identifiant (s'il existe).
     */
    Optional<Client> findById(UUID id);

    /**
     * Récupère la liste de tous les clients.
     * @return Liste de tous les clients enregistrés dans la base de données.
     */
    List<Client> findAll();

    /**
     * Recherche un ou plusieurs clients par leur nom (insensible à la casse).
     * @param nom Nom partiel ou complet du client.
     * @return Liste des clients correspondant à la recherche.
     */
    List<Client> findByNomContainsIgnoreCase(String nom);

    /**
     * Recherche un client par son numéro de sécurité sociale.
     * @param numeroSecu Numéro de sécurité sociale du client.
     * @return Un client correspondant au numéro de sécurité sociale (s'il existe).
     */
    Optional<Client> findByNumeroSecu(String numeroSecu);

    /**
     * Recherche des clients ayant une mutuelle spécifique.
     * @param mutuelle Nom de la mutuelle.
     * @return Liste des clients affiliés à cette mutuelle.
     */
    List<Client> findByMutuelle(String mutuelle);

    /**
     * Vérifie si un client existe déjà avec cette adresse email.
     * @param email Adresse email du client.
     * @return true si un client avec cet email existe, sinon false.
     */
    boolean existsByEmail(String email);

    /**
     * Vérifie si un client existe déjà avec ce numéro de téléphone.
     * @param telephone Numéro de téléphone du client.
     * @return true si un client avec ce numéro existe, sinon false.
     */
    boolean existsByTelephone(String telephone);
}