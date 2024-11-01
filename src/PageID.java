import java.io.*;
public class PageID {
	private int FileIdx;
	private int PageIdx;
	private int Pagesize;
	public PageID(int fileIdx, int pageIdx) {
		FileIdx = fileIdx;
		PageIdx = pageIdx;
	}

	public int getFileIdx() {
		return FileIdx;
	}

	public int getPageIdx() {
		return PageIdx;
	}

	public void setFileIdx(int fileIdx) {
		FileIdx = fileIdx;
	}

	public void setPageIdx(int pageIdx) {
		PageIdx = pageIdx;
	}

	public void setPagesize(int pagesize) {
		Pagesize = pagesize;
	}

	public int getPagesize() {
		return Pagesize;
	}

	

	
	
	

	

}
