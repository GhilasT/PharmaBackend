import java.io.IOException;
import java.nio.ByteBuffer;

public class Main {
	public static void main(String[]args) throws IOException {
		DBConfig FichTest = DBConfig.LoadDBConfig("testTP1.json");
		System.out.println("Dbpath de test : "+ FichTest.getDBpath()+" taille : "+ FichTest.getPagesize()+" taille max "+ FichTest.getDM_maxfilesize());
	
		DBConfig FichTest2 = DBConfig.LoadDBConfig("testTP2.json");
		System.out.println("Dbpath de test 2: "+ FichTest.getDBpath()+" taille : "+ FichTest2.getPagesize()+" taille max "+ FichTest2.getDM_maxfilesize());
		DiskManager DiskTest = new DiskManager(FichTest2);
		PageID p1 = DiskTest.AllocPage();
	}

}
