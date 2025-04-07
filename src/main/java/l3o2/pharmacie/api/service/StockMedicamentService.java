package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.response.StockMedicamentDTO;
import l3o2.pharmacie.api.model.entity.medicament.*;
import l3o2.pharmacie.api.repository.StockMedicamentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockMedicamentService {

    private static final int PAGE_SIZE = 50;
    private final StockMedicamentRepository stockMedicamentRepository;

    @Autowired
    public StockMedicamentService(StockMedicamentRepository stockMedicamentRepository) {
        this.stockMedicamentRepository = stockMedicamentRepository;
    }

    // Méthode pour la pagination standard
    public Page<StockMedicamentDTO> getMedicamentsPagines(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return stockMedicamentRepository.findAll(pageable)
            .map(this::convertToStockMedicamentDTO);
    }

    // Méthode pour la recherche paginée
    public Page<StockMedicamentDTO> searchMedicamentsPagines(String searchTerm, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        // ✅ LOG pour vérifier le searchTerm
        System.out.println("📥 Recherche dans le backend : [" + searchTerm + "] (page " + page + ")");

        Page<StockMedicament> stockPage;
    
        if (searchTerm == null || searchTerm.isBlank()) {
            stockPage = stockMedicamentRepository.findAll(pageable);
        } else {
            stockPage = stockMedicamentRepository.searchByLibelleOrCodeCIS(searchTerm.toLowerCase(), pageable);
        }
    
        return stockPage.map(this::convertToStockMedicamentDTO);
    }
    

    // Conversion d'une entité vers DTO
    private StockMedicamentDTO convertToStockMedicamentDTO(StockMedicament stock) {
        CisBdpm cisBdpm = stock.getPresentation().getCisBdpm();
        CisCipBdpm cisCipBdpm = stock.getPresentation();
        
        String codeCIS = cisBdpm.getCodeCis();
        String libelle = cisCipBdpm.getLibellePresentation();
        String denomination = cisBdpm.getDenomination();

        String dosage = "Aucun";
        String reference = "Aucun";
        List<CisCompoBdpm> compositions = cisBdpm.getCompositions();
        
        if (!compositions.isEmpty()) {
            List<CisCompoBdpm> substancesActives = compositions.stream()
                .filter(comp -> "SA".equals(comp.getNatureComposant()))
                .collect(Collectors.toList());
            
                if (!substancesActives.isEmpty()) {
                    CisCompoBdpm composition = substancesActives.get(0);
                    dosage = StringUtils.hasText(composition.getDosage()) ? 
                            composition.getDosage() : "Aucun";
                    reference = StringUtils.hasText(composition.getReferenceDosage()) ? 
                            composition.getReferenceDosage() : "Aucun";
                }
        }

        String surOrdonnance = cisBdpm.getConditionsPrescription().isEmpty() ? 
            "Sans" : "Avec";
        int stockQuantite = stock.getQuantite() != null ? stock.getQuantite() : 0;
        
        // Récupération des informations de prix, agrément et remboursement
        java.math.BigDecimal prixHT = cisCipBdpm.getPrixHT();
        java.math.BigDecimal prixTTC = cisCipBdpm.getPrixTTC();
        java.math.BigDecimal taxe = cisCipBdpm.getTaxe();
        String agrementCollectivites = cisCipBdpm.getAgrementCollectivites();
        String tauxRemboursement = cisCipBdpm.getTauxRemboursement();

        return StockMedicamentDTO.builder()
            .codeCIS(codeCIS)
            .libelle(libelle)
            .denomination(denomination)
            .dosage(dosage)
            .reference(reference)
            .surOrdonnance(surOrdonnance)
            .stock(stockQuantite)
            .prixHT(prixHT)
            .prixTTC(prixTTC)
            .taxe(taxe)
            .agrementCollectivites(agrementCollectivites)
            .tauxRemboursement(tauxRemboursement)
            .build();
    }
    public Page<StockMedicamentDTO> searchByLibelleOrCodeCIS(String searchTerm, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<StockMedicament> stockPage = stockMedicamentRepository.searchByLibelleOrCodeCIS(searchTerm, pageable);
        return stockPage.map(this::convertToStockMedicamentDTO);
    }
    
}