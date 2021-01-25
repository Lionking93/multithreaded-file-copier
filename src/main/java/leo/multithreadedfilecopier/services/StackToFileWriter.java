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
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreadedfilecopier.dto.Message;
import leo.multithreadedfilecopier.dto.MessageType;

/**
 *
 * @author Leo Kallonen
 */
public class StackToFileWriter implements Runnable {
    private final String fileName;
    private final BlockingDeque stack;
    private final int stackReadTimeoutInSeconds;
    private BiConsumer<String, Exception> errorHandler;
    
    public void setErrorHandler(BiConsumer<String, Exception> pErrorHandler) {
        this.errorHandler = pErrorHandler;
    }
    
    public StackToFileWriter(String pFileName, BlockingDeque pStack, int pStackReadTimeoutInSeconds) {
        this.fileName = pFileName;
        this.stack = pStack;
        this.stackReadTimeoutInSeconds = pStackReadTimeoutInSeconds;
        this.errorHandler = (String errorMsg, Exception ex) -> Logger.getLogger(StackToFileWriter.class.getName()).log(Level.SEVERE, errorMsg, ex);
    }
    
    public StackToFileWriter(String pFileName, BlockingDeque pStack, int pStackReadTimeoutInSeconds, BiConsumer<String, Exception> pErrorHandler) {
        this(pFileName, pStack, pStackReadTimeoutInSeconds);
        this.errorHandler = pErrorHandler;
    }

    @Override
    public void run() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName, StandardCharsets.UTF_8))) {
            while (true) {
                Message msgFromStack = (Message)this.stack.pollFirst(this.stackReadTimeoutInSeconds, TimeUnit.SECONDS);

                if (msgFromStack == null) {
                    throw new TimeoutException(
                            String.format("Stack didn't have any content in %d seconds.", 
                                    this.stackReadTimeoutInSeconds));
                }

                if (msgFromStack.getType() == MessageType.POISON_PILL) {
                    return;
                }

                bw.write(msgFromStack.getContent());
            }
        } catch (IOException ex) {
            this.errorHandler.accept("Writing char from stack to dest file failed.", ex);
        } catch (TimeoutException ex2) {
            this.errorHandler.accept("No new elements in stack before timeout.", ex2);
        } catch (InterruptedException ex3) {
            this.errorHandler.accept("Interrupted while waiting for new elements to appear in stack.", ex3);
        }  
    }
    
}
