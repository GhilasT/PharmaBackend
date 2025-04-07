package l3o2.pharmacie.api.model.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Builder
public class ClientCreateRequest {

    @NotBlank(message = "Le nom ne peut pas être vide")
    private String nom;

    @NotBlank(message = "Le prénom ne peut pas être vide")
    private String prenom;


    private String email;

    @NotBlank(message = "Le numéro de téléphone ne peut pas être vide")
    private String telephone;


    private String adresse;


    private String password;

    private String numeroSecu;

    private String mutuelle;
}
