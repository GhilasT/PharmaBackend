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

    /**
     * Récupère toutes les commandes.
     * @return Une ResponseEntity contenant la liste de toutes les CommandeResponse et le statut HTTP OK.
     */
    @GetMapping
    public ResponseEntity<List<CommandeResponse>> getAllCommandes() {
        System.out.println("Get all commandes controller");
        List<CommandeResponse> commandes = commandeService.getAll();
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

    /**
     * Met à jour le statut d'une commande à "Reçue".
     * @param reference La référence de la commande à mettre à jour.
     * @return Une ResponseEntity contenant la CommandeResponse mise à jour ou un statut NOT_FOUND si la commande n'existe pas.
     */
    @PutMapping("/StatusRecu/{reference}")
    public ResponseEntity<CommandeResponse> updateStatusRecu(@PathVariable String reference) {
        try {
            CommandeResponse commandeResponse = commandeService.validerReceptionCommande(UUID.fromString(reference));
            return ResponseEntity.ok(commandeResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    /**
     * Met à jour une commande marquée comme incomplète avec de nouvelles quantités.
     * @param reference La référence de la commande à mettre à jour.
     * @param nouvellesQuantites La liste des nouvelles quantités pour les médicaments de la commande.
     * @return Une ResponseEntity contenant la CommandeResponse mise à jour ou un statut NOT_FOUND si une erreur survient.
     */
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