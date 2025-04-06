import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONException;
import org.json.JSONObject;


public class DBConfig {
    private String dbpath;
    private int pagesize;
    private int dm_maxfilesize;
    private int bm_buffercount;
    private String bm_policy;



    public DBConfig(String dbpath, int pagesize, int dm_maxfilesize, int bm_buffercount, String bm_policy) {
        if (pagesize <= 0 || dm_maxfilesize <= 0 || bm_buffercount <= 0) {
            throw new IllegalArgumentException("Les tailles et compteurs doivent être positifs.");
        }
        if (!bm_policy.equals("LRU") && !bm_policy.equals("MRU")) {
            throw new IllegalArgumentException("Politique de remplacement inconnue.");
        }
        this.dbpath = dbpath;
        this.pagesize = pagesize;
        this.dm_maxfilesize = dm_maxfilesize;
        this.bm_buffercount = bm_buffercount;
        this.bm_policy = bm_policy;
    }

    public String getDbpath() { return dbpath; }
    public int getPagesize() { return pagesize; }
    public int getDM_maxfilesize() { return dm_maxfilesize; }
    public int getBm_buffercount() { return bm_buffercount; }
    public String getBm_policy() { return bm_policy; }

    public static DBConfig LoadDBConfig(File fichier_config) throws IOException {
        try {
            String jsonString = new String(Files.readAllBytes(Paths.get(fichier_config.getPath())));
            JSONObject jsonObject = new JSONObject(jsonString);

            String dbpath = jsonObject.getString("dbpath");
            int pagesize = jsonObject.getInt("pagesize");
            int dm_maxfilesize = jsonObject.getInt("dm_maxfilesize");
            int bm_buffercount = jsonObject.getInt("bm_buffercount");
            String bm_policy = jsonObject.getString("bm_policy");

            return new DBConfig(dbpath, pagesize, dm_maxfilesize, bm_buffercount, bm_policy);
        } catch (IOException e) {
            throw new IOException("Erreur lors de la lecture du fichier de configuration : " + e.getMessage());
        } catch (JSONException e) {
            throw new IllegalArgumentException("Format JSON invalide : " + e.getMessage());
        }
    }

    public int getDm_maxfilesize() {
        return dm_maxfilesize;
    }
}