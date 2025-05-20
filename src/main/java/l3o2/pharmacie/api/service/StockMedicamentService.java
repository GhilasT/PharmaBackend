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

/**
 * Service gérant la logique métier pour le stock de médicaments.
 * Fournit des méthodes pour rechercher, paginer et récupérer les détails des médicaments en stock.
 */
@Service
public class StockMedicamentService {

    private static final int PAGE_SIZE = 50;
    private final StockMedicamentRepository stockMedicamentRepository;

    private final CisCipBdpmRepository cisCipBdpmRepository; 

    /**
     * Constructeur pour l'injection de dépendances.
     * @param stockMedicamentRepository Le référentiel pour les entités StockMedicament.
     * @param cisCipBdpmRepository Le référentiel pour les entités CisCipBdpm.
     */
    public StockMedicamentService(
            StockMedicamentRepository stockMedicamentRepository,
            CisCipBdpmRepository cisCipBdpmRepository
    ) {
        this.stockMedicamentRepository = stockMedicamentRepository;
        this.cisCipBdpmRepository = cisCipBdpmRepository;
    }

    /**
     * Récupère une page de médicaments en stock.
     * @param page Le numéro de la page à récupérer (commence à 0).
     * @return Une page de {@link StockMedicamentDTO}.
     */
    public Page<StockMedicamentDTO> getMedicamentsPagines(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return stockMedicamentRepository.findAll(pageable)
                .map(this::convertToStockMedicamentDTO);
    }

    /**
     * Recherche tous les médicaments correspondant à un terme de recherche, avec une limite de résultats.
     * La recherche s'effectue sur le libellé de la présentation ou le code CIS.
     * @param searchTerm Le terme à rechercher. Peut être vide ou nul pour récupérer tous les médicaments (limités).
     * @return Une liste de {@link StockMedicamentDTO} correspondant à la recherche.
     */
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

    /**
     * Recherche des médicaments en stock de manière paginée.
     * La recherche s'effectue sur le libellé de la présentation ou le code CIS.
     * @param searchTerm Le terme à rechercher. Si nul ou vide, retourne tous les médicaments paginés.
     * @param page Le numéro de la page à récupérer.
     * @return Une page de {@link StockMedicamentDTO} correspondant à la recherche.
     */
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

    /**
     * Convertit une entité {@link StockMedicament} en son DTO {@link StockMedicamentDTO}.
     * @param stock L'entité StockMedicament à convertir.
     * @return Le DTO {@link StockMedicamentDTO} correspondant.
     */
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

    /**
     * Recherche des médicaments par libellé ou code CIS de manière paginée.
     * @param searchTerm Le terme de recherche.
     * @param page Le numéro de la page.
     * @return Une page de {@link StockMedicamentDTO} correspondant aux critères.
     */
    public Page<StockMedicamentDTO> searchByLibelleOrCodeCIS(String searchTerm, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<StockMedicament> stockPage = stockMedicamentRepository.searchByLibelleOrCodeCIS(searchTerm, pageable);
        return stockPage.map(this::convertToStockMedicamentDTO);
    }

    /**
     * Récupère les détails d'un médicament, y compris ses stocks, par son code CIP13.
     * @param cip13 Le code CIP13 du médicament.
     * @return Un {@link MedicamentDetailsDTO} contenant les détails du médicament et ses stocks.
     * @throws RuntimeException si le code CIP13 n'est pas trouvé.
     */
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

    /**
     * Convertit une liste d'entités {@link StockMedicament} en une liste de DTO {@link StockDetailsDTO}.
     * @param stocks La liste des entités StockMedicament à convertir.
     * @return Une liste de {@link StockDetailsDTO}.
     */
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