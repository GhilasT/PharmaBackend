package l3o2.pharmacie.api.model.dto.response;

import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.Builder;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

/**
 * DTO représentant la réponse contenant les informations d'un titulaire.
 * Utilisé pour éviter d'exposer les entités directement.
 */
@Data
@Builder
public class TitulaireResponse {

    private UUID idPersonne;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;
    private String emailPro;
    private String matricule;
    private Date dateEmbauche;
    private Double salaire;
    private PosteEmploye poste;
    private StatutContrat statutContrat;
    private String diplome;
    private String role;
    private String numeroRPPS;
}