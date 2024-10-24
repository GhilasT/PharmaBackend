import java.nio.ByteBuffer;
import java.util.ArrayList;

public class Relation {
	private String nom;
	private int nbColonnes;
	
	private ArrayList<ColInfo> colonnes;
	
	
	
	
	public Relation(String nom, int nbColonnes) {
		this.nom = nom;
		this.nbColonnes = nbColonnes;
		this.colonnes = new ArrayList<>(this.nbColonnes);
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
	
	        // Assurez-vous que la liste de valeurs du record est vide
	        record.getValues().clear();
	
	        for (int i = 0; i < numberOfColumns; i++) {
	            
	            String columnType = typeColonne;
	
	            // Déplace le curseur à la position spécifiée
	            buff.position(pos + totalBytesRead);
	
	            switch (columnType) {
	                case "INT":
	                    // Lire un int depuis le buffer
	                    int intValue = buff.getInt();
	                    record.getValues().add(intValue);
	                    totalBytesRead += Integer.BYTES;
	                    break;
	
	                case "FLOAT":
	                    // Lire un float depuis le buffer
	                    float floatValue = buff.getFloat();
	                    record.getValues().add(floatValue);
	                    totalBytesRead += Float.BYTES;
	                    break;
	
	                case "STRING":
	                    // Lire une chaîne de caractères
	                    // Supposons que la longueur de la chaîne soit connue ou précédemment stockée
	                    // Ici, nous allons lire jusqu'à un certain nombre de caractères
	                    byte[] stringBytes = new byte[100]; // Par exemple, taille maximale de 100 caractères
	                    buff.get(stringBytes);
	                    String stringValue = new String(stringBytes).trim(); // Trimer pour enlever les espaces
	                    record.getValues().add(stringValue);
	                    totalBytesRead += stringBytes.length; // Incrémente par la taille lue
	                    break;
	
	                default:
	                    throw new IllegalArgumentException("Type de colonne non supporté: " + columnType);
	            }
	        }
	
	        return totalBytesRead;
	}
		
	
}
