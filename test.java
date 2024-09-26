import java.io.File;
import java.io.FileNotFoundException;

public class test {
 public static void main(String[] args) throws FileNotFoundException {
    
    File fichier_config = new File("fichier.txt"); 
            DBConfig config = DBConfig.LoadDBConfig(fichier_config);

}
}