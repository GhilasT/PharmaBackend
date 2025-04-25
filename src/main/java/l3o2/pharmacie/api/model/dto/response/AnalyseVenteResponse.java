package l3o2.pharmacie.api.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnalyseVenteResponse {

    private Map<LocalDate,Double> CAJournalier;
    private Map<LocalDate,Integer> ventesJournalier;
    private Map<LocalDate,Integer> commandesJournalier;

}
