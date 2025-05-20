package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.EmployeCreateRequest;
import l3o2.pharmacie.api.model.dto.request.EmployeUpdateRequest;
import l3o2.pharmacie.api.model.dto.response.EmployeResponse;
import l3o2.pharmacie.api.service.EmployeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import l3o2.pharmacie.api.model.entity.Employe;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la gestion des employés en pharmacie.
 */
@RestController
@RequestMapping("/api/employes")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class EmployeController {

    private final EmployeService employeService;

    /**
     * Crée un nouvel employé (tous types confondus).
     * @param request Les informations de l'employé à créer.
     * @return Une ResponseEntity contenant l'EmployeResponse de l'employé créé et le statut HTTP OK.
     */
    @PostMapping
    public ResponseEntity<EmployeResponse> createEmploye(@Valid @RequestBody EmployeCreateRequest request) {
        EmployeResponse employe = employeService.createEmploye(request);
        return ResponseEntity.ok(employe);
    }

    /**
     * Récupère tous les employés.
     * @return Une ResponseEntity contenant la liste de tous les EmployeResponse et le statut HTTP OK.
     */
    @GetMapping
    public ResponseEntity<List<EmployeResponse>> getAllEmployes() {
        List<EmployeResponse> employes = employeService.getAllEmployes();
        return ResponseEntity.ok(employes);
    }

    /**
     * Récupère un employé par son ID.
     * @param id L'identifiant UUID de l'employé.
     * @return Une ResponseEntity contenant l'EmployeResponse de l'employé trouvé et le statut HTTP OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeResponse> getEmployeById(@PathVariable UUID id) {
        EmployeResponse employe = employeService.getEmployeById(id);
        return ResponseEntity.ok(employe);
    }

    /**
     * Met à jour un employé existant.
     * @param id L'identifiant UUID de l'employé à mettre à jour.
     * @param request Les informations de mise à jour de l'employé.
     * @return Une ResponseEntity contenant l'EmployeResponse de l'employé mis à jour et le statut HTTP OK.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeResponse> updateEmploye(@PathVariable UUID id,
                                                         @Valid @RequestBody EmployeUpdateRequest request) {
        EmployeResponse updatedEmploye = employeService.updateEmploye(id, request);
        return ResponseEntity.ok(updatedEmploye);
    }

    /**
     * Met à jour l'email personnel d'un employé.
     * @param id L'identifiant UUID de l'employé.
     * @param email Le nouvel email personnel.
     * @return Une ResponseEntity contenant l'EmployeResponse de l'employé mis à jour et le statut HTTP OK.
     */
    @PutMapping("/email/{id}/{email}")
    public ResponseEntity<EmployeResponse> updateEmployeEmail(@PathVariable UUID id,
                                                              @Valid @PathVariable String email) {
        EmployeResponse updatedEmploye = employeService.updateEmployeEmail(id, email);
        return ResponseEntity.ok(updatedEmploye);
    }

    /**
     * Met à jour l'email professionnel d'un employé.
     * @param id L'identifiant UUID de l'employé.
     * @param emailPro Le nouvel email professionnel.
     * @return Une ResponseEntity contenant l'EmployeResponse de l'employé mis à jour et le statut HTTP OK.
     */
    @PutMapping("/email/{id}/{emailPro}")
    public ResponseEntity<EmployeResponse> updateEmployeEmailPro(@PathVariable UUID id,
                                                                 @Valid @PathVariable String emailPro) {
        EmployeResponse updatedEmploye = employeService.updateEmployeEmailPro(id, emailPro);
        return ResponseEntity.ok(updatedEmploye);
    }

    /**
     * Met à jour le mot de passe d'un employé.
     * @param id L'identifiant UUID de l'employé.
     * @param oldPwd L'ancien mot de passe.
     * @param newPwd1 Le nouveau mot de passe.
     * @param newPwd2 Confirmation du nouveau mot de passe.
     * @return Une ResponseEntity contenant l'EmployeResponse de l'employé mis à jour et le statut HTTP OK.
     */
    @PutMapping("/email/{id}/{oldPwd}/{newPwd1}/{newPwd2}")
    public ResponseEntity<EmployeResponse> updateEmployePassword(@PathVariable UUID id, @Valid @PathVariable String oldPwd,
                                                                 @Valid @PathVariable String newPwd1, @Valid @PathVariable String newPwd2) {
        EmployeResponse updatedEmploye = employeService.updateEmployePassword(id, oldPwd, newPwd1, newPwd2);
        return ResponseEntity.ok(updatedEmploye);
    }

    /**
     * Supprime un employé par son matricule.
     * @param matricule Le matricule de l'employé à supprimer.
     * @return Une ResponseEntity avec le statut HTTP No Content.
     */
    @DeleteMapping("/{matricule}")
    public ResponseEntity<Void> deleteEmploye(@PathVariable String matricule) {
        employeService.deleteEmploye(matricule);
        return ResponseEntity.noContent().build();
    }

    /**
     * Récupère un employé par son email professionnel.
     * @param emailPro L'email professionnel de l'employé.
     * @return Une ResponseEntity contenant l'entité Employe trouvée et le statut HTTP OK.
     */
    @GetMapping("/email/{emailPro}")
    public ResponseEntity<Employe> getEmployeByEmailPro(@PathVariable String emailPro) {
        Employe employe = employeService.getEmployeByEmailPro(emailPro);
        return ResponseEntity.ok(employe);
    }

    /**
     * Récupère uniquement l'ID d'un employé par son email professionnel.
     * @param emailPro L'email professionnel de l'employé.
     * @return Une ResponseEntity contenant l'UUID de l'employé et le statut HTTP OK.
     */
    @GetMapping("/email/{emailPro}/id")
    public ResponseEntity<UUID> getEmployeIdByEmailPro(@PathVariable String emailPro) {
        UUID employeId = employeService.getEmployeIdByEmailPro(emailPro);
        return ResponseEntity.ok(employeId);
    }
}