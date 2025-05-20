package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.exceptions.DuplicateRPPSException;
import l3o2.pharmacie.api.exceptions.InvalidDataException;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
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

/**
 * Service gérant la logique métier pour les pharmaciens titulaires.
 * Fournit des méthodes pour créer, récupérer et supprimer des titulaires.
 */
@Service
@RequiredArgsConstructor
public class TitulaireService {

    private final TitulaireRepository titulaireRepository;
    private final EmployeService employeService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Crée un pharmacien titulaire avec toutes les informations nécessaires.
     * 
     * @param request Données du titulaire.
     * @return TitulaireResponse avec les détails du titulaire créé.
     */
    @Transactional
    public TitulaireResponse createTitulaire(TitulaireCreateRequest request) {
        if (titulaireRepository.existsByNumeroRPPS(request.getNumeroRPPS())) {
            throw new DuplicateRPPSException(request.getNumeroRPPS());
        }

        Titulaire titulaire = Titulaire.builder()
                .nom(request.getNom().trim())
                .prenom(request.getPrenom().trim())
                .email(request.getEmail().toLowerCase().trim())
                .telephone(request.getTelephone().replaceAll("\\s+", ""))
                .adresse(request.getAdresse().trim())
                .emailPro(request.getEmailPro().trim())
                .password(passwordEncoder.encode(request.getPassword()))
                .dateEmbauche(request.getDateEmbauche())
                .salaire(request.getSalaire())
                .statutContrat(request.getStatutContrat())
                .diplome(request.getDiplome() != null ? request.getDiplome().trim() : null)
                .role(request.getRole().trim())
                .numeroRPPS(request.getNumeroRPPS().trim())
                .poste(request.getPoste()) // Assure-toi que tu passes le poste ici
                .permissions(List.of("ADMINISTRER","COMMANDER","VENDRE","GERER_ADMIN")) //liste permissions backend
                .build();

        // Génération du matricule
        String baseMatricule = "TITULAIRE";
        titulaire.generateMatricule(baseMatricule);

        try {
            return mapToResponse(titulaireRepository.save(titulaire));
        } catch (DataIntegrityViolationException e) {
            throw new InvalidDataException("Données invalides ou contraintes violées");
        }
    }

    /**
     * Récupère le titulaire actuel.
     * 
     * @return TitulaireResponse avec toutes les informations.
     */
    @Transactional(readOnly = true)
    public TitulaireResponse getTitulaire() {
        Titulaire titulaire = titulaireRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Titulaire", "enregistré", "aucun"));
        return mapToResponse(titulaire);
    }

    /**
     * Supprime un titulaire par son identifiant.
     * 
     * @param id Identifiant du titulaire.
     */
    @Transactional
    public void deleteTitulaire(UUID id) {
        if (!titulaireRepository.existsById(id)) {
            throw new ResourceNotFoundException("Titulaire", "id", id);
        }
        titulaireRepository.deleteById(id);
    }

    /**
     * Convertit une entité Titulaire en DTO TitulaireResponse.
     * 
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
                .role(entity.getRole())
                .numeroRPPS(entity.getNumeroRPPS())
                .build();
    }
}