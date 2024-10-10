import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class DBConfig {
    private String dbpath;
    private int pagesize;
    private int dm_maxfilesize;
    //nombre de buffers
    private int bm_buffercount;
    //la politique de remplacement utilisée
    private int bm_policy;


    

    public DBConfig(String dbpath, int pagesize, int dm_maxfilesize, int bm_buffercount, int bm_policy) {
        this.dbpath = dbpath;
        this.pagesize = pagesize;
        this.dm_maxfilesize = dm_maxfilesize;
        this.bm_buffercount = bm_buffercount;
        this.bm_policy = bm_policy;
    }


    public String getDbpath() {
        return dbpath;
    }


    public int getPagesize() {
        return pagesize;
    }


    public int getDM_maxfilesize() {
        return dm_maxfilesize;
    }

    


    public int getBm_buffercount() {
        return bm_buffercount;
    }
    public int getBm_policy() {
        return bm_policy;
    }

 // 2 constructeur
    public static DBConfig LoadDBConfig(String fichier_config) throws IOException {
        // Lire le contenu du fichier JSON dans une chaîne de caractères
        String jsonString;
    	try {
        	jsonString = new String(Files.readAllBytes(Paths.get(fichier_config)));
        } catch (IOException e) {
        	e.printStackTrace();
        	return null; // Ou lancez une exception selon votre gestion des erreurs
        }
        JSONObject json = new JSONObject(sb.toString());


        String dbpa = json.optString("dbpath", "../DB");
        int pagesi = json.optInt("pagesize");
        int dm_max = json.optInt("dm_maxfilesize");
        int bm_buff = json.optInt("bm_buffercount");
        int bm_pol = json.optInt("bm_policy");
        reader.close();


        return new DBConfig(dbpa,pagesi,dm_max,bm_buff,bm_pol);
    }


}
