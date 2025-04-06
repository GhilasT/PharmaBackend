import java.nio.ByteBuffer;
import java.util.List;

public class TestRelation {
    public static void main(String[] args) {
        try {
            // *** CONFIGURATION DE LA BASE DE DONNÉES ***
            DBConfig config = new DBConfig(
                    "/Users/imenebakhouche/Downloads/toto/Test",
                    2048, // Taille des pages
                    4220, // Taille maximale des fichiers
                    3,    // Nombre de buffers
                    "LRU" // Politique de gestion des buffers
            );
            DiskManager disk = new DiskManager(config);
            BufferManager bm = new BufferManager(config, disk);

            // *** DÉFINITION DU SCHÉMA DE LA RELATION ***
            List<ColInfo> columns = List.of(
                    new ColInfo("ID", "INT"),
                    new ColInfo("Name", "CHAR(20)"),
                    new ColInfo("Age", "CHAR(20)")
            );

            Relation relation = new Relation("per", columns, disk, bm);

            // *** TEST 1 : AJOUT DE PAGES DE DONNÉES ***
            System.out.println("Ajout de nouvelles pages de données...");
            relation.addDataPage();
            relation.addDataPage();

            PageID headerPageId = relation.getHeaderPageId();
            Buffer headerBuffer = bm.GetPage(headerPageId);
            ByteBuffer pageData = headerBuffer.getPageData();
            System.out.println("nbr page : "+ pageData.getInt(0));
            System.out.println("Pages ajoutées avec succès !");

            // *** TEST 2 : CRÉATION DES RECORDS ***
            System.out.println("\n*** Création des records ***");
            Record record1 = new Record();
            record1.addValue(new RecordInfo("INT", 112));
            record1.addValue(new RecordInfo("VARCHAR(20)", "toto"));
            record1.addValue(new RecordInfo("VARCHAR(20)", "recordtoto"));

            Record record2 = new Record();
            record2.addValue(new RecordInfo("INT", 2));
            record2.addValue(new RecordInfo("VARCHAR(20)", "alice"));
            record2.addValue(new RecordInfo("VARCHAR(20)", "recordalice"));

            Record record3 = new Record();
            record3.addValue(new RecordInfo("INT", 3));
            record3.addValue(new RecordInfo("VARCHAR(20)", "bob"));
            record3.addValue(new RecordInfo("VARCHAR(20)", "recordbob"));

            System.out.println("Records créés avec succès.");

            // *** TEST 3 : INSERTION DES RECORDS DANS LES PAGES ***
            System.out.println("\n*** Insertion des records avec InsertRecord ***");
            try {
                relation.InsertRecord(record1);
                relation.InsertRecord(record2);
                relation.InsertRecord(record3);
                System.out.println("Records insérés avec succès !");
            } catch (Exception e) {
                System.err.println("Erreur lors de l'insertion des records : " + e.getMessage());
            }

            // *** TEST 4 : LECTURE DES RECORDS ET VÉRIFICATION ***
            System.out.println("\n*** Lecture des records insérés ***");
            try {
                List<Record> allRecords = relation.GetAllRecords();
                System.out.println("Records lus :");
                for (Record r : allRecords) {
                    System.out.println(r.toString());
                }
            } catch (Exception e) {
                System.err.println("Erreur lors de la lecture des records : " + e.getMessage());
            }

            System.out.println("\nTest terminé avec succès !");
        } catch (Exception e) {
            System.err.println("Erreur critique dans le test : " + e.getMessage());
            e.printStackTrace();
        }
    }
}