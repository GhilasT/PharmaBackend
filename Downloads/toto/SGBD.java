import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.util.function.Predicate;

// compilation javac -cp libs/json-20180813.jar:libs/gson-2.11.0.jar -d out *.java
// exceution : java -cp out:libs/json-20180813.jar:libs/gson-2.11.0.jar SGBD config.json
public class SGBD {
    private DBConfig dbConfig;
    private DiskManager diskManager;
    private BufferManager bufferManager;
    private DBManager dbManager;

    // Constructeur de la classe SGBD
    public SGBD(DBConfig config) throws IOException {
        this.dbConfig = config;
        this.diskManager = new DiskManager(config);
        this.bufferManager = new BufferManager(config, diskManager);
        this.dbManager = new DBManager(config, diskManager, bufferManager);

        dbManager.LoadState();
    }

    public void Run() {
        Scanner scanner = new Scanner(System.in);
        String commande;

        System.out.println("Bienvenue dans le SGBD. Tapez vos commandes ou 'QUIT' pour sortir.");
        System.out.print("? "); // Prompt initial

        while (true) {
            try {
                // Lire la commande de l'utilisateur
                commande = scanner.nextLine().trim();

                // Vérifier si l'utilisateur veut quitter
                if (commande.equalsIgnoreCase("QUIT")) {
                    ProcessQuitCommand();
                    System.out.println("Au revoir !");
                    break;
                }

                // Déléguer le traitement de la commande
                ProcessXCommand(commande);

            } catch (Exception e) {
                System.err.println("Erreur : Tapez vos commandes ou 'QUIT' pour sortir : " + e.getMessage());
            }

            // Réafficher le prompt
            System.out.print("? ");
        }

        scanner.close();
    }

    public void ProcessQuitCommand() {
        try {
            // Sauvegarder l'état des bases
            dbManager.SaveState();

            // Vider les buffers
            bufferManager.FlushBuffers();

            System.out.println("Toutes les informations ont été sauvegardées avec succès.");
        } catch (Exception e) {
            System.err.println("Erreur lors de la sauvegarde ou du vidage des buffers : " + e.getMessage());
        }
    }


    private Object parseValue(String value, String colType) {
        if (colType.startsWith("CHAR(") || colType.startsWith("VARCHAR(")) {

            int maxLength = Integer.parseInt(colType.replaceAll("\\D+", ""));
            if (value.length() > maxLength) {
                throw new IllegalArgumentException("La valeur dépasse la longueur maximale pour " + colType + ".");
            }
            return value;
        }

        switch (colType.toUpperCase()) {
            case "INT":
                return Integer.parseInt(value);
            case "REAL":
                return Float.parseFloat(value);
            default:
                throw new IllegalArgumentException("Type de colonne inconnu : " + colType);
        }
    }

    public void ProcessXCommand(String texteCommande) {
        try {
            texteCommande = texteCommande.trim();
            String[] mots = texteCommande.split("\\s+", 2); // Découper sur le premier espace
            String commande = mots[0].toUpperCase();

            // ** Validation de base **
            if (mots.length < 2 || mots[1].trim().isEmpty()) {
                throw new IllegalArgumentException("Commande incorrecte ou incomplète : " + texteCommande);
            }

            switch (commande) {
                case "SET":
                    // Gestion de SET DATABASE
                    if (mots.length < 2 || !mots[1].toUpperCase().startsWith("DATABASE")) {
                        throw new IllegalArgumentException("Syntaxe incorrecte pour la commande SET DATABASE.");
                    }
                    String nomBase = mots[1].substring("DATABASE".length()).trim();
                    dbManager.SetCurrentDatabase(nomBase);
                    break;

                case "CREATE":
                    if (mots.length < 2) {
                        throw new IllegalArgumentException("Syntaxe incorrecte pour la commande CREATE.");
                    }
                    if (mots[1].toUpperCase().startsWith("DATABASE")) {
                        String nomBaseCre = mots[1].substring("DATABASE".length()).trim();
                        dbManager.CreateDatabase(nomBaseCre);

                    } else if (mots[1].toUpperCase().startsWith("TABLE")) {
                        if (dbManager.getBaseCourante() != null) {
                            String detailsTable = mots[1].substring("TABLE".length()).trim();
                            String[] parts = detailsTable.split("\\(", 2);
                            if (parts.length != 2) {
                                throw new IllegalArgumentException("Syntaxe incorrecte pour la commande CREATE TABLE.");
                            }
                            String nomTable = parts[0].trim();
                            String colonnesBrutes = parts[1].replace(")", "").trim();

                            List<ColInfo> colonnes = new ArrayList<>();
                            for (String colonne : colonnesBrutes.split(",")) {
                                String[] colParts = colonne.trim().split(":");
                                colonnes.add(new ColInfo(colParts[0].trim(), colParts[1].trim()));
                            }

                            Relation relation = new Relation(nomTable, colonnes, diskManager, bufferManager);
                            dbManager.AddTableToCurrentDatabase(relation);

                            // Logs
                            System.out.println("Table " + nomTable + " créée avec succès !");
                        } else {
                            System.out.println("Pas de base courante définie. Veuillez définir une base avec SET DATABASE avant de créer une table.");
                        }
                    }
                    break;

                case "LIST":
                    // LIST DATABASES ou LIST TABLES
                    if (mots.length < 2) {
                        throw new IllegalArgumentException("Syntaxe incorrecte pour la commande LIST.");
                    }
                    if (mots[1].equalsIgnoreCase("DATABASES")) {
                        dbManager.ListDatabases();
                    } else if (mots[1].equalsIgnoreCase("TABLES")) {
                        dbManager.ListTablesInCurrentDatabase();
                    } else {
                        throw new IllegalArgumentException("Commande LIST non reconnue.");
                    }
                    break;

                case "DROP":
                    if (mots.length < 2) {
                        throw new IllegalArgumentException("Syntaxe incorrecte pour la commande DROP.");
                    }
                    String dropType = mots[1].toUpperCase().trim();

                    if (dropType.startsWith("TABLE ")) {
                        String tableASupprimer = dropType.substring("TABLE".length()).trim();
                        boolean removed = dbManager.RemoveTableFromCurrentDatabase(tableASupprimer);
                        if (!removed) {
                            System.out.println("La table " + tableASupprimer + " n'existe pas ou n'a pas été supprimée.");
                        }
                    } else if (dropType.equals("TABLES")) {
                        // Gestion de DROP TABLES
                        dbManager.RemoveTablesFromCurrentDatabase();
                        
                    } else if (dropType.equals("DATABASE")) {
                        // Gestion de DROP DATABASE
                        if (dbManager.RemoveCurrentDatabase()) {
                            System.out.println("Base de données courante supprimée avec succès !");
                        } else {
                            System.out.println("Aucune base de données courante définie ou la suppression a échoué.");
                        }
                    } else if (dropType.equals("DATABASES")) {
                        // Gestion de DROP DATABASES
                        dbManager.RemoveDatabases();
                        System.out.println("Toutes les bases de données ont été supprimées.");
                    } else {
                        throw new IllegalArgumentException("Commande DROP non reconnue ou syntaxe incorrecte.");
                    }
                    break;


                case "INSERT":
                    // Validation syntaxique : la commande doit commencer par "INSERT INTO"
                    if (!mots[1].toUpperCase().startsWith("INTO")) {
                        throw new IllegalArgumentException("Syntaxe incorrecte pour la commande INSERT.");
                    }

                    // Extraction des détails de la commande
                    String detailsInsert = mots[1].substring("INTO".length()).trim();
                    String[] partsInsert = detailsInsert.split("\\s+VALUES\\s*", 2);
                    if (partsInsert.length != 2 || !partsInsert[1].startsWith("(") || !partsInsert[1].endsWith(")")) {
                        throw new IllegalArgumentException("Syntaxe incorrecte pour la commande INSERT.");
                    }

                    // Nom de la relation
                    String tableNameInsert = partsInsert[0].trim();

                    // Vérification de l'existence de la base courante
                    Database currentDatabase = dbManager.getBaseCourante();
                    if (currentDatabase == null) {
                        throw new IllegalStateException("Aucune base de données courante n'est définie.");
                    }

                    // Vérification de l'existence de la relation
                    Relation tableInsert = currentDatabase.getTable(tableNameInsert);
                    if (tableInsert == null) {
                        throw new IllegalArgumentException("Table introuvable : " + tableNameInsert);
                    }

                    // Extraction et nettoyage des valeurs entre parenthèses
                    String valuesString = partsInsert[1].substring(1, partsInsert[1].length() - 1).trim();
                    String[] values = valuesString.split(",");

                    // Vérification du nombre de colonnes
                    List<ColInfo> columns = tableInsert.getColumns();
                    if (values.length != columns.size()) {
                        throw new IllegalArgumentException("Nombre de valeurs incorrect : attendu " + columns.size() + ", trouvé " + values.length);
                    }

                    // Validation et conversion des valeurs
                    List<RecordInfo> recordValues = new ArrayList<>();
                    for (int i = 0; i < values.length; i++) {
                        String rawValue = values[i].trim();
                        String colType = columns.get(i).getTypeColonne();

                        // Gestion des chaînes de caractères (enlever les guillemets)
                        if (colType.startsWith("CHAR(") || colType.startsWith("VARCHAR(")) {
                            if (!rawValue.startsWith("\"") || !rawValue.endsWith("\"")) {
                                throw new IllegalArgumentException("Valeur de type chaîne mal formatée : " + rawValue);
                            }
                            rawValue = rawValue.substring(1, rawValue.length() - 1); // Retirer les guillemets
                        }

                        // Conversion en type compatible
                        Object value = parseValue(rawValue, colType);
                        recordValues.add(new RecordInfo(colType, value));
                    }

                    // Création et insertion du record
                    Record record = new Record(recordValues);
                    tableInsert.InsertRecord(record);


                    break;

                case "SELECT":
                    ProcessSelectCommand(texteCommande);
                    break;

                case "BULKINSERT":
                    if (mots.length < 2 || !mots[1].startsWith("INTO")) {
                        throw new IllegalArgumentException("Syntaxe incorrecte pour la commande BULKINSERT.");
                    }

                    // Extraire le nom de la table et le fichier CSV
                    String detailsBulkInsert = mots[1].substring("INTO".length()).trim();
                    String[] bulkInsertParts = detailsBulkInsert.split("\\s+", 2);

                    if (bulkInsertParts.length != 2) {
                        throw new IllegalArgumentException("Syntaxe incorrecte pour la commande BULKINSERT.");
                    }

                    String tableNameBulkInsert = bulkInsertParts[0].trim(); // Nom de la table
                    String fileNameBulkInsert = bulkInsertParts[1].trim(); // Nom du fichier

                    // Vérifier si le fichier est accessible
                    File csvFile;
                    if (fileNameBulkInsert.contains("/") || fileNameBulkInsert.contains("\\")) {
                        csvFile = new File(fileNameBulkInsert); // Chemin absolu
                    } else {
                        csvFile = new File(System.getProperty("user.dir"), fileNameBulkInsert); // Chemin relatif au projet
                    }

                    //System.out.println("Chemin complet du fichier CSV : " + csvFile.getAbsolutePath());

                    if (!csvFile.exists() || !csvFile.isFile()) {
                        throw new IllegalArgumentException("Fichier CSV introuvable : " + fileNameBulkInsert);
                    }

                    // Obtenir la base courante
                    Database currentDatabaseBulkInsert = dbManager.getBaseCourante();
                    if (currentDatabaseBulkInsert == null) {
                        throw new IllegalStateException("Aucune base de données courante n'est définie.");
                    }

                    // Obtenir la table
                    Relation tableBulkInsert = currentDatabaseBulkInsert.getTable(tableNameBulkInsert);
                    if (tableBulkInsert == null) {
                        throw new IllegalArgumentException("Table " + tableNameBulkInsert + " introuvable dans la base courante.");
                    }

                    // Lecture et insertion des données du fichier CSV
                    try (Scanner csvScanner = new Scanner(csvFile)) {
                        List<ColInfo> tableColumns = tableBulkInsert.getColumns();
                        //System.out.println("Nombre de colonnes dans la table : " + tableColumns.size());

                        while (csvScanner.hasNextLine()) {
                            String line = csvScanner.nextLine().trim();
                            if (!line.isEmpty()) {
                                String[] valuesBulkInsert = line.split(",");
                                System.out.println("Valeurs extraites : " + Arrays.toString(valuesBulkInsert));

                                if (valuesBulkInsert.length != tableColumns.size()) {
                                    throw new IllegalArgumentException("Nombre de valeurs incorrect dans la ligne : " + line);
                                }

                                // Construire un record pour chaque ligne, en utilisant parseValue
                                List<RecordInfo> ALLecordValues = new ArrayList<>();
                                for (int i = 0; i < valuesBulkInsert.length; i++) {
                                    String colType = tableColumns.get(i).getTypeColonne();
                                    Object value = parseValue(valuesBulkInsert[i].trim(), colType); // Utiliser parseValue
                                    ALLecordValues.add(new RecordInfo(colType, value));
                                    System.out.println("Colonne : " + tableColumns.get(i).getNomColonne() + ", Valeur : " + value);
                                }

                                // Insérer dans la table
                                Record recordBulkInsert = new Record(ALLecordValues);
                                tableBulkInsert.InsertRecord(recordBulkInsert);
                                //System.out.println("Record inséré : " + recordBulkInsert);
                            }
                        }

                        //System.out.println("BULKINSERT terminé avec succès pour la table " + tableNameBulkInsert + " avec : " + tableBulkInsert.GetAllRecords());
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new IllegalArgumentException("Erreur lors du traitement de BULKINSERT : " + e.getMessage());
                    }
                    break;

                case "SELECTINDEX":
                    String detailsSelectIndex = mots[1].substring("FROM".length()).trim();
                    String[] selectParts = detailsSelectIndex.split("WHERE");
                    if (selectParts.length != 2) {
                        throw new IllegalArgumentException("Syntaxe incorrecte pour SELECTINDEX.");
                    }
                    String tableNameSelectIndex = selectParts[0].trim();
                    String[] conditionParts = selectParts[1].split("=");

                    String columnNameSelectIndex = conditionParts[0].trim();
                    String valueSelectIndex = conditionParts[1].trim();

                    Database currentDatabaseSelectIndex = dbManager.getBaseCourante();
                    if (currentDatabaseSelectIndex == null) {
                        throw new IllegalStateException("Aucune base courante définie.");
                    }
                    Relation tableSelectIndex = currentDatabaseSelectIndex.getTable(tableNameSelectIndex);
                    if (tableSelectIndex == null) {
                        throw new IllegalArgumentException("Table introuvable : " + tableNameSelectIndex);
                    }

                    // Rechercher dans l'index
                    List<Record> result = tableSelectIndex.selectUsingIndex(columnNameSelectIndex, valueSelectIndex);
                    System.out.println("Résultats : " + result);
                    break;

                case "CREATEINDEX":

                    String detailsCreateIndex = mots[1].substring("ON".length()).trim();
                    String[] indexParts = detailsCreateIndex.split("KEY=");
                    if (indexParts.length != 2 || !indexParts[1].contains("ORDER=")) {
                        throw new IllegalArgumentException("Syntaxe incorrecte pour CREATEINDEX.");
                    }
                    String tableNameIndex = indexParts[0].trim();
                    String[] keyOrderParts = indexParts[1].split("ORDER=");
                    String columnNameIndex = keyOrderParts[0].trim();
                    int order = Integer.parseInt(keyOrderParts[1].trim());


                    Database currentDatabaseIndex = dbManager.getBaseCourante();
                    if (currentDatabaseIndex == null) {
                        throw new IllegalStateException("Aucune base courante définie.");
                    }
                    Relation tableIndex = currentDatabaseIndex.getTable(tableNameIndex);
                    if (tableIndex == null) {
                        throw new IllegalArgumentException("Table introuvable : " + tableNameIndex);
                    }

                    // Création de l'index
                    tableIndex.createIndex(columnNameIndex, order);
                    System.out.println("Index créé pour la colonne " + columnNameIndex + " dans la table " + tableNameIndex);
                    break;
                default:
                    throw new IllegalArgumentException("Commande non reconnue : " + commande);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors du traitement de la commande : " + texteCommande);
            System.err.println("Message d'erreur : " + e.getMessage());
        }


    }


    public void ProcessSelectCommand(String texteCommande) throws Exception {
        if (!texteCommande.toUpperCase().startsWith("SELECT")) {
            throw new IllegalArgumentException("La commande ne commence pas par SELECT : " + texteCommande);
        }

        // Découper SELECT colonne(s) FROM table alias WHERE condition
        String[] parts = texteCommande.split("\\s+", 4);

        // Gestion des colonnes
        String columnsPart = parts[1];
        boolean selectAll = columnsPart.equals("*");

        // Identifier la table et son alias
        String fromPart = parts[3];
        String[] fromParts = fromPart.split("\\s+");
        String tableName = fromParts[0];
        String alias = fromParts.length > 1 ? fromParts[1] : null;

        // Gestion des conditions (WHERE)
        String conditionPart = texteCommande.contains("WHERE") ? texteCommande.split("WHERE")[1].trim() : null;

        // Récupérer la table et la base courante
        Database currentDatabase = dbManager.getBaseCourante();
        if (currentDatabase == null) {
            throw new IllegalStateException("Aucune base de données courante n'est définie.");
        }
        Relation table = currentDatabase.getTable(tableName);
        if (table == null) {
            throw new IllegalArgumentException("Table introuvable : " + tableName);
        }

        // Récupérer tous les records
        List<Record> records = table.GetAllRecords();

        // Filtrer les records selon la condition
        if (conditionPart != null) {
            String[] conditions = conditionPart.split("\\s+AND\\s+");
            for (String condition : conditions) {
                Condition cond = new Condition(condition, table.getColumns(), alias);
                records = records.stream()
                        .filter(record -> cond.evaluate(record))
                        .toList();
            }
        }

        // Sélectionner les colonnes spécifiques ou toutes les colonnes
        List<String> selectedColumns = selectAll ? null : Arrays.asList(columnsPart.split(","));

        // Afficher les résultats
        System.out.println("Résultats :");
        for (Record record : records) {
            if (selectAll) {
                System.out.println(record);
            } else {
                System.out.print("[");
                List<RecordInfo> values = record.getValues();
                for (String column : selectedColumns) {
                    String actualColumnName = alias != null ? column.split("\\.")[1] : column.trim(); // Extraire la colonne en cas d'alias
                    for (int i = 0; i < table.getColumns().size(); i++) {
                        if (table.getColumns().get(i).getNomColonne().equalsIgnoreCase(actualColumnName)) {
                            System.out.print(values.get(i).getValeur() + " ");
                        }
                    }
                }
                System.out.println("]");
            }
        }
        System.out.println("Total records = " + records.size());
    }

    public DBManager getDBmanager() {
        return dbManager;
    }
/*
    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage : java SGBD <config-file-path>");
            return;
        }
        String configPath = args[0];
        try {
            DBConfig config = DBConfig.LoadDBConfig(new File(configPath));
            SGBD sgbd = new SGBD(config);
            sgbd.Run();
        } catch (Exception e) {
            System.err.println("Erreur lors de l'initialisation du SGBD : " + e.getMessage());
            e.printStackTrace();
        }
    }

public static void main(String[] args) {
    try {
        // Création de la configuration du SGBD
        DBConfig config = new DBConfig(
                "/Users/imenebakhouche/Downloads/toto/Test", // Remplacez par un chemin valide
                2480,              // Taille des pages
                4880,             // Taille maximale des fichiers
                3,                 // Nombre de buffers
                "LRU"              // Politique de gestion des buffers
        );

        // Initialisation du SGBD
        SGBD sgbd = new SGBD(config);

        System.out.println(">>> Début des tests du SGBD <<<");

        // 1. Création d'une base de données
        System.out.println(">>> Test : CREATE DATABASE");
        sgbd.ProcessXCommand("CREATE DATABASE TestBDD2");
        //ystem.out.println("Base de données TestDB créée avec succès.");

        // 2. Définir la base courante
        System.out.println(">>> Test : SET DATABASE");
        sgbd.ProcessXCommand("SET DATABASE TestBDD2");

        // 3. Création des tables
        System.out.println(">>> Test : CREATE TABLE");
        //sgbd.ProcessXCommand("CREATE TABLE Personnes (ID:INT,Nom:CHAR(20),Age:INT)");
        sgbd.ProcessXCommand("CREATE TABLE Pomme (C1:INT,C2:CHAR(3),C3:INT)");
        //System.out.println("Tables créées avec succès.");

        // 4. Insertion de données
//        System.out.println(">>> Test : INSERT INTO");
//        sgbd.ProcessXCommand("INSERT INTO Personnes VALUES (1,\"Alice\",25)");
//        sgbd.ProcessXCommand("INSERT INTO Personnes VALUES (2,\"Bob\",30)");
//        sgbd.ProcessXCommand("INSERT INTO Personnes VALUES (3,\"Charlie\",35)");

//        sgbd.ProcessXCommand("INSERT INTO Pomme VALUES (33,\"aab\",2)");
//        sgbd.ProcessXCommand("INSERT INTO Pomme VALUES (2,\"ab\",2)");
//        sgbd.ProcessXCommand("INSERT INTO Pomme VALUES (1,\"agh\",1)");
//        //System.out.println("Données insérées avec succès.");

//        // 5. BULKINSERT
        sgbd.ProcessXCommand("CREATE TABLE S (C1:INT,C2:REAL,C3:INT,C4:INT,C5:INT)");
        System.out.println(">>> Test : BULKINSERT");
        try {
            sgbd.ProcessXCommand("BULKINSERT INTO S S.csv"); // Assurez-vous que S.csv existe

        } catch (Exception e) {
            System.err.println("Erreur lors du BULKINSERT : " + e.getMessage());
        }

        // 6. Sélection de toutes les données

        System.out.println(">>> Test : SELECT * FROM S");
        sgbd.ProcessXCommand("SELECT * FROM S");

        // 7. Sélection avec condition WHERE
        //System.out.println(">>> Test : SELECT * FROM Personnes WHERE Age > 25");
        //sgbd.ProcessXCommand("SELECT * FROM Personnes WHERE Age > 25");

//        System.out.println(">>> Test : SELECT * FROM Pomme WHERE C3=2");
//        sgbd.ProcessXCommand("SELECT * FROM Pomme WHERE C3=2");
//
//        // 8. Sélection avec des colonnes spécifiques
//        System.out.println(">>> Test : SELECT C2 FROM Pomme WHERE C1=1");
//        sgbd.ProcessXCommand("SELECT C2 FROM Pomme WHERE C1=1");
//
//        System.out.println(">>> Test : SELECT C1,C2 FROM Pomme WHERE C3=1");
//        sgbd.ProcessXCommand("SELECT C1,C2 FROM Pomme WHERE C3=1");
//
//        // 9. Sélection avec conditions multiples
//        System.out.println(">>> Test : SELECT * FROM Pomme WHERE C1=1 AND C3=2");
//        sgbd.ProcessXCommand("SELECT * FROM Pomme WHERE C1=1 AND C3=2");
//
//        System.out.println(">>> Test : SELECT C1 FROM Pomme WHERE C2=\"aab\" AND C3=2");
//        sgbd.ProcessXCommand("SELECT C1 FROM Pomme WHERE C2=\"aab\" AND C3=2");

        // Sauvegarde de l'état
        //sgbd.dbManager.SaveState();

        System.out.println(">>> Fin des tests du SGBD <<<");

    } catch (Exception e) {
        System.err.println("Erreur pendant les tests : " + e.getMessage());
        e.printStackTrace();
    }
}
*/public static void main(String[] args) {
    try {
        // Création de la configuration du SGBD
        DBConfig config = new DBConfig(
                "/Users/imenebakhouche/Downloads/toto/Test", // Remplacez par un chemin valide
                2480,              // Taille des pages
                4880,              // Taille maximale des fichiers
                3,                 // Nombre de buffers
                "LRU"              // Politique de gestion des buffers
        );

        // Initialisation du SGBD
        SGBD sgbd = new SGBD(config);

        System.out.println(">>> Début des tests du SGBD <<<");

        // 1. Création d'une base de données
        System.out.println(">>> Test : CREATE DATABASE");
        sgbd.ProcessXCommand("CREATE DATABASE TestBDDA");
        sgbd.ProcessXCommand("SET DATABASE TestBDDA");

        // 2. Création d'une table
        System.out.println(">>> Test : CREATE TABLE");
        sgbd.ProcessXCommand("CREATE TABLE Pomme (C1:INT,C2:VARCHAR(3),C3:INT)");

        // 3. Insertion de données dans la table
        System.out.println(">>> Test : INSERT INTO Pomme");
        sgbd.ProcessXCommand("INSERT INTO Pomme VALUES (1,\"aab\",2)");
        sgbd.ProcessXCommand("INSERT INTO Pomme VALUES (2,\"ab\",2)");
        sgbd.ProcessXCommand("INSERT INTO Pomme VALUES (1,\"agh\",1)");

        // 4. Requête SELECT pour récupérer toutes les données
        System.out.println(">>> Test : SELECT * FROM Pomme");
        sgbd.ProcessXCommand("SELECT * FROM Pomme");

        // 5. Requête SELECT avec condition
        System.out.println(">>> Test : SELECT * FROM Pomme WHERE C1=1");
        sgbd.ProcessXCommand("SELECT * FROM Pomme WHERE C1=1");

        // 6. Requête SELECT avec alias et condition
        System.out.println(">>> Test : SELECT p.C2 FROM Pomme p WHERE p.C1=1");
        sgbd.ProcessXCommand("SELECT p.C2 FROM Pomme p WHERE p.C1=1");

        // 7. Requête SELECT avec plusieurs colonnes et alias
        System.out.println(">>> Test : SELECT p.C2,p.C3 FROM Pomme p WHERE p.C2=\"ab\"");
        sgbd.ProcessXCommand("SELECT p.C2,p.C3 FROM Pomme p WHERE p.C2=\"ab\"");

        // 8. Test avec une condition complexe
        System.out.println(">>> Test : SELECT * FROM Pomme WHERE C1=1 AND C3=2");
        sgbd.ProcessXCommand("SELECT * FROM Pomme WHERE C1=1 AND C3=2");

        // 9. Suppression de la table
        System.out.println(">>> Test : DROP TABLE");
        sgbd.ProcessXCommand("DROP TABLE Pomme");


        // 10. Liste des tables après suppression
        System.out.println(">>> Test : LIST TABLES après suppression");
        sgbd.ProcessXCommand("LIST TABLES");

        System.out.println(">>> Fin des tests du SGBD <<<");

    } catch (Exception e) {
        System.err.println("Erreur pendant les tests : " + e.getMessage());
        e.printStackTrace();
    }
}

}