/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreadedfilecopier.dto.BufferContents;

/**
 *
 * @author Leo Kallonen
 */
public class BufferToFileWriter implements Runnable {
    private final String fileName;
    private final BufferManager stackWaitNotify;
    private BiConsumer<String, Exception> errorHandler;
    
    public void setErrorHandler(BiConsumer<String, Exception> pErrorHandler) {
        this.errorHandler = pErrorHandler;
    }
    
    public BufferToFileWriter(String pFileName, BufferManager pStackWaitNotify) {
        this.fileName = pFileName;
        this.stackWaitNotify = pStackWaitNotify;
        this.errorHandler = (String errorMsg, Exception ex) -> Logger.getLogger(BufferToFileWriter.class.getName()).log(Level.SEVERE, errorMsg, ex);
    }
    
    public BufferToFileWriter(String pFileName, BufferManager pStackWaitNotify, BiConsumer<String, Exception> pErrorHandler) {
        this(pFileName, pStackWaitNotify);
        this.errorHandler = pErrorHandler;
    }

    @Override
    public void run() {
        if (!this.stackWaitNotify.isBuffer()) {
            this.stackWaitNotify.waitForBufferToFill();
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName, StandardCharsets.UTF_8))) {
            while (true) {        
                BufferContents bufferContents = this.stackWaitNotify.getBufferContents();

                bw.write(bufferContents.getContents());
                
                if (bufferContents.isEndOfFileReached()) {
                    System.out.println("End of file reached");
                    break;
                }
                
                this.stackWaitNotify.signalReceiveEnded();
                this.stackWaitNotify.waitForBufferToFill();
                
                System.out.println("Hello there");
            }
        } catch (IOException ex) {
            this.errorHandler.accept("Writing char from stack to dest file failed.", ex);
        }
    }
    
}
