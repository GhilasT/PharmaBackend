import java.nio.ByteBuffer;

public class DiskManager {
	private DBConfig config;
	
	public DiskManager(DBConfig config) {
		this.config = config;
		
	}
	
	public PageID AllocPage() {
		
	}
	
	public void ReadPage(PageID pageId,ByteBuffer buff) {
		
	}
	
	public void WritePage(PageID pageId, ByteBuffer buff) {
		
	}
	
	public void DeallocPage (PageID pageId) {
		
	}
	
	public void SaveState() {
		
	}
	
	public void LoadState() {
		
	}

}
