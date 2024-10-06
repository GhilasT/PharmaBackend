import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;




public class Main {
        public static void main(String[] args) {
            //  le chemin  à changer pour chaque ordi
            DBConfig config = new DBConfig("/Users/Projet_BDDA/src", 4096, 1048576,0,0);
            DiskManager diskManager = new DiskManager(config);

            diskManager.DeallocPage(new PageID(0,1));
            diskManager.DeallocPage(new PageID(0,2));

            try {
                diskManager.SaveState();
                diskManager.LoadState();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

}
