package l3o2.pharmacie.api.model.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedecinUpdateRequest {


    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")

    private String email;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Pattern(regexp = "^(\\+?[0-9]{6,15})$", message = "Le numéro de téléphone doit contenir entre 6 et 15 chiffres")
    private String telephone;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @NotBlank(message = "Le mot de passe est obligatoire")
    @Size(min = 8, message = "Le mot de passe doit contenir au moins 8 caractères")
    private String password;


    @NotBlank(message = "Le code CIS est obligatoire")
    private String codeCIS;

    @NotBlank(message = "La dénomination du médicament est obligatoire")
    private String denomination;

    @NotBlank(message = "La forme pharmaceutique est obligatoire")
    private String formePharmaceutique;

    @NotBlank(message = "Les voies d'administration sont obligatoires")
    private String voiesAdministration;

    @NotBlank(message = "Le statut administratif AMM est obligatoire")
    private String statutAdministratifAMM;

    @NotBlank(message = "Le type de procédure AMM est obligatoire")
    private String typeProcedureAMM;

    @NotBlank(message = "L'état de commercialisation est obligatoire")
    private String etatCommercialisation;

    private LocalDate dateAMM;

    @NotBlank(message = "Le titulaire de l'AMM est obligatoire")
    private String titulaire;

    private String statutBdm;

    @Positive(message = "Le prix doit être un nombre positif")
    private Double prix;

    private String surveillanceRenforcee;

    private String numAutorisationEuropeenne;

    @NotBlank(message = "Le code CIP est obligatoire")
    private String codeCIP;

    @NotBlank(message = "L'indication thérapeutique est obligatoire")
    private String indication;

    @NotBlank(message = "La posologie est obligatoire")
    private String posologie;

    @NotBlank(message = "Le groupe générique est obligatoire")
    private String generique;
}