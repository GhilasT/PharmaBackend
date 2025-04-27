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

@Data
@Builder
public class OrdonnanceCreateRequest {

    @JsonFormat(
            shape   = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd'T'HH",
            timezone = "UTC"
    )
    private Date dateEmission;
    private String rppsMedecin;
    @JsonProperty("clientId")
    private UUID clientId;
    private List<PrescriptionCreateRequest> prescriptions;

}