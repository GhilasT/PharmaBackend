import java.nio.ByteBuffer;
import java.util.ArrayList;


public class BufferManager {
	private DBConfig config;
	private DiskManager disk;
	private ArrayList<ByteBuffer> buffListe;
	
	public BufferManager(DBConfig config, DiskManager disk) {
		this.config = config;
		this.disk = disk;
	}
	
	
	public ByteBuffer GetPage(PageID pageId) {
		
		disk.ReadPage(pageId,buffListe.get(Page));
		
	return buffListe.get(?);
	
	}
	
	public void FreePage(PageID pageId, boolean valdirty) {
		
	}
	
	public void SetCurrentReplacementPolicy(String policy) {
		
	}
	
	public void FlushBuffers() {
		
	}
}
