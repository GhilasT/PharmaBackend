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
    private UUID idMedecin;  // Identifiant unique pour le médecin

    @Column(nullable = false)
    private String civilite;  // Civilité (M. / Mme, etc.)

    @Column(nullable = false)
    private String nomExercice;  // Nom d'exercice

    @Column(nullable = false)
    private String prenomExercice;  // Prénom d'exercice

    @Column(nullable = false, unique = true)
    private String rppsMedecin;  // Numéro RPPS unique

    @Column(nullable = false)
    private String categorieProfessionnelle;  // Civil, militaire, étudiant, etc.

    @Column(nullable = false)
    private String profession;  // Profession (Médecin, Chirurgien, etc.)

    @Column(nullable = true)  // Changer nullable = false en nullable = true
    private String modeExercice;  // Mode d'exercice (libéral, salarié, bénévole)

    @Column(nullable = true)  // Changer nullable = false en nullable = true
    private String qualifications;  // Qualifications, diplômes, autorisations, savoir-faire

    @Column(nullable = true)  // Changer nullable = false en nullable = true
    private String structureExercice;  // Coordonnées des structures d'exercice (cabinet, hôpital, etc.)

    @Column(nullable = true)  // Changer nullable = false en nullable = true
    private String fonctionActivite;  // Fonction dans l'exercice médical (par exemple, Médecin traitant)

    @Column(nullable = true)  // Changer nullable = false en nullable = true
    private String genreActivite;  // Genre d’activité (consultation, chirurgie, soins palliatifs, etc.)
}