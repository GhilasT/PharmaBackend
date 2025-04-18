package l3o2.pharmacie.api.util;

import l3o2.pharmacie.api.model.dto.response.VenteResponse;

import java.util.List;

/**
 * classe pour faire de la comptabilité de base
 * @author raphaelcharoze
 */
public class Comptabilite {

    public static double calculCA(List<VenteResponse> ventes){
        double sum = 0;
        for (VenteResponse v : ventes){
            sum += vente.getMontantTotal();
        }
        return sum;
    }
}
