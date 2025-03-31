package l3o2.pharmacie.api.service;


import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import l3o2.pharmacie.api.repository.MedicamentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CsvImportService {
/* 

private final MedicamentRepository medicamentRepository;
private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

public void importMedicamentsFromCsv(String csvFilePath) {
    log.info("Début de l'importation des médicaments depuis le fichier: {}", csvFilePath);
        
        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                 CSVReader reader = new CSVReader(br)) {
                
                reader.readNext();
                
                String[] line;
                int count = 0;
                List<Medicament> medicaments = new ArrayList<>();
                
                while ((line = reader.readNext()) != null) {
                    try {
                        Medicament medicament = parseCsvLine(line);
                        medicaments.add(medicament);
                        count++;
                        
                        if (count % 100 == 0) {
                            medicamentRepository.saveAll(medicaments);
                            medicaments.clear();
                            log.info("Importation en cours: {} médicaments traités", count);
                        }
                    } catch (Exception e) {
                        log.warn("Erreur lors du traitement de la ligne: {}", String.join(",", line), e);
                    }
                }
                
                if (!medicaments.isEmpty()) {
                    medicamentRepository.saveAll(medicaments);
                }
                
                log.info("Importation terminée. {} médicaments importés avec succès", count);
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Erreur lors de l'importation du fichier CSV", e);
            throw new RuntimeException("Échec de l'importation du fichier CSV", e);
        }
    }
    
    private Medicament parseCsvLine(String[] line) throws ParseException {
        return Medicament.builder()
                .codeCIS(line[0])
                .denomination(line[1])
                .formePharmaceutique(line[2])
                .voiesAdministration(line[3])
                .statutAdministratifAMM(line[4])
                .typeProcedureAMM(line[5])
                .etatCommercialisation(line[6])
                .dateAMM(parseDate(line[7]))
                .titulaire(line[8])
                .statutBdm(getValueOrNull(line, 9))
                .surveillanceRenforcee(getValueOrNull(line, 11))
                .numAutorisationEuropeenne(getValueOrNull(line, 12))
                .quantite(0)
                .build();
    }
    
    private Date parseDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return new Date();
        }
        return DATE_FORMAT.parse(dateStr);
    }
    
    private double parseDouble(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        try {
            return Double.parseDouble(value.replace(",", "."));
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
    
    private String getValueOrNull(String[] array, int index) {
        if (array.length <= index || array[index] == null || array[index].trim().isEmpty()) {
            return null;
        }
        return array[index];
    
        }
    */
}
