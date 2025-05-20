package l3o2.pharmacie.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date; // Ce import semble inutilisé, peut être retiré si non nécessaire.
import java.util.Map;

/**
 * DTO représentant la réponse pour l'analyse des ventes.
 * Contient des données agrégées sur le chiffre d'affaires, le nombre de ventes et de commandes par jour.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseVenteResponse {

    /** Une carte associant chaque date (LocalDate) au chiffre d'affaires (Double) correspondant. */
    private Map<LocalDate,Double> CAJournalier;
    /** Une carte associant chaque date (LocalDate) au nombre de ventes (Integer) correspondant. */
    private Map<LocalDate,Integer> ventesJournalier;
    /** Une carte associant chaque date (LocalDate) au nombre de commandes (Integer) correspondant. */
    private Map<LocalDate,Integer> commandesJournalier;

}
