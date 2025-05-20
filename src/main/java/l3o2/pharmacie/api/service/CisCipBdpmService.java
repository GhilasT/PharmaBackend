package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.entity.medicament.CisCipBdpm;
import l3o2.pharmacie.api.repository.CisCipBdpmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service pour la gestion des codes CIP des médicaments (CisCipBdpm).
 * Fournit des méthodes pour interagir avec le repository CisCipBdpmRepository.
 */
@Service
@RequiredArgsConstructor
public class CisCipBdpmService {

    private final CisCipBdpmRepository cisCipBdpmRepository;

    /**
     * Sauvegarde (ou met à jour) un objet CisCipBdpm en base de données.
     * 
     * @param cip L'objet CisCipBdpm à sauvegarder.
     * @return L'objet CisCipBdpm sauvegardé.
     */
    public CisCipBdpm saveCip(CisCipBdpm cip) {
        return cisCipBdpmRepository.save(cip);
    }

    /**
     * Vide le cache de persistance en base de données.
     * Utile pour s'assurer que toutes les modifications en attente sont écrites en base de données.
     */
    public void flush() {
        cisCipBdpmRepository.flush();
    }

}