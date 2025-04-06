public class RecordInfo {
    private String type;   // Le type de la colonne (ex: "INT", "REAL", "VARCHAR(10)", "CHAR(20)")
    private Object valeur; // La valeur associée à la colonne

    // Constructeur
    public RecordInfo(String type, Object valeur) {
        this.type = type;
        this.valeur = valeur;
    }

    // Getter pour le type
    public String getType() {
        return type;
    }

    // Setter pour le type
    public void setType(String type) {
        this.type = type;
    }

    // Getter pour la valeur
    public Object getValeur() {
        return valeur;
    }

    // Setter pour la valeur
    public void setValeur(Object valeur) {
        this.valeur = valeur;
    }

    // Représentation lisible pour le débogage
    @Override
    public String toString() {
        return " "+valeur ;
    }
}