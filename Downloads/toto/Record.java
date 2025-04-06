import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class Record {
    private List<RecordInfo> values;
    private RecordId recordId;

    public Record(List<RecordInfo> values) {
        this.values = new ArrayList<>(values);
    }

    public Record() {
        this.values = new ArrayList<>();
    }

    public List<RecordInfo> getValues() {
        return values;
    }

    public void setValues(List<RecordInfo> values) {
        this.values = values;
    }

    public void addValue(RecordInfo value) {
        this.values.add(value);
    }

    public boolean isTailleFixe() {
        // Si VARCHAR est traité comme CHAR, tous les enregistrements sont à taille fixe
        return true;
    }

    public Object getValueByColumnName(String columnName, List<ColInfo> columns) {
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getNomColonne().equals(columnName)) {
                return values.get(i).getValeur();
            }
        }
        throw new IllegalArgumentException("Colonne " + columnName + " non trouvée dans ce record.");
    }

    public int getSizeInBytes() {
        int size = 0;

        for (RecordInfo recordInfo : values) {
            String type = recordInfo.getType();
            Object value = recordInfo.getValeur();

            if (type.equals("INT")) {
                size += Integer.BYTES; // 4 octets pour un INT
            } else if (type.equals("REAL")) {
                size += Float.BYTES; // 4 octets pour un REAL
            } else if (type.startsWith("CHAR(") || type.startsWith("VARCHAR(")) {
                // Utiliser la taille maximale définie pour CHAR et VARCHAR
                int maxLength = Integer.parseInt(type.replaceAll("\\D+", ""));
                size += maxLength; // Taille fixe
            } else {
                throw new IllegalArgumentException("Type de colonne inconnu : " + type);
            }
        }

        return size;
    }

    @Override
    public String toString() {
        return "Record{" + values + '}';
    }

    public void setRecordId(RecordId recordId) {
        this.recordId = recordId;
    }

    public RecordId getRecordId() {
        return recordId;
    }
}