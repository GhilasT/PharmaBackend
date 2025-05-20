package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.OrdonnanceCreateRequest;
import l3o2.pharmacie.api.model.entity.Ordonnance;
import l3o2.pharmacie.api.service.OrdonnanceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour gérer les ordonnances.
 */
@RestController
@RequestMapping("/api/ordonnances")
public class OrdonnanceController {
    private final OrdonnanceService service;

    /**
     * Constructeur pour OrdonnanceController.
     *
     * @param service Le service OrdonnanceService à injecter.
     */
    public OrdonnanceController(OrdonnanceService service) {
        this.service = service;
    }

    /**
     * Crée une nouvelle ordonnance.
     *
     * @param dto Données de création de l'ordonnance.
     * @return ResponseEntity contenant l'identifiant de l'ordonnance créée et le statut HTTP CREATED.
     */
    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody OrdonnanceCreateRequest dto) {
        UUID id = service.createOrdonnance(dto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }

    /**
     * Récupère toutes les ordonnances.
     *
     * @return ResponseEntity contenant la liste de toutes les ordonnances et le statut HTTP OK.
     */
    @GetMapping
    public ResponseEntity<List<Ordonnance>> getAll() {
        List<Ordonnance> ordonnances = service.getAllOrdonnances();
        return ResponseEntity.ok(ordonnances);
    }
}