package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.entity.medicament.CisCipBdpm;
import l3o2.pharmacie.api.repository.CisCipBdpmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CisCipBdpmService {

    private final CisCipBdpmRepository cisCipBdpmRepository;

    /**
     * Sauvegarde (ou met à jour) un objet CisCipBdpm en base.
     */
    public CisCipBdpm saveCip(CisCipBdpm cip) {
        return cisCipBdpmRepository.save(cip);
    }
    public void flush() {
        cisCipBdpmRepository.flush();
    }


}