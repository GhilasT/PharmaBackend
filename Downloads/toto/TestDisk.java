import java.io.File;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class TestDisk {

    private static DBConfig config;
    private static DiskManager diskManager;

    private static void verifyAndCreateDbPath(String dbpath) {
        File directory = new File(dbpath);
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                System.out.println("Répertoire créé : " + dbpath);
            } else {
                System.out.println("Erreur : Impossible de créer le répertoire : " + dbpath);
                throw new RuntimeException("Impossible de créer le répertoire.");
            }
        } else {
            System.out.println("Répertoire existant : " + dbpath);
        }
    }

    public static void main(String[] args) throws Exception {
        // Configuration de test
        String dbpath = "Test"; // Nom du dossier
        verifyAndCreateDbPath(dbpath);


        config = new DBConfig("/Users/imenebakhouche/Downloads/Projet_Correct/Test", 4, 1024, 5, "LRU"); // Page de 4 octets, fichier max 1024 octets

        config = new DBConfig("/Users/imenebakhouche/Downloads/Projet_Correct/Test", 512, 2048, 2, "LRU"); // Page de 4 octets, fichier max 1024 octets

        diskManager = new DiskManager(config);

        // Nettoyer les fichiers de test
        //cleanupTestFiles();

        // Lancer les tests
        TestEcritureLecturePage(dbpath);

        //cleanupTestFiles();
        //TestDeallocPage();
        //TestSaveLoadState();



    }

    public static void TestEcritureLecturePage(String pth) throws Exception {
        System.out.println("== TestEcritureLecturePage ==");

        // Allocation de trois pages
        PageID pageId1 = diskManager.AllocPage();
        PageID pageId2 = diskManager.AllocPage();
        PageID pageId3 = diskManager.AllocPage();

        if (pageId1 != null && pageId2 != null && pageId3 != null) {
            // Afficher les pages allouées
            System.out.println("Page 1 : FileIdx = " + pageId1.getFileIdx() + ", PageIdx = " + pageId1.getPageIdx());
            System.out.println("Page 2 : FileIdx = " + pageId2.getFileIdx() + ", PageIdx = " + pageId2.getPageIdx());
            System.out.println("Page 3 : FileIdx = " + pageId3.getFileIdx() + ", PageIdx = " + pageId3.getPageIdx());
        } else {
            System.out.println("Erreur : Les pages n'ont pas été correctement allouées.");
            return;
        }

        // Vérifier si le fichier F0.rsdb a été créé
        File file = new File(pth + "/F0.rsdb.txt");
        if (file.exists()) {
            System.out.println("Fichier créé : " + file.getPath());
        } else {
            System.out.println("Erreur : Le fichier n'a pas été créé.");
            return;
        }

        ByteBuffer buffer = ByteBuffer.allocate(config.getPagesize());
        for (int i = 0; i < config.getPagesize(); i++) {
            buffer.put((byte) (i + 1)); // Remplit le buffer avec des données incrémentales
        }
        buffer.flip(); // Préparer le buffer pour l'écriture

        diskManager.WritePage(pageId1, buffer);
        buffer.rewind(); // Réinitialiser le buffer pour réutilisation
        diskManager.WritePage(pageId2, buffer);
        buffer.rewind();
        diskManager.WritePage(pageId3, buffer);
        // Lecture et vérification des données
        try {
            verifyPageData(diskManager, pageId1, config, 1); // Offset 1
            verifyPageData(diskManager, pageId2, config, 2); // Offset 2
            verifyPageData(diskManager, pageId3, config, 3); // Offset 3

        } catch (Exception e) {
            e.getMessage();
        }
        //test d'ecritue
        String testData = "Hello, DiskManager!";
        ByteBuffer buffertest = ByteBuffer.allocate(config.getPagesize());
        System.out.println(config.getPagesize());
        try{

            buffertest.put(testData.getBytes());
        } catch (Exception e) {
            System.out.println("erreur put ");
        }

        buffertest.flip();


        try {
            diskManager.WritePage(pageId1, buffertest);
            buffertest.rewind();
            diskManager.WritePage(pageId2, buffertest);
            buffertest.rewind();
            diskManager.WritePage(pageId3, buffertest);
        }catch (Exception e) {
            System.out.println("erreur ecriture ");
        }


        try {
            verifyPageData(diskManager, pageId1, config, 1);
            verifyPageData(diskManager, pageId2, config, 2);
            verifyPageData(diskManager, pageId3, config, 3);

        } catch (Exception e) {
            System.out.println("Erreur lors de la vérification des données : " + e.getMessage());
            return;
        }

        System.out.println("TestEcritureLecturePage terminé avec succès.");

        // desalouer ou vider les page
        System.out.println("== TestSaveLoadState ==");
        diskManager.DeallocPage(pageId1);
        diskManager.DeallocPage(pageId2);

        //diskManager.DeallocPage(pageId3);

        diskManager.DeallocPage(pageId3);

        // Charger l'état
        diskManager.SaveState();
        diskManager.LoadState();
        System.out.println("État chargé.");
    }

    private static void verifyPageData(DiskManager diskManager, PageID pageId, DBConfig config, int offset) throws Exception {
        ByteBuffer readBuffer = ByteBuffer.allocate(config.getPagesize());

        System.out.println("=== ReadPage ===");
        try {

            diskManager.ReadPage(pageId, readBuffer); // Le buffer est rempli par ReadPage
            readBuffer.flip(); // Préparer le buffer pour la lecture

            diskManager.ReadPage(pageId, readBuffer);
            readBuffer.flip();
        } catch (Exception e) {
            System.out.println("Erreur lors de la lecture de la page : " + e.getMessage());
            throw e;
        }

        System.out.println("Données lues pour PageID (" + pageId.getFileIdx() + ", " + pageId.getPageIdx() + ") : " + Arrays.toString(readBuffer.array()));


        // Vérification de la taille restante
        if (readBuffer.remaining() != config.getPagesize()) {
            System.out.println("Erreur : Taille des données lues incorrecte. Attendu : " + config.getPagesize() + ", Trouvé : " + readBuffer.remaining());
            return;
        }


        // Vérification des données
        int index = 0;
        while (readBuffer.hasRemaining()) {
            byte expected = (byte) (index + offset);
            byte actual = readBuffer.get();
            if (actual != expected) {
                System.out.println("Erreur : Données incorrectes dans la page. Attendu : " + expected + ", Trouvé : " + actual);
                return;
            }
            index++;
        }

        System.out.println("Données correctes pour PageID (" + pageId.getFileIdx() + ", " + pageId.getPageIdx() + ")");
    }

}