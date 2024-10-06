import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


public class DiskManager {
	private DBConfig config;
	private  int Xfichier ; // indice du fichier actuel 
	private int   indicPage; // indice de la page dans le fichier actuel
	private int tailleActuFich; // la taille actuel du fichier (octets)
	private ArrayList<PageID>page_libre;

	
	public DiskManager(DBConfig config, ArrayList<Integer> page_libre) {
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
		buff.put(Page2);																		// transfere les données du tableau d'octet Page dans le ByteBuffer buff
		buff.flip();																		// assure du bon lancement à la prochaine utilisation de buff (remet le curseur à 0)
		file.close();
	}
	
	
	public void WritePage(PageID Page, ByteBuffer buff) throws Exception {
		String path="F"+Page.getFileIdx()+".rsdb";
		RandomAccessFile file = new RandomAccessFile(path,"rw");			// ouvre le fichier en mode ecriture
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
		Gson gson = new Gson();
		String json = gson.toJson(page_libre);													// traduit le tableau page_libre en JSon
		FileWriter writer = new FileWriter("dm_save.json");										// ouvre le fichier en droit ecriture
		writer.write(json);																		// écrit json dans le fichier dm_save.json
		writer.close();
	}


	public void LoadState() throws IOException {
		Gson gson = new Gson();
		BufferedReader reader = new BufferedReader(new FileReader("dm_save.json"));				// ouvre le fichier en droit lecture
		Type type = new TypeToken<ArrayList<ArrayList<Integer>>>() {}.getType();				// crée un type arraylist en 2d pour la fonction en bas gson.fromJson
		page_libre = gson.fromJson(reader,type);												// gson.fromJson vas traduire de language Json à Java 
		reader.close();
	}

}
