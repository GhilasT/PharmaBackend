import java.util.List;

public class Condition {
    private int columnIndex; // Indice de la colonne concernée
    private Object value;    // Valeur de la condition
    private String operator; // Opérateur de comparaison (e.g., =, >, <)

    public Condition(String condition, List<ColInfo> columns, String alias) {
        // Parse la condition (ex: "p.C1=9" -> colonne p.C1, opérateur =, valeur 9)
        String[] parts = condition.split("\\s*(=|<|>|<=|>=|!=)\\s*");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Condition mal formée : " + condition);
        }

        String columnName = parts[0].trim();
        String valueString = parts[1].trim();
        this.operator = condition.replaceAll(".*?(=|<|>|<=|>=|!=).*", "$1");

        // Si un alias est présent (ex: p.C1), on le retire
        if (alias != null && columnName.startsWith(alias + ".")) {
            columnName = columnName.substring(alias.length() + 1); // Supprimer l'alias du nom de colonne
        }

        // Trouver l'indice de la colonne dans les métadonnées
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getNomColonne().equalsIgnoreCase(columnName)) {
                this.columnIndex = i;

                // Parser la valeur selon le type de la colonne
                String colType = columns.get(i).getTypeColonne();
                this.value = parseValue(valueString, colType);
                return;
            }
        }

        throw new IllegalArgumentException("Colonne non trouvée : " + columnName);
    }

    public boolean evaluate(Record record) {
        Object columnValue = record.getValues().get(columnIndex).getValeur();

        if (columnValue instanceof Comparable && value instanceof Comparable) {
            @SuppressWarnings("unchecked")
            Comparable<Object> comparableColumnValue = (Comparable<Object>) columnValue;

            switch (operator) {
                case "=": return comparableColumnValue.compareTo(value) == 0;
                case "!=": return comparableColumnValue.compareTo(value) != 0;
                case "<": return comparableColumnValue.compareTo(value) < 0;
                case ">": return comparableColumnValue.compareTo(value) > 0;
                case "<=": return comparableColumnValue.compareTo(value) <= 0;
                case ">=": return comparableColumnValue.compareTo(value) >= 0;
                default: throw new IllegalArgumentException("Opérateur non supporté : " + operator);
            }
        }

        throw new IllegalArgumentException("Types incompatibles pour la condition.");
    }

    private Object parseValue(String value, String colType) {
        if (colType.startsWith("CHAR(") || colType.startsWith("VARCHAR(")) {
            return value.replace("\"", "").trim();
        }
        switch (colType.toUpperCase()) {
            case "INT": return Integer.parseInt(value);
            case "REAL": return Float.parseFloat(value);
            default: throw new IllegalArgumentException("Type de colonne inconnu : " + colType);
        }
    }
}