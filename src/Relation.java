import java.nio.Buffer;

public class Relation {
	private String nom;
	private int nbColonnes;
	private String nomColonne;
	private String typeColonne;
	
	
	
	
	public int writeRecordToBuffer(Record record, Buffer buff, int pos) {
		int totalBytesWritten = 0;

        for (int i = 0; i < record.getSize(); i++) {
            
            String columnType = typeColonne;
            Object value = record.getValue(i);

            // Déplace le curseur à la position spécifiée
            buff.position(pos + totalBytesWritten);

            switch (columnType) {
                case "INT":
                    // Convertir en int et écrire dans le buffer
                    int intValue = Integer.parseInt(value.toString());
                    buff.putInt(intValue);
                    totalBytesWritten += Integer.BYTES;
                    break;

                case "FLOAT":
                    // Convertir en float et écrire dans le buffer
                    float floatValue = Float.parseFloat(value.toString());
                    buff.putFloat(floatValue);
                    totalBytesWritten += Float.BYTES;
                    break;

                case "STRING":
                    // Écrire la chaîne de caractères
                    String stringValue = value.toString();
                    byte[] stringBytes = stringValue.getBytes();
                    buff.put(stringBytes);
                    totalBytesWritten += stringBytes.length;
                    break;

                default:
                    throw new IllegalArgumentException("Type de colonne non supporté: " + columnType);
            }
        }

        return totalBytesWritten;
    }
		
	}
	
	public int readFromBuffer(Record record, Buffer buff, int pos) {
		
	}
}
