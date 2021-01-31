/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreadedfilecopier.dto.Message;
import leo.multithreadedfilecopier.dto.MessageType;

/**
 *
 * @author Leo Kallonen
 */
public class FileToBufferWriter implements Runnable {
    private final String fileName;
    private final BufferManager bufferManager;
    private final Charset sourceFileEncoding;
    // Not final because should be set after initialization
    private BiConsumer<String, Exception> errorHandler;
    
    public void setErrorHandler(BiConsumer<String, Exception> pErrorHandler) {
        this.errorHandler = pErrorHandler;
    }
    
    public FileToBufferWriter(String pFileName, BufferManager pBufferManager, Charset pSourceFileEncoding) {
        this.fileName = pFileName;
        this.bufferManager = pBufferManager;
        this.sourceFileEncoding = pSourceFileEncoding;
        System.out.println(this.sourceFileEncoding.toString());
        this.errorHandler = (String errorMsg, Exception ex) -> Logger.getLogger(FileToBufferWriter.class.getName()).log(Level.SEVERE, errorMsg, ex);
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName, this.sourceFileEncoding))) { 
            int charNum = br.read();
            
            while (charNum != -1 ) {
                if (this.bufferManager.isBuffer()) {
                    this.bufferManager.signalTransferEnded();
                    this.bufferManager.waitForBufferToEmpty();
                }
                
                char nextChar = (char)charNum;
                
                this.bufferManager.writeMessageToBuffer(new Message(MessageType.LETTER, nextChar));
                
                charNum = br.read();   
            }
        } catch (IOException ex) {
            this.errorHandler.accept("Writing char from source file to stack failed.", ex);
        } catch (InterruptedException ex3) {
            this.errorHandler.accept("Interrupted while waiting for space in stack.", ex3);
        }
        
        try {
            while (this.bufferManager.isBuffer()) {
                this.bufferManager.signalTransferEnded();
                this.bufferManager.waitForBufferToEmpty();
            }
            
            this.bufferManager.writeMessageToBuffer(new Message(MessageType.POISON_PILL));
            this.bufferManager.signalTransferEnded();
            Logger.getLogger(FileToBufferWriter.class.getName()).log(Level.INFO, "Whole file has been copied.");
        } catch (InterruptedException ex) {
            this.errorHandler.accept("Interrupted while waiting for space in stack.", ex);
        }
    }
    
}
