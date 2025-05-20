package l3o2.pharmacie.api.controller;

import jakarta.persistence.EntityNotFoundException;
import l3o2.pharmacie.api.model.dto.request.MedecinCreateRequest;
import l3o2.pharmacie.api.model.dto.response.MedecinResponse;
import l3o2.pharmacie.api.service.MedecinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour gérer les médecins.
 * Fournit des endpoints pour créer, récupérer, mettre à jour et supprimer des médecins,
 * ainsi que pour rechercher des médecins et vérifier leur existence par RPPS.
 */
@RestController
@RequestMapping("/api/medecins")
public class MedecinController {

    private final MedecinService medecinService;

    /**
     * Constructeur pour MedecinController.
     *
     * @param medecinService Le service MedecinService à injecter.
     */
    @Autowired
    public MedecinController(MedecinService medecinService) {
        this.medecinService = medecinService;
    }

    /**
     * Crée un nouveau médecin.
     *
     * @param request Données de création du médecin.
     * @return ResponseEntity contenant le médecin créé et le statut HTTP CREATED.
     */
    @PostMapping
    public ResponseEntity<MedecinResponse> createMedecin(@RequestBody MedecinCreateRequest request) {
        MedecinResponse medecinResponse = medecinService.createMedecin(request);
        return new ResponseEntity<>(medecinResponse, HttpStatus.CREATED);
    }

    /**
     * Récupère un médecin par son identifiant.
     *
     * @param id Identifiant UUID du médecin.
     * @return ResponseEntity contenant le médecin trouvé et le statut HTTP OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MedecinResponse> getMedecinById(@PathVariable UUID id) {
        MedecinResponse medecinResponse = medecinService.getMedecinById(id);
        return new ResponseEntity<>(medecinResponse, HttpStatus.OK);
    }

    /**
     * Récupère la liste de tous les médecins.
     *
     * @return ResponseEntity contenant la liste des médecins et le statut HTTP OK.
     */
    @GetMapping
    public ResponseEntity<List<MedecinResponse>> getAllMedecins() {
        List<MedecinResponse> medecins = medecinService.getAllMedecins();
        return new ResponseEntity<>(medecins, HttpStatus.OK);
    }

    /**
     * Récupère une liste paginée de médecins.
     *
     * @param page Numéro de la page (commence à 0).
     * @param size Nombre d'éléments par page.
     * @return ResponseEntity contenant la liste des médecins pour la page demandée et le statut HTTP OK.
     */
    @GetMapping("/page")
    public ResponseEntity<List<MedecinResponse>> getMedecinsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        List<MedecinResponse> medecins = medecinService.getMedecinsPaginated(page, size).getContent();
        return new ResponseEntity<>(medecins, HttpStatus.OK);
    }

    /**
     * Met à jour les informations d'un médecin existant.
     *
     * @param id Identifiant UUID du médecin à mettre à jour.
     * @param request Données de mise à jour du médecin.
     * @return ResponseEntity contenant le médecin mis à jour et le statut HTTP OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MedecinResponse> updateMedecin(@PathVariable UUID id, @RequestBody MedecinCreateRequest request) {
        MedecinResponse updatedMedecin = medecinService.updateMedecin(id, request);
        return new ResponseEntity<>(updatedMedecin, HttpStatus.OK);
    }

    /**
     * Supprime un médecin par son identifiant.
     *
     * @param id Identifiant UUID du médecin à supprimer.
     * @return ResponseEntity avec le statut HTTP NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedecin(@PathVariable UUID id) {
        medecinService.deleteMedecin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Gère les exceptions de type EntityNotFoundException.
     *
     * @param ex L'exception EntityNotFoundException levée.
     * @return ResponseEntity contenant un message d'erreur et le statut HTTP NOT_FOUND.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>("Médecin non trouvé", HttpStatus.NOT_FOUND);
    }

    /**
     * Vérifie l'existence d'un médecin par son numéro RPPS et retourne ses informations s'il existe.
     *
     * @param rpps Numéro RPPS du médecin.
     * @return ResponseEntity contenant les informations du médecin si trouvé, ou le statut NOT_FOUND/INTERNAL_SERVER_ERROR sinon.
     */
    @GetMapping("/rpps/{rpps}")
    public ResponseEntity<MedecinResponse> checkMedecinByRpps(@PathVariable String rpps) {
        try {
            MedecinResponse medecinResponse = medecinService.checkMedecinByRpps(rpps);
            if (medecinResponse != null) {
                return new ResponseEntity<>(medecinResponse, HttpStatus.OK); // Si trouvé, renvoyer les données du médecin
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Aucun médecin trouvé
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // En cas d'erreur
        }
    }

    /**
     * Supprime un médecin par son numéro RPPS.
     *
     * @param rpps Numéro RPPS du médecin à supprimer.
     * @return ResponseEntity avec le statut NO_CONTENT en cas de succès, NOT_FOUND si non trouvé, ou INTERNAL_SERVER_ERROR en cas d'autre erreur.
     */
    @DeleteMapping("/rpps/{rpps}")
    public ResponseEntity<Void> deleteMedecinByRpps(@PathVariable String rpps) {
        try {
            // Appel à la méthode de suppression par RPPS dans le service
            medecinService.deleteMedecinByRpps(rpps);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Réponse de succès
        } catch (RuntimeException e) {
            // Si le médecin n'est pas trouvé, renvoyer une erreur 404
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            // Si une autre erreur survient, renvoyer une erreur 500
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Recherche des médecins par nom ou prénom.
     *
     * @param term Terme de recherche.
     * @return ResponseEntity contenant la liste des médecins correspondants et le statut HTTP OK.
     */
    @GetMapping("/search")
    public ResponseEntity<List<MedecinResponse>> searchMedecins(@RequestParam String term) {
        List<MedecinResponse> medecins = medecinService.searchMedecins(term);
        return new ResponseEntity<>(medecins, HttpStatus.OK);
    }
}