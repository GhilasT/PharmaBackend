package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.CommandeCreateRequest;
import l3o2.pharmacie.api.model.dto.response.CommandeResponse;
import l3o2.pharmacie.api.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Contrôleur pour gérer les commandes dans l'application.
 */
@RestController
@RequestMapping("/api/commandes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CommandeController {

    private final CommandeService commandeService;

    /**
     * Crée une nouvelle commande.
     * @param request Requête contenant les informations nécessaires à la création de la commande.
     * @return La réponse contenant les informations de la commande créée.
     */
    @PostMapping
    public ResponseEntity<CommandeResponse> createCommande(@RequestBody CommandeCreateRequest request) {
        CommandeResponse commandeResponse = commandeService.createCommande(request);
        return new ResponseEntity<>(commandeResponse, HttpStatus.CREATED);
    }

    /**
     * Récupère une commande spécifique par son identifiant unique (UUID).
     * @param reference Identifiant de la commande.
     * @return La réponse contenant les informations de la commande demandée.
     */
    @GetMapping("/{reference}")
    public ResponseEntity<CommandeResponse> getCommande(@PathVariable UUID reference) {
        CommandeResponse commandeResponse = commandeService.getCommande(reference);
        return ResponseEntity.ok(commandeResponse);
    }

}