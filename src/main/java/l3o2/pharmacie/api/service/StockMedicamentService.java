package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.dto.response.MedicamentDetailsDTO;
import l3o2.pharmacie.api.model.dto.response.StockDetailsDTO;
import l3o2.pharmacie.api.model.dto.response.StockMedicamentDTO;
import l3o2.pharmacie.api.model.entity.medicament.*;
import l3o2.pharmacie.api.repository.CisCipBdpmRepository;
import l3o2.pharmacie.api.repository.StockMedicamentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockMedicamentService {

    private static final int PAGE_SIZE = 50;
    private final StockMedicamentRepository stockMedicamentRepository;

    private final CisCipBdpmRepository cisCipBdpmRepository; 

    public StockMedicamentService(
            StockMedicamentRepository stockMedicamentRepository,
            CisCipBdpmRepository cisCipBdpmRepository
    ) {
        this.stockMedicamentRepository = stockMedicamentRepository;
        this.cisCipBdpmRepository = cisCipBdpmRepository;
    }

    // Méthode pour la pagination standard
    public Page<StockMedicamentDTO> getMedicamentsPagines(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return stockMedicamentRepository.findAll(pageable)
                .map(this::convertToStockMedicamentDTO);
    }

    public List<StockMedicamentDTO> searchAllMedicaments(String searchTerm) {
        Pageable limit = PageRequest.of(0, 20); // Limite à 50 résultats
        Page<StockMedicament> stockPage;

        if (StringUtils.hasText(searchTerm)) {
            stockPage = stockMedicamentRepository.searchByLibelleOrCodeCIS(searchTerm.toLowerCase(), limit);
        } else {
            stockPage = stockMedicamentRepository.findAll(limit);
        }

        return stockPage.getContent()
                .stream()
                .map(this::convertToStockMedicamentDTO)
                .collect(Collectors.toList());
    }

    // Méthode pour la recherche paginée
    public Page<StockMedicamentDTO> searchMedicamentsPagines(String searchTerm, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        System.out.println("Recherche dans le backend : [" + searchTerm + "] (page " + page + ")");

        Page<StockMedicament> stockPage;

        if (searchTerm == null || searchTerm.isBlank()) {
            stockPage = stockMedicamentRepository.findAll(pageable);
        } else {
            stockPage = stockMedicamentRepository.searchByLibelleOrCodeCIS(searchTerm.toLowerCase(), pageable);
        }

        return stockPage.map(this::convertToStockMedicamentDTO);
    }

    // Conversion d'une entité vers DTO
    public StockMedicamentDTO convertToStockMedicamentDTO(StockMedicament stock) {
        CisBdpm cisBdpm = stock.getPresentation().getCisBdpm();
        CisCipBdpm cisCipBdpm = stock.getPresentation();
        
        long id = stock.getId();//modifier
        
        String codeCIS = cisBdpm.getCodeCis();
        String libelle = cisCipBdpm.getLibellePresentation();
        String denomination = cisBdpm.getDenomination();
        String codeCip13 = cisCipBdpm.getCodeCip13();

        String dosage = "Aucun";
        String reference = "Aucun";
        List<CisCompoBdpm> compositions = cisBdpm.getCompositions();

        if (!compositions.isEmpty()) {
            List<CisCompoBdpm> substancesActives = compositions.stream()
                    .filter(comp -> "SA".equals(comp.getNatureComposant()))
                    .collect(Collectors.toList());

            if (!substancesActives.isEmpty()) {
                CisCompoBdpm composition = substancesActives.get(0);
                dosage = StringUtils.hasText(composition.getDosage()) ? composition.getDosage() : "Aucun";
                reference = StringUtils.hasText(composition.getReferenceDosage()) ? composition.getReferenceDosage()
                        : "Aucun";
            }
        }

        String surOrdonnance = cisBdpm.getConditionsPrescription().isEmpty() ? "Sans" : "Avec";
        int stockQuantite = stock.getQuantite() != null ? stock.getQuantite() : 0;

        // Récupération des informations de prix, agrément et remboursement
        java.math.BigDecimal prixHT = cisCipBdpm.getPrixHT();
        java.math.BigDecimal prixTTC = cisCipBdpm.getPrixTTC();
        java.math.BigDecimal taxe = cisCipBdpm.getTaxe();
        String agrementCollectivites = cisCipBdpm.getAgrementCollectivites();
        String tauxRemboursement = cisCipBdpm.getTauxRemboursement();

        return StockMedicamentDTO.builder()
             .id(id)    // modifier  pour test 
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
                .codeCip13(codeCip13)
                .build();
    }
    public Page<StockMedicamentDTO> searchByLibelleOrCodeCIS(String searchTerm, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<StockMedicament> stockPage = stockMedicamentRepository.searchByLibelleOrCodeCIS(searchTerm, pageable);
        return stockPage.map(this::convertToStockMedicamentDTO);
    }

    public MedicamentDetailsDTO getMedicamentDetailsByCip13(String cip13) {
        // 1. Récupérer la présentation CIP13
        CisCipBdpm presentation = cisCipBdpmRepository.findByCodeCip13(cip13)
                .orElseThrow(() -> new RuntimeException("Code CIP13 non trouvé"));

        // 2. Récupérer les stocks associés
        List<StockMedicament> stocks = stockMedicamentRepository.findByPresentation_CodeCip13(cip13);

        // 3. Construire le DTO
        return MedicamentDetailsDTO.builder()
                .denomination(presentation.getCisBdpm().getDenomination())
                .formePharmaceutique(presentation.getCisBdpm().getFormePharmaceutique())
                .voiesAdministration(presentation.getCisBdpm().getVoiesAdministration())
                .libellePresentation(presentation.getLibellePresentation())
                .tauxRemboursement(presentation.getTauxRemboursement())
                .prixHT(presentation.getPrixHT())
                .prixTTC(presentation.getPrixTTC())
                .taxe(presentation.getTaxe())
                .indicationsRemboursement(presentation.getIndicationsRemboursement())
                .stocks(convertToStockDetailsDTO(stocks))
                .build();
    }

    // Méthode de conversion existante (à ajouter)
    private List<StockDetailsDTO> convertToStockDetailsDTO(List<StockMedicament> stocks) {
        return stocks.stream()
                .map(stock -> StockDetailsDTO.builder()
                        .quantite(stock.getQuantite())
                        .numeroLot(stock.getNumeroLot())
                        .datePeremption(stock.getDatePeremption())
                        .dateMiseAJour(stock.getDateMiseAJour())
                        .seuilAlerte(stock.getSeuilAlerte())
                        .emplacement(stock.getEmplacement())
                        .build())
                .collect(Collectors.toList());
    }

    
}