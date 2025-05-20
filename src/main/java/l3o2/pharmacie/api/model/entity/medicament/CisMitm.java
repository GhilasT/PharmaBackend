package l3o2.pharmacie.api.model.entity.medicament;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * Classe représentant un Médicament d'Intérêt Thérapeutique Majeur (MITM)
 * référencé dans la BDPM.
 * Contient les informations sur les médicaments jugés essentiels pour la prise
 * en charge thérapeutique.
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
@Table(name = "cis_mitm")
public class CisMitm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    /** Clé primaire auto-générée pour identifier un médicament MITM. */
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_cis", nullable = false)
    /** Référence au médicament principal (CIS). */
    private CisBdpm cisBdpm;

    @Column(name = "code_atc", nullable = false, length = 7)
    @NotBlank(message = "Le code ATC est obligatoire")
    @Pattern(regexp = "^[A-Z0-9]{4,7}$", message = "Format ATC invalide")
    /** Code ATC (Anatomical Therapeutic Chemical Classification System) du médicament. */
    private String codeAtc;

    @Column(name = "denomination", nullable = false, length = 500)
    @NotBlank(message = "La dénomination est obligatoire")
    /** Dénomination du médicament (ex: "Paracétamol 500mg, comprimé"). */
    private String denomination;

    @Column(name = "lien_bdpm", nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Le lien BDPM est obligatoire")
    /** Lien vers la fiche BDPM du médicament. */
    private String lienBdpm;
}