package l3o2.pharmacie.api.model.entity.medicament;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;
import java.util.Date;

/**
 * Classe représentant les informations importantes concernant un médicament référencé dans la BDPM.
 * Contient des informations temporaires ou critiques liées à un médicament.
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
@Table(name = "cis_info_importantes")
public class CisInfoImportantes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Identifiant unique de l'information importante.
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code_cis", nullable = false)
    // Référence au médicament principal (CIS).
    private CisBdpm cisBdpm;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_debut", nullable = false)
    // Date de début de validité de l'information importante.
    private Date dateDebut;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_fin")
    // Date de fin de validité de l'information importante (si applicable).
    private Date dateFin;

    @Lob
    @Column(name = "texte_lien_html", nullable = false, columnDefinition = "TEXT")
    // Texte contenant des liens HTML relatifs à l'information importante.
    private String texteLienHtml;
}