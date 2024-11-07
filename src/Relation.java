import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.swing.text.html.HTMLDocument;

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
	                    
	                    byte[] stringBytes = new byte[100]; 
	                    buff.get(stringBytes);
	                    String stringValue = new String(stringBytes).trim();
	                    record.addValue(index, stringValue);
	                    totalBytesRead += stringBytes.length;
	                    break;
	
	                default:
	                    throw new IllegalArgumentException("Type de colonne non supporté: " + columnType);
	            }
				index++;
	        }
	
	        return totalBytesRead;
	}
	
	public void addDataPage() {
		PageID PageVide = this.diskManager.AllocPage();
		 // créer un buffer pour la nouvelle page vide
		ByteBuffer dataPageBuffer = ByteBuffer.allocate(PageVide.getPagesize());
		dataPageBuffer.putInt(PageVide.getPageIdx()); 
 
		int espacelibre = PageVide.getPagesize() ;
		PageDirectory.addPage(PageVide, espacelibre);
 
	}
	
	public PageID getFreeDataPageId(int sizeRecord) {
		return PageDirectory.findPageWithSpace(sizeRecord);
   		 
	}
	private int findNextFreeSpace(ByteBuffer buffer) {
    .
    		int freeSpacePositionOffset = buffer.capacity() - 8; 
    		int freeSpacePosition = buffer.getInt(freeSpacePositionOffset);
    		return freeSpacePosition;
	}


	//           à finir
	public RecordId writeRecordToDataPage (Record record, PageID pageId) throws Exception {
    		ByteBuffer buffer = bufferManager.GetPage(pageId);

    		// Étape 2 : Déterminer la position pour le nouvel enregistrement à partir du pointeur d'espace libre
    		int recordPosition = findNextFreeSpace(buffer);

    		// Étape 3 : Écrire les données de l'enregistrement dans le tampon
    		int bytesWritten = writeRecordToBuffer(record, buffer, recordPosition);

    		// Étape 4 : Mettre à jour le répertoire des emplacements avec la position et la taille du nouvel enregistrement
    		int slotCountOffset = buffer.capacity() - 4;
    		int slotCount = buffer.getInt(slotCountOffset); // Obtenir le nombre d'emplacements actuel

    		int newSlotOffset = buffer.capacity() - 8 - (slotCount * 8); // Chaque entrée d'emplacement fait 8 octets (4 octets pour la position + 4 octets pour la taille)
   	 	buffer.putInt(newSlotOffset, recordPosition); // Position de l'enregistrement
   		buffer.putInt(newSlotOffset + 4, bytesWritten); // Taille de l'enregistrement

    		// Étape 5 : Mettre à jour le pointeur d'espace libre et le nombre d'emplacements dans le répertoire des emplacements
    		buffer.putInt(buffer.capacity() - 8, recordPosition + bytesWritten); // Mettre à jour le pointeur d'espace libre
    		buffer.putInt(slotCountOffset, slotCount + 1); // Incrémenter le nombre d'emplacements

    		// Étape 6 : Marquer la page comme modifiée et retourner l'identifiant de l'enregistrement
    		bufferManager.FreePage(pageId, true);
    		return new RecordId(pageId, slotCount); // Retourner l'indice de l'emplacement comme identifiant de l'enregistrement
	}

	    
			
		
	}
	public ArrayList<PageID> getRecordsInDataPage(PageID PageId) {
		ArrayList<Record> records = new ArrayList<>();

     		// Étape 1 : Récupérer le tampon pour la page spécifiée
    		ByteBuffer buffer = bufferManager.GetPage(pageId);

    		// Étape 2 : Lire les métadonnées du répertoire des emplacements à la fin de la page
    		int slotCountOffset = buffer.capacity() - 4;
    		int slotCount = buffer.getInt(slotCountOffset);

    		// Étape 3 : Parcourir chaque emplacement dans le répertoire des emplacements pour récupérer les enregistrements
    		for (int slotIndex = 0; slotIndex < slotCount; slotIndex++) {
        	// Calculer le décalage pour les métadonnées de l'emplacement actuel (chaque emplacement fait 8 octets)
        		int slotOffset = buffer.capacity() - 8 - (slotIndex * 8);
        
        		int recordPosition = buffer.getInt(slotOffset);
        		int recordSize = buffer.getInt(slotOffset + 4);

        		// Si la taille de l'enregistrement est 0, l'enregistrement a été supprimé ; le sauter
        		if (recordSize == 0) continue;

        		// Étape 4 : Extraire l'enregistrement du tampon en utilisant la position et la taille
        		byte[] recordBytes = new byte[recordSize];
        		buffer.position(recordPosition);
       			buffer.get(recordBytes);

        		// Convertir le tableau d'octets en un objet Record (en supposant qu'il existe un constructeur ou une méthode pour cela)
        		Record record = Record.fromBytes(recordBytes);
        
        		// Ajouter l'enregistrement à la liste
      	 	 	records.add(record);
    		}

    		// Étape 5 : Libérer la page après la lecture
    		bufferManager.FreePage(pageId, false);  // Marquer la page comme non modifiée (false)

    		return records;
	}

	
	
	 
	public ArrayList<PageID> getDataPages(){
		ArrayList<PageID> pageIds = new ArrayList<>();

	    // Étape 1 : Récupérer le buffer de la page d'en-tête depuis le BufferManager
	    	ByteBuffer buffer = bufferManager.GetPage(headerPageId);
	
	    // Étape 2 : Lire le nombre de pages de données depuis la page d'en-tête
	    	int pageCount = buffer.getInt(0); // Les 4 premiers octets contiennent le nombre de pages
	
	    // Étape 3 : Itérer sur chaque entrée dans la page d'en-tête pour récupérer les PageIDs
		int offset = 4; // On commence après le nombre de pages (4 premiers octets)
		for (int i = 0; i < pageCount; i++) {
		// Lire l'index du fichier de PageID
			int fileIdx = buffer.getInt(offset);
		        
		 // Lire l'index de la page de PageID
			int pageIdx = buffer.getInt(offset + 4);
		
		// Créer un nouveau PageID et l'ajouter à la liste
			pageIds.add(new PageID(fileIdx, pageIdx));
	
	        // Passer à l'entrée suivante dans l'en-tête (chaque entrée est de 12 octets : 8 pour PageID, 4 pour espace libre)
	        	offset += 12;
	    	}
	
	    	// Étape 4 : Libérer la page d'en-tête après lecture
	    	bufferManager.FreePage(headerPageId, false); // Marquer la page comme non modifiée (false)

    		return pageIds;
		
	}
	
	  public RecordId InsertRecord(Record record) {
        	// 1. Trouver une page de données avec de l'espace libre
        	PageID pageId = getFreeDataPageId(record.getSize());
        	if (pageId == null) {
            	// Si aucune page libre n'est trouvée, allouer une nouvelle page de données
            	addDataPage();
            	pageId = getFreeDataPageId(record.getSize());
        	}

        	// 2. Écrire l'enregistrement dans la page de données
        	try {
            		return writeRecordToDataPage(record, pageId);
        	}catch (Exception e) {
            		throw new RuntimeException("Erreur lors de l'insertion de l'enregistrement", e);
        	}
   	 }

    // Méthode pour récupérer tous les enregistrements
	public ArrayList<Record> GetAllRecords() {
        	ArrayList<Record> allRecords = new ArrayList<>();

        	// 1. Obtenez toutes les pages de données associées à la relation
	        ArrayList<PageID> dataPages = getDataPages();

        	// 2. Parcourez chaque page de données pour extraire les enregistrements
        	for (PageID pageId : dataPages) {
            		// 3. Récupérer les enregistrements présents dans la page
            		ArrayList<Record> recordsInPage = getRecordsInDataPage(pageId);
           		 allRecords.addAll(recordsInPage);
        	}	

        	return allRecords;
    	}
}
