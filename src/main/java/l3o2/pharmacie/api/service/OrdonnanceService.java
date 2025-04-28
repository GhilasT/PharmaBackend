package l3o2.pharmacie.api.service;

import jakarta.persistence.EntityNotFoundException;
import l3o2.pharmacie.api.model.dto.request.OrdonnanceCreateRequest;
import l3o2.pharmacie.api.model.dto.request.PrescriptionCreateRequest;
import l3o2.pharmacie.api.model.entity.Client;
import l3o2.pharmacie.api.model.entity.Ordonnance;
import l3o2.pharmacie.api.model.entity.Prescription;
import l3o2.pharmacie.api.repository.ClientRepository;
import l3o2.pharmacie.api.repository.OrdonnanceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OrdonnanceService {
    private final ClientRepository clientRepo;
    private final OrdonnanceRepository ordRepo;

    public OrdonnanceService(ClientRepository clientRepo,
                             OrdonnanceRepository ordRepo) {
        this.clientRepo = clientRepo;
        this.ordRepo = ordRepo;
    }

    @Transactional
    public UUID createOrdonnance(OrdonnanceCreateRequest dto) {
        Client client = clientRepo.findById(dto.getClientId())
                .orElseThrow(() -> new EntityNotFoundException("Client introuvable"));
        // 2) Génère un numéro aléatoire unique entre 100000 et 999999
        String numero;
        do {
            numero = String.format("%06d",
                    ThreadLocalRandom.current().nextInt(0, 1_000_000)
            );
        } while (ordRepo.existsByNumeroOrd(numero));

        // Construction de l'entité Ordonnance
        Ordonnance ord = Ordonnance.builder()
                .dateEmission(dto.getDateEmission())
                .rppsMedecin(dto.getRppsMedecin())
                .numeroOrd(numero)
                .client(client)
                .build();

        // Début du traitement des prescriptions
        List<PrescriptionCreateRequest> prescList = dto.getPrescriptions();
        if (prescList != null && !prescList.isEmpty()) {
            int idx = 0;
            for (PrescriptionCreateRequest p : prescList) {
                if (p == null) {
                    System.err.println("Prescription nulle détectée, on passe");
                    idx++;
                    continue;
                }

                System.out.println("→ Ajout prescription #" + idx + ":");
                System.out.println("    medicament        = " + p.getMedicament());
                System.out.println("    quantitePrescrite = " + p.getQuantitePrescrite());
                System.out.println("    duree             = " + p.getDuree());
                System.out.println("    posologie         = " + p.getPosologie());
                System.out.println("probeleme !!");

                // Construction de l'entité Prescription
                Prescription ent = Prescription.builder()
                        .medicament(p.getMedicament())
                        .posologie(p.getPosologie())
                        .quantitePrescrite(p.getQuantitePrescrite())
                        .duree(p.getDuree())
                        .build();
                System.out.println("le test !");

                // Ajout à la collection gérée par JPA
                ord.getPrescriptions().add(ent);
                System.out.println("le test finale ");
                idx++;
            }
        }

        // Persist de l'ordonnance et de ses prescriptions grâce au cascade
        ordRepo.save(ord);
        System.out.println("L'ID de l'ordonnance créée: " + ord.getIdOrdonnance());
        return ord.getIdOrdonnance();
    }

    public List<Ordonnance> getAllOrdonnances() {
        return ordRepo.findAll();
    }
}
