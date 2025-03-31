package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.entity.medicament.CisBdpm;
import l3o2.pharmacie.api.repository.CisBdpmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CisBdpmService {

    private final CisBdpmRepository cisBdpmRepository;

    /**
     * Méthode pour sauvegarder un objet CisBdpm en base.
     */
    public CisBdpm saveCisBdpm(CisBdpm cisBdpm) {
        return cisBdpmRepository.save(cisBdpm);
    }
}