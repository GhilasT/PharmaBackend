import java.nio.ByteBuffer;
import java.io.*;
import java.util.ArrayList;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class DiskManager {
	private DBConfig config;
	private int tailleFichierOccupe = 0;
	private ArrayList<ArrayList<Integer>>page_libre;											// tableau 2d [fichier] [numéros de page]
	
	public DiskManager(DBConfig config, ArrayList<Integer> page_libre) {
		this.config = config;
		this.page_libre = new ArrayList<ArrayList<Integer>>();
	}
	
	
	public PageID AllocPage() {
		int i=0;																						// comptage de l'indice de page
		int y = 0;																						//compte de l'indice du fichier
		int Fx;
		int indicePage = 0;
		boolean verification = false;
		
		while((y<page_libre.size()) && verification) {													//verifie pour chaque fichier
			while ((i<page_libre.get(y).size()) && verification) {										// verfifie le tableau des pages vide => 1 si rempli 0 si vide
				if (page_libre.get(y).get(i)== 0) {
					indicePage = i;
					i = page_libre.get(i).size();
					verification = true;
					indicePage = i;
					}
			if ((page_libre.size() * config.getPagesize()) < config.getDM_maxfilesize()) {				//verifie si il y a de la place pour créer une autre page
				page_libre.get(y).add(1);
				verification = true;
			}
			y += 1;
		
		}
			
		}	
			if (!verification){																//si aucune page ajoutée jusque la
				page_libre.get(y).add(1);
				indicePage = page_libre.size();
				tailleFichierOccupe += config.getPagesize();								// rajoute la place prise par la nouvelle page dans le fichier
		}
		else {
			page_libre.get(page_libre.size()+1).add(1);										//ajoute un nouveau fichier et une page du style [15] [1] 
			tailleFichierOccupe = 0;														// reinitialise la place occupée pour compter pour le nouveau fichier créé
		}
		
			
		Fx = page_libre.size()*page_libre.get(page_libre.size()-1).size();
		PageID page = new PageID(Fx,indicePage);
		return page;
	}
	
	
	public void ReadPage(PageID pageID, ByteBuffer buff) throws Exception{
		
		
		RandomAccessFile file = new RandomAccessFile(pageID.getNomdePage(),"r");			// ouvre le fichier en mode lecture 
		file.seek(pageID.getFileIdx()*config.getPagesize());								// on se place dans le fichier au niveau de la page
		byte[] Page = new byte[config.getPagesize()];										// on crée le tableau d'octet qui va récupérer les données de la page à lire
		file.readFully(Page);
		
		buff.clear();																		// verifie que le buffer est vide pour recevoir les données
		buff.put(Page);																		// transfere les données du tableau d'octet Page dans le ByteBuffer buff
		buff.flip();																		// assure du bon lancement à la prochaine utilisation de buff (remet le curseur à 0)
		file.close();
	}
	
	
	public void WritePage(PageID pageID, ByteBuffer buff) throws Exception {
		RandomAccessFile file = new RandomAccessFile(pageID.getNomdePage(),"rw");			// ouvre le fichier en mode ecriture
		file.seek(pageID.getFileIdx()*config.getPagesize());
		byte[] Page = new byte[config.getPagesize()];	
		buff.get(Page);																		// transfère les données du ByteBuffer buff dans le tableau d'octet Byte
		buff.flip();
		file.write(Page);
		file.close();
	}
	
	
	public void DeallocPage(PageID pageID) {
		FileWriter writer = new FileWriter(pageID.getNomdePage());
		writer.write("");																		// efface le contenu de la page
		page_libre.get(pageID.getFileIdx()).set(pageID.getPageIdx(), 0);						// donne la valeur 0 à la page situé à l'endroit [FileIdx][PageIdx] dans le tableau des pages libre pour le remettre en utilisable
		writer.close();
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
