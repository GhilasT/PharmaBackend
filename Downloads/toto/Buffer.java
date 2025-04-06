import java.nio.ByteBuffer;

public class Buffer {
    private PageID pageId;
    private ByteBuffer pageData;
    private int pinCount;
    private boolean dirty;
    private long lastAccessTime;
    private boolean locked;
    private int capaciterActu;

    public Buffer(int pageSize) {
        this.pageId = null;
        this.pageData = ByteBuffer.allocate(pageSize);
        this.pinCount = 0;
        this.dirty = false;
        this.lastAccessTime = System.currentTimeMillis();
        this.capaciterActu=0;
    }

    public PageID getPageId() {
        return pageId;
    }

    public void setPageId(PageID pageId) {
        this.pageId = pageId;
    }

    public ByteBuffer getPageData() {
        return pageData;
    }

    public int getPinCount() {
        return pinCount;
    }

    public void incrementPinCount() {
        this.pinCount++;
    }

    public void decrementPinCount() {
        if (this.pinCount > 0) {
            this.pinCount--;
        } else {
            throw new IllegalStateException("Pin count ne peut pas être négatif");
        }
    }

    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }

    public long getLastAccessTime() {
        return lastAccessTime;
    }

    public void updateLastAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    public boolean isLocked() {
        return locked;
    }

    public void lock() {
        this.locked = true;
    }

    public void unlock() {
        this.locked = false;
    }

    public void reset() {
        this.pageId = null;
        this.pageData.clear(); // Réinitialise les données
        this.pinCount = 0;
        this.dirty = false;
        this.lastAccessTime = System.currentTimeMillis();
    }

    public boolean isFree() {
        return this.pinCount == 0 && !this.dirty;
    }

    public void setCapaciterActu(int oct ){
        this.capaciterActu += oct;
    }

    public int getCapaciterActu(){
        return this.capaciterActu;
    }
}