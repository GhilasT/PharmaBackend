import java.nio.ByteBuffer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BufferManager {
	private DBConfig config;
	private DiskManager disk;
	private ArrayList<Buffer> buffListe;

	// spécifier le nombre de buffeur
	public BufferManager(DBConfig config, DiskManager disk, int nbrBuff) {
		this.config = config;
		this.disk = disk;
		this.buffListe = new ArrayList<>();
		for (int i = 0; i < nbrBuff; i++) {
            buffListe.add(new Buffer());
        }
	}
	
	// j'ai utilisé la politique de remplacement LRU
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

	
	public void FreePage(PageID pageId, boolean valdirty) {
		for (Buffer buffer : buffListe) {
			if(buffer.getPageId() != null && buffer.getPageId().equals(pageId)){
				buffer.decrementPinCount();
				buffer.setDirty(valdirty);
				buffer.updateLastAccessTime();
			}
		}
		
	}
	
	public void SetCurrentReplacementPolicy(String policy) {
		
	}
	
	public void FlushBuffers() {
		
	}
}
