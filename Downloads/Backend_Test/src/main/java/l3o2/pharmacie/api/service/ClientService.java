package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.request.ClientCreateRequest;
import l3o2.pharmacie.api.model.dto.response.ClientResponse;
import l3o2.pharmacie.api.model.entity.Client;
import l3o2.pharmacie.api.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service pour gérer les clients.
 * Contient la logique métier pour la gestion des comptes clients.
 */
@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Création d'un nouveau client.
     * @param request Données du client à créer.
     * @return ClientResponse contenant les informations du client créé.
     */
    public ClientResponse createClient(ClientCreateRequest request) {
        // Normalisation du téléphone
        String normalizedTelephone = request.getTelephone().trim().replaceAll("\\s+", "");

        // Vérification par téléphone : s'il existe déjà, retourner le client existant
        Optional<Client> clientOpt = clientRepository.findByTelephone(normalizedTelephone);
        if (clientOpt.isPresent()) {
            return mapToResponse(clientOpt.get());
        }

        // Traitement de l'email (non obligatoire)
        String normalizedEmail = null;
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            normalizedEmail = request.getEmail().trim().toLowerCase();
            // Vérification de l'unicité de l'email
            if (clientRepository.existsByEmail(normalizedEmail)) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "L'email est déjà utilisé");
            }
        }

        // Création de l'objet Client
        Client client = Client.builder()
                .nom(request.getNom().trim())
                .prenom(request.getPrenom().trim())
                .email(normalizedEmail)  // email peut être null
                .telephone(normalizedTelephone)
                .adresse(request.getAdresse().trim())
                .numeroSecu(request.getNumeroSecu() != null ? request.getNumeroSecu().toUpperCase().trim() : null)
                .mutuelle(request.getMutuelle() != null ? request.getMutuelle().toUpperCase().trim() : null)
                .build();

        try {
            Client clientSaved = clientRepository.save(client);
            return mapToResponse(clientSaved);
        } catch (DataIntegrityViolationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Données dupliquées ou invalides");
        }
    }
    public boolean existsByEmail(String email) {
        return clientRepository.existsByEmail(email.trim().toLowerCase());
    }

    /**
     * Conversion d'un Client en ClientResponse (DTO).
     * @param entity Client à convertir.
     * @return ClientResponse.
     */
    private ClientResponse mapToResponse(Client entity) {
        return ClientResponse.builder()
                .idPersonne(entity.getIdPersonne())
                .nom(entity.getNom())
                .prenom(entity.getPrenom())
                .email(entity.getEmail())
                .telephone(entity.getTelephone())
                .adresse(entity.getAdresse())
                .numeroSecu(entity.getNumeroSecu())
                .mutuelle(entity.getMutuelle())
                .build();
    }

    /**
     * Récupère tous les clients enregistrés.
     * @return Liste des clients sous forme de ClientResponse.
     */
    public List<ClientResponse> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Recherche un client par son ID.
     * @param id Identifiant UUID du client.
     * @return ClientResponse si trouvé.
     * @throws ResponseStatusException si le client n'est pas trouvé.
     */
    public ClientResponse getClientById(UUID id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé"));
        return mapToResponse(client);
    }

    /**
     * Recherche un client par son email.
     * @param email Email du client.
     * @return ClientResponse si trouvé.
     * @throws ResponseStatusException si le client n'est pas trouvé.
     */
    public ClientResponse getClientByEmail(String email) {
        Client client = clientRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé"));
        return mapToResponse(client);
    }
    //supprimer un client
    public void deleteClient(UUID id) {
        if (!clientRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé");
        }
        clientRepository.deleteById(id);
    }

    public ClientResponse getClientByTelephone(String telephone) {
        Client client = clientRepository.findByTelephone(telephone.trim())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Client non trouvé"));
        return mapToResponse(client);
    }
}