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


}
