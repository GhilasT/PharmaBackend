package l3o2.pharmacie.api.model.dto.request;

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

    private UUID idOrdonnance;
    private Date dateEmission;
    private Date dateExpiration;
    private String rppsMedecin;
    private Client client;
    private Medecin medecin;
    private List<Prescription> prescriptions;
    private UUID venteId;
}