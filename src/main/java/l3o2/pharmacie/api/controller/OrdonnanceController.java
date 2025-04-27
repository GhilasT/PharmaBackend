package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.OrdonnanceCreateRequest;

import l3o2.pharmacie.api.service.OrdonnanceService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/ordonnances")
public class OrdonnanceController {
    private final OrdonnanceService service;
    public OrdonnanceController(OrdonnanceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<UUID> create(@RequestBody OrdonnanceCreateRequest dto) {
        UUID id = service.createOrdonnance(dto);
        return new ResponseEntity<>(id, HttpStatus.CREATED);
    }
}