package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.MedicamentRequest;
import l3o2.pharmacie.api.model.dto.response.MedicamentResponse;
import l3o2.pharmacie.api.service.MedicamentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
    @GetMapping("/{id}")
    public MedicamentResponse getOne(@PathVariable Long id) {
        return medicamentService.getMedicamentById(id);
    }

    // recuperer la liste des medicament
    @GetMapping
    public List<MedicamentResponse> getAll() {
        return medicamentService.getAll();
    }

    // maitre a jour
    @PutMapping("/{id}")
    public MedicamentResponse update(@PathVariable Long id, @RequestBody MedicamentRequest request) {
        return medicamentService.updateMedicament(id, request);
    }

    // supprimer
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        medicamentService.deleteMedicament(id);
    }

    // vérifier si ordonnance est requise
    @GetMapping("/{id}/ordonnance")
    public boolean checkOrdonnance(@PathVariable Long id) {
        return medicamentService.isOrdonnanceRequise(id);
    }
}