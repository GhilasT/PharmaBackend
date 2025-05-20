package l3o2.pharmacie.api.model.entity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * Classe représentant un médecin dans le système.
 * Hérite de la classe Personne et ajoute des informations spécifiques aux médecins.
 */
@Entity
@Table(name = "medecins")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medecin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false, unique = true)
    /** Identifiant unique pour le médecin. */
    private UUID idMedecin;

    @Column(nullable = false)
    /** Civilité (M. / Mme, etc.). */
    private String civilite;

    @Column(nullable = false)
    /** Nom d'exercice du médecin. */
    private String nomExercice;

    @Column(nullable = false)
    /** Prénom d'exercice du médecin. */
    private String prenomExercice;

    @Column(nullable = false, unique = true)
    /** Numéro RPPS unique du médecin. */
    private String rppsMedecin;

    @Column(nullable = false)
    /** Catégorie professionnelle du médecin (Civil, militaire, étudiant, etc.). */
    private String categorieProfessionnelle;

    @Column(nullable = false)
    /** Profession du médecin (Médecin, Chirurgien, etc.). */
    private String profession;

    @Column(nullable = true)
    /** Mode d'exercice du médecin (libéral, salarié, bénévole). */
    private String modeExercice;

    @Column(nullable = true)
    /** Qualifications, diplômes, autorisations, savoir-faire du médecin. */
    private String qualifications;

    @Column(nullable = true)
    /** Coordonnées des structures d'exercice (cabinet, hôpital, etc.). */
    private String structureExercice;

    @Column(nullable = true)
    /** Fonction dans l'exercice médical (par exemple, Médecin traitant). */
    private String fonctionActivite;

    @Column(nullable = true)
    /** Genre d’activité (consultation, chirurgie, soins palliatifs, etc.). */
    private String genreActivite;
}