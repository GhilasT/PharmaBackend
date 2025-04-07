package l3o2.pharmacie.api.service;

import l3o2.pharmacie.api.model.entity.medicament.CisCpdBdpm;
import l3o2.pharmacie.api.repository.CisCpdBdpmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CisCpdBdpmService {

    private final CisCpdBdpmRepository cisCpdBdpmRepository;

    /**
     * Sauvegarde (ou met à jour) une condition de prescription CisCpdBdpm
     */
    public CisCpdBdpm saveCondition(CisCpdBdpm condition) {
        return cisCpdBdpmRepository.save(condition);
    }


}