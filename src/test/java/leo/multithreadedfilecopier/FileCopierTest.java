package leo.multithreadedfilecopier;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import leo.multithreadedfilecopier.util.TestUtil;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class FileCopierTest {    
    @Test
    public void testThatTargetFileContentsAreSameAsSourceFileContents() throws IOException {
        String sourceFilePath = getClass().getClassLoader().getResource("test-source-file.txt").getPath();
        String destFilePath = FileCopierTest.class.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + "file-copier-test-1.txt";
        
        String[] filePaths = { sourceFilePath, destFilePath };
        
        FileCopier.main(filePaths);
                
        String destFileContents = TestUtil.readTestFileContents(destFilePath);
        assertEquals("testi", destFileContents);
    }
    
    @Test
    public void testThatTargetFileContentsAreSameAsLongSourceFileContents() throws IOException {
        String sourceFilePath = getClass().getClassLoader().getResource("test-source-file-long.txt").getPath();
        String destFilePath = FileCopierTest.class.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + "file-copier-test-2.txt";
        
        String[] filePaths = { sourceFilePath, destFilePath };
                
        FileCopier.main(filePaths);
                        
        String sourceFileContents = TestUtil.readTestFileContents(sourceFilePath);
        String destFileContents = TestUtil.readTestFileContents(destFilePath);

        assertEquals(sourceFileContents, destFileContents);
    }
    
    @Test
    public void testThatSourceFileIsCopiedProperlyIfPoisonPillDoesNotFitInBuffer() throws IOException {
        String sourceFilePath = getClass().getClassLoader().getResource("test-source-file-poison-pill-does-not-fit.txt").getPath();
        String destFilePath = FileCopierTest.class.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + "file-copier-test-3.txt";
        
        String[] filePaths = { sourceFilePath, destFilePath };
                
        FileCopier.main(filePaths);
                        
        String sourceFileContents = TestUtil.readTestFileContents(sourceFilePath);
        String destFileContents = TestUtil.readTestFileContents(destFilePath);

        assertEquals(sourceFileContents, destFileContents);
    }
    
    @Test
    public void testThatFileWithAnsiEncodingIsCopiedCorrectly() throws IOException {
        String sourceFilePath = getClass().getClassLoader().getResource("test-source-file-ansi.txt").getPath();
        String destFilePath = FileCopierTest.class.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + "file-copier-test-4.txt";
        
        String[] mainArguments = { sourceFilePath, destFilePath, "Cp1252" };
                
        FileCopier.main(mainArguments);
                        
        String destFileContents = TestUtil.readTestFileContents(destFilePath, Charset.forName("Cp1252"));

        assertEquals("ääkkösiä", destFileContents);
    }
    
    @Test
    public void testThatFileCopierThrowsExceptionIfTooFewArguments() {    
        String[] filePaths = { "test.txt" };
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            FileCopier.main(filePaths);
        });        
    }
    
    @Test
    public void testThatFileCopierThrowsExceptionIfTooManyArguments() {
        String[] filePaths = { "test1.txt", "test2.txt", "test3.txt" };
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
           FileCopier.main(filePaths);
        });
    }
    
    @Test
    public void testThatFileCopierExitsCleanlyIfSourceFileDoesNotExist() {
        String destFilePath = FileCopierTest.class.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + "file-copier-test-2.txt";
        
        String[] filePaths = { "höpö-tiedosto.txt", destFilePath };
                
        FileCopier.main(filePaths);
    }
    
    @Test
    public void testThatFileCopierExitsCleanlyIfDestFilePathIsInvalid() {
        String sourceFilePath = getClass().getClassLoader().getResource("test-source-file.txt").getPath();
        String destFilePath = "::/\\";
        
        String[] filePaths = { sourceFilePath, destFilePath };
        
        FileCopier.main(filePaths);
    }
}