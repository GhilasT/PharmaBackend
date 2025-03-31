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
        if (medecinRepository.findByRpps(request.getRpps()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un médecin avec ce RPPS existe déjà.");
        }
        if (medecinRepository.findBySiret(request.getSiret()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un médecin avec ce SIRET existe déjà.");
        }

        Medecin medecin = Medecin.builder()
                .nom(request.getNom().trim())
                .prenom(request.getPrenom().trim())
                .email(request.getEmail().trim())
                .telephone(request.getTelephone().trim())
                .adresse(request.getAdresse().trim())
                .rpps(request.getRpps().trim())
                .adeli(request.getAdeli().trim())
                .civilite(request.getCivilite().trim())
                .profession(request.getProfession().trim())
                .specialitePrincipale(request.getSpecialitePrincipale().trim())
                .specialiteSecondaire(request.getSpecialiteSecondaire() != null ? request.getSpecialiteSecondaire().trim() : null)
                .modeExercice(request.getModeExercice().trim())
                .codePostal(request.getCodePostal().trim())
                .ville(request.getVille().trim())
                .siteWeb(request.getSiteWeb() != null ? request.getSiteWeb().trim() : null)
                .secteur(request.getSecteur().trim())
                .conventionnement(request.getConventionnement().trim())
                .honoraires(request.getHonoraires().trim())
                .languesParlees(request.getLanguesParlees() != null ? request.getLanguesParlees() : List.of())
                .siret(request.getSiret().trim())
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