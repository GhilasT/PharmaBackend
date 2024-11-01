import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;




public class DiskManager {
	private DBConfig config;
	private  int Xfichier ; // indice du fichier actuel
	private int   indicPage; // indice de la page dans le fichier actuel
	private int tailleActuFich; // la taille actuel du fichier (octets)
	private ArrayList<PageID>page_libre;

	private String filechemin ; // chemin du fichier ou sauvegarder la liste des pages libres


	public DiskManager(DBConfig config ) {
		this.config = config;
		this.Xfichier =0;
		this.indicPage =0;
		this. tailleActuFich = 0;
		this.page_libre = new ArrayList<PageID>();

	}

	public PageID AllocPage() {
		//Vérifier qu'on peut ajouter une page au fichier (taille)
		if(page_libre.size() > 0 ){
			PageID page = new PageID(page_libre.get(page_libre.size() - 1).getPageIdx(), page_libre.get(page_libre.size() - 1).getFileIdx());
			this.page_libre.remove(this.page_libre.size() -1);
		}
		if(this.tailleActuFich + config.getPagesize() > config.getDM_maxfilesize()){
			this.Xfichier ++;
			this.tailleActuFich =0;
			this.indicPage = 0;
		}

		PageID page = new PageID(this.Xfichier, this.indicPage);
		this.indicPage++;
		this.tailleActuFich += this.config.getPagesize();
		page.setPagesize(this.config.getPagesize());
		return page;

	}

	public void ReadPage(PageID page, ByteBuffer buff) throws Exception{
		String path="F"+page.getFileIdx()+".rsdb";
		// ouvre le fichier en mode lecture
		RandomAccessFile file = new RandomAccessFile(path,"r");
		// on se place dans le fichier au niveau de la page
		file.seek(page.getPageIdx()*config.getPagesize());
		// on crée le tableau d'octet qui va récupérer les données de la page à lire
		byte[] Page2 = new byte[config.getPagesize()];
		file.readFully(Page2);
		// verifie que le buffer est vide pour recevoir les données
		buff.clear();
		buff.put(Page2);
		buff.flip();
		file.close();
	}

	public void WritePage(PageID Page, ByteBuffer buff) throws Exception {
		String path="F"+Page.getFileIdx()+".rsdb";
		RandomAccessFile file = new RandomAccessFile(path,"rw");
		file.seek(Page.getFileIdx()*config.getPagesize());
		byte[] page = new byte[config.getPagesize()];
		// transfère les données du ByteBuffer buff dans le tableau d'octet Byte
		buff.get(page);
		buff.flip();
		file.write(page);
		file.close();
	}






	public void DeallocPage(PageID Page) {
	Boolean dedans = false;
		int i = 0;
		while (!dedans && (i< page_libre.size())){
			if (Page == page_libre.get(i)) {
				dedans = true;
			}
			i++;
		}
		if (!dedans) {
			this.page_libre.add(Page);
	}
	}





	public void SaveState() throws IOException {
		JSONArray jsonArray = new JSONArray();
        for (PageID pg : this.page_libre) {
        	JSONObject pageObject = new JSONObject();
        	pageObject.put("FileIdx", pg.getFileIdx());
        	pageObject.put("PageIdx", pg.getPageIdx());
        	jsonArray.put(pageObject);
        }
        
        String jsonString = jsonArray.toString(4);
        
        String filechemin = config.getDbpath() + "/dm_save";
        
        try (FileWriter writer = new FileWriter(filechemin)){
        	writer.write(jsonString);
        	System.out.println("Sauvegarde réussie dans " + filechemin);
        }catch (IOException e) {
        	 System.err.println("Erreur lors de la sauvegarde du fichier " + filechemin);
             e.printStackTrace();
        }
	}


	public void LoadState() throws IOException {
		String filechemin = config.getDbpath()+"/dm_save";
		try {	
			String json = new String(Files.readAllBytes(Paths.get(filechemin)));
			JSONArray jsonArray = new JSONArray(json);
			page_libre.clear();
			
			for (int i = 0;i< jsonArray.length();i++) {
				JSONObject pageObject = jsonArray.getJSONObject(i);
				int fileIdx = pageObject.getInt("FileIdx");
				int pageIdx = pageObject.getInt("PageIdx");
				page_libre.add(new PageID(fileIdx, pageIdx));
			}
			System.out.println("Chargement réussi depuis " + filechemin);
		}catch(IOException e) {
			System.err.println("Erreur lors du chargement du fichier " + filechemin);
            e.printStackTrace();
		}
	}
}
