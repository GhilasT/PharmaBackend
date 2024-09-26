import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DBConfig {
    String dbpath;

    
    public DBConfig(String dbp) {
        this.dbpath = dbp;
    }

    
    public static DBConfig LoadDBConfig(File fichier_config) throws FileNotFoundException {
        DBConfig dbConfig = null;  
        Scanner myReader = new Scanner(fichier_config);

       
        if (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            dbConfig = new DBConfig(data); 
			System.out.println(data); 
        }

        myReader.close();
        return dbConfig;  
    }

    

    
}
