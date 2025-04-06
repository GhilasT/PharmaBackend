import java.io.*;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DBManager {
    private Map<String, Database> basesDeDonnées; // Map pour stocker les bases
    private Database baseCourante;
    private DBConfig configurationBD;
    private DiskManager diskManager;
    private  BufferManager bufferManager;

    // Constructeur
    public DBManager(DBConfig config , DiskManager diskManager,BufferManager bufferManager) {
        this.basesDeDonnées = new HashMap<>();
        this.baseCourante = null;
        this.configurationBD = config;
        this.diskManager = diskManager;
        this.bufferManager = bufferManager;
        System.out.println("Gestionnaire initialisé avec le chemin : " + configurationBD.getDbpath());
    }

    // Méthode pour créer une base de données
    public void CreateDatabase(String nomBdd) {
        if (basesDeDonnées.containsKey(nomBdd)) {
            throw new IllegalArgumentException("La base de données " + nomBdd + " existe déjà.");
        }
        basesDeDonnées.put(nomBdd, new Database(nomBdd));
        System.out.println("Base de données créée (Vide) : " + nomBdd);

    }

    // Méthode pour définir une base de données courante
    public void SetCurrentDatabase(String nomBdd) {
        if (!basesDeDonnées.containsKey(nomBdd)) {
            System.out.println("La base de données " + nomBdd + " n'existe pas.");
        }else{
            this.baseCourante = basesDeDonnées.get(nomBdd);
            System.out.println("Base de données courante définie est  : " + nomBdd);
        }

    }

    // Méthode pour ajouter une table à la base courante
    public void AddTableToCurrentDatabase(Relation table) {
        if (baseCourante == null) {
            System.out.println("Aucune base de données courante n'est définie.");
        }else {
            baseCourante.addTable(table.getName(), table);
            System.out.println("Table ajoutée : " + table.getName());
        }

    }

    // Méthode pour récupérer une table dans la base courante
    public Relation GetTableFromCurrentDatabase(String nomTable) {
        Relation relation = null;
        if (baseCourante == null) {
            System.out.println("Aucune base de données courante n'est définie.");
        }else {
            relation = baseCourante.getTable(nomTable);
        }
        if(relation == null){
            System.out.println("la table n'existe pas "+nomTable+" :");
        }
        return relation;

    }

    // Méthode pour supprimer une table dans la base courante
    public Boolean RemoveTableFromCurrentDatabase(String nomTable) {
        if (baseCourante == null) {
            throw new IllegalStateException("Aucune base de données courante n'est définie.");
        }
        if (baseCourante.removeTable(nomTable)){

            return true ;
        }else {

            return false ;
        }

    }

    // Méthode pour supprimer une base de données
    public void RemoveDatabase(String nomBdd) {
        if (!basesDeDonnées.containsKey(nomBdd)) {
            System.out.println("La base de données " + nomBdd + " n'existe pas.");
        }else {
            basesDeDonnées.remove(nomBdd);
            if (baseCourante != null && baseCourante.getNom().equals(nomBdd)) {
                baseCourante = null;
            }
            System.out.println("Base de données supprimée : " + nomBdd);
        }
    }

    // Méthode pour supprimer toutes les tables de la base courante
    public void RemoveTablesFromCurrentDatabase() {
        if (baseCourante == null) {
            System.out.println("Aucune base de données courante n'est définie.");
            return;
        }

        if (baseCourante.getTables().isEmpty()) {
            System.out.println("Aucune table à supprimer dans la base courante : " + baseCourante.getNom());
        } else {
            baseCourante.removeAll();
            System.out.println("Toutes les tables ont été supprimées de la base courante : " + baseCourante.getNom());
        }
    }


    public void RemoveDatabases() {
        if (basesDeDonnées.isEmpty()) {
            System.out.println("Aucune base de données à supprimer.");
        } else {
            basesDeDonnées.clear();
            baseCourante = null;

        }
    }


    // Méthode pour lister toutes les bases de données
    public void ListDatabases() {
        if (basesDeDonnées.isEmpty()) {
            System.out.println("Aucune base de données existante.");
        } else {
            System.out.println("Bases de données existantes :");
            for (String nomBase : basesDeDonnées.keySet()) {
                System.out.println("- " + nomBase);
            }
        }
    }

    // Méthode pour lister les tables dans la base courante
    public void ListTablesInCurrentDatabase() {
        if (baseCourante == null) {
            System.out.println("Aucune base de données courante n'est définie.");
        } else {
            baseCourante.listTables();
        }
    }
    public boolean RemoveCurrentDatabase() {
        if (baseCourante == null) {
            System.out.println("Aucune base de données courante n'est définie.");
            return false;
        }

        String currentDatabaseName = baseCourante.getNom();
        if (basesDeDonnées.containsKey(currentDatabaseName)) {
            basesDeDonnées.remove(currentDatabaseName);
            baseCourante = null;

            return true;
        } else {
            System.out.println("Erreur : la base courante n'existe pas dans le SGBD.");
            return false;
        }
    }


    public void SaveState() throws Exception {
        File parentDirectory = new File(configurationBD.getDbpath()).getParentFile();
        String saveDirectoryPath = parentDirectory + "/databases.save";
        File saveDirectory = new File(saveDirectoryPath);

        // Créer le dossier s'il n'existe pas
        if (!saveDirectory.exists() && !saveDirectory.mkdir()) {
            throw new IOException("Erreur : impossible de créer le dossier " + saveDirectoryPath);
        }

        // Sauvegarder chaque base de données
        for (Map.Entry<String, Database> entry : basesDeDonnées.entrySet()) {
            String databaseName = entry.getKey();
            Database database = entry.getValue();
            JSONObject jsonDatabase = new JSONObject();
            jsonDatabase.put("nom", databaseName);

            JSONArray jsonRelations = new JSONArray();

            // Sauvegarder chaque relation de la base
            for (Map.Entry<String, Relation> tableEntry : database.getTables().entrySet()) {
                Relation table = tableEntry.getValue();
                JSONObject jsonRelation = new JSONObject();
                jsonRelation.put("nom", table.getName());

                // Sauvegarder les colonnes
                JSONArray jsonColumns = new JSONArray();
                for (ColInfo col : table.getColumns()) {
                    JSONObject jsonColumn = new JSONObject();
                    jsonColumn.put("nomColonne", col.getNomColonne());
                    jsonColumn.put("typeColonne", col.getTypeColonne());
                    jsonColumns.put(jsonColumn);
                }
                jsonRelation.put("colonnes", jsonColumns);

                // Sauvegarder l'identifiant de la Header Page
                JSONObject headerPageId = new JSONObject();
                headerPageId.put("fileIdx", table.getHeaderPageId().getFileIdx());
                headerPageId.put("pageIdx", table.getHeaderPageId().getPageIdx());
                jsonRelation.put("headerPageId", headerPageId);

                // Sauvegarder les records (données)
                JSONArray jsonRecords = new JSONArray();
                List<PageID> dataPages = table.getDataPages();
                for (PageID pageId : dataPages) {
                    List<Record> records = table.getRecordsInDataPage(pageId);
                    for (Record record : records) {
                        JSONArray jsonRecord = new JSONArray();
                        for (RecordInfo value : record.getValues()) {
                            jsonRecord.put(value.getValeur());
                        }
                        jsonRecords.put(jsonRecord);
                    }
                }
                jsonRelation.put("records", jsonRecords);

                jsonRelations.put(jsonRelation);
            }

            jsonDatabase.put("relations", jsonRelations);

            // Écrire dans le fichier
            String filePath = saveDirectoryPath + "/" + databaseName + ".json";
            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(jsonDatabase.toString(4)); // Indentation pour lisibilité
                System.out.println("Base de données sauvegardée : " + databaseName + " dans " + filePath);
            }
        }
    }

    // Méthode pour charger l'état des bases de données

    public void LoadState() {
        try {
            // Définir le chemin de sauvegarde
            File parentDirectory = new File(configurationBD.getDbpath()).getParentFile();
            String saveDirectoryPath = parentDirectory + "/databases.save";
            File saveDirectory = new File(saveDirectoryPath);

            // Vérifier l'existence du répertoire de sauvegarde
            if (!saveDirectory.exists() || !saveDirectory.isDirectory()) {
                System.out.println("Aucune sauvegarde trouvée dans : " + saveDirectoryPath);
                return;
            }

            // Lister tous les fichiers JSON dans le répertoire
            File[] files = saveDirectory.listFiles((dir, name) -> name.endsWith(".json"));
            if (files == null || files.length == 0) {
                System.out.println("Aucune base sauvegardée.");
                return;
            }

            // Charger les métadonnées de chaque base
            for (File file : files) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    // Lire le contenu JSON
                    StringBuilder jsonContent = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        jsonContent.append(line);
                    }

                    // Parser le contenu JSON
                    JSONObject jsonDatabase = new JSONObject(jsonContent.toString());
                    String databaseName = jsonDatabase.getString("nom");
                    Database database = new Database(databaseName);

                    // Charger les relations (métadonnées uniquement)
                    JSONArray jsonRelations = jsonDatabase.getJSONArray("relations");
                    for (int i = 0; i < jsonRelations.length(); i++) {
                        JSONObject jsonRelation = jsonRelations.getJSONObject(i);
                        String tableName = jsonRelation.getString("nom");

                        // Charger les colonnes
                        JSONArray jsonColumns = jsonRelation.getJSONArray("colonnes");
                        List<ColInfo> columns = new ArrayList<>();
                        for (int j = 0; j < jsonColumns.length(); j++) {
                            JSONObject jsonColumn = jsonColumns.getJSONObject(j);
                            columns.add(new ColInfo(
                                    jsonColumn.getString("nomColonne"),
                                    jsonColumn.getString("typeColonne")
                            ));
                        }

                        // Charger le Header Page ID
                        JSONObject headerPageIdJson = jsonRelation.getJSONObject("headerPageId");
                        PageID headerPageId = new PageID(
                                headerPageIdJson.getInt("fileIdx"),
                                headerPageIdJson.getInt("pageIdx")
                        );

                        // Ajouter la relation au gestionnaire (métadonnées uniquement)
                        Relation relation = new Relation(tableName, columns, diskManager, bufferManager);
                        relation.setHeaderPageId(headerPageId); // Méthode à implémenter dans Relation
                        database.addTable(tableName, relation);
                    }

                    // Ajouter la base au gestionnaire
                    basesDeDonnées.put(databaseName, database);
                    System.out.println("Base restaurée : " + databaseName);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    public Database getBaseCourante() {
        return baseCourante;
    }
}

