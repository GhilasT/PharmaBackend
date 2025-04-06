import org.json.JSONException;

import java.io.IOException;
import java.util.List;

public class testBaseD {
    public static void main(String[] args) throws Exception {
        try {
            // *** Configuration de la base de données ***
            String dbpath = "Test";
            DBConfig config = new DBConfig(
                    "/Users/imenebakhouche/Downloads/Projet_Correct/Test",
                    1024, // Taille des pages
                    2096, // Taille maximale des fichiers
                    3,    // Nombre de buffers
                    "LRU" // Politique de gestion des buffers
            );
            DiskManager disk = new DiskManager(config);
            BufferManager bm = new BufferManager(config, disk);

            // *** Création du gestionnaire de bases de données ***
            DBManager dbManager = new DBManager(config,disk,bm);

            // *** Création d'une base de données ***
            String BDD1 = "Universites";
            String BDD2 = "Ecole";
            System.out.println("\nCréation de la base de données : " + BDD1);
            dbManager.CreateDatabase(BDD1);
            dbManager.CreateDatabase(BDD2);
            dbManager.SetCurrentDatabase(BDD1);

            // *** Création des tables ***
            System.out.println("\nCréation des tables...");
            List<ColInfo> studentColumns = List.of(
                    new ColInfo("ID", "INT"),
                    new ColInfo("Name", "CHAR(25)"),
                    new ColInfo("Age", "INT")
            );
            Relation studentTable = new Relation("Students", studentColumns, disk, bm);
            dbManager.AddTableToCurrentDatabase(studentTable);

            List<ColInfo> teacherColumns = List.of(
                    new ColInfo("ID", "INT"),
                    new ColInfo("Name", "CHAR(25)"),
                    new ColInfo("matier", "CHAR(25)")
            );
            Relation teacherTable = new Relation("Teachers", teacherColumns, disk, bm);
            dbManager.AddTableToCurrentDatabase(teacherTable);

            List<ColInfo> studentECOlColumns = List.of(
                    new ColInfo("ID", "INT"),
                    new ColInfo("Name", "CHAR(25)"),
                    new ColInfo("Age", "INT"),
                    new ColInfo("Formation", "CHAR(25)")
            );
            Relation studentEcolTable = new Relation("StudentsEcole", studentECOlColumns, disk, bm);
            dbManager.SetCurrentDatabase(BDD2);
            dbManager.AddTableToCurrentDatabase(studentEcolTable);

            System.out.println(dbManager.GetTableFromCurrentDatabase("Students"));

            dbManager.SetCurrentDatabase(BDD1);
            System.out.println(dbManager.GetTableFromCurrentDatabase("Students"));

            dbManager.RemoveTableFromCurrentDatabase("lolo");
            //dbManager.RemoveTableFromCurrentDatabase("Students");

            dbManager.RemoveDatabase("titi");
            //dbManager.RemoveDatabase(BDD1);

            //dbManager.RemoveTablesFromCurrentDatabase();
            System.out.println(dbManager.GetTableFromCurrentDatabase("Students"));

            dbManager.ListDatabases();

            dbManager.ListTablesInCurrentDatabase();

            dbManager.SaveState();
            dbManager.RemoveDatabases();
            System.out.println("  ***** Test si le sauvgarde est fait ********");
            dbManager.ListDatabases();
            dbManager.SetCurrentDatabase("Universites");
            dbManager.ListTablesInCurrentDatabase();



            System.out.println("\n*** Test : Chargement des bases de données ***");

                dbManager.LoadState();

                dbManager.ListDatabases();
                dbManager.SetCurrentDatabase(BDD1);
                dbManager.ListTablesInCurrentDatabase();



            // *** Test terminé ***
            System.out.println("\nTest terminé avec succès !");
        } catch (Exception e) {
            e.getMessage();
        }
    }
}