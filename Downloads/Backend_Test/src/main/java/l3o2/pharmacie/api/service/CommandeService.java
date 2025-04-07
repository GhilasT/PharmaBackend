package l3o2.pharmacie.api.service;

import jakarta.transaction.Transactional;
import l3o2.pharmacie.api.model.dto.request.CommandeCreateRequest;
import l3o2.pharmacie.api.model.dto.request.LigneCommandeCreateRequest;
import l3o2.pharmacie.api.model.dto.response.CommandeResponse;
import l3o2.pharmacie.api.model.dto.response.LigneCommandeResponse;
import l3o2.pharmacie.api.model.entity.Commande;
import l3o2.pharmacie.api.model.entity.LigneCommande;
import l3o2.pharmacie.api.model.entity.medicament.StockMedicament;
import l3o2.pharmacie.api.repository.CommandeRepository;
import l3o2.pharmacie.api.repository.MedicamentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private  final MedicamentRepository medicamentRepository;

    public CommandeResponse createCommande(CommandeCreateRequest request) {
        Commande commande = Commande.builder()
                .dateCommande(new Date())
                .statut("En attente")
                .pharmacienAdjointId(request.getPharmacienAdjointId())
                .fournisseurId(request.getFournisseurId())
                .ligneCommandes(request.getLigneCommandes().stream()
                        .map(this::mapLigneCommandeToEntity)
                        .collect(Collectors.toList()))
                .build();

        // Affecter la commande à chaque ligne
        commande.getLigneCommandes().forEach(ligne -> ligne.setCommande(commande));


        BigDecimal total;
        for(LigneCommande f : commande.getLigneCommandes()) {
            System.out.println( "le totale !! : "+ f.getMontantLigne());
            System.out.println("et  le prix unitaire : " + f.getPrixUnitaire());
            System.out.println("le medoc : "+f.getStockMedicament());
        }


        BigDecimal montantTotal = commande.getLigneCommandes().stream()
                .map(LigneCommande::getMontantLigne)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        commande.setMontantTotal(montantTotal);

        // Sauvegarder la commande en base
        commandeRepository.save(commande);

        return mapToResponse(commande);
    }

    // Mapper Commande en CommandeResponse
    private CommandeResponse mapToResponse(Commande commande) {
        return CommandeResponse.builder()
                .reference(commande.getReference())
                .dateCommande(commande.getDateCommande())
                .montantTotal(commande.getMontantTotal())
                .statut(commande.getStatut())
                .fournisseurId(commande.getFournisseurId())
                .pharmacienAdjointId(commande.getPharmacienAdjointId())
                .ligneCommandes(commande.getLigneCommandes().stream()
                        .map(this::mapLigneToResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // Mapper une ligne de commande pour la réponse
    private LigneCommandeResponse mapLigneToResponse(LigneCommande ligneCommande) {
        return LigneCommandeResponse.builder()
                .stockMedicament(ligneCommande.getStockMedicament())
                .quantite(ligneCommande.getQuantite())
                .prixUnitaire(ligneCommande.getPrixUnitaire())
                .montantLigne(ligneCommande.getMontantLigne())
                .build();
    }

    private LigneCommande mapLigneCommandeToEntity(LigneCommandeCreateRequest req) {
        StockMedicament stockMedicament = medicamentRepository.findById(req.getStockMedicamentId())
                .orElseThrow(() -> new RuntimeException("StockMedicament non trouvé pour l'ID : " + req.getStockMedicamentId()));

        BigDecimal prixUnitaire = stockMedicament.getPresentation().getPrixUnitaireAvecReduction();
        BigDecimal montantLigne = prixUnitaire.multiply(BigDecimal.valueOf(req.getQuantite()));

        return LigneCommande.builder()
                .stockMedicament(stockMedicament)
                .quantite(req.getQuantite())
                .prixUnitaire(prixUnitaire)
                .montantLigne(montantLigne)
                .build();
    }
    // Récupérer une commande par son identifiant
    @Transactional
    public CommandeResponse getCommande(UUID reference) {
        Commande commande = commandeRepository.findById(reference)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        return mapToResponse(commande);
    }

    @Transactional
    public void validerReceptionCommande(UUID referenceCommande) {
        // Récupérer la commande
        Commande commande = commandeRepository.findById(referenceCommande)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée pour la référence : " + referenceCommande));

        // Pour chaque ligne de commande, incrémenter le stock correspondant
        commande.getLigneCommandes().forEach(ligne -> {
            StockMedicament stock = ligne.getStockMedicament();
            int nouvelleQuantite = stock.getQuantite() + ligne.getQuantite();
            stock.setQuantite(nouvelleQuantite);
            // On sauvegarde le stock mis à jour via le repository dédié
            medicamentRepository.save(stock);
        });


        commande.setStatut("Reçu");

        // Sauvegarder la commande mise à jour
        commandeRepository.save(commande);
    }
}