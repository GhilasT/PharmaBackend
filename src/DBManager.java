import java.util.*;
import java.io.*;
public class DBManager {
	private DBConfig dbc;
	private Map<String,DataBase> databases;
	private DataBase currentDataBase=null;
	
	public DBManager(DBConfig d) {
		this.dbc=d;
		this.databases= new HashMap<String,DataBase>();
	}
	
	public void CreateDatabase(String nombdd){
		if(!databases.containsKey(nombdd)) {
			DataBase db= new DataBase(nombdd);
			databases.put(nombdd, db);
			System.out.println("la base de données "+nombdd+" a été créer");
			
		}
		System.out.println("la base de données "+nombdd+" existe deja");
		
		
	}
	public  void SetCurrentDatabase(String nombdd) {
		DataBase db= databases.get(nombdd);
		if(db==null) {
			System.out.println("la base de données n'existe pas");
		}
		else {
			this.currentDataBase=db;
			System.out.println("la base de données est prete");
		}
		
	}
	public void AddTableToCurrentDatabase(Relation tab) {
		if(currentDataBase!=null) {
			currentDataBase.addDataBase(tab);
			System.out.println("la relation"+tab.getNom()+" a été ajouter a la base de données"+currentDataBase.getNom());
		}
		else {
			System.out.println("aucunde bdd active");
		}
		
	}
	
	public void getTableFromCurrentDataBase(String nomTable) {
		if(currentDataBase!=null) {
			currentDataBase.removeTable(nomTable);
			System.out.println(nomTable+" supprimer");
	
		}
		else {
			System.out.println("pas de table activer");
		}
		
	}
	public void RemoveDataBase(String nombdd) {
		if(databases.containsKey(nombdd)) {
			databases.remove(nombdd);
			System.out.println("la base de données "+nombdd+"a été supprimer");
			if(currentDataBase!=null &&currentDataBase.getNom().equals(nombdd)) {
				currentDataBase=null;
				
			}
		else {
			System.out.println("la base de données "+nombdd+"n'existe pas");
		}
			
			
		}
		databases.get(nombdd).removeAll();
		databases.remove(nombdd);
		System.out.println("la base de données "+nombdd+"a été supprimer");
	}
	public void RemoveTablesFromCurrentDataBase(String nomTable) {
		if (currentDataBase != null) {
			currentDataBase.removeTable(nomTable);
		}
		else {
			System.out.println("la base de données actuelle n'existe pas");
		}
	}
	public void RemoveDataBases() {
		if(currentDataBase != null) {
			this.currentDataBase.removeAll();
		}
		else {
			System.out.println("la base de données actuelle n'existe pas");
		}
	}
	public void ListDataBases(){
		for (String nomDB : databases.keySet()) {
			System.out.println(nomDB + "\n");
		}
	}
	public void ListTablesInCurrentDataBase() {
		if(currentDataBase != null) {
			for (String nomTable : currentDataBase.getTables().keySet()) {
				System.out.println(nomTable + "\n");
			}
		}
		else {
			System.out.println("la base de données actuelle n'existe pas");
		}
	}
	public void SaveState() {
		try {
	        File dbDirectory = new File(dbc.getDbPath());
	        if (!dbDirectory.exists()) {
	            dbDirectory.mkdirs();  // Crée le dossier s'il n'existe pas
	        }

	        File saveFile = new File(dbDirectory, "databases.save");
	        try (BufferedWriter writer = new BufferedWriter(new FileWriter(saveFile))) {
	            for (String dbName : databases.keySet()) {
	                DataBase db = databases.get(dbName);
	                writer.write("Database: " + dbName + "\n");

	                // Sauvegarder les tables de la base de données
	                for (String tableName : db.getTables().keySet()) {
	                    Relation table = db.getTables().get(tableName);
	                    writer.write("  Table: " + tableName + ", HeaderPageId: " + table.getHeaderPageId() + "\n");
	                }
	            }
	            System.out.println("L'état des bases de données a été sauvegardé dans " + saveFile.getAbsolutePath());
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	        System.out.println("Erreur lors de la sauvegarde de l'état des bases de données.");
	    }
		
	}
	public void LoadState() {
			  try {
		        File saveFile = new File(dbc.getDbPath(), "databases.save");
		        if (!saveFile.exists()) {
		            System.out.println("Aucun fichier de sauvegarde trouvé.");
		            return;
		        }

		        try (BufferedReader reader = new BufferedReader(new FileReader(saveFile))) {
		            String line;
		            DataBase currentDb = null;
		            while ((line = reader.readLine()) != null) {
		                if (line.startsWith("Database: ")) {
		                    // Créer une nouvelle base de données
		                    String dbName = line.substring(10).trim();
		                    currentDb = new DataBase(dbName);
		                    databases.put(dbName, currentDb);
		                    System.out.println("Base de données chargée: " + dbName);
		                } else if (line.startsWith("  Table: ")) {
		                    // Charger une table
		                    String[] parts = line.substring(10).split(", ");
		                    String tableName = parts[0].substring(7).trim();
		                    int headerPageId = Integer.parseInt(parts[1].substring(14).trim());

		                    // Créer la table et l'ajouter à la base de données
		                    if (currentDb != null) {
		                        Relation table = new Relation(tableName, headerPageId);
		                        currentDb.addDataBase(table);
		                        System.out.println("Table chargée: " + tableName);
		                    }
		                }
		            }
		        }

		    } catch (IOException e) {
		        e.printStackTrace();
		        System.out.println("Erreur lors du chargement de l'état des bases de données.");
		    }
		}
		
	}
		
		
	}
		

}
