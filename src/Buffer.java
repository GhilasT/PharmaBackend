public class Buffer {
    // Identifiant de la page dans ce buffer
    private PageID pageId;
    // Les données de la page chargée dans ce buffer
    private byte[] pageData;
    // nombre d'utilisations de la page 
    private int pinCount;
    // Indique si la page a été modifiée
    private boolean dirty;
    // Temps du dernier accès pour LRU
    private long lastAccessTime;

    public Buffer() {
        this.pageId = null;
        this.pageData = null;
        this.pinCount = 0;
        this.dirty = false;
        // initialiser avec le temps courant
        this.lastAccessTime = System.currentTimeMillis();

    }
    public PageID getPageId() {
        return pageId;
    }

    public void setPageId(PageID pageId) {
        this.pageId = pageId;
    }

    public byte[] getPageData() {
        return pageData;
    }

    public void setPageData(byte[] pageData) {
        this.pageData = pageData;
    }

    
    public int getPinCount() {
        return pinCount;
    }

    public void incrementPinCount() {
        pinCount++;
    }

    public void decrementPinCount() {
        if (pinCount > 0) {
            pinCount--;
        }
    }

    public void setPinCount(int n) {
    	pinCount = n;
    }
    public boolean isDirty() {
        return dirty;
    }

    public void setDirty(boolean dirty) {
        this.dirty = dirty;
    }
   // Met à jour le temps actuel
    public void updateLastAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }
    //Modif
}
