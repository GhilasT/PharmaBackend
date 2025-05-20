package l3o2.pharmacie.api.model.entity.medicament;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Classe représentant un médicament dans la base de données publique des médicaments (BDPM)
 * Contient les informations réglementaires et administratives des médicaments référencés
 */
@Entity
// Définit le nom de la table associée dans la base de données
@Table(name = "cis_bdpm")
// Génère automatiquement les getters, setters et les méthodes toString, equals, hashCode
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CisBdpm {

    @Id
    @Column(name = "code_cis", nullable = false, unique = true, length = 20)
    @NotBlank(message = "Le code CIS est obligatoire")
    /** Code unique d'identification du médicament (CIS). */
    private String codeCis;

    @Column(name = "denomination", nullable = false, length = 500)
    @NotBlank(message = "La dénomination est obligatoire")
    /** Dénomination du médicament. */
    private String denomination;

    @Column(name = "forme_pharmaceutique", length = 100)
    /** Forme pharmaceutique (ex: comprimé, solution buvable). */
    private String formePharmaceutique;

    @Column(name = "voies_administration", length = 200)
    /** Voies d'administration possibles, séparées par ";". */
    private String voiesAdministration;

    @Column(name = "statut_amm", length = 50)
    /** Statut administratif de l'Autorisation de Mise sur le Marché (AMM). */
    private String statutAMM;

    @Column(name = "type_procedure_amm", length = 50)
    /** Type de procédure d'AMM (ex: nationale, européenne). */
    private String typeProcedureAMM;

    @Column(name = "etat_commercialisation", length = 50)
    /** Indique si le médicament est commercialisé ou non. */
    private String etatCommercialisation;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_amm")
    /** Date d'obtention de l'AMM (format JJ/MM/AAAA). */
    private Date dateAMM;

    @Column(name = "statut_bdm", length = 30)
    @Pattern(regexp = "Alerte|Warning disponibilité", message = "Valeur invalide pour StatutBdm")
    /** Statut dans la base des médicaments (ex: alerte de disponibilité). */
    private String statutBdm;

    @Column(name = "numero_autorisation_europeenne", length = 50)
    /** Numéro d'autorisation européenne (si applicable). */
    private String numeroAutorisationEuropeenne;

    @Column(name = "titulaires", length = 500)
    /** Titulaires de l'AMM (laboratoires pharmaceutiques), séparés par ";". */
    private String titulaires;

    @Column(name = "surveillance_renforcee", length = 3)
    @Pattern(regexp = "Oui|Non", message = "Valeur invalide pour Surveillance renforcée")
    /** Indique si le médicament est sous surveillance renforcée. */
    private String surveillanceRenforcee;

    // Relations avec d'autres entités

    @OneToMany(mappedBy = "cisBdpm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    /** Liste des présentations associées au médicament. */
    private List<CisCipBdpm> presentations = new ArrayList<>();

    @OneToMany(mappedBy = "cisBdpm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    /** Liste des compositions du médicament. */
    private List<CisCompoBdpm> compositions = new ArrayList<>();

    @OneToMany(mappedBy = "cisBdpm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    /** Liste des avis SMR (Service Médical Rendu). */
    private List<CisHasSmr> avisSmr = new ArrayList<>();

    @OneToMany(mappedBy = "cisBdpm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    /** Liste des avis ASMR (Amélioration du Service Médical Rendu). */
    private List<CisHasAsmr> avisAsmr = new ArrayList<>();

    @OneToMany(mappedBy = "cisBdpm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    /** Liste des groupes génériques liés au médicament. */
    private List<CisGenerBdpm> groupesGeneriques = new ArrayList<>();

    @OneToMany(mappedBy = "cisBdpm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    /** Liste des conditions de prescription et de délivrance. */
    private List<CisCpdBdpm> conditionsPrescription = new ArrayList<>();

    @OneToMany(mappedBy = "cisBdpm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    /** Liste des informations importantes relatives au médicament. */
    private List<CisInfoImportantes> informationsImportantes = new ArrayList<>();

    @OneToMany(mappedBy = "cisBdpm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    /** Liste des disponibilités du médicament (ex: rupture de stock). */
    private List<CisCipDispoSpec> disponibilites = new ArrayList<>();

    @OneToMany(mappedBy = "cisBdpm", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    /** Liste des médicaments MITM (Médicaments d'Intérêt Thérapeutique Majeur). */
    private List<CisMitm> medicamentsMitm = new ArrayList<>();

}