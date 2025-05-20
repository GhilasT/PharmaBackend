package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.entity.medicament.CisCpdBdpm;
import l3o2.pharmacie.api.repository.CisCpdBdpmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des conditions de prescription des médicaments (CisCpdBdpm).
 * Fournit des méthodes pour interagir avec le repository CisCpdBdpmRepository.
 */
@Service
@RequiredArgsConstructor
public class CisCpdBdpmService {

    private final CisCpdBdpmRepository cisCpdBdpmRepository;

    /**
     * Sauvegarde (ou met à jour) une condition de prescription CisCpdBdpm.
     * 
     * @param condition L'objet CisCpdBdpm à sauvegarder.
     * @return L'objet CisCpdBdpm sauvegardé.
     */
    public CisCpdBdpm saveCondition(CisCpdBdpm condition) {
        return cisCpdBdpmRepository.save(condition);
    }

}