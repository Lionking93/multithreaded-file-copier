package leo.multithreadedfilecopier.services;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreadedfilecopier.dto.BufferContents;

/**
 * This class contains the functionality to read characters from a stack buffer and write
 * them to new file in an own thread.
 * 
 * @author Leo Kallonen
 */
public class BufferToFileWriter implements Runnable {
    private final String fileName;
    private final BufferManager stackWaitNotify;
    private final Charset destFileEncoding;
    // Not final because needs to be set after initialization
    private BiConsumer<String, Exception> errorHandler;
    
    public void setErrorHandler(BiConsumer<String, Exception> pErrorHandler) {
        this.errorHandler = pErrorHandler;
    }
    
    public BufferToFileWriter(String pFileName, BufferManager pStackWaitNotify, Charset pDestFileEncoding) {
        this.fileName = pFileName;
        this.destFileEncoding = pDestFileEncoding;
        this.stackWaitNotify = pStackWaitNotify;
        this.errorHandler = (String errorMsg, Exception ex) -> Logger.getLogger(BufferToFileWriter.class.getName()).log(Level.SEVERE, errorMsg, ex);
    }

    @Override
    public void run() {
        // Buffer reader thread should go to wait state at the beginning because buffer
        // needs to be filled first
        if (!this.stackWaitNotify.isBufferFull()) {
            this.stackWaitNotify.waitForBufferToFill();
        }
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(this.fileName, this.destFileEncoding))) {
            while (true) {        
                // Buffer contents are read one character at a time and results are combined to a string
                // that is stored in BufferContents object
                BufferContents bufferContents = this.stackWaitNotify.getBufferContents();

                // Write buffer contents to file.
                bw.write(bufferContents.getContents());
                
                // File copying is completed if end of source file is reached, while loop can be
                // exited.
                if (bufferContents.isEndOfFileReached()) {
                    Logger.getLogger(BufferToFileWriter.class.getName(), "End of file reached.");
                    break;
                }
                
                // When buffer is empty, this thread can go to waiting state to wait for the 
                // buffer to fill.
                this.stackWaitNotify.signalReceiveEnded();
                this.stackWaitNotify.waitForBufferToFill();                
            }
        } catch (IOException ex) {
            this.errorHandler.accept("Writing char from stack to dest file failed.", ex);
        }
    }
    
}
