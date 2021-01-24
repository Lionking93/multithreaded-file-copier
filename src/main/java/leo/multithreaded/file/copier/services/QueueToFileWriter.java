/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreaded.file.copier.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
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
public class QueueToFileWriter implements Runnable {
    private final String fileName;
    private final BlockingQueue queue;
    private final int queueReadTimeoutInSeconds;
    
    public QueueToFileWriter(String pFileName, BlockingQueue pQueue, int pQueueReadTimeoutInSeconds) {
        this.fileName = pFileName;
        this.queue = pQueue;
        this.queueReadTimeoutInSeconds = pQueueReadTimeoutInSeconds;
    }

    @Override
    public void run() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName))) {
            while (true) {
                Message msgFromQueue = (Message)this.queue.poll(this.queueReadTimeoutInSeconds, TimeUnit.SECONDS);

                if (msgFromQueue == null) {
                    throw new TimeoutException(
                            String.format(
                                    "Queue didn't have any content in %d seconds.", 
                                    this.queueReadTimeoutInSeconds));
                }

                if (msgFromQueue.getType() == MessageType.POISON_PILL) {
                    return;
                }

                bw.write(msgFromQueue.getContent());
            }
        } catch (IOException | InterruptedException | TimeoutException ex) {
            Logger.getLogger(QueueToFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
    
}
