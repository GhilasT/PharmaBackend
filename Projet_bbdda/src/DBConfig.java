import java.io.*;
import com.google.gson.*;

public class DBConfig {
	private String dbpath;
	private int pagesize;
	private int dm_maxfilesize;
	
	
	
	public DBConfig(String dbpath, int pagesize, int dm_maxfilesize) {
		this.dbpath = dbpath;
		this.pagesize = pagesize;
		this.dm_maxfilesize = dm_maxfilesize;
	}
	
	public String getDBpath() {
		return dbpath;
	}
	
	public int getPagesize() {
		return pagesize;
	}

	public int getDM_maxfilesize() {
		return dm_maxfilesize;
	}

	
	public static DBConfig LoadDBConfig(String fichier_config) throws IOException {		
		Gson gson = new Gson();																		// crée une instance de gson
		FileReader reader = new FileReader(fichier_config);											// Ouvre le fichier JSON
		DBConfig fichier = gson.fromJson(reader,DBConfig.class);							// gson.fromJson vas désérialiser le fichier , le traduire du langage JSON pour le java
		
		return fichier;																		// retourne le fichier chargé
	}
}