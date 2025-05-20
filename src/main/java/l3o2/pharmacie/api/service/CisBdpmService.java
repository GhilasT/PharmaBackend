package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.entity.medicament.CisBdpm;
import l3o2.pharmacie.api.repository.CisBdpmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des informations générales des médicaments (CisBdpm).
 * Fournit des méthodes pour interagir avec le repository CisBdpmRepository.
 */
@Service
@RequiredArgsConstructor
public class CisBdpmService {

    private final CisBdpmRepository cisBdpmRepository;

    /**
     * Méthode pour sauvegarder un objet CisBdpm en base de données.
     * 
     * @param cisBdpm L'objet CisBdpm à sauvegarder.
     * @return L'objet CisBdpm sauvegardé.
     */
    public CisBdpm saveCisBdpm(CisBdpm cisBdpm) {
        return cisBdpmRepository.save(cisBdpm);
    }
}