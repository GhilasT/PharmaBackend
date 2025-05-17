package l3o2.pharmacie.api.model.dto.response;

import lombok.*;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MedecinResponse {

    private UUID idMedecin;  // Identifiant unique du médecin
    private String civilite;  // Civilité (M. / Mme, etc.)
    private String nomExercice;  // Nom d'exercice
    private String prenomExercice;  // Prénom d'exercice
    private String rppsMedecin;  // Numéro RPPS unique
    private String categorieProfessionnelle;  // Civil, militaire, étudiant, etc.
    private String profession;  // Profession (Médecin, Chirurgien, etc.)
    private String modeExercice;  // Mode d'exercice (libéral, salarié, bénévole)
    private String qualifications;  // Qualifications, diplômes, autorisations, savoir-faire
    private String structureExercice;  // Coordonnées des structures d'exercice (cabinet, hôpital, etc.)
    private String fonctionActivite;  // Fonction dans l'exercice médical (par exemple, Médecin traitant)
    private String genreActivite;  // Genre d’activité (consultation, chirurgie, soins palliatifs, etc.)
}