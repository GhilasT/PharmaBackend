import java.nio.ByteBuffer;

public class DiskManager {
	private DBConfig config;
	
	public DiskManager(DBConfig config) {
		this.config = config;
		
	}
	int indFich = 0;
	int indPage = 0;
	public PageID AllocPage() {
		PageID page = new PageID(indFich, indPage);
		indFich++;
		indPage ++;
		return page;
		
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
