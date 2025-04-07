package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.MedicamentRequest;
import l3o2.pharmacie.api.model.dto.response.MedicamentResponse;
import l3o2.pharmacie.api.model.dto.response.StockMedicamentDTO;
import l3o2.pharmacie.api.service.MedicamentService;
import l3o2.pharmacie.api.service.StockMedicamentService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/medicaments")
@RequiredArgsConstructor
public class MedicamentController {

    private final MedicamentService medicamentService;

    // creation d'un nouveau medicament
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicamentResponse create(@RequestBody MedicamentRequest request) {
        return medicamentService.createMedicament(request);
    }

    // recuperer un medicament  à partir de son ID
    @GetMapping("/id/{id}")
    public MedicamentResponse getOne(@PathVariable Long id) {
        return medicamentService.getMedicamentById(id);
    }


    // maitre a jour
    @PutMapping("/id/{id}")
    public MedicamentResponse update(@PathVariable Long id, @RequestBody MedicamentRequest request) {
        return medicamentService.updateMedicament(id, request);
    }

    // supprimer
    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        medicamentService.deleteMedicament(id);
    }

    // vérifier si ordonnance est requise
    @GetMapping("/id/{id}/ordonnance")
    public boolean checkOrdonnance(@PathVariable Long id) {
        return medicamentService.isOrdonnanceRequise(id);
    }

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

@GetMapping("/search/all")
public ResponseEntity<List<StockMedicamentDTO>> searchAllMedicaments(
    @RequestParam(required = false) String searchTerm
) {
    List<StockMedicamentDTO> results = stockMedicamentService.searchAllMedicaments(searchTerm);
    return ResponseEntity.ok(results);
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