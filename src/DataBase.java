import java.util.*;
public class DataBase {
	private String nom;
	private Map<String,Relation>tables ;
	public DataBase(String n) {
		this.nom=nom;
		this.tables= new HashMap<String,Relation>();
		
	}
	public void addDataBase(Relation r) {
		tables.put(r.getNom(), r);
	}
	public Relation getDatabase(String t){
		return tables.get(t);
	}
	public void removeTable(String g) {
		tables.remove(g);
	}
	
	public void printDataBase() {
		if(tables.isEmpty()) {
			System.out.println("la table:"+nom+" est vide");
		}
		else {
			System.out.println("la table :"+nom+" contient");
			for(Map.Entry<String,Relation> entry:tables.entrySet()) {
				System.out.println("Nomtable:"+entry.getKey()+"		"+entry.getValue())	;
				
			}
			
		}
		
	}
	
	public void removeAll() {
		tables.clear();
	}
	public String getNom() {
		return nom;
	}
	
}
