package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import l3o2.pharmacie.api.model.dto.response.ClientResponse;
import l3o2.pharmacie.api.service.ClientService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Contrôleur pour gérer les vues liées aux ventes, notamment la recherche d'historique de ventes par client.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/vente")
@PreAuthorize("permitAll()")
public class VenteViewController {

    private final ClientService clientService;
    private static final Logger LOGGER = Logger.getLogger(VenteViewController.class.getName());

    /**
     * Affiche la page d'accueil pour la recherche de ventes (historique client).
     *
     * @param model Modèle pour passer des données à la vue.
     * @return Nom de la vue à afficher (vente-recherche).
     */
    @GetMapping
    public String showVenteHomePage(Model model) {
        // Afficher la page du formulaire de recherche
        return "vente-recherche";
    }
    
    /**
     * Recherche l'historique des ventes d'un client par son numéro de téléphone et redirige vers la page correspondante.
     *
     * @param telephone Numéro de téléphone du client.
     * @param redirectAttributes Attributs pour la redirection, utilisés pour passer des messages d'erreur/succès.
     * @return Chaîne de redirection vers la page d'historique du client ou la page de recherche en cas d'erreur.
     */
    @PostMapping("/recherche")
    public String rechercherHistoriqueVentes(@RequestParam("telephone") String telephone, 
                                            RedirectAttributes redirectAttributes) {
        try {
            // Normaliser le numéro de téléphone (supprimer espaces et caractères spéciaux)
            String normalizedPhone = telephone.replaceAll("\\s+", "").trim();
            
            // Rechercher le client par son numéro de téléphone
            ClientResponse client = clientService.getClientByTelephone(normalizedPhone);
            
            // Rediriger vers la page d'historique des achats du client
            return "redirect:/ventes/client/" + client.getIdPersonne() + "/html";
            
        } catch (ResourceNotFoundException e) {
            LOGGER.log(Level.INFO, "Client non trouvé avec le numéro: {0}", telephone);
            redirectAttributes.addFlashAttribute("error", 
                "Aucun client trouvé avec ce numéro de téléphone. Veuillez vérifier et réessayer.");
            return "redirect:/vente";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur lors de la recherche du client: {0}", e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", 
                "Une erreur est survenue lors de la recherche. Veuillez réessayer plus tard.");
            return "redirect:/vente";
        }
    }
}
