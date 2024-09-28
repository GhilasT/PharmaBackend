import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DBConfig {
    String dbpath;
    int pagesize;
    int dm_maxfilesize;
    
    public DBConfig(String dbp,int ps,int dmax) {
        this.dbpath = dbp;
        this.pagesize=ps;
        this.dm_maxfilesize=dmax;
    }

    
    public static DBConfig LoadDBConfig(File fichier_config) throws FileNotFoundException {
        DBConfig dbConfig = null;  
        Scanner myReader = new Scanner(fichier_config);
        String data;
        int ps;
        int dmax;

       
        if (myReader.hasNextLine()) {
            data = myReader.nextLine();
			
        
        	if(myReader.hasNextInt()) {
        		ps=myReader.nextInt();
        		
        		if(myReader.hasNextInt()) {
        			dmax=myReader.nextInt();
        			dbConfig=new DBConfig(data,ps,dmax);
        			
        		}
        		
        	}
        	
        }

        myReader.close();
        return dbConfig;  
    }

    test

    
}
