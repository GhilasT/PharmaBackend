package l3o2.pharmacie.api.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ClientResponse {

    @JsonProperty("idPersonne")
    private UUID idPersonne;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String numeroSecu;
    private String mutuelle;
}
