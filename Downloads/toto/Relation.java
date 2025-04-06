
import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Relation {
    private String name;
    private List<ColInfo> columns;
    private int nbrcolon;
    private PageID headerPageId;
    private DiskManager diskManager;
    private BufferManager bufferManager;
    private PageID pageactu ;
    private Map<String, BPlusTree> indexes = new HashMap<>();

    public Relation(String name, List<ColInfo> columns, DiskManager diskManager, BufferManager bufferManager) throws IOException {
        this.name = name;
        this.columns = columns;
        this.nbrcolon = columns.size();
        this.diskManager = diskManager;
        this.bufferManager = bufferManager;
        this.headerPageId = diskManager.AllocPage();
    }

    public String getName() {
        return name;
    }

    // Écriture dans le buffer
    public int writeRecordToBuffer(Record record, ByteBuffer buffer, int pos) {
        //System.out.println("Début de l'écriture. Position initiale : " + pos);
        int initialPos = pos;
        List<RecordInfo> values = record.getValues();

        for (RecordInfo value : values) {
            String type = value.getType();
            Object obj = value.getValeur();
            //System.out.println("Écriture d'une valeur de type " + type + " : " + obj);

            if (type.equals("INT")) {
                buffer.putInt(pos, (Integer) obj);
                pos += Integer.BYTES;
            } else if (type.equals("REAL")) {
                buffer.putFloat(pos, (Float) obj);
                pos += Float.BYTES;
            }else if (type.startsWith("CHAR(") || type.startsWith("VARCHAR(")) {
                // Traiter VARCHAR comme CHAR avec une longueur maximale
                int length = Integer.parseInt(type.replaceAll("\\D+", ""));
                String str = (String) obj;
                byte[] strBytes = str.getBytes(StandardCharsets.UTF_8);
                byte[] paddedBytes = new byte[length];
                System.arraycopy(strBytes, 0, paddedBytes, 0, Math.min(strBytes.length, length));
                buffer.position(pos);
                buffer.put(paddedBytes);
                pos += length;
            } else {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
            //System.out.println("Nouvelle position après écriture : " + pos);
        }

        //System.out.println("Écriture terminée. Taille totale écrite : " + (pos - initialPos));
        return pos - initialPos;
    }    // Lecture depuis le buffer
    public int readFromBuffer(Record record, ByteBuffer buffer, int pos) {
        //System.out.println("Début de la lecture. Position initiale : " + pos);
        int initialPos = pos;
        List<RecordInfo> values = new ArrayList<>();

        for (ColInfo col : columns) {
            String type = col.getTypeColonne();
            //System.out.println("Lecture d'une colonne de type : " + type);

            if (type.equals("INT")) {
                int value = buffer.getInt(pos);
                values.add(new RecordInfo("INT", value));
                pos += Integer.BYTES;
            } else if (type.equals("REAL")) {
                float value = buffer.getFloat(pos);
                values.add(new RecordInfo("REAL", value));
                pos += Float.BYTES;
            } else if (type.startsWith("CHAR(") || type.startsWith("VARCHAR(")) {
                // Traiter VARCHAR comme CHAR avec une longueur maximale
                int length = Integer.parseInt(type.replaceAll("\\D+", ""));
                byte[] strBytes = new byte[length];
                buffer.position(pos);
                buffer.get(strBytes);
                String value = new String(strBytes, StandardCharsets.UTF_8).trim();
                values.add(new RecordInfo(type.startsWith("CHAR(") ? "CHAR" : "VARCHAR", value));
                pos += length;
            } else {
                throw new IllegalArgumentException("Unsupported type: " + type);
            }
            //System.out.println("Valeur lue et position après lecture : " + values.get(values.size() - 1) + ", " + pos);
        }

        record.setValues(values);
        //
        // System.out.println("Lecture terminée. Taille totale lue : " + (pos - initialPos));
        return pos - initialPos;
    }
    public void addDataPage() throws Exception {
        //System.out.println("Début addDataPage...");
        Buffer headerBuffer = bufferManager.GetPage(headerPageId);
        ByteBuffer pageData = headerBuffer.getPageData();

        // Initialisation de la Header Page si elle est vide
        if (pageData.limit() == 0 || pageData.getInt(0) < 0) {
            //System.out.println("Initialisation explicite de la Header Page...");
            pageData.clear();
            pageData.putInt(0, 0);
            headerBuffer.setDirty(true);
        }

        try {
            // Lire le compteur de pages actuel
            int pageCount = pageData.getInt(0);


            // Allouer une nouvelle page
            PageID newPageId = diskManager.AllocPage();
            this.pageactu = newPageId;
            // System.out.println("Nouvelle page allouée : FileIdx=" + newPageId.getFileIdx() + ", PageIdx=" + newPageId.getPageIdx());

            // Calculer l'offset pour ajouter cette page dans la Header Page
            int offset = 4 + (pageCount * 12);
            if (offset + 12 > pageData.capacity()) {
                throw new IllegalStateException("Offset dépasse la capacité du buffer Header Page !");
            }

            pageData.putInt(offset, newPageId.getFileIdx()); // Indice du fichier
            pageData.putInt(offset + 4, newPageId.getPageIdx()); // Indice de la page
            pageData.putInt(offset + 8, diskManager.getConfig().getPagesize() - 8); // Espace libre initial

            pageData.putInt(0, pageCount + 1);
            System.out.println("Nombre de page dans le Header Page : " + (pageCount+1));

            headerBuffer.setDirty(true);
            bufferManager.FreePage(headerPageId, true);

            //System.out.println("Nouvelle page ajoutée à la Header Page. Total pages : " + pageData.getInt(0));
        } catch (IndexOutOfBoundsException e) {
            bufferManager.FreePage(headerPageId, true);
            throw new IllegalStateException("Erreur lors de l'accès à la Header Page : " + e.getMessage(), e);
        }
    }

    // Trouve une page avec assez d'espace pour un enregistrement
    public PageID getFreeDataPageId(int sizeRecord) throws Exception {
        //System.out.println("Recherche d'une DataPage avec au moins " + sizeRecord + " octets d'espace libre...");

        Buffer headerBuffer = bufferManager.GetPage(headerPageId);
        try {
            ByteBuffer pageData = headerBuffer.getPageData();
            int pageCount = pageData.getInt(0);
            //System.out.println("Nombre de pages dans la Header Page : " + pageCount);

            for (int i = 0; i < pageCount; i++) {
                int offset = 4 + (i * 12);
                int fileIdx = pageData.getInt(offset);
                int pageIdx = pageData.getInt(offset + 4);
                int freeSpace = pageData.getInt(offset + 8);

                if (freeSpace > sizeRecord) {
                    //System.out.println("Page trouvée avec suffisamment d'espace : FileIdx=" + fileIdx + ", PageIdx=" + pageIdx);
                    return new PageID(fileIdx, pageIdx);
                }
            }

            //System.out.println("Aucune page libre trouvée. Retourne null pour créer une nouvelle page.");
            return null;
        } finally {
            bufferManager.FreePage(headerPageId, false);
        }
    }

    public RecordId writeRecordToDataPage(Record record, PageID pageId) throws Exception {

        Buffer buffer = bufferManager.GetPage(pageId);
        ByteBuffer pageData = buffer.getPageData();

        int freeSpacePosition = findFreeSpacePosition(pageData);

        int recordSize = writeRecordToBuffer(record, pageData, freeSpacePosition);

        pageData.putInt(0, freeSpacePosition + recordSize);
        buffer.setCapaciterActu(recordSize);



        // Mettre à jour la Header Page
        Buffer headerBuffer = bufferManager.GetPage(headerPageId);
        ByteBuffer headerData = headerBuffer.getPageData();
        int pageCount = headerData.getInt(0);

        for (int i = 0; i < pageCount; i++) {
            int offset = 4 + (i * 12);
            int fileIdx = headerData.getInt(offset);
            int pageIdx = headerData.getInt(offset + 4);
            int newFreeSpace = headerData.getInt(offset + 8) - recordSize;

            if (fileIdx == pageId.getFileIdx() && pageIdx == pageId.getPageIdx()) {
                headerData.putInt(offset + 8, newFreeSpace);
                break;
            }
        }

        headerBuffer.setDirty(true);
        bufferManager.FreePage(headerPageId, true);

        RecordId recordId = new RecordId(pageId, freeSpacePosition);

        buffer.setDirty(true);
        //System.out.println("taille record (ecrie ) : "+recordSize);

        bufferManager.FreePage(pageId, true);

        return recordId;
    }

    // Méthode pour trouver la position libre dans le buffer
    private int findFreeSpacePosition(ByteBuffer pageData) {
        int usedSpaceOffset = 0;

        // Lire l'espace utilisé dans les métadonnées de la page
        if (pageData.limit() >= 4) {
            usedSpaceOffset = pageData.getInt(0);
        }

        // La position libre est après l'espace utilisé
        if (usedSpaceOffset < pageData.capacity()) {
            return usedSpaceOffset;
        } else {
            throw new IllegalStateException("Pas d'espace libre trouvé dans la page !");
        }
    }

    public List<Record> getRecordsInDataPage(PageID pageId) throws Exception {
        List<Record> records = new ArrayList<>();
        Buffer buffer = null;

        try {

            buffer = bufferManager.GetPage(pageId);
            ByteBuffer pageData = buffer.getPageData();

            int pos = 0;

            while (pos < buffer.getCapaciterActu()) {

                try {
                    Record recordLu = new Record();
                    int tailleLue = readFromBuffer(recordLu, pageData, pos);

                    if (tailleLue == 0) {
                        // Aucun record valide trouvé, arrêter la lecture
                        //System.out.println("Aucun record valide lu à la position : " + pos);
                        break;
                    }

                    // Ajouter le record à la liste
                    records.add(recordLu);
                    //System.out.println(recordLu.toString());
                    pos += tailleLue;
                    //System.out.println("recup record from page");

                } catch (Exception e) {

                    System.err.println("Erreur lors de la lecture du record à la position " + pos + " : " + e.getMessage());
                    break;
                }
            }
        } finally {
            // Libérer le buffer pour la page
            if (buffer != null) {
                bufferManager.FreePage(pageId, false);
            }
        }

        return records;
    }

    public List<PageID> getDataPages() throws IOException {
        List<PageID> dataPages = new ArrayList<>();
        Buffer buffer = null;

        try {

            buffer = bufferManager.GetPage(headerPageId);
            ByteBuffer pageData = buffer.getPageData();

            int pageCount = pageData.getInt(0);
            //System.out.println("Nombre de pages de données dans la Header Page : " + pageCount);

            for (int i = 0; i < pageCount; i++) {
                int offset = 4 + (i * 12);
                int fileIdx = pageData.getInt(offset);
                int pageIdx = pageData.getInt(offset + 4);
                dataPages.add(new PageID(fileIdx, pageIdx));
            }

        } finally {

            if (buffer != null) {
                bufferManager.FreePage(headerPageId, false);
            }
        }

        return dataPages;
    }

    // API : Insère un record dans la relation
    public RecordId InsertRecord(Record record) throws Exception {
        int recordSize = record.getSizeInBytes();
        //ystem.out.println("Insertion de l'enregistrement : " + record.getValues());

        PageID pagelibre = getFreeDataPageId(recordSize);

        if (pagelibre == null) {
            //System.out.println("Aucune page avec suffisamment d'espace. Création d'une nouvelle page...");
            addDataPage();
            pagelibre = getFreeDataPageId(recordSize);
            //System.out.println("apres creation :"+pagelibre.toString());

            if (this.pageactu == null) {
                throw new IllegalStateException("Échec de l'allocation d'une nouvelle page !");
            }
        }

        RecordId recordId = writeRecordToDataPage(record, pagelibre);

        record.setRecordId(recordId);

//        // Mettre à jour les index existants
//        for (String columnName : indexes.keySet()) {
//            int columnIndex = -1;
//            for (int i = 0; i < columns.size(); i++) {
//                if (columns.get(i).getNomColonne().equalsIgnoreCase(columnName)) {
//                    columnIndex = i;
//                    break;
//                }
//            }
//            if (columnIndex != -1) {
//                Object key = record.getValues().get(columnIndex).getValeur();
//                indexes.get(columnName).insert(key, recordId.getSlotIdx()); // Utilisation de slotIdx pour l'indexation
//            }
//        }
        //System.out.println("Record inséré avec succès dans la page : " + pagelibre);

        return recordId;
    }

    public List<Record> GetAllRecords() throws Exception {
        List<Record> allRecords = new ArrayList<>();

        List<PageID> dataPages = getDataPages();
        //System.out.println("Nombre total de pages de données : " + dataPages.size());

        for (PageID pageId : dataPages) {
            //System.out.println("Lecture des records dans la page : FileIdx=" + pageId.getFileIdx() + ", PageIdx=" + pageId.getPageIdx());

            List<Record> recordsInPage = getRecordsInDataPage(pageId);

            //System.out.println("Taille après récupération : " + recordsInPage.size()); // Devrait afficher 4
            //System.out.println("Contenu après récupération : " + recordsInPage);

            allRecords.addAll(recordsInPage);
        }

        return allRecords;
    }

    public PageID getHeaderPageId() {
        return headerPageId;
    }

    public List<ColInfo> getColumns() {
        return columns;
    }

    public void setHeaderPageId(PageID headerPageId) {
        this.headerPageId = headerPageId;
    }

    public DiskManager getDiskManager() {
        return diskManager;
    }

    public int getNbrcolont() {
        return nbrcolon;
    }

    public BufferManager getBufferManager() {
        return bufferManager;
    }

    public void createIndex(String columnName, int order) {
        int columnIndex = -1;
        for (int i = 0; i < columns.size(); i++) {
            if (columns.get(i).getNomColonne().equalsIgnoreCase(columnName)) {
                columnIndex = i;
                break;
            }
        }
        if (columnIndex == -1) {
            throw new IllegalArgumentException("Colonne introuvable : " + columnName);
        }

        BPlusTree index = new BPlusTree(order);
        List<Record> allRecords;
        try {
            allRecords = GetAllRecords();
        } catch (Exception e) {
            throw new IllegalStateException("Erreur lors de la récupération des enregistrements : " + e.getMessage(), e);
        }

        for (Record record : allRecords) {
            Object key = record.getValues().get(columnIndex).getValeur();
            index.insert(key, record.getRecordId().getSlotIdx());
        }

        indexes.put(columnName, index);
    }
    public List<Record> selectUsingIndex(String columnName, Object value) {
        BPlusTree index = indexes.get(columnName);
        if (index == null) {
            throw new IllegalStateException("Aucun index trouvé pour la colonne " + columnName);
        }

        List<Record> results = new ArrayList<>();
        List<Integer> slotIndices = index.search(value); // Rechercher les slotIdx associés à la clé

        for (Integer slotIdx : slotIndices) {
            try {
                Record record = new Record();
                Buffer buffer = bufferManager.GetPage(pageactu);
                ByteBuffer pageData = buffer.getPageData();
                readFromBuffer(record, pageData, slotIdx); // Lire le record depuis le slotIdx
                results.add(record);
                bufferManager.FreePage(pageactu, false);
            } catch (Exception e) {
                throw new IllegalStateException("Erreur lors de la lecture des données à partir de l'index : " + e.getMessage());
            }
        }

        return results;
    }

}