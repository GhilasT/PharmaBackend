import java.util.ArrayList;

public class Record {
    private ArrayList<Object> values; // Liste des valeurs du tuple

    public Record(ArrayList<Object> values) {
        this.values = values;
    }

    // Méthode pour obtenir la taille du record
    public int getSize() {
        return values.size();
    }

    // Méthode pour obtenir une valeur à un index donné
    public Object getValue(int index) {
        return values.get(index);
    }

    // Méthode pour obtenir toutes les valeurs
    public ArrayList<Object> getValues() {
        return values;
    }
}
