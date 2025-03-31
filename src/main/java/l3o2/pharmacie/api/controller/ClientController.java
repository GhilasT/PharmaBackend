package l3o2.pharmacie.api.controller;

import l3o2.pharmacie.api.model.dto.request.ClientCreateRequest;
import l3o2.pharmacie.api.model.dto.response.ClientResponse;
import l3o2.pharmacie.api.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/client")
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


}
