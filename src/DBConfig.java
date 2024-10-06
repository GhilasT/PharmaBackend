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
    public static DBConfig LoadDBConfig(File fichier_config) throws IOException {
        Gson gson = new Gson();
        FileReader reader = new FileReader(fichier_config);
        //lire son contenu du fichier json
        DBConfig dbConfig = gson.fromJson(reader, DBConfig.class);
        reader.close();

        return dbConfig;
    }



    
}
