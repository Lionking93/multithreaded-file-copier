/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreadedfilecopier.dto.Message;
import leo.multithreadedfilecopier.dto.MessageType;

/**
 *
 * @author Leo Kallonen
 */
public class FileToStackWriter implements Runnable {
    private final String fileName;
    private final BlockingDeque stack;
    private final int stackWriteTimeoutInSeconds;
    
    public FileToStackWriter(String pFileName, BlockingDeque pStack, int pStackWriteTimeoutInSeconds) {
        this.fileName = pFileName;
        this.stack = pStack;
        this.stackWriteTimeoutInSeconds = pStackWriteTimeoutInSeconds;
    }
    
    private void writeMessageToStack(Message charMsg) throws InterruptedException, TimeoutException {
        if (!this.stack.offerFirst(charMsg, this.stackWriteTimeoutInSeconds, TimeUnit.SECONDS)) {
            throw new TimeoutException(
                    String.format("Stack didn't have space for new elements in %d seconds.", 
                            this.stackWriteTimeoutInSeconds));
        }
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName))) { 
            int charNum = br.read();
            
            writeMessageToStack(new Message(MessageType.POISON_PILL));
            
            while (charNum != -1) {
                char nextChar = (char)charNum;
                writeMessageToStack(new Message(MessageType.LETTER, nextChar));
                charNum = br.read();
            }            
        } catch (IOException | InterruptedException | TimeoutException ex) {
            Logger.getLogger(FileToStackWriter.class.getName()).log(Level.SEVERE, "Writing char from source file to stack failed.", ex);
        }        
    }
    
}
