package l3o2.pharmacie.api.model.entity.medicament;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Classe représentant les liens vers les avis de la Commission de la Transparence (CT) de la HAS.
 * Contient les références aux évaluations publiées pour un médicament.
 */
@Getter
@Setter
// Indique que cette classe est une entité JPA.
@Entity
// Définit le nom de la table associée dans la base de données.
@Table(name = "has_liens_page_ct")
public class HasLiensPageCT {

    @Id
    @Column(name = "code_dossier_has", length = 50)
    // Identifiant unique du dossier HAS associé à l'avis de la CT.
    private String codeDossierHas;

    @Column(name = "lien_avis_ct", columnDefinition = "TEXT")
    // Lien vers l'avis publié par la Commission de la Transparence (CT).
    private String lienAvisCt;
}