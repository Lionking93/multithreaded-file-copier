/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier.services;

import java.io.BufferedReader;
import java.io.FileReader;
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
public class FileToStackWriter implements Runnable {
    private final String fileName;
    private final BlockingDeque stack;
    private final int stackWriteTimeoutInSeconds;
    private BiConsumer<String, Exception> errorHandler;
    
    public void setErrorHandler(BiConsumer<String, Exception> pErrorHandler) {
        this.errorHandler = pErrorHandler;
    }
    
    public FileToStackWriter(String pFileName, BlockingDeque pStack, int pStackWriteTimeoutInSeconds) {
        this.fileName = pFileName;
        this.stack = pStack;
        this.stackWriteTimeoutInSeconds = pStackWriteTimeoutInSeconds;
        this.errorHandler = (String errorMsg, Exception ex) -> Logger.getLogger(StackToFileWriter.class.getName()).log(Level.SEVERE, errorMsg, ex);
    }

    public FileToStackWriter(String pFileName, BlockingDeque pStack, int pStackWriteTimeoutInSeconds, BiConsumer<String, Exception> pErrorHandler) {
        this(pFileName, pStack, pStackWriteTimeoutInSeconds);
        this.errorHandler = pErrorHandler;
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
        try (BufferedReader br = new BufferedReader(new FileReader(this.fileName, StandardCharsets.UTF_8))) { 
            int charNum = br.read();

            writeMessageToStack(new Message(MessageType.POISON_PILL));

            while (charNum != -1) {
                char nextChar = (char)charNum;
                writeMessageToStack(new Message(MessageType.LETTER, nextChar));
                charNum = br.read();
            }            
        } catch (IOException ex) {
            this.errorHandler.accept("Writing char from source file to stack failed.", ex);
        } catch (TimeoutException ex2) {
            this.errorHandler.accept("No free space in stack before timeout.", ex2);
        } catch (InterruptedException ex3) {
            this.errorHandler.accept("Interrupted while waiting for space in stack.", ex3);
        }
    }
    
}
