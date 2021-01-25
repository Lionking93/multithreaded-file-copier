package leo.multithreadedfilecopier.util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TestUtil {
    
    public static String readTestFileContents(String pFileName) throws FileNotFoundException, IOException {
        String fileContents = "";
        
        try (BufferedReader br = new BufferedReader(new FileReader(pFileName, StandardCharsets.UTF_8))) {
            int charNum = br.read();
            while (charNum != -1) {
                fileContents += (char)charNum;
                charNum = br.read();
            }
        }
        
        return fileContents;
    }
}