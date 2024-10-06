import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;


public class DBConfig {
    String dbpath;
    int pagesize;
    int dm_maxfilesize;
    
    public String getDbpath() {
        return dbpath;
    }


    public int getPagesize() {
        return pagesize;
    }


    public int getDM_maxfilesize() {
        return dm_maxfilesize;
    }


    public DBConfig(String dbp,int ps,int dmax) {
        this.dbpath = dbp;
        this.pagesize=ps;
        this.dm_maxfilesize=dmax;
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
