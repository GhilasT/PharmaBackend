package l3o2.pharmacie.api.model.entity.medicament;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

/**
 * Classe représentant la composition d'un médicament référencé dans la BDPM.
 * Contient les informations sur les substances actives et les excipients d'un médicament.
 */
@Getter
@Setter
// Génère automatiquement un constructeur sans paramètres.
@NoArgsConstructor
// Génère automatiquement un constructeur avec tous les paramètres.
@AllArgsConstructor
// Indique que cette classe est une entité JPA.
@Entity
// Définit le nom de la table associée dans la base de données.
@Table(name = "cis_compo_bdpm")
public class CisCompoBdpm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Identifiant unique de la composition du médicament. */
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_cis", nullable = false)
    /** Référence au médicament principal (CIS). */
    private CisBdpm cisBdpm;

    @Column(name = "designation_element")
    /** Désignation de l'élément composant le médicament (ex: substance active, excipient). */
    private String designationElement;

    @Column(name = "code_substance")
    /** Code unique d'identification de la substance. */
    private String codeSubstance;

    @Column(name = "denomination_substance")
    /** Nom de la substance présente dans le médicament. */
    private String denominationSubstance;

    @Column(name = "dosage")
    /** Dosage de la substance dans le médicament (ex: "500 mg", "1 UI"). */
    private String dosage;

    @Column(name = "reference_dosage")
    /** Référence du dosage (ex: "base", "sel", "hydrate"). */
    private String referenceDosage;

    @Column(name = "nature_composant")
    @Pattern(regexp = "SA|ST|FT", message = "La nature doit être 'SA' (principe actif) ou 'ST' (fraction thérapeutique) ou 'FT' (excipient)")
    /** Nature du composant : SA = Substance Active, ST = Fraction Thérapeutique, FT = Excipient à effet notoire. */
    private String natureComposant;

    @Column(name = "numero_liaison_saft")
    /** Numéro de liaison avec la base SAFT (Substance Active pour une Fiche Thérapeutique). */
    private String numeroLiaisonSaft;
}