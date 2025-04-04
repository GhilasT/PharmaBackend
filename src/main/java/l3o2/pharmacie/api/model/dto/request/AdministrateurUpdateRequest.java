package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.Email;

import l3o2.pharmacie.api.model.entity.StatutContrat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdministrateurUpdateRequest {
    // Champs modifiables de Personne
    private String nom;
    private String prenom;
    @Email 
    private String email;
    private String telephone;
    private String adresse;

    // Champs modifiables de Employe
    private Double salaire;
    private StatutContrat statutContrat;
    private String diplome;
    private String emailPro;

    // Champ spécifique à Administrateur
    private String role;
    
}