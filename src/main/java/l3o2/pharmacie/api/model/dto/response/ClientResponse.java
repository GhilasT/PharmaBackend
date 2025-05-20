package l3o2.pharmacie.api.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

/**
 * DTO représentant la réponse contenant les informations d'un client.
 * Utilisé pour éviter d'exposer l'entité Client directement.
 */
@Getter
@Builder
public class ClientResponse {

    /** L'identifiant unique de la personne (client). */
    @JsonProperty("idPersonne")
    private UUID idPersonne;
    /** Le nom du client. */
    private String nom;
    /** Le prénom du client. */
    private String prenom;
    /** L'adresse email du client. */
    private String email;
    /** Le numéro de téléphone du client. */
    private String telephone;
    /** L'adresse postale du client. */
    private String adresse;
    /** Le numéro de sécurité sociale du client. */
    private String numeroSecu;
    /** La mutuelle du client. */
    private String mutuelle;
}
