package l3o2.pharmacie;

import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import jakarta.transaction.Transactional;
import l3o2.pharmacie.api.model.dto.request.*;
import l3o2.pharmacie.api.model.dto.response.*;
import l3o2.pharmacie.api.model.entity.*;
import l3o2.pharmacie.api.repository.MedicamentRepository;
import l3o2.pharmacie.api.service.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import  l3o2.pharmacie.api.model.entity.medicament.*;
import l3o2.pharmacie.api.model.entity.medicament.MedicamentPanier;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    @Transactional
    @Profile("!test") // Ne pas exécuter ce code lors des tests
    public CommandLineRunner initialDataLoader(PharmacienAdjointService pharmacienService,
                                               EmployeService employeService,
                                               CsvImportService csvImportService,
                                               AdministrateurService administrateurService,
                                               ApprentiService apprentiService,
                                               PreparateurService preparateurService,
                                               TitulaireService titulaireService,
                                               ClientService clientService,
                                               FournisseurService fournisseurService,
                                               MedecinService medecinService,
                                               MedicamentService medicamentService,
                                               CisBdpmService cisBdpmService,
                                               CisCpdBdpmService cisCpdBdpmService,
                                               CisCipBdpmService cisCipBdpmService,
                                                MedicamentRepository medicamentRepository,
                                                VenteService venteService,
                                               CommandeService commandeService) {
        return args -> {
            try {
/* 
                //  Ajouter un pharmacien adjoint

                UUID pharmacienAdjointId = null;
                String emailPro1 = "toto.pro@example.com";
                if (!employeService.existsByEmailPro(emailPro1)) {
                    PharmacienAdjointCreateRequest request1 = PharmacienAdjointCreateRequest.builder()
                            .nom("Admin")
                            .prenom("Pharmacien")
                            .email("pharmacien@example.com")
                            .telephone("0612345678")
                            .adresse("1 Rue de la Pharmacie")
                            .password("mdp123")
                            .emailPro(emailPro1)
                            .dateEmbauche(Date.valueOf("2025-03-17"))
                            .salaire(2500.0)
                            .poste(PosteEmploye.PHARMACIEN_ADJOINT)
                            .statutContrat(StatutContrat.CDI)
                            .build();
                    pharmacienService.createPharmacienAdjoint(request1);
                     pharmacienAdjointId = pharmacienService.findByEmailPro(emailPro1).getIdPersonne();
                    System.out.println(" Pharmacien adjoint avec email pro '" + emailPro1 + "' créé avec succès.");
                } else {
                    System.out.println("!! Pharmacien adjoint avec email pro '" + emailPro1 + "' existe déjà.");
                }

                //  Ajouter un autre pharmacien adjoint
                String emailPro2 = "jean.pro@example.com";
                if (!employeService.existsByEmailPro(emailPro2)) {
                    PharmacienAdjointCreateRequest request2 = PharmacienAdjointCreateRequest.builder()
                            .nom("Jean")
                            .prenom("Pharmacien")
                            .email("jean@example.com")
                            .telephone("0612345679")
                            .adresse("2 Rue de la Pharmacie")
                            .password("mdp456")
                            .emailPro(emailPro2)
                            .dateEmbauche(Date.valueOf("2025-03-18"))
                            .salaire(3000.0)
                            .poste(PosteEmploye.PHARMACIEN_ADJOINT)
                            .statutContrat(StatutContrat.CDI)
                            .build();
                    pharmacienService.createPharmacienAdjoint(request2);
                    System.out.println(" Deuxième pharmacien adjoint avec email pro '" + emailPro2 + "' créé avec succès.");
                } else {
                    System.out.println("!! Pharmacien adjoint avec email pro '" + emailPro2 + "' existe déjà.");
                }


                // Ajouter le pharmacien lui meme (avec le meme mail)
                String emailPro3 = "toto.pro@example.com";
                if (!employeService.existsByEmailPro(emailPro3)) {
                    PharmacienAdjointCreateRequest request3 = PharmacienAdjointCreateRequest.builder()
                            .nom("Pharmacien")
                            .prenom("Pro")
                            .email("pharmacien.pro@example.com")
                            .telephone("0612345680")
                            .adresse("3 Rue de la Pharmacie")
                            .password("mdp789")
                            .emailPro(emailPro3)
                            .dateEmbauche(Date.valueOf("2025-03-19"))
                            .salaire(3500.0)
                            .poste(PosteEmploye.PHARMACIEN_ADJOINT)
                            .statutContrat(StatutContrat.CDI)
                            .build();
                    pharmacienService.createPharmacienAdjoint(request3);
                    System.out.println(" Pharmacien adjoint avec email pro '" + emailPro3 + "' créé avec succès.");
                } else {
                    System.out.println("!! Pharmacien adjoint avec email pro '" + emailPro3 + "' existe déjà.");
                }



                // Ajouter un administrateur
                String emailProAdmin = "admin.pro@example.com";
                if (!employeService.existsByEmailPro(emailProAdmin)) {
                    AdministrateurCreateRequest requestAdmin = AdministrateurCreateRequest.builder()
                            .nom("Admin")
                            .prenom("Super")
                            .email("admin@example.com")
                            .telephone("0612345670")
                            .adresse("4 Rue de l'Administration")
                            .password("admin123")
                            .emailPro(emailProAdmin)
                            .dateEmbauche(Date.valueOf("2025-03-17"))
                            .salaire(3500.0)
                            .poste(PosteEmploye.ADMINISTRATEUR)
                            .statutContrat(StatutContrat.CDI)
                            .role("SuperAdmin")
                            .build();
                    administrateurService.createAdministrateur(requestAdmin);
                    System.out.println(" Administrateur avec email pro '" + emailProAdmin + "' créé avec succès.");
                } else {
                    System.out.println("!! Administrateur avec email pro '" + emailProAdmin + "' existe déjà.");
                }

                // Ajouter un titulaire
                String emailProTitulaire = "titulaire.pro@example.com";
                if (!employeService.existsByEmailPro(emailProTitulaire)) {
                    TitulaireCreateRequest requestTitulaire = TitulaireCreateRequest.builder()
                            .nom("Titulaire")
                            .prenom("Super")
                            .email("titulaire@example.com")
                            .telephone("0612345673")
                            .adresse("7 Rue du Titulaire")
                            .password("titulaire123")
                            .emailPro(emailProTitulaire)
                            .dateEmbauche(Date.valueOf("2025-03-20"))
                            .salaire(4000.0)
                            .poste(PosteEmploye.TITULAIRE)
                            .statutContrat(StatutContrat.CDI)
                            .role("Titulaire")
                            .numeroRPPS("123456789")
                            .build();
                    titulaireService.createTitulaire(requestTitulaire);
                    System.out.println("Titulaire avec email pro '" + emailProTitulaire + "' créé avec succès.");
                } else {
                    System.out.println("!! Titulaire avec email pro '" + emailProTitulaire + "' existe déjà.");
                }

                // Ajouter un apprenti
                String emailProApprenti = "apprenti.pro@example.com";
                if (!employeService.existsByEmailPro(emailProApprenti)) {
                    ApprentiCreateRequest requestApprenti = ApprentiCreateRequest.builder()
                            .nom("Apprenti")
                            .prenom("Junior")
                            .email("apprenti@example.com")
                            .telephone("0612345681")
                            .adresse("5 Rue de l'Apprentissage")
                            .password("apprenti123")
                            .emailPro(emailProApprenti)
                            .dateEmbauche(new Date())
                            .salaire(1500.0)
                            .poste(PosteEmploye.APPRENTI)
                            .statutContrat(StatutContrat.CDD)
                            .ecole("Ecole de Pharmacie")
                            .build();
                    apprentiService.createApprenti(requestApprenti);
                    System.out.println("Apprenti avec email pro '" + emailProApprenti + "' créé avec succès.");
                } else {
                    System.out.println("!! Apprenti avec email pro '" + emailProApprenti + "' existe déjà.");
                }
/* 
                // Ajouter un préparateur
                String emailProPrepa = "preparateur.pro@example.com";
                if (!employeService.existsByEmailPro(emailProPrepa)) {
                    PreparateurCreateRequest requestPrepa = PreparateurCreateRequest.builder()
                            .nom("Prepa")
                            .prenom("Exemple")
                            .email("preparateur@example.com")
                            .telephone("0612345682")
                            .adresse("6 Rue du Préparateur")
                            .password("prepa123")
                            .emailPro(emailProPrepa)
                            .dateEmbauche(Date.valueOf("2025-03-19"))
                            .salaire(1800.0)
                            .poste(PosteEmploye.PREPARATEUR)
                            .statutContrat(StatutContrat.CDI)
                            .build();
                    preparateurService.createPreparateur(requestPrepa);
                    System.out.println(" Préparateur avec email pro '" + emailProPrepa + "' créé avec succès.");
                } else {
                    System.out.println("!! Préparateur avec email pro '" + emailProPrepa + "' existe déjà.");
                }





                //  Afficher les matricules des employés avant la suppression
                System.out.println("Liste des matricules des employés avant suppression :");
                employeService.getAllEmployes().forEach(employe -> {
                    System.out.println("Matricule: " + employe.getMatricule());
                });


                // Ajouter un client
                UUID clientId= null ;
                String emailClient = "client@example.com";
                if (!clientService.existsByEmail(emailClient)) {
                    ClientCreateRequest requestClient = ClientCreateRequest.builder()
                            .nom("Client")
                            .prenom("Test")
                            .email(emailClient)
                            .telephone("0612345689")
                            .adresse("10 Rue des Clients")
                            .numeroSecu("1234567890123")
                            .mutuelle("MutuelleTest")
                            .build();
                    ClientResponse clientResponse = clientService.createClient(requestClient);
                    clientId = clientResponse.getIdPersonne();
                    System.out.println("Client avec email '" + emailClient + "' créé avec succès.");
                } else {
                    System.out.println("!! Client avec email '" + emailClient + "' existe déjà.");
                }



                // suprimer le client :
                //clientService.deleteClient(clientId);
                //System.out.println("Client avec ID '" + clientId + "' supprimé avec succès.");


                // Ajouter un fournisseur
                UUID fournisseurId = null ;
                String emailFournisseur = "fournisseur@example.com";
                if (!fournisseurService.existsByEmail(emailFournisseur)) {
                    FournisseurCreateRequest requestFournisseur = FournisseurCreateRequest.builder()
                            .nomSociete("Fournisseur Test")
                            .email(emailFournisseur)
                            .telephone("0612345689")
                            .adresse("10 Rue des Fournisseurs")
                            .fax("123456")
                            .sujetFonction("Fournisseur de Médicaments")
                            .build();
                    FournisseurResponse fournisseurResponse = fournisseurService.createFournisseur(requestFournisseur);
                    fournisseurId = fournisseurResponse.getIdFournisseur();
                    System.out.println("Fournisseur avec email '" + emailFournisseur + "' créé avec succès.");
                } else {
                    System.out.println("!! Fournisseur avec email '" + emailFournisseur + "' existe déjà.");
                }

                //Supprimer le fournisseur
                //fournisseurService.deleteFournisseur(fournisseurId);
                //System.out.println("Fournisseur avec ID '" + fournisseurId + "' supprimé avec succès.");



                // Ajouter un médecin
                UUID medecinId = null ;
                String emailMedecin = "medecin@example.com";
                if (!medecinService.existsByEmail(emailMedecin)) {
                    MedecinCreateRequest requestMedecin = MedecinCreateRequest.builder()
                            .nom("Dr")
                            .prenom("Test")
                            .email(emailMedecin)
                            .telephone("0612345678")
                            .adresse("10 Rue des Médecins")
                            .rpps("123456789")
                            .adeli("987654321")
                            .civilite("Dr")
                            .profession("Médecin généraliste")
                            .specialitePrincipale("Médecine générale")
                            .specialiteSecondaire("Cardiologie")
                            .modeExercice("Libéral")
                            .codePostal("75001")
                            .ville("Paris")
                            .siteWeb("www.medecinparis.fr")
                            .secteur("Public")
                            .conventionnement("Secteur 1")
                            .honoraires("50€")
                            .languesParlees(List.of("Français", "Anglais"))
                            .siret("12345678901234")
                            .dateMiseAJour(LocalDate.now())
                            .build();


                    MedecinResponse medecinResponse = medecinService.createMedecin(requestMedecin);
                    medecinId = medecinResponse.getIdPersonne();
                    System.out.println("Médecin avec email '" + emailMedecin + "' créé avec succès.");
                } else {
                    System.out.println("!! Médecin avec email '" + emailMedecin + "' existe déjà.");
                }



                // le 1 médicament => sous ordonnance

                CisBdpm cis1 = new CisBdpm();
                cis1.setCodeCis("12345678");
                cis1.setDenomination("Médicament Sous Ordonnance");
                cisBdpmService.saveCisBdpm(cis1);

                CisCpdBdpm condA = new CisCpdBdpm();
                condA.setCisBdpm(cis1);
                condA.setConditionPrescription("Condition rendant l'ordonnance obligatoire");
                cisCpdBdpmService.saveCondition(condA);


                CisCipBdpm cip1 = new CisCipBdpm();
                cip1.setCisBdpm(cis1);
                cip1.setCodeCip7("1234567");
                cip1.setCodeCip13("1324");
                cip1.setLibellePresentation("Boîte de 20 comprimés");
                cisCipBdpmService.saveCip(cip1);
                cisCipBdpmService.flush();

                System.out.println("cis1 fait !!");

                MedicamentRequest medRequest1 = MedicamentRequest.builder()
                        .codeCip13("1324")
                        .quantite(10)
                        .numeroLot("LOT-Aspirine")
                        .datePeremption(LocalDate.now().plusMonths(6))
                        .build();

                MedicamentResponse medResp1 = medicamentService.createMedicament(medRequest1);
                System.out.println("Premier médicament créé => ID = " + medResp1.getId());


                // 2 medicamant => sans ordonnance
                CisBdpm cis2 = new CisBdpm();
                cis2.setCodeCis("87654321");
                cis2.setDenomination("Médicament Sans Ordonnance");
                cisBdpmService.saveCisBdpm(cis2);

                CisCipBdpm cip2 = new CisCipBdpm();
                cip2.setCisBdpm(cis2);
                cip2.setCodeCip7("7654321");
                cip2.setCodeCip13("Doliprane");
                cip2.setLibellePresentation("Boîte de 12 sachets");
                cip2.setPrixHT(new BigDecimal("6.50"));
                cisCipBdpmService.saveCip(cip2);

                System.out.println("cis2 fait  ");
                MedicamentRequest medRequest2 = MedicamentRequest.builder()
                        .codeCip13("Doliprane")
                        .quantite(5)
                        .numeroLot("Lot-Doliprane")
                        .datePeremption(LocalDate.now().plusMonths(9))
                        .build();
                MedicamentResponse medResp2 = medicamentService.createMedicament(medRequest2);
                System.out.println("Deuxième médicament créé => ID = " + medResp2.getId());

                boolean ord1 = medicamentService.isOrdonnanceRequise(medResp1.getId());
                System.out.println("Medicament ID=" + medResp1.getId()+ " " +medResp1.getNumeroLot() +" => isOrdonnanceRequise = " + ord1);

                boolean ord2 = medicamentService.isOrdonnanceRequise(medResp2.getId());
                System.out.println("Medicament ID=" + medResp2.getId() +" " + medResp2.getNumeroLot()+" => isOrdonnanceRequise = " + ord2);






                // 3ème médicament => sans ordonnance
                CisBdpm cis3 = new CisBdpm();
                cis3.setCodeCis("33445566");
                cis3.setDenomination("Autre Médicament");
                cisBdpmService.saveCisBdpm(cis3);

                CisCipBdpm cip3 = new CisCipBdpm();
                cip3.setCisBdpm(cis3);
                cip3.setCodeCip7("9876543");
                cip3.setCodeCip13("toto");
                cip3.setLibellePresentation("Boîte de 15 comprimés");
                cip3.setPrixHT(new BigDecimal("3.50"));
                cisCipBdpmService.saveCip(cip3);

                MedicamentRequest medRequest3 = MedicamentRequest.builder()
                        .codeCip13("toto")
                        .quantite(7)
                        .numeroLot("Lot-Bioxin")
                        .datePeremption(LocalDate.now().plusMonths(8))
                        .build();
                MedicamentResponse medResp3 = medicamentService.createMedicament(medRequest3);
                System.out.println("Troisième médicament créé => ID = " + medResp3.getId());


                StockMedicament med1 = medicamentRepository.findById(medResp1.getId())
                        .orElseThrow(() -> new RuntimeException("Medicament 1 introuvable"));
                StockMedicament med2 = medicamentRepository.findById(medResp2.getId())
                        .orElseThrow(() -> new RuntimeException("Medicament 2 introuvable"));
                StockMedicament med3 = medicamentRepository.findById(medResp3.getId())
                        .orElseThrow(() -> new RuntimeException("Medicament 3 introuvable"));

                // Afficher les nouveaux stocks après la vente
                System.out.println(" !! Quantité de stock avant la vente :");
                System.out.println("Medicament 1 "+med1.getNumeroLot()+"- Quantité avant vente: " + med1.getQuantite());
                System.out.println("Medicament 2 - Quantité avant vente: " + med2.getQuantite());
                System.out.println("Medicament 3 - Quantité avant vente: " + med3.getQuantite());



                MedicamentPanier panier1 = new MedicamentPanier(med1, 3);
                MedicamentPanier panier2 = new MedicamentPanier(med2, 3);
                MedicamentPanier panier3 = new MedicamentPanier(med3, 3);

                VenteCreateRequest venteCreateRequest = VenteCreateRequest.builder()
                        .pharmacienAdjointId(pharmacienAdjointId)
                        .clientId(clientId)
                        .medicaments(List.of(panier1, panier2, panier3))
                        .dateVente(new java.util.Date())
                        .modePaiement("Carte")
                        .montantTotal(10.0f)
                        .montantRembourse(30.0f)
                        .build();

                VenteResponse venteResponse = venteService.createVente(venteCreateRequest);
                System.out.println("Vente créée: " + venteResponse.getIdVente());
                if (venteResponse.getNotification() != null) {
                    System.out.println("!! Notification: ");
                    System.out.println( venteResponse.getNotification());
                } else {
                    System.out.println("Pas de notification particulière.");
                }

                // Vérifier la mise à jour du stock après la vente
                StockMedicament updatedMed1 = medicamentRepository.findById(med1.getId())
                        .orElseThrow(() -> new RuntimeException("Medicament 1 après vente introuvable"));
                StockMedicament updatedMed2 = medicamentRepository.findById(med2.getId())
                        .orElseThrow(() -> new RuntimeException("Medicament 2 après vente introuvable"));
                StockMedicament updatedMed3 = medicamentRepository.findById(med3.getId())
                        .orElseThrow(() -> new RuntimeException("Medicament 3 après vente introuvable"));

              //Afficher les nouveaux stocks après la vente
                System.out.println(" ");
                System.out.println("Quantité de stock après la vente :");
                System.out.println("Medicament 1 - Quantité après vente: " + updatedMed1.getQuantite());
                System.out.println("Medicament 2 - Quantité après vente: " + updatedMed2.getQuantite());
                System.out.println("Medicament 3 - Quantité après vente: " + updatedMed3.getQuantite());


                // Créer la commande avec deux lignes de commande
                CommandeCreateRequest commandeRequest = CommandeCreateRequest.builder()
                        .fournisseurId(fournisseurId)
                        .pharmacienAdjointId(pharmacienAdjointId)
                        .ligneCommandes(List.of(
                                // la 1 ligne C
                                LigneCommandeCreateRequest.builder()
                                        .stockMedicamentId(med3.getId())
                                        .quantite(5)
                                        .prixUnitaire(med1.getPresentation().getPrixUnitaireAvecReduction())
                                        .build(),
                                // la 2 ligne de C
                                LigneCommandeCreateRequest.builder()
                                        .stockMedicamentId(med2.getId())
                                        .quantite(10)
                                        .prixUnitaire(med2.getPresentation().getPrixUnitaireAvecReduction())
                                        .build()
                        ))
                        .build();

                // Créer la commande
                CommandeResponse commandeResponse = commandeService.createCommande(commandeRequest);
                UUID commadeId = commandeResponse.getReference();
                System.out.println("Commande créée avec succès: " + commandeResponse);


                System.out.println(" !!! METTRE A JOUR LE STOCK  !!!");
                commandeService.validerReceptionCommande(commadeId);
                CommandeResponse updatedCommandeResponse = commandeService.getCommande(commadeId);
                System.out.println(updatedCommandeResponse.getStatut());


                // Vérifier la mise à jour du stock après la Commande
                StockMedicament uppm1 = medicamentRepository.findById(updatedMed1.getId())
                        .orElseThrow(() -> new RuntimeException("Medicament 1 après vente introuvable"));
                StockMedicament uppm2 = medicamentRepository.findById(updatedMed2.getId())
                        .orElseThrow(() -> new RuntimeException("Medicament 2 après vente introuvable"));
                StockMedicament uppm3 = medicamentRepository.findById(updatedMed3.getId())
                        .orElseThrow(() -> new RuntimeException("Medicament 3 après vente introuvable"));


                //Afficher les nouveaux stocks après la commande
                System.out.println("Quantité de stock après la commande :");
                System.out.println("Medicament 1 - Quantité après commande: " + uppm1.getQuantite());
                System.out.println("Medicament 2 - Quantité après commamnde: " + uppm2.getQuantite());
                System.out.println("Medicament 3 - Quantité après commande: " + uppm3.getQuantite());




                // Supprimer le medecin

                System.out.println("  ");
                medecinService.deleteMedecin(medecinId);
                System.out.println("Médecin avec ID '" + medecinId + "' supprimé avec succès.");


                //  Suppression d'un pharmacien adjoint par matricule (à condition de mettre le bon matricule)
                String matriculeASupprimer = "EMP-PHARMACIEN_ADJOINT-00002";
                //employeService.deleteEmploye(matriculeASupprimer);

                //  Afficher les employés existants après la suppression
                System.out.println("Liste des employés après suppression :");
                employeService.getAllEmployes().forEach(employe -> {
                    System.out.println(employe.getNom() + " " + employe.getPrenom() + " - " + employe.getEmailPro());
                });
*/
            } catch (Exception e) {
                System.err.println(" Erreur lors de l'initialisation des données : " + e.getMessage());
            }
        };
    }
}