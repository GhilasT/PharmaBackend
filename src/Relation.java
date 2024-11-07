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

    // Step 2: Determine the position for the new record from the free space pointer
    		int recordPosition = findNextFreeSpace(buffer);

    // Step 3: Write the record data to the buffer
		int bytesWritten = writeRecordToBuffer(record, buffer, recordPosition);
	
	    // Step 4: Update the slot directory with the new record's position and size
		int slotCountOffset = buffer.capacity() - 4;
	    	int slotCount = buffer.getInt(slotCountOffset); // Get current slot count
		
		int newSlotOffset = buffer.capacity() - 8 - (slotCount * 8); // Each slot entry is 8 bytes (4 bytes position + 4 bytes size)
		buffer.putInt(newSlotOffset, recordPosition); // Record position
		buffer.putInt(newSlotOffset + 4, bytesWritten); // Record size
		
		    // Step 5: Update the free space pointer and slot count in the slot directory
		buffer.putInt(buffer.capacity() - 8, recordPosition + bytesWritten); // Update free space pointer
		buffer.putInt(slotCountOffset, slotCount + 1); // Increment slot count
		
		    // Step 6: Mark the page as modified and return the RecordId
		bufferManager.FreePage(pageId, true);
		return new RecordId(pageId, slotCount); // Return the slot index as the record
	    
			
		
	}
	
	public ArrayList<PageID> getRecordsInDataPage(PageID PageId) {
		ArrayList<Record> records = new ArrayList<>();

	    // Step 1: Retrieve the buffer for the specified page
		ByteBuffer buffer = bufferManager.GetPage(pageId);
	
	    // Step 2: Read metadata from the slot directory at the end of the page
	    	int slotCountOffset = buffer.capacity() - 4;
	    	int slotCount = buffer.getInt(slotCountOffset);
	
	    // Step 3: Iterate through each slot in the slot directory to retrieve records
	    	for (int slotIndex = 0; slotIndex < slotCount; slotIndex++) {
	        // Calculate the offset for the current slot's metadata (each slot has 8 bytes)
	        int slotOffset = buffer.capacity() - 8 - (slotIndex * 8);
	        
	        int recordPosition = buffer.getInt(slotOffset);
	        int recordSize = buffer.getInt(slotOffset + 4);
	
	        // If the recordSize is 0, the record has been deleted; skip it
	        if (recordSize == 0) continue;
	
	        // Step 4: Extract the record from the buffer using the position and size
	        byte[] recordBytes = new byte[recordSize];
	        buffer.position(recordPosition);
	        buffer.get(recordBytes);
	
	        // Convert byte array into a Record object (assuming a Record constructor or method for this)
	        Record record = Record.fromBytes(recordBytes);
	        
	        // Add the record to the list
	        records.add(record);
	    	}
	
	    // Step 5: Free the page after reading
	    	bufferManager.FreePage(pageId, false);  // Mark the page as not modified (false)
	
	    	return records;
		
	}
	 
	public ArrayList<PageID> getDataPages(){
		
	}
	
	public RecordId InsertRecord (Record record) {
		
	}
		
	public ArrayList<PageID> GetAllRecords(){
		
	}
}
