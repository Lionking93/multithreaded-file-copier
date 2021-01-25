/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.services;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import leo.multithreadedfilecopier.dto.Message;
import leo.multithreadedfilecopier.dto.MessageType;
import leo.multithreadedfilecopier.util.TestUtil;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Omistaja
 */
public class StackToFileWriterTest {
    
    private BlockingDeque stack;
    private String filePath;
    private StackToFileWriter fileWriter;
    
    @BeforeEach
    public void initFileReader() {
        this.stack = new LinkedBlockingDeque();
        this.stack.addFirst(new Message(MessageType.POISON_PILL));
        this.filePath = StackToFileWriterTest.class.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + "stack-to-file-writer-test.txt";
        this.fileWriter = new StackToFileWriter(filePath, this.stack, 20);
    }

    @Test
    public void testThatCharsAreAddedToStackInCorrectOrder() throws IOException {
        addStringToStack("testi");

        this.fileWriter.run();
        
        String fileContents = TestUtil.readTestFileContents(this.filePath);
        
        assertEquals("itset", fileContents);
    }
    
    @Test
    public void testThatCopyingWorksForUtf8Chars() throws IOException {
        addStringToStack("ääkkösiä");
        
        this.fileWriter.run();
        
        String fileContents = TestUtil.readTestFileContents(this.filePath);
        
        assertEquals("äisökkää", fileContents);
    }
    
    private void addStringToStack(String str) {        
        for (char c : str.toCharArray()) {
            this.stack.addFirst(new Message(MessageType.LETTER, c));
        }
    }
}
