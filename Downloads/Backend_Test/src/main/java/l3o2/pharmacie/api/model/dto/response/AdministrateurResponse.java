package l3o2.pharmacie.api.model.dto.response;

import l3o2.pharmacie.api.model.entity.PosteEmploye;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.Email;
import l3o2.pharmacie.api.model.entity.StatutContrat;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministrateurResponse {
    private UUID idPersonne;
    private String nom;
    private String prenom;
    @Email 
    private String email;
    private String telephone;
    private String adresse;

    // Employe
    private String matricule;
    private LocalDate dateEmbauche;
    private Double salaire;
    private PosteEmploye poste;
    private StatutContrat statutContrat;
    private String diplome;
    private String emailPro;

    // Administrateur
    private String role;
}