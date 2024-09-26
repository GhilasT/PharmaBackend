import java.nio.ByteBuffer;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DiskManager {
	private DBConfig config;
	private  int Xfichier ; // indice du fichier actuel 
	private int   indicPage; // indice de la page dans le fichier actuel
	private int tailleActuFich; // la taille actuel du fichier (octets)
	private ArrayList<PageID> page_vide;

	
	public DiskManager(DBConfig config) {
		this.config = config;
		this.Xfichier =0;
		this.indicPage =0;
		this. tailleActuFich = 0;
		
	}
	
	public PageID AllocPage() {
		//Vérifier qu'on peut ajouter une page au fichier (taille)

		if(!(page_vide.isEmpty)){
			PageID page1= page_vide.get(0);
			page_vide.remove(0)
			return page1;
		}
		else if(this.tailleActuFich + config.getPagesize() > config.getDm_maxfilesize()){
			this.Xfichier ++;
			this.tailleActuFich =0;
			this.indicPage = 0;
		}

		PageID page = new PageID(this.Xfichier, this.indicPage);
		this.indicPage++;
		this.tailleActuFich += this.config.getPagesize();
		return page;

	}

	public void ReadPage(PageID pageId,ByteBuffer buff) {

	}

	public void WritePage(PageID pageId, ByteBuffer buff) {

	}

	public void DeallocPage (PageID pageId) {

	}

	public void SaveState() {

	}
	
	public void LoadState() {
		
	}

}
