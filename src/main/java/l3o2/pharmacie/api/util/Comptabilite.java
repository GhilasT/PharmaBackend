package l3o2.pharmacie.api.util;

import l3o2.pharmacie.api.model.dto.response.VenteResponse;

import java.util.List;

/**
 * classe pour faire de la comptabilité de base
 * @author raphaelcharoze
 */
public class Comptabilite {

    public static Double calculCA(List<VenteResponse> ventes){
        Double sum = 0.00;
        for (VenteResponse v : ventes){
            sum += v.getMontantTotal();
        }
        return sum;
    }
}
