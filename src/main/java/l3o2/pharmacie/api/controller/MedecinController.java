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

@RestController
@RequestMapping("/api/medecins")
public class MedecinController {

    private final MedecinService medecinService;

    @Autowired
    public MedecinController(MedecinService medecinService) {
        this.medecinService = medecinService;
    }

    // Endpoint pour créer un médecin
    @PostMapping
    public ResponseEntity<MedecinResponse> createMedecin(@RequestBody MedecinCreateRequest request) {
        MedecinResponse medecinResponse = medecinService.createMedecin(request);
        return new ResponseEntity<>(medecinResponse, HttpStatus.CREATED);
    }

    // Endpoint pour récupérer un médecin par son ID
    @GetMapping("/{id}")
    public ResponseEntity<MedecinResponse> getMedecinById(@PathVariable UUID id) {
        MedecinResponse medecinResponse = medecinService.getMedecinById(id);
        return new ResponseEntity<>(medecinResponse, HttpStatus.OK);
    }

    // Endpoint pour récupérer tous les médecins
    @GetMapping
    public ResponseEntity<List<MedecinResponse>> getAllMedecins() {
        List<MedecinResponse> medecins = medecinService.getAllMedecins();
        return new ResponseEntity<>(medecins, HttpStatus.OK);
    }

    // Endpoint pour récupérer les médecins paginés
    @GetMapping("/page")
    public ResponseEntity<List<MedecinResponse>> getMedecinsPaginated(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        List<MedecinResponse> medecins = medecinService.getMedecinsPaginated(page, size).getContent();
        return new ResponseEntity<>(medecins, HttpStatus.OK);
    }

    // Endpoint pour mettre à jour un médecin
    @PutMapping("/{id}")
    public ResponseEntity<MedecinResponse> updateMedecin(@PathVariable UUID id, @RequestBody MedecinCreateRequest request) {
        MedecinResponse updatedMedecin = medecinService.updateMedecin(id, request);
        return new ResponseEntity<>(updatedMedecin, HttpStatus.OK);
    }

    // Endpoint pour supprimer un médecin
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedecin(@PathVariable UUID id) {
        medecinService.deleteMedecin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException ex) {
        return new ResponseEntity<>("Médecin non trouvé", HttpStatus.NOT_FOUND);
    }
    // Endpoint pour vérifier si un médecin existe déjà avec un numéro RPPS
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

    // Endpoint pour supprimer un médecin par RPPS
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

    // Endpoint pour rechercher un médecin par nom ou prénom
    @GetMapping("/search")
    public ResponseEntity<List<MedecinResponse>> searchMedecins(@RequestParam String term) {
        List<MedecinResponse> medecins = medecinService.searchMedecins(term);
        return new ResponseEntity<>(medecins, HttpStatus.OK);
    }
}