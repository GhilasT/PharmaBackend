import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class BufferManagerTest {

    public static void main(String[] args) throws Exception {
        // Configuration initiale
        String dbPath = "Test";
        DBConfig config = new DBConfig(dbPath, 512, 2048, 3, "LRU");
        DiskManager disk = new DiskManager(config);
        BufferManager bufferManager = new BufferManager(config, disk);

        // Créer le répertoire de base si nécessaire
        File testDirectory = new File(dbPath);
        if (!testDirectory.exists() && !testDirectory.mkdirs()) {
            throw new IOException("Impossible de créer le répertoire 'Test'.");
        }

        // Étape 1 : Allocation de pages
        PageID page1 = disk.AllocPage();
        PageID page2 = disk.AllocPage();
        PageID page3 = disk.AllocPage();
        PageID page4 = disk.AllocPage(); // Provoquera un remplacement plus tard

        bufferManager.displayBufferState();
        System.out.println("=========================");

        // Étape 2 : Charger des pages dans les buffers
        System.out.println("Chargement des pages...");
        bufferManager.GetPage(page1);
        bufferManager.displayBufferState();
        System.out.println("1");
        bufferManager.GetPage(page1);
        System.out.println("++++++++");

        bufferManager.GetPage(page2);
        System.out.println("2");
        bufferManager.displayBufferState();


        bufferManager.GetPage(page3);
        bufferManager.displayBufferState();
        System.out.println("3");
        bufferManager.FreePage(page2, true);
        bufferManager.FreePage(page1, true);
        bufferManager.FreePage(page3, true);

        // Étape 3 : Provoquer un remplacement
        System.out.println("\nChargement de la page 4 (remplacement attendu)...");
        bufferManager.GetPage(page4);
        bufferManager.displayBufferState();

        // Étape 4 : Libérer une page
        System.out.println("\nLibération de la page 2...");
        bufferManager.FreePage(page2, true);
        bufferManager.displayBufferState();

        // Étape 5 : Charger une page existante (page 1)
        System.out.println("\nRecharge de la page 1...");
        bufferManager.GetPage(page1);
        bufferManager.displayBufferState();

        // Étape 6 : Changer la politique de remplacement
        System.out.println("\nChangement de politique : MRU");
        bufferManager.SetCurrentReplacementPolicy("MRU");

        // Étape 7 : Charger une nouvelle page pour tester MRU
        System.out.println("\nChargement de la page 3 (MRU)...");
        bufferManager.GetPage(page3);
        bufferManager.displayBufferState();

        // Étape 8 : Flusher les buffers
        System.out.println("\nFlush des buffers...");
        bufferManager.FlushBuffers();
        bufferManager.displayBufferState();

        System.out.println("\nTest terminé.");
    }
}