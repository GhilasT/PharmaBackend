import java.util.ArrayList;

public class Record {
    // Liste des valeurs du tuple
    private ArrayList<String> listeInfo;

    

    public Record(){
        this.listeInfo = new ArrayList<>();
    }

    public int getSize() {
        return listeInfo.size();
    }

    public String getValue(int index) {
        return listeInfo.get(index);
    }

    public void addValue(int index, String element){
        this.listeInfo.add(index, element);
    }
    public static Record fromBytes(byte[] recordBytes) {
        Record record = new Record();
        ByteBuffer buffer = ByteBuffer.wrap(recordBytes);

        // Lire les valeurs à partir du tampon et les ajouter au Record
        while (buffer.hasRemaining()) {
            // Supposons que les valeurs dans l'enregistrement sont des chaînes de caractères
            // Détecter la longueur du string
            int stringLength = buffer.getInt(); // La première partie de chaque donnée est la longueur du string

            byte[] stringBytes = new byte[stringLength];
            buffer.get(stringBytes); // Lire les octets du string
            String value = new String(stringBytes);

            // Ajouter la valeur au Record
            record.addValue(record.getSize(), value);
        }

        return record;
    }


}
