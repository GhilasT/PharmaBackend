package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.CommandeCreateRequest;
//import l3o2.pharmacie.api.model.dto.request.CommandeUpdateRequest;  
import l3o2.pharmacie.api.model.dto.response.CommandeResponse;
import l3o2.pharmacie.api.model.entity.Commande;
import l3o2.pharmacie.api.service.CommandeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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


    @GetMapping
    public ResponseEntity<List<CommandeResponse>> getAllCommandes() {
        System.out.println("Get all commandes controller");
        List<CommandeResponse> commandes = commandeService.getAll();
        System.out.println(commandes);
        return ResponseEntity.ok(commandes);
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

    @PutMapping("/StatusRecu/{reference}")
    public ResponseEntity<CommandeResponse> updateStatusRecu(@PathVariable String reference) {
        try {
            CommandeResponse commandeResponse = commandeService.validerReceptionCommande(UUID.fromString(reference));
            return ResponseEntity.ok(commandeResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    @PutMapping("/updateIncomplete/{reference}")
    public ResponseEntity<CommandeResponse> updateCommandeIncomplete(
            @PathVariable UUID reference,
            @RequestBody List<Integer> nouvellesQuantites) {
        try {
            CommandeResponse commandeResponse = commandeService.updateCommandeIncomplete(reference, nouvellesQuantites);
            return ResponseEntity.ok(commandeResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    
}