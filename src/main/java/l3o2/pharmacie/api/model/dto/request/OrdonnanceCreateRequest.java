package l3o2.pharmacie.api.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import l3o2.pharmacie.api.model.entity.Client;
import l3o2.pharmacie.api.model.entity.Medecin;
import l3o2.pharmacie.api.model.entity.Prescription;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour la création d'une ordonnance médicale.
 * Contient les informations nécessaires pour enregistrer une nouvelle ordonnance.
 */
@Data
@Builder
public class OrdonnanceCreateRequest {

    /**
     * La date d'émission de l'ordonnance. Format attendu : "yyyy-MM-dd'T'HH".
     */
    @JsonFormat(
            shape   = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH",
            timezone = "UTC"
    )
    private Date dateEmission;
    /**
     * Le numéro RPPS (Répertoire Partagé des Professionnels de Santé) du médecin prescripteur.
     */
    private String rppsMedecin;
    /**
     * L'identifiant unique du client pour lequel l'ordonnance est émise.
     */
    @JsonProperty("clientId")
    private UUID clientId;
    /**
     * La liste des prescriptions (médicaments) incluses dans cette ordonnance.
     */
    private List<PrescriptionCreateRequest> prescriptions;

}