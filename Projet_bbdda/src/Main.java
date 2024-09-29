import java.io.IOException;

public class Main {
	public static void main(String[]args) throws IOException {
		DBConfig FichTest = DBConfig.LoadDBConfig("testTP1.json");
		System.out.println("Dbpath de test : "+ FichTest.getDBpath()+" taille : "+ FichTest.getPagesize()+" taille max "+ FichTest.getDM_maxfilesize());
	}

}
