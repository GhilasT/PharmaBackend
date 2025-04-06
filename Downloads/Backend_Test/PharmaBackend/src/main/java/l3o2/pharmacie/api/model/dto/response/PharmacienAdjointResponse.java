package l3o2.pharmacie.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PharmacienAdjointResponse {
    // Hérité de Personne
    private UUID idPersonne;
    private String nom;
    private String prenom;
    private String email;
    private String telephone;
    private String adresse;

    // Hérité de Employe
    private String matricule;
    private Date dateEmbauche; 
    private Double salaire;
    private PosteEmploye poste;
    private StatutContrat statutContrat; 
    private String diplome;
    private String emailPro;

}