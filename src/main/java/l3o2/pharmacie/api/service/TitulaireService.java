package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.TitulaireCreateRequest;
import l3o2.pharmacie.api.model.dto.response.TitulaireResponse;
import l3o2.pharmacie.api.model.entity.Titulaire;
import l3o2.pharmacie.api.repository.TitulaireRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.Preparateur;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TitulaireService {

    private final TitulaireRepository titulaireRepository;
    private final EmployeService employeService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Crée un pharmacien titulaire avec toutes les informations nécessaires.
     * @param request Données du titulaire.
     * @return TitulaireResponse avec les détails du titulaire créé.
     */
    @Transactional
    public TitulaireResponse createTitulaire(TitulaireCreateRequest request) {
        if (titulaireRepository.existsByNumeroRPPS(request.getNumeroRPPS())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Un titulaire avec ce RPPS existe déjà.");
        }

        Titulaire titulaire = Titulaire.builder()
                .nom(request.getNom().trim())
                .prenom(request.getPrenom().trim())
                .email(request.getEmail().toLowerCase().trim())
                .telephone(request.getTelephone().replaceAll("\\s+", ""))
                .adresse(request.getAdresse().trim())
                .emailPro(request.getEmailPro().trim())
                .password(passwordEncoder.encode(request.getPassword())) //TODO : j'ai un doute, je crois que cela ne marche pas, qu'il ne faut pas encore l'encoder, ou alors c'est refait apres et donc doubler
                .dateEmbauche(request.getDateEmbauche())
                .salaire(request.getSalaire())
                .statutContrat(request.getStatutContrat())
                .diplome(request.getDiplome() != null ? request.getDiplome().trim() : null)
                .numeroRPPS(request.getNumeroRPPS().trim())
                .poste(request.getPoste())  // Assure-toi que tu passes le poste ici
                .permissions(List.of("ADMINISTRER","COMMANDER","VENDRE","GERER_ADMIN")) //liste permissions backend
                .build();

        // Génération du matricule
        String baseMatricule = "TITULAIRE";
        titulaire.generateMatricule(baseMatricule);

        try {
            return mapToResponse(titulaireRepository.save(titulaire));
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données dupliquées ou invalides.");
        }
    }

    /**
     * Récupère le titulaire actuel.
     * @return TitulaireResponse avec toutes les informations.
     */
    @Transactional(readOnly = true)
    public TitulaireResponse getTitulaire() {
        Titulaire titulaire = titulaireRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Titulaire non trouvé"));
        return mapToResponse(titulaire);
    }

    /**
     * Supprime un titulaire par son identifiant.
     * @param id Identifiant du titulaire.
     */
    @Transactional
    public void deleteTitulaire(UUID id) {
        if (!titulaireRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Titulaire non trouvé.");
        }
        titulaireRepository.deleteById(id);
    }

    /**
     * Convertit une entité Titulaire en DTO TitulaireResponse.
     * @param entity L'entité Titulaire.
     * @return DTO contenant toutes les informations.
     */
    private TitulaireResponse mapToResponse(Titulaire entity) {
        return TitulaireResponse.builder()
                .idPersonne(entity.getIdPersonne())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .email(entity.getEmail())
                .telephone(entity.getTelephone())
                .adresse(entity.getAdresse())
                .emailPro(entity.getEmailPro())
                .matricule(entity.getMatricule())
                .dateEmbauche(entity.getDateEmbauche())
                .salaire(entity.getSalaire())
                .statutContrat(entity.getStatutContrat())
                .diplome(entity.getDiplome())
                .numeroRPPS(entity.getNumeroRPPS())
                .build();
    }
}