import java.io.File;

public class DBConfigTest {
    public static void main(String[] args) throws Exception {
        File configFile = new File("config.json"); // Un fichier JSON avec des paramètres valides

        // Charger la configuration
        DBConfig config = DBConfig.LoadDBConfig(configFile);

        // Tester les champs chargés
        System.out.println("DBPath: " + config.getDbpath());
        System.out.println("PageSize: " + config.getPagesize());
        System.out.println("MaxFileSize: " + config.getDM_maxfilesize());
        System.out.println("BufferCount: " + config.getBm_buffercount());
        System.out.println("Policy: " + config.getBm_policy());
    }
}
