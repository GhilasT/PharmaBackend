import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TestReadWriteBuffer {
    public static void main(String[] args) {
        try {
            // Schéma de la relation

            DBConfig config = new DBConfig(
                    "/Users/imenebakhouche/Downloads/Projet_Correct/Test",
                    2058, // Taille des pages
                    5000, // Taille maximale des fichiers
                    3, // nbr de buffers
                    "LRU" // Politique de gestion des buffers
            );
            DiskManager disk = new DiskManager(config);
            BufferManager bm = new BufferManager(config, disk);
            List<ColInfo> columns = new ArrayList<>();
            columns.add(new ColInfo("age", "INT"));
            columns.add(new ColInfo("nom", "CHAR(20)"));
            columns.add(new ColInfo("taille", "REAL"));

            // Création d'une relation
            Relation relation = new Relation("Personne", columns, disk, bm);

            // Record d'exemple
            Record record = new Record();
            record.addValue(new RecordInfo("INT", 25));
            record.addValue(new RecordInfo("CHAR(20)", "to"));
            record.addValue(new RecordInfo("REAL", 5.6f));

            Record record2 = new Record();
            record2.addValue(new RecordInfo("INT", 20));
            record2.addValue(new RecordInfo("CHAR(20)", "toto"));
            record2.addValue(new RecordInfo("REAL", 5.6f));

            ByteBuffer buffer = ByteBuffer.allocate(4064);
            int pos = 0;

            // Écriture du record dans le buffer
            int tailleEcrite = relation.writeRecordToBuffer(record, buffer, pos);
            int tailleEcrite2 = relation.writeRecordToBuffer(record2, buffer, tailleEcrite);
            System.out.println("Taille écrite dans le buffer : " + tailleEcrite);

            System.out.println("la taille du records est : "+ (record.getSizeInBytes() + record2.getSizeInBytes()));

            // Lecture du record depuis le buffer
            Record recordLu = new Record();
            int tailleLue = relation.readFromBuffer(recordLu, buffer, 0);
            System.out.println("Taille lue dans le buffer1 : " + tailleLue);

            Record recordLu2 = new Record();
            int tailleLue2 = relation.readFromBuffer(recordLu2, buffer, tailleLue);
            System.out.println("Taille lue dans le buffer2 : " + tailleLue);


            System.out.println("Taille lue : " + tailleLue);
            System.out.println("Record 1 lu : " + recordLu);
            System.out.println("Record 2 lu : " + recordLu2);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}