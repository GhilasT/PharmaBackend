import java.io.IOException;
import java.util.ArrayList;

public class BufferManager {
    private DBConfig config;
    private DiskManager disk;
    private ArrayList<Buffer> buffers;
    private String currentReplacementPolicy;

    public BufferManager(DBConfig config, DiskManager diskManager) {
        this.config = config;
        this.disk = diskManager;
        this.buffers = new ArrayList<>();

        // Initialisation des buffers
        for (int i = 0; i < config.getBm_buffercount(); i++) {
            Buffer buffer = new Buffer(config.getPagesize());
            buffers.add(buffer);
            System.out.println("Buffer " + i + " initialisé avec une taille de " + config.getPagesize());
        }

        // Politique de remplacement par défaut
        this.currentReplacementPolicy = "LRU";
    }

    public DiskManager getDisk() {
        return disk;
    }

    public Buffer GetPage(PageID pageId) throws IOException {
        // Étape 1 : Vérifier si la page est déjà en mémoire
        for (Buffer buffer : buffers) {
            if (pageId.equals(buffer.getPageId())) {
                buffer.incrementPinCount();
                buffer.updateLastAccessTime();
                //System.out.println("Page " + pageId + " trouvée en mémoire. Pin Count : " + buffer.getPinCount());
                return buffer;
            }
        }

        // Étape 2 : Chercher un buffer libre
        for (Buffer buffer : buffers) {
            if (buffer.getPageId() == null) {
                disk.ReadPage(pageId, buffer.getPageData());
                buffer.setPageId(pageId);
                buffer.incrementPinCount();
                buffer.updateLastAccessTime();
                buffer.setDirty(false);
                //System.out.println("Page " + pageId + " chargée dans un buffer libre.");
                return buffer;
            }
        }

        // Étape 3 : Appliquer la politique de remplacement
        Buffer bufferToReplace = null;

        if ("LRU".equals(currentReplacementPolicy)) {
            long minLastAccessTime = Long.MAX_VALUE;
            for (Buffer buffer : buffers) {
                if (buffer.getPinCount() == 0 && buffer.getLastAccessTime() < minLastAccessTime) {
                    bufferToReplace = buffer;
                    minLastAccessTime = buffer.getLastAccessTime();
                }
            }
        } else if ("MRU".equals(currentReplacementPolicy)) {
            long maxLastAccessTime = Long.MIN_VALUE;
            for (Buffer buffer : buffers) {
                if (buffer.getPinCount() == 0 && buffer.getLastAccessTime() > maxLastAccessTime) {
                    bufferToReplace = buffer;
                    maxLastAccessTime = buffer.getLastAccessTime();
                }
            }
        }

        if (bufferToReplace == null) {
            throw new IOException("Aucun buffer disponible pour remplacement !");
        }

        // Étape 4 : Remplacer le contenu du buffer sélectionné
        if (bufferToReplace.isDirty()) {
            // Si le buffer est "dirty", sauvegarder la page actuelle sur disque
            disk.WritePage(bufferToReplace.getPageId(), bufferToReplace.getPageData());
        }

        // Lire la nouvelle page dans le buffer
        disk.ReadPage(pageId, bufferToReplace.getPageData());
        bufferToReplace.setPageId(pageId);
        bufferToReplace.incrementPinCount();
        bufferToReplace.updateLastAccessTime();
        bufferToReplace.setDirty(false);
        //System.out.println("Page " + pageId + " chargée dans un buffer remplacé.");
        return bufferToReplace;
    }

    public void FreePage(PageID pageId, boolean dirty) {
        for (Buffer buffer : buffers) {
            if (pageId.equals(buffer.getPageId())) {
                buffer.decrementPinCount();
                if (dirty) {
                    buffer.setDirty(true);
                }
                //System.out.println("Page " + pageId + " libérée. Pin Count : " + buffer.getPinCount());
                return;
            }
        }
        System.err.println("Erreur : PageID " + pageId + " non trouvée dans les buffers !");
    }

    public void FlushBuffers() throws IOException {
        for (Buffer buffer : buffers) {
            if (buffer.isDirty() && buffer.getPageId() != null) {
                disk.WritePage(buffer.getPageId(), buffer.getPageData());
                buffer.setDirty(false);
            }
        }
        //System.out.println("Tous les tampons ont été vidés sur disque.");
    }

    public void displayBufferState() {
        System.out.println("=== État des buffers ===");
        for (int i = 0; i < buffers.size(); i++) {
            Buffer buffer = buffers.get(i);

        }

    }

    public void SetCurrentReplacementPolicy(String policy) {
        if (!policy.equals("LRU") && !policy.equals("MRU")) {
            throw new IllegalArgumentException("Politique inconnue : " + policy);
        }
        this.currentReplacementPolicy = policy;
        System.out.println("Politique de remplacement changée : " + policy);
    }

    public ArrayList<Buffer> getBuffers() {
        return buffers;
    }
}