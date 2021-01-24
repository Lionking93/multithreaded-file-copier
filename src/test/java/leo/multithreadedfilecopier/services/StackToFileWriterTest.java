/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.services;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import leo.multithreadedfilecopier.dto.Message;
import leo.multithreadedfilecopier.dto.MessageType;
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
        this.filePath = StackToFileWriterTest.class.getProtectionDomain().getCodeSource().getLocation().getPath() + File.separator + "test-file-2.txt";
        this.fileWriter = new StackToFileWriter(filePath, this.stack, 20);
    }

    @Test
    public void testThatCharsAreAddedToStackInCorrectOrder() throws InterruptedException, FileNotFoundException, IOException {
        this.stack.addFirst(new Message(MessageType.POISON_PILL));
        this.stack.addFirst(new Message(MessageType.LETTER, 't'));
        this.stack.addFirst(new Message(MessageType.LETTER, 'e'));
        this.stack.addFirst(new Message(MessageType.LETTER, 's'));
        this.stack.addFirst(new Message(MessageType.LETTER, 't'));
        this.stack.addFirst(new Message(MessageType.LETTER, 'i'));

        this.fileWriter.run();
        
        String fileContents = "";
        
        try (BufferedReader br = new BufferedReader(new FileReader(this.filePath))) {
            int charNum = br.read();
            while (charNum != -1) {
                fileContents += (char)charNum;
                charNum = br.read();
            }
        }
        
        assertEquals("itset", fileContents);
    }
}
