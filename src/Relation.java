import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Relation {
	private String nom;
	private int nbColonnes;
	
	private ArrayList<ColInfo> colonnes;
	private PageID headerPageId ;
	private DiskManager diskManager;
	private BufferManager bufferManager;
	
	
	
	
	public Relation(String nom, int nbColonnes,PageID pageId,DiskManager dm,BufferManager bm) {
		this.nom = nom;
		this.nbColonnes = nbColonnes;
		this.colonnes = new ArrayList<>(this.nbColonnes);
		this.headerPageId=pageId;
		this.diskManager=dm;
		this.bufferManager=bm;
	}

	public void initialiserListeColonne(ColInfo c){
		this.colonnes.add(c);
	}



	public int writeRecordToBuffer(Record record, ByteBuffer buff, int pos) {
		int totalBytesWritten = 0;
			
	        for (ColInfo colonne : colonnes ) {
				int index =0;
				
	            
	            String columnType = colonne.getTypeColonne() ;
	        	String nom = colonne.getNomColonne();
	
	            // Déplace le curseur à la position spécifiée
	            buff.position(pos + totalBytesWritten);
	
	            switch (columnType) {
	                case "INT":
	                    // Convertir en int et écrire dans le buffer
	                    int intValue = Integer.parseInt(record.getValue(index));
	                    buff.putInt(intValue);
	                    totalBytesWritten += Integer.BYTES;
	                    break;
	
	                case "FLOAT":
	                    // Convertir en float et écrire dans le buffer
	                    float floatValue = Float.parseFloat(record.getValue(index));
	                    buff.putFloat(floatValue);
	                    totalBytesWritten += Float.BYTES;
	                    break;
	
	                case "STRING":
	                    // Écrire la chaîne de caractères
	                    String stringValue = record.getValue(index);
	                    byte[] stringBytes = stringValue.getBytes();
	                    buff.put(stringBytes);
	                    totalBytesWritten += stringBytes.length;
	                    break;
	
	                default:
	                    throw new IllegalArgumentException("Type de colonne non supporté: " + columnType);
	            }
				index++;
	        }
	
	        return totalBytesWritten;
	}
		
	
	
	public int readFromBuffer(Record record, ByteBuffer buff, int pos) {
		int totalBytesRead = 0;
	        int numberOfColumns = nbColonnes;
	
	        for (ColInfo colonne : colonnes) {
				int index =0;
	            
	            String columnType = colonne.getTypeColonne() ;;
	
	            // Déplace le curseur à la position spécifiée
	            buff.position(pos + totalBytesRead);
	
	            switch (columnType) {
	                case "INT":
	                    // Lire un int depuis le buffer
	                    int intValue = buff.getInt();
						String xText = Integer.toString(intValue);
	                    record.addValue(index, xText);
	                    totalBytesRead += Integer.BYTES;
	                    break;
	
	                case "FLOAT":
	                    // Lire un float depuis le buffer
	                    float floatValue = buff.getFloat();
						String leFloat = Float.toString(floatValue);
	                    record.addValue(index, leFloat);
	                    totalBytesRead += Float.BYTES;
	                    break;
	
	                case "STRING":
	                    
	                    byte[] stringBytes = new byte[100]; // Par exemple, taille maximale de 100 caractères
	                    buff.get(stringBytes);
	                    String stringValue = new String(stringBytes).trim(); // Trimer pour enlever les espaces
	                    record.addValue(index, stringValue);
	                    totalBytesRead += stringBytes.length; // Incrémente par la taille lue
	                    break;
	
	                default:
	                    throw new IllegalArgumentException("Type de colonne non supporté: " + columnType);
	            }
				index++;
	        }
	
	        return totalBytesRead;
	}
		
	public void addDataPage() {
		
	}
	
	public PageID getFreeDataPageId(int sizeRecord) {
		
	}
	
	public RecordId writeRecordToDataPage (Record record, PageID pageId) {
		
	}
	
	public ArrayList<PageID> getRecordsInDataPage(PageID PageId) {
		
	}
	 
	public ArrayList<PageID> getDataPages(){
		
	}
	
	public RecordId InsertRecord (Record record) {
		
	}
		
	public ArrayList<PageID> GetAllRecords(){
		
	}
}
