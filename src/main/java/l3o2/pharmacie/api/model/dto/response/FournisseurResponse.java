package l3o2.pharmacie.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurResponse {
    private UUID idFournisseur;
    private String nomSociete;
    private String sujetFonction;
    private String telephone;
    private String fax;
    private String adresse;
    private String email;
}