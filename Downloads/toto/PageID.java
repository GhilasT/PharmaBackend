import java.util.Objects;

public class PageID {
    private int FileIdx;
    private int PageIdx;
    private int Pagesize;

    public PageID(int fileIdx, int pageIdx) {
        this.FileIdx = fileIdx;
        this.PageIdx = pageIdx;
    }

    // Getters et setters
    public int getFileIdx() { return FileIdx; }
    public int getPageIdx() { return PageIdx; }
    public int getPagesize() { return Pagesize; }
    public void setFileIdx(int fileIdx) { FileIdx = fileIdx; }
    public void setPageIdx(int pageIdx) { PageIdx = pageIdx; }
    public void setPagesize(int pagesize) { Pagesize = pagesize; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PageID pageID = (PageID) obj;
        return FileIdx == pageID.FileIdx && PageIdx == pageID.PageIdx;
    }

    @Override
    public int hashCode() {
        return Objects.hash(FileIdx, PageIdx);
    }
}
