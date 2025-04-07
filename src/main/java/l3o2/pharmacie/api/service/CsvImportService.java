package l3o2.pharmacie.api.service;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import l3o2.pharmacie.api.model.entity.medicament.*;
import l3o2.pharmacie.api.repository.CisBdpmRepository;
import l3o2.pharmacie.api.util.HibernateSessionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.hibernate.FlushMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import org.hibernate.FlushMode;
import org.hibernate.Session;
import l3o2.pharmacie.api.util.HibernateSessionUtil;

/**
 * Service pour l'importation des données CSV dans la base de données.
 * Gère l'importation de tous les fichiers CSV du dossier data.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CsvImportService {

    private final CisBdpmRepository cisBdpmRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private static final SimpleDateFormat DATE_FORMAT_FR = new SimpleDateFormat("dd/MM/yyyy");
    private static final SimpleDateFormat DATE_FORMAT_ISO = new SimpleDateFormat("yyyyMMdd");
    private static final String CSV_FOLDER = "data/";

    /**
     * Importe tous les fichiers CSV du dossier data.
     *
     * @return Map contenant le nombre d'enregistrements importés pour chaque fichier
     */
    @Transactional
    public Map<String, Integer> importAllCsvFiles() {
        Map<String, Integer> results = new HashMap<>();

        // Importer les médicaments (CIS_bdpm.csv) en premier car les autres entités en dépendent
        results.put("CIS_bdpm.csv", importCisBdpm(CSV_FOLDER + "CIS_bdpm.csv"));

        // Importer les autres fichiers CSV un par un pour éviter les problèmes de session
        results.put("CIS_CIP_bdpm.csv", importCisCipBdpm(CSV_FOLDER + "CIS_CIP_bdpm.csv"));
        results.put("CIS_COMPO_bdpm.csv", importCisCompoBdpm(CSV_FOLDER + "CIS_COMPO_bdpm.csv"));
        results.put("CIS_CPD_bdpm.csv", importCisCpdBdpm(CSV_FOLDER + "CIS_CPD_bdpm.csv"));
        results.put("CIS_GENER_bdpm.csv", importCisGenerBdpm(CSV_FOLDER + "CIS_GENER_bdpm.csv"));
        results.put("CIS_HAS_ASMR_bdpm.csv", importCisHasAsmr(CSV_FOLDER + "CIS_HAS_ASMR_bdpm.csv"));
        results.put("CIS_HAS_SMR_bdpm.csv", importCisHasSmr(CSV_FOLDER + "CIS_HAS_SMR_bdpm.csv"));
        results.put("CIS_InfoImportantes.csv", importCisInfoImportantes(CSV_FOLDER + "CIS_InfoImportantes.csv"));
        results.put("CIS_CIP_Dispo_Spec.csv", importCisCipDispoSpec(CSV_FOLDER + "CIS_CIP_Dispo_Spec.csv"));
        results.put("CIS_MITM.csv", importCisMitm(CSV_FOLDER + "CIS_MITM.csv"));
        results.put("HAS_LiensPageCT_bdpm.csv", importHasLiensPageCT(CSV_FOLDER + "HAS_LiensPageCT_bdpm.csv"));

        return results;
    }

    /**
     * Importe les médicaments depuis le fichier CIS_bdpm.csv.
     *
     * @param csvFilePath Chemin du fichier CSV dans les ressources
     * @return Nombre de médicaments importés avec succès
     */
    @Transactional
    public int importCisBdpm(String csvFilePath) {
        log.info("Début de l'importation des médicaments depuis le fichier: {}", csvFilePath);

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                 CSVReader reader = new CSVReader(br)) {

                // Ignorer la ligne d'en-tête
                reader.readNext();

                String[] line;
                int count = 0;
                List<CisBdpm> medicaments = new ArrayList<>();

                while ((line = reader.readNext()) != null) {
                    try {
                        CisBdpm medicament = parseCisBdpmLine(line);
                        medicaments.add(medicament);
                        count++;

                        // Enregistrer par lots de 100 pour optimiser les performances
                        if (count % 100 == 0) {
                            cisBdpmRepository.saveAll(medicaments);
                            medicaments.clear();
                            log.info("Importation CIS_bdpm en cours: {} médicaments traités", count);
                        }
                    } catch (Exception e) {
                        log.warn("Erreur lors du traitement de la ligne: {}", String.join(",", line), e);
                    }
                }

                // Enregistrer les médicaments restants
                if (!medicaments.isEmpty()) {
                    cisBdpmRepository.saveAll(medicaments);
                }

                log.info("Importation CIS_bdpm terminée. {} médicaments importés avec succès", count);
                return count;
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Erreur lors de l'importation du fichier CSV", e);
            throw new RuntimeException("Échec de l'importation du fichier CSV", e);
        }
    }

    /**
     * Importe les présentations de médicaments depuis le fichier CIS_CIP_bdpm.csv.
     *
     * @param csvFilePath Chemin du fichier CSV dans les ressources
     * @return Nombre de présentations importées avec succès
     */
    @Transactional
    public int importCisCipBdpm(String csvFilePath) {
        log.info("Début de l'importation des présentations depuis le fichier: {}", csvFilePath);

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                 CSVReader reader = new CSVReader(br)) {

                // Ignorer la ligne d'en-tête
                reader.readNext();

                String[] line;
                int count = 0;

                while ((line = reader.readNext()) != null) {
                    try {
                        // Vérifier si le médicament existe
                        String codeCis = line[0];
                        CisBdpm cisBdpm = null;

                        try {
                            cisBdpm = entityManager.createQuery(
                                    "SELECT c FROM CisBdpm c WHERE c.codeCis = :codeCis", CisBdpm.class)
                                    .setParameter("codeCis", codeCis)
                                    .getSingleResult();
                        } catch (NoResultException e) {
                            log.warn("Médicament avec code CIS {} non trouvé pour la présentation. Création d'un médicament temporaire.", codeCis);
                            // Créer un médicament temporaire si non trouvé
                            cisBdpm = new CisBdpm();
                            cisBdpm.setCodeCis(codeCis);
                            cisBdpm.setDenomination("Médicament temporaire " + codeCis);
                            cisBdpm.setFormePharmaceutique("Non spécifiée");
                            cisBdpm.setVoiesAdministration("Non spécifiée");
                            cisBdpm.setStatutAMM("Non spécifié");
                            cisBdpm.setTypeProcedureAMM("Non spécifié");
                            cisBdpm.setEtatCommercialisation("Non spécifié");
                            cisBdpm.setDateAMM(new Date());
                            entityManager.persist(cisBdpm);
                            entityManager.flush();
                        }

                        if (cisBdpm != null) {
                            CisCipBdpm presentation = new CisCipBdpm();
                            presentation.setCisBdpm(cisBdpm);
                            presentation.setCodeCip7(line[1]);
                            presentation.setLibellePresentation(line[2]);
                            presentation.setStatutAdministratif(line[3]);
                            presentation.setEtatCommercialisation(line[4]);

                            try {
                                if (line.length > 5 && line[5] != null && !line[5].trim().isEmpty()) {
                                    presentation.setDateDeclarationCommercialisation(parseDate(line[5]));
                                }
                            } catch (ParseException e) {
                                log.warn("Erreur lors du parsing de la date de déclaration de commercialisation: {}", line[5], e);
                            }

                            if (line.length > 6 && line[6] != null && !line[6].trim().isEmpty()) {
                                presentation.setCodeCip13(line[6]);
                            }

                            if (line.length > 7 && line[7] != null && !line[7].trim().isEmpty()) {
                                presentation.setAgrementCollectivites(line[7].toLowerCase());
                            }

                            if (line.length > 8 && line[8] != null && !line[8].trim().isEmpty()) {
                                presentation.setTauxRemboursement(line[8]);
                            }

                            if (line.length > 9 && line[9] != null && !line[9].trim().isEmpty()) {
                                try {
                                    // Nettoyer la chaîne de caractères pour ne garder qu'un seul séparateur décimal
                                    String prixHT = cleanNumberString(line[9]);
                                    presentation.setPrixHT(new BigDecimal(prixHT));
                                } catch (NumberFormatException e) {
                                    log.warn("Erreur lors du parsing du prix HT: {}", line[9], e);
                                }
                            }

                            if (line.length > 10 && line[10] != null && !line[10].trim().isEmpty()) {
                                try {
                                    // Nettoyer la chaîne de caractères pour ne garder qu'un seul séparateur décimal
                                    String prixTTC = cleanNumberString(line[10]);
                                    presentation.setPrixTTC(new BigDecimal(prixTTC));
                                } catch (NumberFormatException e) {
                                    log.warn("Erreur lors du parsing du prix TTC: {}", line[10], e);
                                }
                            }

                            if (line.length > 11 && line[11] != null && !line[11].trim().isEmpty()) {
                                try {
                                    // Nettoyer la chaîne de caractères pour ne garder qu'un seul séparateur décimal
                                    String taxe = cleanNumberString(line[11]);
                                    presentation.setTaxe(new BigDecimal(taxe));
                                } catch (NumberFormatException e) {
                                    log.warn("Erreur lors du parsing de la taxe: {}", line[11], e);
                                }
                            }

                            if (line.length > 12 && line[12] != null && !line[12].trim().isEmpty()) {
                                presentation.setIndicationsRemboursement(line[12]);
                            }

                            entityManager.persist(presentation);
                            count++;

                            if (count % 100 == 0) {
                                entityManager.flush();
                                log.info("Importation CIS_CIP_bdpm en cours: {} présentations traitées", count);
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Erreur lors du traitement de la ligne: {}", String.join(",", line), e);
                    }
                }

                entityManager.flush();
                log.info("Importation CIS_CIP_bdpm terminée. {} présentations importées avec succès", count);
                return count;
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Erreur lors de l'importation du fichier CSV", e);
            throw new RuntimeException("Échec de l'importation du fichier CSV", e);
        }
    }

    /**
     * Nettoie une chaîne de caractères représentant un nombre pour ne garder qu'un seul séparateur décimal.
     * Remplace les virgules par des points et supprime les séparateurs de milliers.
     *
     * @param numberStr Chaîne de caractères représentant un nombre
     * @return Chaîne de caractères nettoyée
     */
    private String cleanNumberString(String numberStr) {
        // Remplacer les virgules par des points
        String cleaned = numberStr.replace(',', '.');

        // Compter le nombre de points
        int pointCount = 0;
        for (char c : cleaned.toCharArray()) {
            if (c == '.') {
                pointCount++;
            }
        }

        // S'il y a plus d'un point, on considère que les points sauf le dernier sont des séparateurs de milliers
        if (pointCount > 1) {
            StringBuilder sb = new StringBuilder();
            int lastPointIndex = cleaned.lastIndexOf('.');

            for (int i = 0; i < cleaned.length(); i++) {
                char c = cleaned.charAt(i);
                if (c != '.' || i == lastPointIndex) {
                    sb.append(c);
                }
            }

            cleaned = sb.toString();
        }

        return cleaned;
    }

    /**
     * Importe les compositions de médicaments depuis le fichier CIS_COMPO_bdpm.csv.
     *
     * @param csvFilePath Chemin du fichier CSV dans les ressources
     * @return Nombre de compositions importées avec succès
     */
    @Transactional
    public int importCisCompoBdpm(String csvFilePath) {
        log.info("Début de l'importation des compositions depuis le fichier: {}", csvFilePath);

        // Désactiver temporairement le flush automatique
        boolean previousAutoFlushMode = HibernateSessionUtil.disableAutoFlush(entityManager);

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                 CSVReader reader = new CSVReader(br)) {

                // Ignorer la ligne d'en-tête
                reader.readNext();

                String[] line;
                int count = 0;
                List<CisCompoBdpm> batchCompositions = new ArrayList<>();

                while ((line = reader.readNext()) != null) {
                    try {
                        // Vérifier si le médicament existe
                        String codeCis = line[0];
                        CisBdpm cisBdpm = null;

                        try {
                            cisBdpm = entityManager.createQuery(
                                    "SELECT c FROM CisBdpm c WHERE c.codeCis = :codeCis", CisBdpm.class)
                                    .setParameter("codeCis", codeCis)
                                    .getSingleResult();
                        } catch (NoResultException e) {
                            log.warn("Médicament avec code CIS {} non trouvé pour la composition. Création d'un médicament temporaire.", codeCis);
                            // Créer un médicament temporaire si non trouvé
                            cisBdpm = new CisBdpm();
                            cisBdpm.setCodeCis(codeCis);
                            cisBdpm.setDenomination("Médicament temporaire " + codeCis);
                            cisBdpm.setFormePharmaceutique("Non spécifiée");
                            cisBdpm.setVoiesAdministration("Non spécifiée");
                            cisBdpm.setStatutAMM("Non spécifié");
                            cisBdpm.setTypeProcedureAMM("Non spécifié");
                            cisBdpm.setEtatCommercialisation("Non spécifié");
                            cisBdpm.setDateAMM(new Date());
                            entityManager.persist(cisBdpm);
                            entityManager.flush(); // Flush immédiatement pour obtenir l'ID
                        }

                        if (cisBdpm != null) {
                            CisCompoBdpm composition = new CisCompoBdpm();
                            composition.setCisBdpm(cisBdpm);

                            // Ne pas définir l'ID car il est auto-généré

                            if (line.length > 1 && line[1] != null && !line[1].trim().isEmpty()) {
                                composition.setDesignationElement(line[1]);
                            }

                            if (line.length > 2 && line[2] != null && !line[2].trim().isEmpty()) {
                                composition.setCodeSubstance(line[2]);
                            }

                            if (line.length > 3 && line[3] != null && !line[3].trim().isEmpty()) {
                                composition.setDenominationSubstance(line[3]);
                            }

                            if (line.length > 4 && line[4] != null && !line[4].trim().isEmpty()) {
                                composition.setDosage(line[4]);
                            }

                            if (line.length > 5 && line[5] != null && !line[5].trim().isEmpty()) {
                                composition.setReferenceDosage(line[5]);
                            }

                            if (line.length > 6 && line[6] != null && !line[6].trim().isEmpty()) {
                                // Vérifier que la valeur est "SA" ou "ST" pour respecter la contrainte @Pattern
                                String natureComposant = line[6];
                                if ("SA".equals(natureComposant) || "ST".equals(natureComposant) || "FT".equals(natureComposant) || "ST ".equals(natureComposant)) {
                                    composition.setNatureComposant(natureComposant);
                                } else {
                                    // Valeur par défaut si la valeur n'est pas valide
                                    log.warn("Valeur de nature composant non valide: {}. Utilisation de la valeur par défaut 'SA'", natureComposant);
                                    composition.setNatureComposant("SA");
                                }
                            } else {
                                // Valeur par défaut si la valeur est vide
                                composition.setNatureComposant("SA");
                            }

                            if (line.length > 7 && line[7] != null && !line[7].trim().isEmpty()) {
                                composition.setNumeroLiaisonSaft(line[7]);
                            }

                            entityManager.persist(composition);
                            batchCompositions.add(composition);
                            count++;

                            if (count % 50 == 0) { // Réduire la taille du batch à 50
                                HibernateSessionUtil.flushAndClear(entityManager);
                                batchCompositions.clear();
                                log.info("Importation CIS_COMPO_bdpm en cours: {} compositions traitées", count);
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Erreur lors du traitement de la ligne: {}", String.join(",", line), e);
                        // Continuer avec la ligne suivante sans interrompre le processus
                    }
                }

                // Flush final pour les entités restantes
                if (!batchCompositions.isEmpty()) {
                    HibernateSessionUtil.flushAndClear(entityManager);
                }

                log.info("Importation CIS_COMPO_bdpm terminée. {} compositions importées avec succès", count);
                return count;
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Erreur lors de l'importation du fichier {}: {}", csvFilePath, e.getMessage(), e);
            throw new RuntimeException("Erreur lors de l'importation du fichier " + csvFilePath, e);
        } finally {
            // Restaurer le mode de flush automatique si nécessaire
            HibernateSessionUtil.restoreAutoFlush(entityManager, previousAutoFlushMode);
        }
    }

    /**
     * Importe les conditions de prescription et de délivrance depuis le fichier CIS_CPD_bdpm.csv.
     *
     * @param csvFilePath Chemin du fichier CSV dans les ressources
     * @return Nombre de conditions importées avec succès
     */
    @Transactional
    public int importCisCpdBdpm(String csvFilePath) {
        log.info("Début de l'importation des conditions de prescription depuis le fichier: {}", csvFilePath);

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                 CSVReader reader = new CSVReader(br)) {

                // Ignorer la ligne d'en-tête
                reader.readNext();

                String[] line;
                int count = 0;

                while ((line = reader.readNext()) != null) {
                    try {
                        // Vérifier si le médicament existe
                        String codeCis = line[0];
                        CisBdpm cisBdpm = null;

                        try {
                            cisBdpm = entityManager.createQuery(
                                    "SELECT c FROM CisBdpm c WHERE c.codeCis = :codeCis", CisBdpm.class)
                                    .setParameter("codeCis", codeCis)
                                    .getSingleResult();
                        } catch (NoResultException e) {
                            log.warn("Médicament avec code CIS {} non trouvé pour la condition de prescription. Création d'un médicament temporaire.", codeCis);
                            // Créer un médicament temporaire si non trouvé
                            cisBdpm = new CisBdpm();
                            cisBdpm.setCodeCis(codeCis);
                            cisBdpm.setDenomination("Médicament temporaire " + codeCis);
                            cisBdpm.setFormePharmaceutique("Non spécifiée");
                            cisBdpm.setVoiesAdministration("Non spécifiée");
                            cisBdpm.setStatutAMM("Non spécifié");
                            cisBdpm.setTypeProcedureAMM("Non spécifié");
                            cisBdpm.setEtatCommercialisation("Non spécifié");
                            cisBdpm.setDateAMM(new Date());
                            entityManager.persist(cisBdpm);
                            entityManager.flush();
                        }

                        if (cisBdpm != null) {
                            CisCpdBdpm condition = new CisCpdBdpm();
                            condition.setCisBdpm(cisBdpm);

                            // Ne pas définir l'ID car il est auto-généré

                            if (line.length > 1 && line[1] != null && !line[1].trim().isEmpty()) {
                                condition.setConditionPrescription(line[1]);
                            }

                            entityManager.persist(condition);
                            count++;

                            if (count % 100 == 0) {
                                entityManager.flush();
                                log.info("Importation CIS_CPD_bdpm en cours: {} conditions traitées", count);
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Erreur lors du traitement de la ligne: {}", String.join(",", line), e);
                    }
                }

                entityManager.flush();
                log.info("Importation CIS_CPD_bdpm terminée. {} conditions importées avec succès", count);
                return count;
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Erreur lors de l'importation du fichier CSV", e);
            throw new RuntimeException("Échec de l'importation du fichier CSV", e);
        }
    }

    /**
     * Importe les groupes génériques depuis le fichier CIS_GENER_bdpm.csv.
     *
     * @param csvFilePath Chemin du fichier CSV dans les ressources
     * @return Nombre de groupes génériques importés avec succès
     */
    @Transactional
    public int importCisGenerBdpm(String csvFilePath) {
        log.info("Début de l'importation des groupes génériques depuis le fichier: {}", csvFilePath);

        // Désactiver temporairement le flush automatique
        boolean previousAutoFlushMode = HibernateSessionUtil.disableAutoFlush(entityManager);

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                 CSVReader reader = new CSVReader(br)) {

                // Ignorer la ligne d'en-tête
                reader.readNext();

                String[] line;
                int count = 0;
                int batchSize = 0;
                Set<String> processedIds = new HashSet<>(); // Pour suivre les IDs déjà traités

                while ((line = reader.readNext()) != null) {
                    try {
                        // Vérifier si l'identifiant du groupe générique est présent (position 0)
                        if (line.length <= 0 || line[0] == null || line[0].trim().isEmpty()) {
                            log.warn("Identifiant de groupe générique manquant pour la ligne");
                            continue;
                        }

                        // Vérifier si le code CIS est présent (position 2)
                        if (line.length <= 2 || line[2] == null || line[2].trim().isEmpty()) {
                            log.warn("Code CIS manquant pour le groupe générique {}", line[0]);
                            continue;
                        }

                        String codeCis = line[2]; // Code CIS est en position 2

                        // Limiter la longueur de l'identifiant si nécessaire
                        String identifiantGroupe = line[0]; // Identifiant est en position 0
                        if (identifiantGroupe.length() > 1000) {
                            log.warn("Identifiant de groupe générique trop long, troncature à 1000 caractères : {}", identifiantGroupe);
                            identifiantGroupe = identifiantGroupe.substring(0, 1000);
                        }

                        // Vérifier si cet identifiant a déjà été traité dans cette session d'importation
                        if (processedIds.contains(identifiantGroupe)) {
                            log.warn("Doublon détecté pour l'identifiant de groupe générique: {}. Ligne ignorée.", identifiantGroupe);
                            continue;
                        }

                        // Vérifier si l'entité existe déjà en base de données
                        CisGenerBdpm existingGenerique = null;
                        try {
                            existingGenerique = entityManager.find(CisGenerBdpm.class, identifiantGroupe);
                        } catch (Exception e) {
                            log.warn("Erreur lors de la recherche du groupe générique: {}", identifiantGroupe, e);
                            // Continuer avec la ligne suivante en cas d'erreur
                            continue;
                        }

                        if (existingGenerique != null) {
                            log.info("Le groupe générique avec l'identifiant {} existe déjà. Mise à jour des informations.", identifiantGroupe);
                            // Mettre à jour l'entité existante si nécessaire
                            if (line.length > 1 && line[1] != null && !line[1].trim().isEmpty()) {
                                String libelle = line[1]; // Libellé est en position 1
                                if (libelle.length() > 1000) {
                                    log.warn("Libellé de groupe générique trop long, troncature à 1000 caractères : {}", libelle);
                                    libelle = libelle.substring(0, 1000);
                                }
                                existingGenerique.setLibelleGroupeGenerique(libelle);
                            }

                            // Continuer avec les autres mises à jour...
                            processedIds.add(identifiantGroupe);
                            count++;
                            batchSize++;

                            if (batchSize >= 20) {
                                try {
                                    HibernateSessionUtil.flushAndClear(entityManager);
                                    batchSize = 0;
                                } catch (Exception e) {
                                    log.error("Erreur lors du flush après mise à jour : {}", e.getMessage(), e);
                                    // Réinitialiser la session en cas d'erreur
                                    entityManager.clear();
                                    batchSize = 0;
                                }
                            }
                            continue;
                        }

                        CisBdpm cisBdpm = null;
                        try {
                            cisBdpm = entityManager.createQuery(
                                    "SELECT c FROM CisBdpm c WHERE c.codeCis = :codeCis", CisBdpm.class)
                                    .setParameter("codeCis", codeCis)
                                    .getSingleResult();
                        } catch (NoResultException e) {
                            log.warn("Médicament avec code CIS {} non trouvé pour le groupe générique. Création d'un médicament temporaire.", codeCis);
                            // Créer un médicament temporaire si non trouvé
                            cisBdpm = new CisBdpm();
                            cisBdpm.setCodeCis(codeCis);
                            cisBdpm.setDenomination("Médicament temporaire " + codeCis);
                            cisBdpm.setFormePharmaceutique("Non spécifiée");
                            cisBdpm.setVoiesAdministration("Non spécifiée");
                            cisBdpm.setStatutAMM("Non spécifié");
                            cisBdpm.setTypeProcedureAMM("Non spécifié");
                            cisBdpm.setEtatCommercialisation("Non spécifié");
                            cisBdpm.setDateAMM(new Date());

                            try {
                                entityManager.persist(cisBdpm);
                                entityManager.flush();
                            } catch (Exception ex) {
                                log.error("Erreur lors de la persistance du médicament temporaire : {}", ex.getMessage(), ex);
                                // Continuer avec la ligne suivante en cas d'erreur
                                continue;
                            }
                        } catch (Exception e) {
                            log.error("Erreur lors de la recherche du médicament : {}", e.getMessage(), e);
                            // Continuer avec la ligne suivante en cas d'erreur
                            continue;
                        }

                        if (cisBdpm != null) {
                            try {
                                CisGenerBdpm generique = new CisGenerBdpm();
                                generique.setCisBdpm(cisBdpm);

                                // Utiliser l'identifiant du groupe générique comme clé primaire
                                generique.setIdentifiantGroupeGenerique(identifiantGroupe);

                                if (line.length > 1 && line[1] != null && !line[1].trim().isEmpty()) {
                                    String libelle = line[1]; // Libellé est en position 1
                                    if (libelle.length() > 1000) {
                                        log.warn("Libellé de groupe générique trop long, troncature à 1000 caractères : {}", libelle);
                                        libelle = libelle.substring(0, 1000);
                                    }
                                    generique.setLibelleGroupeGenerique(libelle);
                                }

                                if (line.length > 3 && line[3] != null && !line[3].trim().isEmpty()) {
                                    try {
                                        // Vérifier si la valeur est numérique
                                        if (line[3].matches("\\d+")) {
                                            // Convertir la valeur numérique en code d'énumération
                                            int typeCode = Integer.parseInt(line[3]);
                                            boolean found = false;

                                            // Rechercher l'énumération avec le code correspondant
                                            for (TypeGenerique type : TypeGenerique.values()) {
                                                if (type.getCode() == typeCode) {
                                                    generique.setTypeGenerique(type);
                                                    found = true;
                                                    break;
                                                }
                                            }

                                            if (!found) {
                                                log.warn("Code de type générique non trouvé: {}. Utilisation de la valeur par défaut.", typeCode);
                                                generique.setTypeGenerique(TypeGenerique.PRINCEPS);
                                            }
                                        } else {
                                            // Essayer de convertir directement le nom de l'énumération
                                            generique.setTypeGenerique(TypeGenerique.valueOf(line[3]));
                                        }
                                    } catch (Exception e) {
                                        log.warn("Type de générique inconnu: {}. Utilisation de la valeur par défaut.", line[3], e);
                                        // Utiliser une valeur par défaut
                                        generique.setTypeGenerique(TypeGenerique.PRINCEPS);
                                    }
                                } else {
                                    // Valeur par défaut si manquante
                                    generique.setTypeGenerique(TypeGenerique.PRINCEPS);
                                }

                                if (line.length > 4 && line[4] != null && !line[4].trim().isEmpty()) {
                                    try {
                                        generique.setNumeroTri(Integer.parseInt(line[4]));
                                    } catch (NumberFormatException e) {
                                        log.warn("Erreur lors du parsing du numéro de tri: {}", line[4], e);
                                    }
                                }

                                entityManager.persist(generique);
                                processedIds.add(identifiantGroupe); // Marquer cet ID comme traité
                                count++;
                                batchSize++;

                                if (batchSize >= 20) { // Réduire la taille du batch à 20
                                    try {
                                        HibernateSessionUtil.flushAndClear(entityManager);
                                        log.info("Importation CIS_GENER_bdpm en cours: {} groupes génériques traités", count);
                                        batchSize = 0;
                                    } catch (Exception e) {
                                        log.error("Erreur lors du flush : {}", e.getMessage(), e);
                                        // Réinitialiser la session en cas d'erreur
                                        entityManager.clear();
                                        batchSize = 0;
                                    }
                                }
                            } catch (Exception e) {
                                log.error("Erreur lors de la création du groupe générique : {}", e.getMessage(), e);
                                // Continuer avec la ligne suivante en cas d'erreur
                                continue;
                            }
                        } else {
                            log.warn("Médicament avec code CIS {} non trouvé pour le groupe générique", codeCis);
                        }
                    } catch (Exception e) {
                        log.warn("Erreur lors du traitement de la ligne: {}", String.join(",", line), e);
                        // Continuer avec la ligne suivante sans interrompre le processus
                    }
                }

                // Flush final pour les entités restantes
                if (batchSize > 0) {
                    try {
                        HibernateSessionUtil.flushAndClear(entityManager);
                    } catch (Exception e) {
                        log.error("Erreur lors du flush final : {}", e.getMessage(), e);
                    }
                }

                log.info("Importation CIS_GENER_bdpm terminée. {} groupes génériques importés avec succès", count);
                return count;
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Erreur lors de l'importation du fichier CSV", e);
            throw new RuntimeException("Échec de l'importation du fichier CSV", e);
        } finally {
            // Restaurer le mode de flush automatique si nécessaire
            HibernateSessionUtil.restoreAutoFlush(entityManager, previousAutoFlushMode);
        }
    }

    /**
     * Importe les avis ASMR depuis le fichier CIS_HAS_ASMR_bdpm.csv.
     *
     * @param csvFilePath Chemin du fichier CSV dans les ressources
     * @return Nombre d'avis ASMR importés avec succès
     */
    @Transactional
    public int importCisHasAsmr(String csvFilePath) {
        log.info("Début de l'importation des avis ASMR depuis le fichier: {}", csvFilePath);

        // Désactiver temporairement le flush automatique
        boolean previousAutoFlushMode = HibernateSessionUtil.disableAutoFlush(entityManager);

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                 CSVReader reader = new CSVReader(br)) {

                // Ignorer la ligne d'en-tête
                reader.readNext();

                String[] line;
                int count = 0;
                int batchSize = 0;
                Set<String> processedIds = new HashSet<>(); // Pour suivre les IDs déjà traités

                // Format de date pour la lecture du CSV (yyyyMMdd)
                SimpleDateFormat csvDateFormat = new SimpleDateFormat("yyyyMMdd");
                // Format de date pour le stockage en base de données (dd-MM-yyyy)
                SimpleDateFormat dbDateFormat = new SimpleDateFormat("dd-MM-yyyy");

                while ((line = reader.readNext()) != null) {
                    try {
                        // Vérifier si les données minimales sont présentes
                        if (line.length < 2 || line[0] == null || line[0].trim().isEmpty() || line[1] == null || line[1].trim().isEmpty()) {
                            log.warn("Données insuffisantes pour l'importation de l'avis ASMR. Ligne ignorée: {}", String.join(",", line));
                            continue;
                        }

                        String codeCis = line[0]; // Code CIS en position 0
                        String codeDossierHas = line[1]; // Code dossier HAS en position 1

                        // Vérifier si cet identifiant a déjà été traité dans cette session d'importation
                        if (processedIds.contains(codeDossierHas)) {
                            log.warn("Doublon détecté pour l'identifiant d'avis ASMR: {}. Ligne ignorée.", codeDossierHas);
                            continue;
                        }

                        // Vérifier si l'entité existe déjà en base de données
                        CisHasAsmr existingAsmr = null;
                        try {
                            existingAsmr = entityManager.find(CisHasAsmr.class, codeDossierHas);
                        } catch (Exception e) {
                            log.warn("Erreur lors de la recherche de l'avis ASMR: {}", codeDossierHas, e);
                            // Continuer avec la ligne suivante en cas d'erreur
                            continue;
                        }

                        if (existingAsmr != null) {
                            log.info("L'avis ASMR avec l'identifiant {} existe déjà. Mise à jour des informations.", codeDossierHas);

                            // Mettre à jour l'entité existante
                            if (line.length > 2 && line[2] != null && !line[2].trim().isEmpty()) {
                                existingAsmr.setMotifEvaluation(line[2]);
                            }

                            if (line.length > 3 && line[3] != null && !line[3].trim().isEmpty()) {
                                try {
                                    // Convertir la date du format CSV au format de la base de données
                                    Date csvDate = csvDateFormat.parse(line[3]);
                                    String dbDateStr = dbDateFormat.format(csvDate);
                                    Date dbDate = dbDateFormat.parse(dbDateStr);
                                    existingAsmr.setDateAvis(dbDate);
                                } catch (ParseException e) {
                                    log.warn("Format de date invalide: {}. La date n'a pas été mise à jour.", line[3]);
                                }
                            }

                            if (line.length > 4 && line[4] != null && !line[4].trim().isEmpty()) {
                                existingAsmr.setValeurAsmr(line[4]);
                            }

                            if (line.length > 5 && line[5] != null && !line[5].trim().isEmpty()) {
                                existingAsmr.setLibelleAsmr(line[5]);
                            }

                            processedIds.add(codeDossierHas);
                            count++;
                            batchSize++;

                            if (batchSize >= 20) {
                                try {
                                    HibernateSessionUtil.flushAndClear(entityManager);
                                    batchSize = 0;
                                } catch (Exception e) {
                                    log.error("Erreur lors du flush après mise à jour : {}", e.getMessage(), e);
                                    // Réinitialiser la session en cas d'erreur
                                    entityManager.clear();
                                    batchSize = 0;
                                }
                            }
                            continue;
                        }

                        // Rechercher le médicament associé
                        CisBdpm cisBdpm = null;
                        try {
                            cisBdpm = entityManager.createQuery(
                                    "SELECT c FROM CisBdpm c WHERE c.codeCis = :codeCis", CisBdpm.class)
                                    .setParameter("codeCis", codeCis)
                                    .getSingleResult();
                        } catch (NoResultException e) {
                            log.warn("Médicament avec code CIS {} non trouvé pour l'avis ASMR. Création d'un médicament temporaire.", codeCis);
                            // Créer un médicament temporaire si non trouvé
                            cisBdpm = new CisBdpm();
                            cisBdpm.setCodeCis(codeCis);
                            cisBdpm.setDenomination("Médicament temporaire " + codeCis);
                            cisBdpm.setFormePharmaceutique("Non spécifiée");
                            cisBdpm.setVoiesAdministration("Non spécifiée");
                            cisBdpm.setStatutAMM("Non spécifié");
                            cisBdpm.setTypeProcedureAMM("Non spécifié");
                            cisBdpm.setEtatCommercialisation("Non spécifié");
                            cisBdpm.setDateAMM(new Date());

                            try {
                                entityManager.persist(cisBdpm);
                                entityManager.flush();
                            } catch (Exception ex) {
                                log.error("Erreur lors de la persistance du médicament temporaire : {}", ex.getMessage(), ex);
                                // Continuer avec la ligne suivante en cas d'erreur
                                continue;
                            }
                        } catch (Exception e) {
                            log.error("Erreur lors de la recherche du médicament : {}", e.getMessage(), e);
                            // Continuer avec la ligne suivante en cas d'erreur
                            continue;
                        }

                        if (cisBdpm != null) {
                            try {
                                CisHasAsmr asmr = new CisHasAsmr();
                                asmr.setCisBdpm(cisBdpm);
                                asmr.setCodeDossierHas(codeDossierHas);

                                if (line.length > 2 && line[2] != null && !line[2].trim().isEmpty()) {
                                    asmr.setMotifEvaluation(line[2]);
                                }

                                if (line.length > 3 && line[3] != null && !line[3].trim().isEmpty()) {
                                    try {
                                        // Convertir la date du format CSV au format de la base de données
                                        Date csvDate = csvDateFormat.parse(line[3]);
                                        String dbDateStr = dbDateFormat.format(csvDate);
                                        Date dbDate = dbDateFormat.parse(dbDateStr);
                                        asmr.setDateAvis(dbDate);
                                    } catch (ParseException e) {
                                        log.warn("Format de date invalide: {}. La date n'a pas été définie.", line[3]);
                                    }
                                }

                                if (line.length > 4 && line[4] != null && !line[4].trim().isEmpty()) {
                                    asmr.setValeurAsmr(line[4]);
                                }

                                if (line.length > 5 && line[5] != null && !line[5].trim().isEmpty()) {
                                    asmr.setLibelleAsmr(line[5]);
                                }

                                entityManager.persist(asmr);
                                processedIds.add(codeDossierHas);
                                count++;
                                batchSize++;

                                if (batchSize >= 20) {
                                    try {
                                        HibernateSessionUtil.flushAndClear(entityManager);
                                        log.info("Importation CIS_HAS_ASMR en cours: {} avis ASMR traités", count);
                                        batchSize = 0;
                                    } catch (Exception e) {
                                        log.error("Erreur lors du flush : {}", e.getMessage(), e);
                                        // Réinitialiser la session en cas d'erreur
                                        entityManager.clear();
                                        batchSize = 0;
                                    }
                                }
                            } catch (Exception e) {
                                log.warn("Erreur lors de la persistance de l'avis ASMR: {}", codeDossierHas, e);
                            }
                        } else {
                            log.warn("Médicament avec code CIS {} non trouvé pour l'avis ASMR", codeCis);
                        }
                    } catch (Exception e) {
                        log.warn("Erreur lors du traitement de la ligne: {}", String.join(",", line), e);
                    }
                }

                // Flush final pour les entités restantes
                if (batchSize > 0) {
                    try {
                        HibernateSessionUtil.flushAndClear(entityManager);
                    } catch (Exception e) {
                        log.error("Erreur lors du flush final : {}", e.getMessage(), e);
                    }
                }

                log.info("Importation CIS_HAS_ASMR terminée. {} avis ASMR importés avec succès", count);
                return count;
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Erreur lors de l'importation du fichier CSV", e);
            throw new RuntimeException("Échec de l'importation du fichier CSV", e);
        } finally {
            // Restaurer le mode de flush automatique si nécessaire
            HibernateSessionUtil.restoreAutoFlush(entityManager, previousAutoFlushMode);
        }
    }

    /**
     * Importe les avis SMR depuis le fichier CIS_HAS_SMR_bdpm.csv.
     *
     * @param csvFilePath Chemin du fichier CSV dans les ressources
     * @return Nombre d'avis SMR importés avec succès
     */
    @Transactional
    public int importCisHasSmr(String csvFilePath) {
        log.info("Début de l'importation des avis SMR depuis le fichier: {}", csvFilePath);

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                 CSVReader reader = new CSVReader(br)) {

                // Ignorer la ligne d'en-tête
                reader.readNext();

                String[] line;
                int count = 0;

                while ((line = reader.readNext()) != null) {
                    try {
                        // Vérifier si le médicament existe
                        String codeCis = line[0];
                        CisBdpm cisBdpm = null;

                        try {
                            cisBdpm = entityManager.createQuery(
                                    "SELECT c FROM CisBdpm c WHERE c.codeCis = :codeCis", CisBdpm.class)
                                    .setParameter("codeCis", codeCis)
                                    .getSingleResult();
                        } catch (NoResultException e) {
                            log.warn("Médicament avec code CIS {} non trouvé pour l'avis SMR. Création d'un médicament temporaire.", codeCis);
                            // Créer un médicament temporaire si non trouvé
                            cisBdpm = new CisBdpm();
                            cisBdpm.setCodeCis(codeCis);
                            cisBdpm.setDenomination("Médicament temporaire " + codeCis);
                            cisBdpm.setFormePharmaceutique("Non spécifiée");
                            cisBdpm.setVoiesAdministration("Non spécifiée");
                            cisBdpm.setStatutAMM("Non spécifié");
                            cisBdpm.setTypeProcedureAMM("Non spécifié");
                            cisBdpm.setEtatCommercialisation("Non spécifié");
                            cisBdpm.setDateAMM(new Date());
                            entityManager.persist(cisBdpm);
                            entityManager.flush();
                        }

                        if (cisBdpm != null && line.length > 1 && line[1] != null && !line[1].trim().isEmpty()) {
                            CisHasSmr avis = new CisHasSmr();
                            avis.setCisBdpm(cisBdpm);

                            // Utiliser le code dossier HAS comme clé primaire
                            avis.setCodeDossierHas(line[1]);

                            if (line.length > 2 && line[2] != null && !line[2].trim().isEmpty()) {
                                avis.setMotifEvaluation(line[2]);
                            }

                            if (line.length > 3 && line[3] != null && !line[3].trim().isEmpty()) {
                                // Pour CisHasSmr, dateAvis est une chaîne de caractères
                                avis.setDateAvis(line[3]);
                            }

                            if (line.length > 4 && line[4] != null && !line[4].trim().isEmpty()) {
                                avis.setValeurSmr(line[4]);
                            }

                            if (line.length > 5 && line[5] != null && !line[5].trim().isEmpty()) {
                                avis.setLibelleSmr(line[5]);
                            }

                            try {
                                entityManager.persist(avis);
                                count++;

                                if (count % 100 == 0) {
                                    entityManager.flush();
                                    log.info("Importation CIS_HAS_SMR_bdpm en cours: {} avis SMR traités", count);
                                }
                            } catch (Exception e) {
                                log.warn("Erreur lors de la persistance de l'avis SMR: {}", avis.getCodeDossierHas(), e);
                            }
                        } else {
                            log.warn("Médicament avec code CIS {} non trouvé pour l'avis SMR ou code dossier HAS manquant", codeCis);
                        }
                    } catch (Exception e) {
                        log.warn("Erreur lors du traitement de la ligne: {}", String.join(",", line), e);
                    }
                }

                entityManager.flush();
                log.info("Importation CIS_HAS_SMR_bdpm terminée. {} avis SMR importés avec succès", count);
                return count;
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Erreur lors de l'importation du fichier CSV", e);
            throw new RuntimeException("Échec de l'importation du fichier CSV", e);
        }
    }

    /**
     * Importe les informations importantes depuis le fichier CIS_InfoImportantes.csv.
     *
     * @param csvFilePath Chemin du fichier CSV dans les ressources
     * @return Nombre d'informations importantes importées avec succès
     */
    @Transactional
    public int importCisInfoImportantes(String csvFilePath) {
        log.info("Début de l'importation des informations importantes depuis le fichier: {}", csvFilePath);

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                 CSVReader reader = new CSVReader(br)) {

                // Ignorer la ligne d'en-tête
                reader.readNext();

                String[] line;
                int count = 0;

                while ((line = reader.readNext()) != null) {
                    try {
                        // Vérifier si le médicament existe
                        String codeCis = line[0];
                        CisBdpm cisBdpm = null;

                        try {
                            cisBdpm = entityManager.createQuery(
                                    "SELECT c FROM CisBdpm c WHERE c.codeCis = :codeCis", CisBdpm.class)
                                    .setParameter("codeCis", codeCis)
                                    .getSingleResult();
                        } catch (NoResultException e) {
                            log.warn("Médicament avec code CIS {} non trouvé pour l'information importante. Création d'un médicament temporaire.", codeCis);
                            // Créer un médicament temporaire si non trouvé
                            cisBdpm = new CisBdpm();
                            cisBdpm.setCodeCis(codeCis);
                            cisBdpm.setDenomination("Médicament temporaire " + codeCis);
                            cisBdpm.setFormePharmaceutique("Non spécifiée");
                            cisBdpm.setVoiesAdministration("Non spécifiée");
                            cisBdpm.setStatutAMM("Non spécifié");
                            cisBdpm.setTypeProcedureAMM("Non spécifié");
                            cisBdpm.setEtatCommercialisation("Non spécifié");
                            cisBdpm.setDateAMM(new Date());
                            entityManager.persist(cisBdpm);
                            entityManager.flush();
                        }

                        if (cisBdpm != null) {
                            CisInfoImportantes info = new CisInfoImportantes();
                            info.setCisBdpm(cisBdpm);

                            // Ne pas définir l'ID car il est auto-généré

                            if (line.length > 1 && line[1] != null && !line[1].trim().isEmpty()) {
                                try {
                                    info.setDateDebut(parseDate(line[1]));
                                } catch (ParseException e) {
                                    log.warn("Erreur lors du parsing de la date de début: {}", line[1], e);
                                    // Utiliser la date actuelle comme valeur par défaut
                                    info.setDateDebut(new Date());
                                }
                            } else {
                                // La date de début est obligatoire (nullable = false)
                                info.setDateDebut(new Date());
                            }

                            if (line.length > 2 && line[2] != null && !line[2].trim().isEmpty()) {
                                try {
                                    info.setDateFin(parseDate(line[2]));
                                } catch (ParseException e) {
                                    log.warn("Erreur lors du parsing de la date de fin: {}", line[2], e);
                                }
                            }

                            if (line.length > 3 && line[3] != null && !line[3].trim().isEmpty()) {
                                info.setTexteLienHtml(line[3]);
                            } else {
                                // Le texte est obligatoire (nullable = false)
                                info.setTexteLienHtml("Information non disponible");
                            }

                            // Persister chaque entité individuellement
                            entityManager.persist(info);
                            entityManager.flush();
                            count++;

                            if (count % 100 == 0) {
                                log.info("Importation CIS_InfoImportantes en cours: {} informations traitées", count);
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Erreur lors du traitement de la ligne: {}", String.join(",", line), e);
                    }
                }

                log.info("Importation CIS_InfoImportantes terminée. {} informations importées avec succès", count);
                return count;
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Erreur lors de l'importation du fichier CSV", e);
            throw new RuntimeException("Échec de l'importation du fichier CSV", e);
        }
    }

    /**
     * Importe les disponibilités spécifiques depuis le fichier CIS_CIP_Dispo_Spec.csv.
     *
     * @param csvFilePath Chemin du fichier CSV dans les ressources
     * @return Nombre de disponibilités importées avec succès
     */
    @Transactional
    public int importCisCipDispoSpec(String csvFilePath) {
        log.info("Début de l'importation des disponibilités spécifiques depuis le fichier: {}", csvFilePath);

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                 CSVReader reader = new CSVReader(br)) {

                // Ignorer la ligne d'en-tête
                reader.readNext();

                String[] line;
                int count = 0;

                while ((line = reader.readNext()) != null) {
                    try {
                        // Vérifier si le médicament existe
                        String codeCis = line[0];
                        CisBdpm cisBdpm = null;

                        try {
                            cisBdpm = entityManager.createQuery(
                                    "SELECT c FROM CisBdpm c WHERE c.codeCis = :codeCis", CisBdpm.class)
                                    .setParameter("codeCis", codeCis)
                                    .getSingleResult();
                        } catch (NoResultException e) {
                            log.warn("Médicament avec code CIS {} non trouvé pour la disponibilité spécifique. Création d'un médicament temporaire.", codeCis);
                            // Créer un médicament temporaire si non trouvé
                            cisBdpm = new CisBdpm();
                            cisBdpm.setCodeCis(codeCis);
                            cisBdpm.setDenomination("Médicament temporaire " + codeCis);
                            cisBdpm.setFormePharmaceutique("Non spécifiée");
                            cisBdpm.setVoiesAdministration("Non spécifiée");
                            cisBdpm.setStatutAMM("Non spécifié");
                            cisBdpm.setTypeProcedureAMM("Non spécifié");
                            cisBdpm.setEtatCommercialisation("Non spécifié");
                            cisBdpm.setDateAMM(new Date());
                            entityManager.persist(cisBdpm);
                            entityManager.flush();
                        }

                        if (cisBdpm != null) {
                            CisCipDispoSpec dispo = new CisCipDispoSpec();
                            dispo.setCisBdpm(cisBdpm);

                            // Ne pas définir l'ID car il est auto-généré

                            if (line.length > 1 && line[1] != null && !line[1].trim().isEmpty()) {
                                dispo.setCodeCip13(line[1]);
                            }

                            if (line.length > 2 && line[2] != null && !line[2].trim().isEmpty()) {
                                try {
                                    dispo.setCodeStatut(Integer.parseInt(line[2]));
                                } catch (NumberFormatException e) {
                                    log.warn("Erreur lors du parsing du code statut: {}", line[2], e);
                                    // Valeur par défaut
                                    dispo.setCodeStatut(1);
                                }
                            } else {
                                // Valeur par défaut
                                dispo.setCodeStatut(1);
                            }

                            if (line.length > 3 && line[3] != null && !line[3].trim().isEmpty()) {
                                dispo.setLibelleStatut(line[3]);
                            } else {
                                dispo.setLibelleStatut("Non spécifié");
                            }

                            if (line.length > 4 && line[4] != null && !line[4].trim().isEmpty()) {
                                try {
                                    dispo.setDateDebut(parseDate(line[4]));
                                } catch (ParseException e) {
                                    log.warn("Erreur lors du parsing de la date de début: {}", line[4], e);
                                    dispo.setDateDebut(new Date());
                                }
                            } else {
                                dispo.setDateDebut(new Date());
                            }

                            if (line.length > 5 && line[5] != null && !line[5].trim().isEmpty()) {
                                try {
                                    dispo.setDateMiseAJour(parseDate(line[5]));
                                } catch (ParseException e) {
                                    log.warn("Erreur lors du parsing de la date de mise à jour: {}", line[5], e);
                                    dispo.setDateMiseAJour(new Date());
                                }
                            } else {
                                dispo.setDateMiseAJour(new Date());
                            }

                            if (line.length > 6 && line[6] != null && !line[6].trim().isEmpty()) {
                                try {
                                    dispo.setDateRemiseDisposition(parseDate(line[6]));
                                } catch (ParseException e) {
                                    log.warn("Erreur lors du parsing de la date de remise en disposition: {}", line[6], e);
                                }
                            }

                            if (line.length > 7 && line[7] != null && !line[7].trim().isEmpty()) {
                                dispo.setLienANSM(line[7]);
                            }

                            // Persister chaque entité individuellement
                            entityManager.persist(dispo);
                            entityManager.flush();
                            count++;

                            if (count % 100 == 0) {
                                log.info("Importation CIS_CIP_Dispo_Spec en cours: {} disponibilités traitées", count);
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Erreur lors du traitement de la ligne: {}", String.join(",", line), e);
                    }
                }

                log.info("Importation CIS_CIP_Dispo_Spec terminée. {} disponibilités importées avec succès", count);
                return count;
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Erreur lors de l'importation du fichier CSV", e);
            throw new RuntimeException("Échec de l'importation du fichier CSV", e);
        }
    }

    /**
     * Importe les médicaments MITM depuis le fichier CIS_MITM.csv.
     *
     * @param csvFilePath Chemin du fichier CSV dans les ressources
     * @return Nombre de médicaments MITM importés avec succès
     */
    @Transactional
    public int importCisMitm(String csvFilePath) {
        log.info("Début de l'importation des médicaments MITM depuis le fichier: {}", csvFilePath);

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                 CSVReader reader = new CSVReader(br)) {

                // Ignorer la ligne d'en-tête
                reader.readNext();

                String[] line;
                int count = 0;

                while ((line = reader.readNext()) != null) {
                    try {
                        // Vérifier si le médicament existe
                        String codeCis = line[0];
                        CisBdpm cisBdpm = null;

                        try {
                            cisBdpm = entityManager.createQuery(
                                    "SELECT c FROM CisBdpm c WHERE c.codeCis = :codeCis", CisBdpm.class)
                                    .setParameter("codeCis", codeCis)
                                    .getSingleResult();
                        } catch (NoResultException e) {
                            log.warn("Médicament avec code CIS {} non trouvé pour le MITM. Création d'un médicament temporaire.", codeCis);
                            // Créer un médicament temporaire si non trouvé
                            cisBdpm = new CisBdpm();
                            cisBdpm.setCodeCis(codeCis);
                            cisBdpm.setDenomination("Médicament temporaire " + codeCis);
                            cisBdpm.setFormePharmaceutique("Non spécifiée");
                            cisBdpm.setVoiesAdministration("Non spécifiée");
                            cisBdpm.setStatutAMM("Non spécifié");
                            cisBdpm.setTypeProcedureAMM("Non spécifié");
                            cisBdpm.setEtatCommercialisation("Non spécifié");
                            cisBdpm.setDateAMM(new Date());
                            entityManager.persist(cisBdpm);
                            entityManager.flush();
                        }

                        if (cisBdpm != null) {
                            CisMitm mitm = new CisMitm();
                            mitm.setCisBdpm(cisBdpm);

                            // Ne pas définir l'ID car il est auto-généré

                            if (line.length > 1 && line[1] != null && !line[1].trim().isEmpty()) {
                                mitm.setCodeAtc(line[1]);
                                mitm.setDenomination(cisBdpm.getDenomination());
                                mitm.setLienBdpm("https://base-donnees-publique.medicaments.gouv.fr/extrait.php?specid=" + codeCis);
                            } else {
                                // Valeurs par défaut pour les champs obligatoires
                                mitm.setCodeAtc("A00AA00");  // Code ATC par défaut
                                mitm.setDenomination(cisBdpm.getDenomination());
                                mitm.setLienBdpm("https://base-donnees-publique.medicaments.gouv.fr/extrait.php?specid=" + codeCis);
                            }

                            // Persister chaque entité individuellement
                            entityManager.persist(mitm);
                            entityManager.flush();
                            count++;

                            if (count % 100 == 0) {
                                log.info("Importation CIS_MITM en cours: {} médicaments MITM traités", count);
                            }
                        }
                    } catch (Exception e) {
                        log.warn("Erreur lors du traitement de la ligne: {}", String.join(",", line), e);
                    }
                }

                log.info("Importation CIS_MITM terminée. {} médicaments MITM importés avec succès", count);
                return count;
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Erreur lors de l'importation du fichier CSV", e);
            throw new RuntimeException("Échec de l'importation du fichier CSV", e);
        }
    }

    /**
     * Importe les liens vers les pages CT depuis le fichier HAS_LiensPageCT_bdpm.csv.
     *
     * @param csvFilePath Chemin du fichier CSV dans les ressources
     * @return Nombre de liens importés avec succès
     */
    @Transactional
    public int importHasLiensPageCT(String csvFilePath) {
        log.info("Début de l'importation des liens vers les pages CT depuis le fichier: {}", csvFilePath);

        try {
            ClassPathResource resource = new ClassPathResource(csvFilePath);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
                 CSVReader reader = new CSVReader(br)) {

                // Ignorer la ligne d'en-tête
                reader.readNext();

                String[] line;
                int count = 0;

                while ((line = reader.readNext()) != null) {
                    try {
                        if (line.length > 0 && line[0] != null && !line[0].trim().isEmpty()) {
                            HasLiensPageCT lien = new HasLiensPageCT();

                            // Utiliser le code dossier HAS comme clé primaire
                            lien.setCodeDossierHas(line[0]);

                            if (line.length > 1 && line[1] != null && !line[1].trim().isEmpty()) {
                                lien.setLienAvisCt(line[1]);
                            }

                            try {
                                entityManager.persist(lien);
                                count++;

                                if (count % 100 == 0) {
                                    entityManager.flush();
                                    log.info("Importation HAS_LiensPageCT_bdpm en cours: {} liens traités", count);
                                }
                            } catch (Exception e) {
                                log.warn("Erreur lors de la persistance du lien page CT: {}", lien.getCodeDossierHas(), e);
                            }
                        } else {
                            log.warn("Code dossier HAS manquant pour le lien page CT");
                        }
                    } catch (Exception e) {
                        log.warn("Erreur lors du traitement de la ligne: {}", String.join(",", line), e);
                    }
                }

                entityManager.flush();
                log.info("Importation HAS_LiensPageCT_bdpm terminée. {} liens importés avec succès", count);
                return count;
            }
        } catch (IOException | CsvValidationException e) {
            log.error("Erreur lors de l'importation du fichier CSV", e);
            throw new RuntimeException("Échec de l'importation du fichier CSV", e);
        }
    }

    /**
     * Importe le stock depuis un fichier CSV.
     * Cette méthode utilise une transaction Spring pour l'ensemble du processus,
     * mais traite chaque ligne indépendamment pour éviter les problèmes de rollback.
     *
     * @param csvFilePath Chemin du fichier CSV
     * @return Nombre d'éléments importés avec succès
     */
    @Autowired
private PlatformTransactionManager transactionManager;

@Transactional(propagation = Propagation.NOT_SUPPORTED)
public int importStock(String csvFilePath) {
    log.info("Début de l'importation du stock depuis le fichier: {}", csvFilePath);
    int count = 0;
    int errorCount = 0;

    try {
        ClassPathResource resource = new ClassPathResource(csvFilePath);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(resource.getInputStream()));
             CSVReader reader = new CSVReader(br)) {

            reader.readNext(); // Skip header
            String[] line;

            while ((line = reader.readNext()) != null) {
                // Traiter chaque ligne dans une transaction indépendante
                TransactionStatus status = null;
                try {
                    // Démarrer une nouvelle transaction
                    status = transactionManager.getTransaction(new DefaultTransactionDefinition());
                    
                    // Traiter la ligne
                    processStockLine(line);
                    
                    // Committer la transaction
                    transactionManager.commit(status);
                    count++;
                    
                    if (count % 50 == 0) {
                        log.info("Importation stock en cours: {} éléments traités", count);
                    }
                } catch (Exception e) {
                    // Rollback si une erreur se produit
                    if (status != null && !status.isCompleted()) {
                        transactionManager.rollback(status);
                    }
                    errorCount++;
                    log.warn("Erreur ligne {}: {} - {}", count + errorCount, 
                             String.join("|", line), e.getMessage());
                }
            }

            log.info("Importation stock terminée. {} éléments importés, {} erreurs", count, errorCount);
            return count;
        }
    } catch (IOException | CsvValidationException e) {
        log.error("Erreur d'importation", e);
        throw new RuntimeException("Échec import stock", e);
    }
}

    /**
     * Traite une ligne du fichier CSV de stock avec une nouvelle transaction.
     * Cela permet d'isoler les erreurs et d'éviter que tout l'import ne soit annulé.
     *
     * @param line Ligne du fichier CSV
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void processStockLineWithNewTransaction(String[] line) {
        processStockLine(line);
    }

    /**
     * Traite une ligne du fichier CSV de stock.
     *
     * @param line Ligne du fichier CSV
     */
    // N'utilisez pas @Transactional ici - la transaction est gérée par importStock
public void processStockLine(String[] line) {
    if (line == null || line.length < 7) {
        throw new IllegalArgumentException("Format de ligne invalide");
    }

    // Extraire les valeurs
    String codeCip13 = line[0].trim();
    Integer quantite = parseInteger(line[1]);
    String numeroLot = line[2];
    LocalDate datePeremption = parseLocalDate(line[3]);
    LocalDate dateMiseAJour = parseLocalDate(line[4]);
    Integer seuilAlerte = parseInteger(line[5]);
    String emplacement = line[6];
    
    // 1. Utiliser findOrCreateCisCipBdpm sans flush
    CisCipBdpm presentation = findOrCreateCisCipBdpm(codeCip13);
    
    // 2. Rechercher un stock existant
    StockMedicament existingStock = null;
    try {
        existingStock = entityManager.createQuery(
            "SELECT s FROM StockMedicament s WHERE s.presentation = :pres AND s.numeroLot = :lot",
            StockMedicament.class)
            .setParameter("pres", presentation)
            .setParameter("lot", numeroLot)
            .getSingleResult();
    } catch (NoResultException e) {
        // Pas de problème si le stock n'existe pas
    }
    
    // 3. Mettre à jour ou créer le stock
    if (existingStock != null) {
        existingStock.setQuantite(quantite != null ? quantite : 0);
        existingStock.setDatePeremption(datePeremption);
        existingStock.setDateMiseAJour(dateMiseAJour != null ? dateMiseAJour : LocalDate.now());
        existingStock.setSeuilAlerte(seuilAlerte != null ? seuilAlerte : 6);
        existingStock.setEmplacement(emplacement);
    } else {
        StockMedicament stock = new StockMedicament();
        stock.setPresentation(presentation);
        stock.setQuantite(quantite != null ? quantite : 0);
        stock.setNumeroLot(numeroLot);
        stock.setDatePeremption(datePeremption);
        stock.setDateMiseAJour(dateMiseAJour != null ? dateMiseAJour : LocalDate.now());
        stock.setSeuilAlerte(seuilAlerte != null ? seuilAlerte : 6);
        stock.setEmplacement(emplacement);
        entityManager.persist(stock);
    }
}

    /**
     * Trouve ou crée une présentation CisCipBdpm en utilisant JPA.
     * Cette version améliorée évite les flush explicites multiples.
     *
     * @param codeCip13 Code CIP13 de la présentation
     * @return Entité CisCipBdpm
     */
    private CisCipBdpm findOrCreateCisCipBdpm(String codeCip13) {
        // Recherche existante
        try {
            return entityManager.createQuery(
                "SELECT c FROM CisCipBdpm c WHERE c.codeCip13 = :code", CisCipBdpm.class)
                .setParameter("code", codeCip13)
                .getSingleResult();
        } catch (NoResultException e) {
            log.warn("Création présentation temporaire pour CIP13 {}", codeCip13);

            // Création CisBdpm temporaire
            String codeCis = codeCip13.substring(0, 7); // Extraction code CIS
            CisBdpm cisBdpm = findOrCreateCisBdpm(codeCis);

            // Création CisCipBdpm minimal
            CisCipBdpm newCip = new CisCipBdpm();
            newCip.setCisBdpm(cisBdpm);
            newCip.setCodeCip13(codeCip13);
            newCip.setCodeCip7(codeCis); // Utiliser le code CIS comme CIP7 temporaire
            newCip.setLibellePresentation("Présentation temporaire " + codeCip13);
            newCip.setEtatCommercialisation("Inconnu");
            entityManager.persist(newCip);

            // Pas de flush explicite ici pour éviter les problèmes

            return newCip;
        }
    }

    /**
     * Trouve ou crée un médicament CisBdpm.
     * Cette version améliorée évite les flush explicites.
     *
     * @param codeCis Code CIS du médicament
     * @return Entité CisBdpm
     */
    private CisBdpm findOrCreateCisBdpm(String codeCis) {
        try {
            return entityManager.createQuery(
                "SELECT c FROM CisBdpm c WHERE c.codeCis = :code", CisBdpm.class)
                .setParameter("code", codeCis)
                .getSingleResult();
        } catch (NoResultException e) {
            log.warn("Création médicament temporaire CIS {}", codeCis);

            CisBdpm newCis = new CisBdpm();
            newCis.setCodeCis(codeCis);
            newCis.setDenomination("Médicament temporaire " + codeCis);
            newCis.setFormePharmaceutique("Inconnue");
            newCis.setVoiesAdministration("Non spécifiée");
            newCis.setStatutAMM("Non spécifié");
            newCis.setTypeProcedureAMM("Non spécifié");
            newCis.setEtatCommercialisation("Non spécifié");
            newCis.setDateAMM(new Date());
            entityManager.persist(newCis);

            // Pas de flush explicite ici pour éviter les problèmes

            return newCis;
        }
    }

    /**
     * Recherche un stock existant par présentation et numéro de lot.
     * Version plus robuste qui évite les erreurs potentielles.
     *
     * @param presentation Présentation du médicament
     * @param numeroLot Numéro de lot
     * @return Entité StockMedicament ou null si non trouvée
     */
    private StockMedicament findStockByPresentationAndLot(CisCipBdpm presentation, String numeroLot) {
        try {
            if (presentation == null || presentation.getId() == null) {
                return null;
            }

            return entityManager.createQuery(
                "SELECT s FROM StockMedicament s WHERE s.presentation = :pres AND s.numeroLot = :lot",
                StockMedicament.class)
                .setParameter("pres", presentation)
                .setParameter("lot", numeroLot)
                .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    private Integer parseInteger(String value) {
        if (value == null || value.trim().isEmpty()) {
            return 0;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            log.warn("Valeur numérique invalide: {}", value);
            return 0;
        }
    }
    private CisBdpm parseCisBdpmLine(String[] line) throws ParseException {
        CisBdpm medicament = new CisBdpm();
        
        medicament.setCodeCis(line[0]);
        medicament.setDenomination(line[1]);
        medicament.setFormePharmaceutique(line[2]);
        medicament.setVoiesAdministration(line[3]);
        medicament.setStatutAMM(line[4]);
        medicament.setTypeProcedureAMM(line[5]);
        medicament.setEtatCommercialisation(line[6]);
        medicament.setDateAMM(parseDate(line[7]));
        
        // Vérifier si les colonnes optionnelles existent et ne sont pas vides
        if (line.length > 8 && line[8] != null && !line[8].trim().isEmpty()) {
            medicament.setStatutBdm(line[8]);
        }
        
        if (line.length > 9 && line[9] != null && !line[9].trim().isEmpty()) {
            medicament.setNumeroAutorisationEuropeenne(line[9]);
        }
        
        if (line.length > 10 && line[10] != null && !line[10].trim().isEmpty()) {
            medicament.setTitulaires(line[10]);
        }
        
        if (line.length > 11 && line[11] != null && !line[11].trim().isEmpty()) {
            medicament.setSurveillanceRenforcee(line[11]);
        }
        
        return medicament;
    }
     /**
     * Parse une chaîne de date au format dd/MM/yyyy.
     * 
     * @param dateStr Chaîne représentant une date
     * @return Objet Date parsé ou date actuelle si la chaîne est vide
     * @throws ParseException Si le format de date est invalide
     */
    private Date parseDate(String dateStr) throws ParseException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return new Date();
        }
        return DATE_FORMAT_FR.parse(dateStr);
    }
    private LocalDate parseLocalDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(dateStr.trim());
        } catch (DateTimeParseException e) {
            log.warn("Date invalide: {}, utilisation de la date du jour", dateStr);
            return LocalDate.now();
        }
    }
}
