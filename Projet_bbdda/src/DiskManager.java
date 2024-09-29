import java.nio.ByteBuffer;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DiskManager {
	private DBConfig config;
	private int compteindiceIdentifiant = 0;
	private ArrayList<ArrayList<Integer>>page_vide;											// tableau 2d [fichier] [numéros de page]
	
	public DiskManager(DBConfig config, ArrayList<Integer> page_vide) {
		this.config = config;
		this.page_vide = new ArrayList<ArrayList<Integer>>();
	}
	
	
	public PageID AllocPage() {
		int i=0;																			// comptage de l'indice de page
		int y = 0;																			//compte de l'indice du fichier
		int Fx = compteindiceIdentifiant +1;
		int indicePage = 0;
		boolean verification = false;
		
		while((y<page_vide.size()) && verification) {															//verifie pour chaque fichier
			while ((i<page_vide.get(y).size()) && verification) {											// verfifie le tableau des pages vide => 1 si rempli 0 si vide
				if (page_vide.get(y).get(i)== 0) {
					indicePage = i;
					i = page_vide.get(i).size();
					verification = true;
					indicePage = i;
					}
			if ((page_vide.size() * config.getPagesize()) < config.getDM_maxfilesize()) {						//verifie si il y a de la place pour créer une autre page
				page_vide.get(y).add(1);
				verification = true;
			}
			y += 1;
		
		}
			
		}	
			if (!verification){																				//si aucune page ajoutée jusque la
				page_vide.get(y).add(1);
				indicePage = page_vide.size();
		}
		else {
			page_vide.get(page_vide.size()+1).add(1);										//ajoute un nouveau fichier et une page du style [15] [1] 
		}
		
			
		this.compteindiceIdentifiant += 1;
		PageID page = new PageID(Fx,indicePage);
		return page;
	}
	
	
	public void ReadPage(PageID pageID, ByteBuffer buff) {
		
	}
	
	
	public void WritePage(PageID pageID, ByteBuffer buff) {
		
	}
	
	
	public void DeallocPage(PageID pageID) {
		
	}
	
	
	public void SaveState() {
		
	}
	
	
	public void LoadState() {
		
	}
}
