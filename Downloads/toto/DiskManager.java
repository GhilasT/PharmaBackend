import java.io.*;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;

public class DiskManager {
    private DBConfig config;
    private int Xfichier;
    private int indicPage;
    private int tailleActuFich;
    private ArrayList<PageID> page_libre;

    public DiskManager(DBConfig config) {
        this.config = config;
        this.Xfichier = 0;
        this.indicPage = 0;
        this.tailleActuFich = 0;
        this.page_libre = new ArrayList<>();
    }

    public ArrayList<PageID> getPage_libre() {
        return page_libre;
    }

    public DBConfig getConfig() {
        return config;
    }



    public PageID AllocPage() throws IOException {
        if (!page_libre.isEmpty()) {
            PageID reusedPage = page_libre.remove(0);
            //System.out.println("[DiskManager] Réutilisation de la page libre : " + reusedPage);
            return reusedPage;
        }

        for (int fileIdx = 0; ; fileIdx++) {
            String filePath = config.getDbpath() + "/F" + fileIdx + ".rsdb.txt";
            File file = new File(filePath);

            if (!file.exists()) {
                file.createNewFile();
            }

            long fileSize = file.length();
            int maxFileSize = config.getDM_maxfilesize();

            if (fileSize + config.getPagesize() <= maxFileSize) {
                PageID newPageId = new PageID(fileIdx, (int) (fileSize / config.getPagesize()));
                //System.out.println("[DiskManager] Nouvelle page allouée : " + newPageId);
                try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
                    raf.setLength(fileSize + config.getPagesize());
                }
                return newPageId;
            }
        }
    }


    public void DeallocPage(PageID page) {
        if (page_libre.stream().noneMatch(p -> p.equals(page))) {
            page_libre.add(page);
            System.out.println("Page " + page.getPageIdx() + " libérée et ajoutée à la liste des pages libres.");
        }
    }

    public void SaveState() throws IOException {
        // Chemin complet pour le fichier de sauvegarde
        String saveFilePath = config.getDbpath() + "/dm_save.json";
        JSONArray jsonArray = new JSONArray();

        // Parcours des pages libres et ajout dans le tableau JSON
        for (PageID page : page_libre) {
            JSONObject pageObject = new JSONObject();
            pageObject.put("FileIdx", page.getFileIdx());
            pageObject.put("PageIdx", page.getPageIdx());
            jsonArray.put(pageObject);
        }

        // Sauvegarde du tableau JSON dans le fichier
        try (FileWriter fileWriter = new FileWriter(saveFilePath)) {
            fileWriter.write(jsonArray.toString(4));
            System.out.println("État sauvegardé dans : " + saveFilePath);
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde de l'état dans " + saveFilePath);
            throw e;
        }
    }

    public void LoadState() throws IOException {
        String filechemin = config.getDbpath() + "/dm_save.json";
        try {
            File file = new File(filechemin);
            if (!file.exists() || file.length() == 0) {
                System.out.println("Aucun fichier de sauvegarde trouvé ou fichier vide : " + filechemin);
                return;
            }

            String json = new String(Files.readAllBytes(Paths.get(filechemin)));
            JSONArray jsonArray = new JSONArray(json);
            page_libre.clear();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject pageObject = jsonArray.getJSONObject(i);
                int fileIdx = pageObject.getInt("FileIdx");
                int pageIdx = pageObject.getInt("PageIdx");
                page_libre.add(new PageID(fileIdx, pageIdx));
            }
            System.out.println("Chargement réussi depuis " + filechemin);
        } catch (IOException e) {
            System.err.println("Erreur lors du chargement du fichier " + filechemin);
            throw e;
        } catch (JSONException e) {
            System.err.println("Le fichier JSON est mal formaté : " + filechemin);
            throw e;
        }
    }
// lire dun fichier
    public void ReadPage(PageID pageId, ByteBuffer buffer) throws IOException {
        String filePath = config.getDbpath() + "/F" + pageId.getFileIdx() + ".rsdb.txt";
        File file = new File(filePath);

        if (!file.exists()) {
            throw new IOException("Le fichier " + filePath + " n'existe pas.");
        }

        long offset = (long) pageId.getPageIdx() * config.getPagesize();
        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            if (offset >= raf.length()) {
                // Si la page n'existe pas dans le fichier, initialiser avec des zéros
                buffer.clear();
                for (int i = 0; i < config.getPagesize(); i++) {
                    buffer.put((byte) 0);
                }
            } else {
                // Lire les données existantes
                byte[] data = new byte[config.getPagesize()];
                raf.seek(offset);
                raf.readFully(data);
                buffer.clear();
                buffer.put(data);
            }
            buffer.flip(); // Préparer le buffer pour la lecture
        }
    }
    public void WritePage(PageID pageId, ByteBuffer buffer) throws IOException {
        if (buffer.remaining() > config.getPagesize()) {
            throw new IOException("Erreur : Taille du ByteBuffer (" + buffer.remaining() +
                    ") plus grande que la taille de la page (" + config.getPagesize() + ").");
        }

        String path = config.getDbpath() + "/F" + pageId.getFileIdx() + ".rsdb.txt";
        File file = new File(path);

        // Préparation du fichier et des répertoires avec gestion des erreurs
        try {
            if (!file.exists()) {
                boolean dirsCreated = file.getParentFile().mkdirs();
                if (!dirsCreated && !file.getParentFile().exists()) {
                    throw new IOException("Impossible de créer les répertoires parent : " + file.getParentFile());
                }

                boolean fileCreated = file.createNewFile();
                if (!fileCreated) {
                    throw new IOException("Impossible de créer le fichier : " + path);
                }
            }
        } catch (IOException e) {
            throw new IOException("Erreur lors de la préparation du fichier : " + path, e);
        }

        long offset = (long) pageId.getPageIdx() * config.getPagesize();

        // Écriture des données dans le fichier
        try (RandomAccessFile raf = new RandomAccessFile(file, "rw")) {
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);

            raf.seek(offset);
            raf.write(data);



        }

        buffer.clear();
    }
}
