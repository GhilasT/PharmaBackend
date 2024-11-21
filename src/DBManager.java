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
	/*public Buffer GetPage(PageID pageId) throws Exception {
    if (buffListe.isEmpty()) {
        throw new IllegalStateException("Aucun buffer n'a été initialisé dans BufferManager.");
    }

    // Vérifier si la page est déjà en mémoire
    for (Buffer buffer : buffListe) {
        if (buffer.getPageId() != null && buffer.getPageId().equals(pageId)) {
            buffer.incrementPinCount(); // Marquer le buffer comme utilisé
            return buffer;
        }
    }

    // Trouver un buffer disponible pour le remplacement
    Buffer bufferToReplace = findBufferToReplace();
    if (bufferToReplace == null) {
        throw new IOException("Aucun buffer libre disponible !");
    }

    // Si le buffer contient une page modifiée, l'écrire sur le disque avant le remplacement
    if (bufferToReplace.isDirty()) {
        disk.WritePage(bufferToReplace.getPageId(), ByteBuffer.wrap(bufferToReplace.getPageData()));
    }

    // Charger la nouvelle page depuis le disque dans un ByteBuffer temporaire
    ByteBuffer tempBuffer = ByteBuffer.allocate(config.getPagesize());
    disk.ReadPage(pageId, tempBuffer);

    // Mettre à jour le buffer avec la nouvelle page
    bufferToReplace.setPageData(tempBuffer.array());
    bufferToReplace.setPageId(pageId);
    bufferToReplace.setDirty(false);
    bufferToReplace.incrementPinCount();

    return bufferToReplace;
}*/
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
