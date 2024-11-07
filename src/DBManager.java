import java.util.*;
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
	public void RemoveDataBaseFromCurrentDataBase() {
		
	}
	public void RemoveDataBases() {
		
	}
	public void ListDataBases() {
		
	}
	public void ListTablesInCurrentDataBase() {
		
	}
	public void SaveState() {
		
	}
	public void LoadState() {
		
	}
		

}
