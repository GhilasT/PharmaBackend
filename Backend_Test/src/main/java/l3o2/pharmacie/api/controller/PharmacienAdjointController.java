package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointCreateRequest;
import l3o2.pharmacie.api.model.dto.response.PharmacienAdjointResponse;
import l3o2.pharmacie.api.service.PharmacienAdjointService;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/pharmaciens-adjoints")
@RequiredArgsConstructor
public class PharmacienAdjointController {

    private final PharmacienAdjointService pharmacienAdjointService;

    @PostMapping
    public ResponseEntity<PharmacienAdjointResponse> createPharmacienAdjoint(
            @Valid @RequestBody PharmacienAdjointCreateRequest request) {
        PharmacienAdjointResponse response = pharmacienAdjointService.createPharmacienAdjoint(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<PharmacienAdjointResponse>> getAllPharmaciensAdjoints() {
        List<PharmacienAdjointResponse> responses = pharmacienAdjointService.getAllPharmaciensAdjoints();
    return ResponseEntity.ok(responses);
}
}