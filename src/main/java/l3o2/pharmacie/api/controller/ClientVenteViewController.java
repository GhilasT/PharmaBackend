package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.response.PharmacienAdjointResponse;
import l3o2.pharmacie.api.model.dto.response.VenteResponse;
import l3o2.pharmacie.api.service.PharmacienAdjointService;
import l3o2.pharmacie.api.service.VenteService;
import l3o2.pharmacie.api.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ventes/client")
public class ClientVenteViewController {

    private final VenteService venteService;
    private final PharmacienAdjointService pharmacienAdjointService; // Ajout du service
    private static final Logger LOGGER = Logger.getLogger(ClientVenteViewController.class.getName());

    @GetMapping("/{clientId}/html")
    public String getVentesClientHtml(@PathVariable UUID clientId, Model model) {
        try {
            List<VenteResponse> ventes = venteService.getByClientId(clientId);
            
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            DateTimeFormatter localDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            Map<UUID, String> venteDates = new HashMap<>();
            Map<UUID, Map<Long, String>> medicamentDates = new HashMap<>();
            Map<UUID, String> pharmacienNoms = new HashMap<>(); // Map pour stocker les noms

            for (VenteResponse vente : ventes) {
                UUID venteId = vente.getIdVente();
                
                // Formatage date de vente
                venteDates.put(venteId, 
                    (vente.getDateVente() != null) 
                        ? dateFormatter.format(vente.getDateVente()) 
                        : "Date inconnue"
                );

                // Récupération nom du pharmacien
                UUID pharmacienId = vente.getPharmacienAdjointId();
                if (pharmacienId != null && !pharmacienNoms.containsKey(pharmacienId)) {
                    try {
                        PharmacienAdjointResponse pharmacien = pharmacienAdjointService.getPharmacienAdjointById(pharmacienId);
                        pharmacienNoms.put(pharmacienId, pharmacien.getNom() + " " + pharmacien.getPrenom());
                    } catch (ResourceNotFoundException e) {
                        pharmacienNoms.put(pharmacienId, "Non spécifié");
                    }
                }

                // Formatage dates des médicaments
                Map<Long, String> medDates = new HashMap<>();
                if (vente.getMedicamentIds() != null) {
                    vente.getMedicamentIds().forEach(med -> {
                        String formattedDate = (med.getDatePeremption() != null)
                            ? med.getDatePeremption().format(localDateFormatter)
                            : "N/A";
                        medDates.put(med.getId(), formattedDate);
                    });
                }
                medicamentDates.put(venteId, medDates);
            }

            model.addAttribute("ventes", ventes);
            model.addAttribute("venteDates", venteDates);
            model.addAttribute("medicamentDates", medicamentDates);
            model.addAttribute("pharmacienNoms", pharmacienNoms);

            return "client-ventes";
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erreur dans getVentesClientHtml: " + e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erreur technique", e);
        }
    }
}