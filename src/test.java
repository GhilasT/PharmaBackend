import java.io.File;
import java.io.IOException;

public class test {
        public static void main(String[] args) {
            try {
                // Charger le fichier JSON
                File fichier_config = new File("fichier.json");
                DBConfig config = DBConfig.LoadDBConfig(fichier_config);
    
                // Afficher les paramètres chargés
                System.out.println("DB Path: " + config.getDbpath());
                System.out.println("Page Size: " + config.getPagesize());
                System.out.println("Max File Size: " + config.getDM_maxfilesize());
    
            } catch (IOException e) {
                System.out.println("erreur de test");
                e.printStackTrace();
            }
        }
    }