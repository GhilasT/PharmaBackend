package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.EmployeCreateRequest;
import l3o2.pharmacie.api.model.dto.request.EmployeUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.EmployeResponse;
import l3o2.pharmacie.api.service.EmployeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des employés en pharmacie.
 */
@RestController
@RequestMapping("/api/employes")
@RequiredArgsConstructor
public class EmployeController {

    private final EmployeService employeService;

    /**
     * Création d'un nouvel employé (tous types confondus).
     * @param request Informations de l'employé à créer.
     * @return L'employé créé.
     */
    @PostMapping
    public ResponseEntity<EmployeResponse> createEmploye(@Valid @RequestBody EmployeCreateRequest request) {
        EmployeResponse employe = employeService.createEmploye(request);
        return ResponseEntity.ok(employe);
    }

    /**
     * Récupère tous les employés.
     * @return Liste des employés.
     */
    @GetMapping
    public ResponseEntity<List<EmployeResponse>> getAllEmployes() {
        List<EmployeResponse> employes = employeService.getAllEmployes();
        return ResponseEntity.ok(employes);
    }

    /**
     * Récupère un employé par son ID.
     * @param id Identifiant de l'employé.
     * @return L'employé trouvé.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeResponse> getEmployeById(@PathVariable UUID id) {
        EmployeResponse employe = employeService.getEmployeById(id);
        return ResponseEntity.ok(employe);
    }

    /**
     * Mise à jour d'un employé existant.
     * @param id Identifiant de l'employé à mettre à jour.
     * @param request Données mises à jour.
     * @return L'employé mis à jour.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeResponse> updateEmploye(@PathVariable UUID id,
                                                         @Valid @RequestBody EmployeUpdateRequest request) {
        EmployeResponse updatedEmploye = employeService.updateEmploye(id, request);
        return ResponseEntity.ok(updatedEmploye);
    }

    /**
     * Suppression d'un employé.
     * @param id Identifiant de l'employé à supprimer.
     */
    @DeleteMapping("/{matricule}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable String matricule) {
        employeService.deleteEmploye(matricule);
        return ResponseEntity.noContent().build();
    }
}