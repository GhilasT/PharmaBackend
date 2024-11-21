import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class BufferManager {
    DBConfig config;
    private DiskManager disk;
    private ArrayList<Buffer> buffListe;
    private String currentReplacementPolicy;

    public BufferManager(DBConfig config, DiskManager disk) {
        this.config = config;
        this.disk = disk;
        this.buffListe = new ArrayList<>();
        for (int i = 0; i < config.getBm_buffercount(); i++) {
            buffListe.add(new Buffer(config.getPagesize()));
        }
        if(config.getBm_policy() == "LRU" || config.getBm_policy() == "MRU") {
        	 this.currentReplacementPolicy = config.getBm_policy();
        	 
        }else {
        	this.currentReplacementPolicy = "LRU";// par defaut
        }
       
    }

    public Buffer GetPage(PageID pageId) throws Exception {
        if (buffListe.isEmpty()) {
            throw new IllegalStateException("Aucun buffer n'a été initialisé dans BufferManager.");
        }

        // Vérifier si la page est déjà en mémoire
        for (Buffer buffer : buffListe) {
            if (buffer.getPageId() != null && buffer.getPageId().equals(pageId)) {
                buffer.incrementPinCount(); // Marquer le buffer comme utilisé
                return buffer;
            }
        }

        // Trouver un buffer disponible pour le remplacement
        Buffer bufferToReplace = findBufferToReplace();
        if (bufferToReplace == null) {
            throw new IOException("Aucun buffer libre disponible !");
        }

        // Si le buffer contient une page modifiée, l'écrire sur le disque avant le remplacement
        if (bufferToReplace.isDirty()) {
            disk.WritePage(bufferToReplace.getPageId(), ByteBuffer.wrap(bufferToReplace.getPageData()));
        }

        // Charger la nouvelle page depuis le disque dans un ByteBuffer temporaire
        ByteBuffer tempBuffer = ByteBuffer.allocate(config.getPagesize());
        disk.ReadPage(pageId, tempBuffer);

        // Mettre à jour le buffer avec la nouvelle page
        bufferToReplace.setPageData(tempBuffer.array());
        bufferToReplace.setPageId(pageId);
        bufferToReplace.setDirty(false);
        bufferToReplace.incrementPinCount();

        return bufferToReplace;
    }
 /*
 	public ByteBuffer GetPage(PageID pageId) throws Exception {
 		// verfier si la page est déjà dans un buffer

 		for (Buffer buffer : buffListe) {
 			if (buffer.getPageId() != null && buffer.getPageId().equals(pageId)) {
 				// incementer le pinCount
 				buffer.incrementPinCount();

 				// Met à jour le dernier accès pour LRU
 				buffer.updateLastAccessTime();		
 				return ByteBuffer.wrap(buffer.getPageData());
 			}
 		}
 	
 		// si la page n'est pas en mémoire => chercher un buffer libre
 		for (Buffer buffer : buffListe) {
 			if (buffer.getPinCount() == 0) {

 				ByteBuffer tempBuffer = ByteBuffer.allocate(config.getPagesize());
 				disk.ReadPage(pageId, tempBuffer);
 				// madifier les attributs du buffer
 				buffer.setPageData(tempBuffer.array());
 				buffer.setPageId(pageId);
 				buffer.setDirty(false);
 				buffer.incrementPinCount();
 				buffer.updateLastAccessTime();
 				return ByteBuffer.wrap(buffer.getPageData());
 			}
 		}
 	
 		// si les buffers sont utilisés (aucun avec pin_count == 0)
 		throw new IOException("Pas de buffer libre disponible");
 	}
 	*/
    public void FreePage(PageID pageId, boolean valdirty) {
        for (Buffer buffer : buffListe) {
            if (buffer.getPageId() != null && buffer.getPageId().equals(pageId)) {
                buffer.decrementPinCount();
                buffer.setDirty(valdirty);
                return;
            }
        }
    }

    public void SetCurrentReplacementPolicy(String policy) {
        if (policy.equals("LRU") || policy.equals("MRU")) {
            this.currentReplacementPolicy = policy;
            System.out.println("Politique de remplacement changée : " + policy);
        } else {
            throw new IllegalArgumentException("Politique inconnue : " + policy);
        }
    }

    public void FlushBuffers() throws Exception {
        for (Buffer buffer : buffListe) {
            if (buffer.isDirty() && buffer.getPinCount() == 0) {
                disk.WritePage(buffer.getPageId(), ByteBuffer.wrap(buffer.getPageData()));
                buffer.setDirty(false);
            }
            buffer.reset();
        }
    }

    private Buffer findBufferToReplace() {
        if (currentReplacementPolicy.equals("LRU")) {
            return buffListe.stream()
                    .filter(buffer -> buffer.getPinCount() == 0)
                    .min((b1, b2) -> Long.compare(b1.getLastAccessTime(), b2.getLastAccessTime()))
                    .orElse(null);
        } else if (currentReplacementPolicy.equals("MRU")) {
            return buffListe.stream()
                    .filter(buffer -> buffer.getPinCount() == 0)
                    .max((b1, b2) -> Long.compare(b1.getLastAccessTime(), b2.getLastAccessTime()))
                    .orElse(null);
        }
        return null;
    }
}
