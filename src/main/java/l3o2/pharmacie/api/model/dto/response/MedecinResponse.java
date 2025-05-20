package l3o2.pharmacie.api.model.dto.response;

import lombok.*;

import java.util.UUID;

/**
 * DTO représentant la réponse contenant les informations d'un médecin.
 * Utilisé pour éviter d'exposer les entités directement et pour structurer les données du médecin.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MedecinResponse {

    /** L'identifiant unique du médecin. */
    private UUID idMedecin;
    /** La civilité du médecin (ex: M., Mme, Dr). */
    private String civilite;
    /** Le nom d'exercice du médecin. */
    private String nomExercice;
    /** Le prénom d'exercice du médecin. */
    private String prenomExercice;
    /** Le numéro RPPS (Répertoire Partagé des Professionnels de Santé) unique du médecin. */
    private String rppsMedecin;
    /** La catégorie professionnelle du médecin (ex: Civil, Militaire, Étudiant). */
    private String categorieProfessionnelle;
    /** La profession du médecin (ex: Médecin, Chirurgien). */
    private String profession;
    /** Le mode d'exercice du médecin (ex: Libéral, Salarié, Bénévole). */
    private String modeExercice;
    /** Les qualifications, diplômes, autorisations et savoir-faire du médecin. */
    private String qualifications;
    /** Les coordonnées des structures d'exercice du médecin (ex: cabinet, hôpital). */
    private String structureExercice;
    /** La fonction du médecin dans son exercice (ex: Médecin traitant). */
    private String fonctionActivite;
    /** Le genre d'activité médicale exercée (ex: Consultation, Chirurgie, Soins palliatifs). */
    private String genreActivite;
}