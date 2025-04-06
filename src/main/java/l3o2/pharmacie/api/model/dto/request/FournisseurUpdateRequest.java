package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FournisseurUpdateRequest {
    private String nomSociete;
    private String sujetFonction;
    
    @Pattern(regexp = "^(\\+?[0-9\\s-]{6,15})$", message = "Le numéro de téléphone doit contenir entre 6 et 15 chiffres")
    private String telephone;
    
    private String fax;
    private String adresse;
    
    @Email(message = "L'email doit être valide")
    private String email;
}