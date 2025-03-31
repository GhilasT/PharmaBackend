package l3o2.pharmacie;

import java.sql.Date;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import l3o2.pharmacie.api.model.dto.request.PharmacienAdjointCreateRequest;
import l3o2.pharmacie.api.model.entity.PosteEmploye;
import l3o2.pharmacie.api.model.entity.StatutContrat;
import l3o2.pharmacie.api.service.CsvImportService;
import l3o2.pharmacie.api.service.PharmacienAdjointService;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

   @Bean
public CommandLineRunner initialDataLoader(PharmacienAdjointService pharmacienService, 
                                        CsvImportService csvImportService) {
    return args -> {
        /*PharmacienAdjointCreateRequest request = PharmacienAdjointCreateRequest.builder()
            .nom("Admin")
            .prenom("Pharmacien")
            .email("pharmacien@example.com")
            .telephone("0612345678")
            .adresse("1 Rue de la Pharmacie")
            .password("mdp123")
            .emailPro("pharmacien.pro@example.com") // Email professionnel
            .dateEmbauche(Date.valueOf("2025-03-17")) // Date d'embauche (au format SQL)
            .salaire(2500.0) // Salaire
            .poste(PosteEmploye.PHARMACIEN_ADJOINT) // Enum correspondant au regex "PHARMACIEN_ADJOINT"
            .statutContrat(StatutContrat.CDI) // Statut du contrat (Enum)
            .matricule("PHARMACIEN-ADJ-1") // Matricule
            .build();
        
        pharmacienService.createPharmacienAdjoint(request);*/
        
        //csvImportService.importMedicamentsFromCsv("medicaments.csv");
    };
}
}
