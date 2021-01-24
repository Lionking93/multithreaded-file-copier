/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreaded.file.copier.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreaded.file.copier.dto.Message;
import leo.multithreaded.file.copier.dto.MessageType;

/**
 *
 * @author Omistaja
 */
public class FileToQueueWriter implements Runnable {
    private final String fileName;
    private final BlockingQueue queue;
    private final int queueWriteTimeoutInSeconds;
    
    public FileToQueueWriter(String pFileName, BlockingQueue pQueue, int pQueueWriteTimeoutInSeconds) {
        this.fileName = pFileName;
        this.queue = pQueue;
        this.queueWriteTimeoutInSeconds = pQueueWriteTimeoutInSeconds;
    }
    
    private void writeMessageToQueue(Message charMsg) throws InterruptedException, TimeoutException {
        if (!this.queue.offer(charMsg, this.queueWriteTimeoutInSeconds, TimeUnit.SECONDS)) {
            throw new TimeoutException(
                    String.format(
                            "Queue didn't have space for new elements in %d seconds.", 
                            this.queueWriteTimeoutInSeconds));
        }
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName))) { 
            int charNum = br.read();
            
            while (charNum != -1) {
                char nextChar = (char)charNum;
                System.out.println(nextChar);
                writeMessageToQueue(new Message(MessageType.LETTER, nextChar));
                charNum = br.read();
            }
            
            writeMessageToQueue(new Message(MessageType.POISON_PILL));
        } catch (IOException | InterruptedException | TimeoutException ex) {
            Logger.getLogger(FileToQueueWriter.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
}
