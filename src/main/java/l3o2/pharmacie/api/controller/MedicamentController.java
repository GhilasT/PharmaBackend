package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.MedicamentRequest;
import l3o2.pharmacie.api.model.dto.response.MedicamentDetailsDTO;
import l3o2.pharmacie.api.model.dto.response.MedicamentResponse;
import l3o2.pharmacie.api.model.dto.response.StockMedicamentDTO;
import l3o2.pharmacie.api.service.MedicamentService;
import l3o2.pharmacie.api.service.StockMedicamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Contrôleur REST pour gérer les médicaments et leur stock.
 * Fournit des endpoints pour les opérations CRUD sur les médicaments
 * ainsi que pour la consultation et la recherche paginée du stock de médicaments.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/medicaments")
@RequiredArgsConstructor
public class MedicamentController {

    // Services
    private final MedicamentService medicamentService;         // Pour les opérations CRUD
    private final StockMedicamentService stockMedicamentService; // Pour l'affichage/pagination

    // ================================================================
    // Endpoints CRUD (Utilisent MedicamentService)
    // ================================================================
    
    /**
     * Crée un nouveau médicament.
     *
     * @param request Données du médicament à créer.
     * @return Le médicament créé.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MedicamentResponse create(@RequestBody MedicamentRequest request) {
        return medicamentService.createMedicament(request);
    }

    /**
     * Récupère un médicament par son identifiant.
     *
     * @param id Identifiant du médicament.
     * @return Le médicament correspondant.
     */
    @GetMapping("/id/{id}")
    public MedicamentResponse getOne(@PathVariable Long id) {
        return medicamentService.getMedicamentById(id);
    }

    /**
     * Met à jour un médicament existant.
     *
     * @param id Identifiant du médicament à mettre à jour.
     * @param request Données de mise à jour du médicament.
     * @return Le médicament mis à jour.
     */
    @PutMapping("/id/{id}")
    public MedicamentResponse update(@PathVariable Long id, @RequestBody MedicamentRequest request) {
        return medicamentService.updateMedicament(id, request);
    }

    /**
     * Supprime un médicament par son identifiant.
     *
     * @param id Identifiant du médicament à supprimer.
     */
    @DeleteMapping("/id/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        medicamentService.deleteMedicament(id);
    }

    /**
     * Vérifie si une ordonnance est requise pour un médicament donné.
     *
     * @param id Identifiant du médicament.
     * @return true si une ordonnance est requise, false sinon.
     */
    @GetMapping("/id/{id}/ordonnance")
    public boolean checkOrdonnance(@PathVariable Long id) {
        return medicamentService.isOrdonnanceRequise(id);
    }

    // ================================================================
    // Endpoints d'affichage (Utilisent StockMedicamentService)
    // ================================================================
    
    /**
     * Récupère une page de médicaments (stock) avec possibilité de recherche.
     *
     * @param page Numéro de la page à récupérer.
     * @param search Terme de recherche optionnel.
     * @return ResponseEntity contenant une map avec la liste des médicaments, les informations de pagination.
     */
    @GetMapping("/{page}")
    public ResponseEntity<Map<String, Object>> getPage(
            @PathVariable int page,
            @RequestParam(required = false) String search) {
        Page<StockMedicamentDTO> resultPage = search != null
                ? stockMedicamentService.searchMedicamentsPagines(search, page)
                : stockMedicamentService.getMedicamentsPagines(page);

        return ResponseEntity.ok(Map.of(
                "content", resultPage.getContent(),
                "currentPage", resultPage.getNumber(),
                "totalPages", resultPage.getTotalPages(),
                "totalElements", resultPage.getTotalElements()));
    }

    /**
     * Recherche tous les médicaments correspondant à un terme de recherche, sans pagination.
     *
     * @param searchTerm Terme de recherche optionnel.
     * @return ResponseEntity contenant la liste des médicaments trouvés.
     */
    @GetMapping("/search/all")
    public ResponseEntity<List<StockMedicamentDTO>> searchAllMedicaments(
            @RequestParam(required = false) String searchTerm) {
        List<StockMedicamentDTO> results = stockMedicamentService.searchAllMedicaments(searchTerm);
        return ResponseEntity.ok(results);
    }

    /**
     * Recherche des médicaments par libellé ou Code CIS, avec pagination.
     *
     * @param searchTerm Terme de recherche.
     * @param page Numéro de la page à récupérer.
     * @return ResponseEntity contenant une map avec la liste des médicaments et les informations de pagination.
     */
    @GetMapping("/search/{page}")
    public ResponseEntity<Map<String, Object>> searchByLibelleOrCodeCIS(
            @RequestParam String searchTerm,
            @PathVariable int page) {
        Page<StockMedicamentDTO> resultPage = stockMedicamentService.searchByLibelleOrCodeCIS(searchTerm, page);
        return ResponseEntity.ok(Map.of(
                "content", resultPage.getContent(),
                "currentPage", resultPage.getNumber(),
                "totalPages", resultPage.getTotalPages(),
                "totalElements", resultPage.getTotalElements()));
    }

    /**
     * Récupère les détails d'un médicament par son code CIP13.
     *
     * @param cip13 Code CIP13 du médicament.
     * @return ResponseEntity contenant les détails du médicament.
     */
    @GetMapping("/code/{cip13}")
    public ResponseEntity<MedicamentDetailsDTO> getDetailsByCip13(@PathVariable String cip13) {
        return ResponseEntity.ok(stockMedicamentService.getMedicamentDetailsByCip13(cip13));
    }
}