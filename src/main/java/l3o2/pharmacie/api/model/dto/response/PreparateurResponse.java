package l3o2.pharmacie.api.model.dto.response;

import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

/**
 * DTO pour afficher les informations d'un préparateur en pharmacie.
 */
@Data
@Builder
public class PreparateurResponse {

    private UUID idPersonne;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String matricule;
    private Date dateEmbauche;
    private Double salaire;
    private String poste;
    private String statutContrat;
    private String diplome;
    private String emailPro;
}