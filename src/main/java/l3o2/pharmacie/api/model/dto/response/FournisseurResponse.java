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
    private UUID idPersonne;
    private String societe;
    private String nom;
    private String prenom;
    private String sujetFonction;
    private String telephone;
    private String fax;
    private String adresse;
    private String email;
}