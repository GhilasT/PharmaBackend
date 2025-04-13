package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.ClientCreateRequest;
import l3o2.pharmacie.api.model.dto.response.ClientResponse;
import l3o2.pharmacie.api.model.entity.Client;
import l3o2.pharmacie.api.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/client")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    public ResponseEntity<ClientResponse> createClient(@RequestBody ClientCreateRequest request) {
        ClientResponse clientResponse = clientService.createClient(request);
        return new ResponseEntity<>(clientResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ClientResponse>> getAllClients() {
        List<ClientResponse> clients = clientService.getAllClients();
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponse> getClientById(@PathVariable UUID id) {
        ClientResponse clientResponse = clientService.getClientById(id);
        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<ClientResponse> getClientByemail(@PathVariable String email) {
        ClientResponse clientResponse = clientService.getClientByEmail(email);
        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }

    @GetMapping("/telephone/{telephone}")
    public ResponseEntity<ClientResponse> getClientByTelephone(@PathVariable String telephone) {
        ClientResponse clientResponse = clientService.getClientByTelephone(telephone);
        return new ResponseEntity<>(clientResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable UUID id) {
        clientService.deleteClient(id);
        return ResponseEntity.ok().build();
    }
}
