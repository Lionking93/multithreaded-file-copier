/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.services;

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
public class FileToStackWriterTest {
    
    private BlockingDeque stack;
    private FileToStackWriter fileReader;
    
    @BeforeEach
    public void initFileReader() {
        this.stack = new LinkedBlockingDeque();
        String filePath = getClass().getClassLoader().getResource("test-file-1.txt").getPath();
        this.fileReader = new FileToStackWriter(filePath, this.stack, 20);
    }

    @Test
    public void testThatCharsAreAddedToStackInCorrectOrder() throws InterruptedException {
        this.fileReader.run();
        assertEquals('i', ((Message)this.stack.takeFirst()).getContent());
        assertEquals('t', ((Message)this.stack.takeFirst()).getContent());
        assertEquals('s', ((Message)this.stack.takeFirst()).getContent());
        assertEquals('e', ((Message)this.stack.takeFirst()).getContent());
        assertEquals('t', ((Message)this.stack.takeFirst()).getContent());
        assertEquals(MessageType.POISON_PILL, ((Message)this.stack.takeFirst()).getType());
    }
}
