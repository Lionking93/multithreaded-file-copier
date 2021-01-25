/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.services;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreadedfilecopier.dto.Message;
import leo.multithreadedfilecopier.dto.MessageType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Omistaja
 */
public class FileToStackWriterTest {

    @Test
    public void testThatCharsAreAddedToStackInCorrectOrder() throws InterruptedException {
        BlockingDeque stack = new LinkedBlockingDeque();
        String filePath = getClass().getClassLoader().getResource("test-source-file.txt").getPath();
        
        FileToStackWriter fileReader = new FileToStackWriter(filePath, stack, 20);
        
        fileReader.run();
        assertEquals('i', ((Message)stack.takeFirst()).getContent());
        assertEquals('t', ((Message)stack.takeFirst()).getContent());
        assertEquals('s', ((Message)stack.takeFirst()).getContent());
        assertEquals('e', ((Message)stack.takeFirst()).getContent());
        assertEquals('t', ((Message)stack.takeFirst()).getContent());
        assertEquals(MessageType.POISON_PILL, ((Message)stack.takeFirst()).getType());
    }
}
