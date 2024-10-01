
public class PageID {
	private int FileIdx;
	private int PageIdx;
	
	
	public PageID(int FileIdx, int PageIdx) {
		this.FileIdx = FileIdx;
		this.PageIdx = PageIdx;
	}
	
	
	public int getFileIdx() {
		return FileIdx;
	}
	
	public int getPageIdx() {
		return PageIdx;
	}
	
	public String getNomdePage() {
		return ("F"+ FileIdx+ PageIdx);
	}
}
