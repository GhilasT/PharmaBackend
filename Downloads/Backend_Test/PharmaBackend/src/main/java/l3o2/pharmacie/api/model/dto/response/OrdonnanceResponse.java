package l3o2.pharmacie.api.model.dto.response;

import l3o2.pharmacie.api.model.entity.Client;
import l3o2.pharmacie.api.model.entity.Medecin;
import l3o2.pharmacie.api.model.entity.Prescription;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO représentant la réponse contenant les informations d'une ordonnance.
 * Utilisé pour éviter d'exposer les entités directement.
 */
@Data
@Builder
public class OrdonnanceResponse {

    private UUID idOrdonnance;
    private String numeroOrd;
    private Date dateEmission;
    private Date dateExpiration;
    private String rppsMedecin;
    private Client client;
    private Medecin medecin;
    private List<Prescription> prescriptions;

}