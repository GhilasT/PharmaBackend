package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.response.StockMedicamentDTO;
import l3o2.pharmacie.api.service.StockMedicamentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des médicaments basé sur StockMedicament.
 * Fournit les endpoints pour récupérer les informations sur les médicaments.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/medicaments")
public class MedicamentController {
    
    @Autowired
    private StockMedicamentService stockMedicamentService;
    
    /**
     * Récupère tous les médicaments.
     * 
     * @return Liste des médicaments formatée pour l'affichage dans le frontend
     */
    @GetMapping("/{page}")
public ResponseEntity<Map<String, Object>> getPage(
    @PathVariable int page,
    @RequestParam(required = false) String search
) {
    Page<StockMedicamentDTO> resultPage = search != null ? 
        stockMedicamentService.searchMedicamentsPagines(search, page) :
        stockMedicamentService.getMedicamentsPagines(page);

    return ResponseEntity.ok(Map.of(
        "content", resultPage.getContent(),
        "currentPage", resultPage.getNumber(),
        "totalPages", resultPage.getTotalPages(),
        "totalElements", resultPage.getTotalElements()
    ));
}
    
    /**
     * Recherche des médicaments par terme de recherche.
     * 
     * @param searchTerm Terme de recherche (optionnel)
     * @return Liste des médicaments correspondant au terme de recherche
     */
    @GetMapping("/search/{page}")
    public ResponseEntity<Map<String, Object>> searchByLibelleOrCodeCIS(
        @RequestParam String searchTerm, 
        @PathVariable int page
    ) {
        Page<StockMedicamentDTO> resultPage = stockMedicamentService.searchByLibelleOrCodeCIS(searchTerm, page);

        return ResponseEntity.ok(Map.of(
            "content", resultPage.getContent(),
            "currentPage", resultPage.getNumber(),
            "totalPages", resultPage.getTotalPages(),
            "totalElements", resultPage.getTotalElements()
        ));
    }

}
