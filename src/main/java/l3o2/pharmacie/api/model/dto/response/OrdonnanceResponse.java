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

    /** L'identifiant unique de l'ordonnance. */
    private UUID idOrdonnance;

    // Note: Les autres champs (client, medecin, prescriptions, etc.) pourraient être ajoutés ici
    // en fonction des informations nécessaires dans la réponse.
    // Par exemple:
    // /** L'identifiant du client associé à l'ordonnance. */
    // private UUID clientId;
    // /** L'identifiant du médecin prescripteur. */
    // private UUID medecinId;
    // /** La date de prescription de l'ordonnance. */
    // private Date datePrescription;
    // /** La liste des prescriptions de l'ordonnance. */
    // private List<PrescriptionResponse> prescriptions; // Nécessiterait un PrescriptionResponse DTO

}