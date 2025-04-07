package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.VenteCreateRequest;
import l3o2.pharmacie.api.model.dto.response.VenteResponse;
import l3o2.pharmacie.api.service.VenteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/ventes")
@RequiredArgsConstructor
public class VenteController {

    private final VenteService venteService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VenteResponse createVente(@RequestBody VenteCreateRequest request) {
        System.out.println("📥 Vente reçue : " + request);
        request.getMedicaments().forEach(m -> System.out.println("📦 Médicament reçu - CodeCIS: " + m.getCodeCip13() + ", Quantité: " + m.getQuantite()));
        return venteService.createVente(request);
    }

    @GetMapping("/{id}")
    public VenteResponse getVente(@PathVariable UUID id) {
        return venteService.getById(id);
    }

    @GetMapping
    public List<VenteResponse> getAllVentes() {
        return venteService.getAll();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVente(@PathVariable UUID id) {
        venteService.delete(id);
    }


}