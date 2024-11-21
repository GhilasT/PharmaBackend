
import java.io.File;
import java.io.*;

public class BufferManagerTest {
    public static void main(String[] args) throws Exception {
    	File configFile = new File("config.json"); // Un fichier JSON avec des paramètres valides

        // Charger la configuration
        DBConfig config = DBConfig.LoadDBConfig(configFile);
        DiskManager dm = new DiskManager(config);
        BufferManager bm = new BufferManager(config, dm);

        PageID page1 = dm.AllocPage();
        PageID page2 = dm.AllocPage();
        PageID page3 = dm.AllocPage();
        PageID page4 = dm.AllocPage();
        System.out.println(page4.getFileIdx()+""+ page4.getPageIdx());

        // Test avec LRU (par défaut)
        System.out.println("Politique actuelle : LRU");
        try {
        bm.GetPage(page1);
        //bm.GetPage(page2);
        //bm.GetPage(page3);
       
        bm.FreePage(page1, true);

        //bm.GetPage(page4); // Devrait remplacer la page 1 (la moins récemment utilisée)
        System.out.println("Test LRU terminé.");

        // Changer pour MRU
        bm.SetCurrentReplacementPolicy("MRU");
        System.out.println("Politique actuelle : MRU");

        bm.GetPage(page2); // Accès à la page 2 pour qu'elle soit la plus récemment utilisée
        bm.GetPage(page4); // Accès à la page 4
        bm.GetPage(page1); // Charger à nouveau la page 1

        bm.FreePage(page3, true); // Libération pour qu'elle puisse être remplacée

        bm.GetPage(page4); // Devrait remplacer la page 3 (la plus récemment utilisée)
        System.out.println("Test MRU terminé.");
        
        }catch(Exception e) {
        	System.out.println(e.getMessage());
        }
        
    }
}

/*40
Politique actuelle : LRU
F1.rsdb (Aucun fichier ou dossier de ce nom)
*/


