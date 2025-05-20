package l3o2.pharmacie.api.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * DTO pour la création d'un médecin.
 * Contient les informations nécessaires pour enregistrer un nouveau médecin dans le système.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MedecinCreateRequest {

    /**
     * La civilité du médecin (ex: M., Mme.).
     */
    private String civilite;
    /**
     * Le nom d'exercice du médecin.
     */
    private String nomExercice;
    /**
     * Le prénom d'exercice du médecin.
     */
    private String prenomExercice;
    /**
     * Le numéro RPPS (Répertoire Partagé des Professionnels de Santé) unique du médecin.
     */
    private String rppsMedecin;
    /**
     * La catégorie professionnelle du médecin (ex: Civil, Militaire).
     */
    private String categorieProfessionnelle;
    /**
     * La profession du médecin (ex: Médecin généraliste, Chirurgien).
     */
    private String profession;
    /**
     * Le mode d'exercice du médecin (ex: Libéral, Salarié).
     */
    private String modeExercice;
    /**
     * Les qualifications, diplômes et autorisations du médecin.
     */
    private String qualifications;
    /**
     * Les coordonnées des structures d'exercice du médecin (ex: cabinet, hôpital).
     */
    private String structureExercice;
    /**
     * La fonction ou l'activité principale du médecin (ex: Médecin traitant).
     */
    private String fonctionActivite;
    /**
     * Le genre d'activité du médecin (ex: Consultation, Chirurgie).
     */
    private String genreActivite;
}