package l3o2.pharmacie.api.util;

import l3o2.pharmacie.api.model.dto.response.VenteResponse;

import java.util.List;

/**
 * Classe utilitaire pour effectuer des opérations de comptabilité de base.
 * @author raphaelcharoze
 */
public class Comptabilite {

    /**
     * Calcule le chiffre d'affaires total à partir d'une liste de ventes.
     * @param ventes La liste des réponses de vente.
     * @return Le chiffre d'affaires total sous forme de Double.
     */
    public static Double calculCA(List<VenteResponse> ventes){
        Double sum = 0.00;
        for (VenteResponse v : ventes){
            sum += v.getMontantTotal();
        }
        return sum;
    }
}
