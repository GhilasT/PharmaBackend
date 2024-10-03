import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DBConfig {
	String dbpath;
	int pagesize;
	int dm_maxfilesize;
	int bm_buffercount;
	int bm_policy;
    
    public DBConfig(String dbp,int ps,int dmax) {
        this.dbpath = dbp;
        this.pagesize=ps;
        this.dm_maxfilesize=dmax;
    }

    
    public static DBConfig LoadDBConfig(File fichier_config) throws FileNotFoundException {
        Gson gson = new Gson();																		// crée une instance de gson
		FileReader reader = new FileReader(fichier_config);											// Ouvre le fichier JSON
		DBConfig fichier = gson.fromJson(reader,DBConfig.class);							// gson.fromJson vas désérialiser le fichier , le traduire du langage JSON pour le java
		
		return fichier;	
    }



    
}
