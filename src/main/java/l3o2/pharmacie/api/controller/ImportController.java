package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.service.CsvImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour gérer les opérations d'importation de données.
 */
@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportController {

    private final CsvImportService csvImportService;

    /**
     * Importe tous les fichiers CSV du dossier data.
     *
     * @return Réponse contenant le nombre d'enregistrements importés pour chaque fichier
     */
    @PostMapping("/all")
    public ResponseEntity<Map<String, Object>> importAllCsvFiles() {
        Map<String, Integer> results = csvImportService.importAllCsvFiles();
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation de tous les fichiers CSV réussie");
        response.put("results", results);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Importe uniquement les médicaments depuis le fichier CIS_bdpm.csv.
     *
     * @return Réponse contenant le nombre de médicaments importés
     */
    @PostMapping("/medicaments")
    public ResponseEntity<Map<String, Object>> importMedicaments() {
        int count = csvImportService.importCisBdpm("data/CIS_bdpm.csv");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation des médicaments réussie");
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Importe uniquement les présentations depuis le fichier CIS_CIP_bdpm.csv.
     *
     * @return Réponse contenant le nombre de présentations importées
     */
    @PostMapping("/presentations")
    public ResponseEntity<Map<String, Object>> importPresentations() {
        int count = csvImportService.importCisCipBdpm("data/CIS_CIP_bdpm.csv");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation des présentations réussie");
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Importe uniquement les compositions depuis le fichier CIS_COMPO_bdpm.csv.
     *
     * @return Réponse contenant le nombre de compositions importées
     */
    @PostMapping("/compositions")
    public ResponseEntity<Map<String, Object>> importCompositions() {
        int count = csvImportService.importCisCompoBdpm("data/CIS_COMPO_bdpm.csv");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation des compositions réussie");
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }
    @PostMapping("/stock")
    public ResponseEntity<Map<String, Object>> importStock() {
        int count = csvImportService.importStock("data/stock.csv");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation du stock réussie");
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }
    /**
     * Importe uniquement les conditions de prescription depuis le fichier CIS_CPD_bdpm.csv.
     *
     * @return Réponse contenant le nombre de conditions importées
     */
    @PostMapping("/conditions")
    public ResponseEntity<Map<String, Object>> importConditions() {
        int count = csvImportService.importCisCpdBdpm("data/CIS_CPD_bdpm.csv");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation des conditions de prescription réussie");
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Importe uniquement les groupes génériques depuis le fichier CIS_GENER_bdpm.csv.
     *
     * @return Réponse contenant le nombre de groupes génériques importés
     */
    @PostMapping("/generiques")
    public ResponseEntity<Map<String, Object>> importGeneriques() {
        int count = csvImportService.importCisGenerBdpm("data/CIS_GENER_bdpm.csv");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation des groupes génériques réussie");
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Importe uniquement les avis ASMR depuis le fichier CIS_HAS_ASMR_bdpm.csv.
     *
     * @return Réponse contenant le nombre d'avis ASMR importés
     */
    @PostMapping("/avis-asmr")
    public ResponseEntity<Map<String, Object>> importAvisAsmr() {
        int count = csvImportService.importCisHasAsmr("data/CIS_HAS_ASMR_bdpm.csv");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation des avis ASMR réussie");
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Importe uniquement les avis SMR depuis le fichier CIS_HAS_SMR_bdpm.csv.
     *
     * @return Réponse contenant le nombre d'avis SMR importés
     */
    @PostMapping("/avis-smr")
    public ResponseEntity<Map<String, Object>> importAvisSmr() {
        int count = csvImportService.importCisHasSmr("data/CIS_HAS_SMR_bdpm.csv");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation des avis SMR réussie");
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Importe uniquement les informations importantes depuis le fichier CIS_InfoImportantes.csv.
     *
     * @return Réponse contenant le nombre d'informations importantes importées
     */
    @PostMapping("/infos-importantes")
    public ResponseEntity<Map<String, Object>> importInfosImportantes() {
        int count = csvImportService.importCisInfoImportantes("data/CIS_InfoImportantes.csv");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation des informations importantes réussie");
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Importe uniquement les disponibilités spécifiques depuis le fichier CIS_CIP_Dispo_Spec.csv.
     *
     * @return Réponse contenant le nombre de disponibilités importées
     */
    @PostMapping("/disponibilites")
    public ResponseEntity<Map<String, Object>> importDisponibilites() {
        int count = csvImportService.importCisCipDispoSpec("data/CIS_CIP_Dispo_Spec.csv");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation des disponibilités spécifiques réussie");
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Importe uniquement les médicaments MITM depuis le fichier CIS_MITM.csv.
     *
     * @return Réponse contenant le nombre de médicaments MITM importés
     */
    @PostMapping("/mitm")
    public ResponseEntity<Map<String, Object>> importMitm() {
        int count = csvImportService.importCisMitm("data/CIS_MITM.csv");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation des médicaments MITM réussie");
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }

    /**
     * Importe uniquement les liens vers les pages CT depuis le fichier HAS_LiensPageCT_bdpm.csv.
     *
     * @return Réponse contenant le nombre de liens importés
     */
    @PostMapping("/liens-ct")
    public ResponseEntity<Map<String, Object>> importLiensCt() {
        int count = csvImportService.importHasLiensPageCT("data/HAS_LiensPageCT_bdpm.csv");
        
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Importation des liens vers les pages CT réussie");
        response.put("count", count);
        
        return ResponseEntity.ok(response);
    }
}
