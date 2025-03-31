package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
import l3o2.pharmacie.api.model.dto.request.MedecinUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;
import l3o2.pharmacie.api.model.entity.Medecin;
import l3o2.pharmacie.api.repository.MedecinRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MedecinService {

    private final MedecinRepository medecinRepository;
    private static final Logger logger = LoggerFactory.getLogger(MedecinService.class);

    public MedecinResponse createMedecin(MedecinCreateRequest request) {

        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "L'email ne peut pas être vide.");
        }

        // Vérification si un médecin existe déjà avec cet email
        if (medecinRepository.findByEmail(request.getEmail().trim()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un médecin avec cet email existe déjà.");
        }

        // Vérification si un médecin existe déjà avec ce RPPS
        if (medecinRepository.findByRpps(request.getRpps()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un médecin avec ce RPPS existe déjà.");
        }

        // Création de l'objet Médecin
        Medecin medecin = Medecin.builder()
                .nom(request.getNom().trim())
                .prenom(request.getPrenom().trim())
                .email(request.getEmail().trim())  // Assurez-vous que l'email est bien passé sans espaces
                .telephone(request.getTelephone().trim())  // Téléphone sans espaces superflus
                .adresse(request.getAdresse().trim())  // Adresse sans espaces inutiles
                .rpps(request.getRpps().trim())  // RPPS sans espaces superflus
                .adeli(request.getAdeli().trim())  // ADELI sans espaces superflus
                .civilite(request.getCivilite().trim())  // Civilité sans espaces superflus
                .profession(request.getProfession().trim())  // Profession sans espaces superflus
                .specialitePrincipale(request.getSpecialitePrincipale().trim())  // Spécialité principale sans espaces
                .specialiteSecondaire(request.getSpecialiteSecondaire() != null ? request.getSpecialiteSecondaire().trim() : null)  // Si secondaire est null, ne rien mettre
                .modeExercice(request.getModeExercice().trim())  // Mode d'exercice sans espaces
                .codePostal(request.getCodePostal().trim())  // Code postal sans espaces
                .ville(request.getVille().trim())  // Ville sans espaces
                .siteWeb(request.getSiteWeb() != null ? request.getSiteWeb().trim() : null)  // Site Web, mais peut être null
                .secteur(request.getSecteur().trim())  // Secteur sans espaces
                .conventionnement(request.getConventionnement().trim())  // Conventionnement sans espaces
                .honoraires(request.getHonoraires().trim())  // Honoraires sans espaces
                .languesParlees(request.getLanguesParlees() != null ? request.getLanguesParlees() : List.of())  // Langues parlées, sinon liste vide
                .siret(request.getSiret().trim())
                .dateMiseAJour(request.getDateMiseAJour())
                .build();

        return mapToResponse(medecinRepository.save(medecin));
    }
    public List<MedecinResponse> getAllMedecins() {
        List<Medecin> medecins = medecinRepository.findAll();

        return medecins.stream()
                .map(this::mapToResponse)
                .toList();
    }

    public MedecinResponse getMedecinById(UUID id) {
        return medecinRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin non trouvé"));
    }

    public MedecinResponse getMedecinByRpps(String rpps) {
        return medecinRepository.findByRpps(rpps)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin non trouvé avec ce RPPS"));
    }

    public MedecinResponse getMedecinBySiret(String siret) {
        return medecinRepository.findBySiret(siret)
                .map(this::mapToResponse)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin non trouvé avec ce SIRET"));
    }

    public List<MedecinResponse> getMedecinsByNomOuPrenom(String query) {
        if (query == null || query.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le paramètre de recherche ne peut pas être vide.");
        }
        return medecinRepository.findByNomContainsIgnoreCaseOrPrenomContainsIgnoreCase(query, query)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // Méthode qui vérifie si un médecin avec un email existe déjà
    public boolean existsByEmail(String email) {
        return medecinRepository.existsByEmail(email.trim());
    }

    public MedecinResponse updateMedecin(UUID id, MedecinUpdateRequest request) {
        Medecin medecin = medecinRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin non trouvé"));

        if (request.getNom() != null) medecin.setNom(request.getNom().trim());
        if (request.getPrenom() != null) medecin.setPrenom(request.getPrenom().trim());
        if (request.getEmail() != null) medecin.setEmail(request.getEmail().trim());
        if (request.getTelephone() != null) medecin.setTelephone(request.getTelephone().trim());
        if (request.getAdresse() != null) medecin.setAdresse(request.getAdresse().trim());

        return mapToResponse(medecinRepository.save(medecin));
    }

    public void deleteMedecin(UUID id) {
        if (!medecinRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Médecin non trouvé");
        }
        medecinRepository.deleteById(id);
    }

    private MedecinResponse mapToResponse(Medecin entity) {
        return MedecinResponse.builder()
                .idPersonne(entity.getIdPersonne())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .email(entity.getEmail())
                .telephone(entity.getTelephone())
                .adresse(entity.getAdresse())
                .rpps(entity.getRpps())
                .adeli(entity.getAdeli())
                .civilite(entity.getCivilite())
                .profession(entity.getProfession())
                .specialitePrincipale(entity.getSpecialitePrincipale())
                .specialiteSecondaire(entity.getSpecialiteSecondaire())
                .modeExercice(entity.getModeExercice())
                .codePostal(entity.getCodePostal())
                .ville(entity.getVille())
                .siteWeb(entity.getSiteWeb())
                .secteur(entity.getSecteur())
                .conventionnement(entity.getConventionnement())
                .languesParlees(entity.getLanguesParlees())
                .siret(entity.getSiret())
                .build();
    }
}