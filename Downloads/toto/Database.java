import java.util.HashMap;
import java.util.Map;

public class Database {
    private String nom;
    private Map<String, Relation> tables;

    // Constructeur
    public Database(String nom) {
        this.nom = nom;
        this.tables = new HashMap<>();
    }

    // Ajouter une table
    public void addTable(String tableName, Relation table) {
        String normalizedTableName = tableName.toLowerCase().trim(); // Normalisation du nom
        if (tables.containsKey(normalizedTableName)) {
            throw new IllegalArgumentException("La table " + tableName + " existe déjà.");
        }
        tables.put(normalizedTableName, table);
        System.out.println("Table ajoutée : " + tableName);
        System.out.println("Tables actuelles : " + tables.keySet());
    }

    // Récupérer une table
    public Relation getTable(String tableName) {
        String normalizedTableName = tableName.toLowerCase().trim(); // Normalisation du nom
        if (!tables.containsKey(normalizedTableName)) {
            System.out.println("La table " + tableName + " n'existe pas.");
            return null;
        }
        return tables.get(normalizedTableName);
    }

    // Lister les tables
    public void listTables() {
        if (tables.isEmpty()) {
            System.out.println("Aucune table dans la base " + nom + ".");
        } else {
            System.out.println("Tables dans la base " + nom + ":");
            for (Map.Entry<String, Relation> entry : tables.entrySet()) {
                String tableName = entry.getKey();
                Relation relation = entry.getValue();
                System.out.println("- " + tableName + " (colonnes : " + relation.getColumns().size() + ")");
                for (ColInfo col : relation.getColumns()) {
                    System.out.println("  - " + col.toString());
                }
            }
        }
    }

    // Supprimer une table
    public boolean removeTable(String tableName) {
        String normalizedTableName = tableName.toLowerCase().trim(); // Normalisation du nom
        if (tables.containsKey(normalizedTableName)) {
            tables.remove(normalizedTableName);
            System.out.println("Table supprimée : " + tableName);
            return true;
        } else {
            System.out.println("La table " + tableName + " n'existe pas.");
            return false;
        }
    }

    // Supprimer toutes les tables
    public void removeAll() {
        if (tables.isEmpty()) {
            System.out.println("Aucune table à supprimer dans la base " + nom + ".");
        } else {
            System.out.println("Suppression de toutes les tables dans la base " + nom + "...");
            tables.clear();
        }
    }

    // Récupérer le nom de la base
    public String getNom() {
        return nom;
    }

    // Récupérer toutes les tables (pour d'autres traitements)
    public Map<String, Relation> getTables() {
        return tables;
    }
}