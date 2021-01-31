package leo.multithreadedfilecopier;

import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreadedfilecopier.commandlineargs.CommandLineArgParseResult;
import leo.multithreadedfilecopier.commandlineargs.CommandLineArgParser;
import leo.multithreadedfilecopier.services.BufferManager;
import leo.multithreadedfilecopier.properties.FileCopierProperties;
import leo.multithreadedfilecopier.properties.PropertyLoader;
import leo.multithreadedfilecopier.services.FileToBufferWriter;
import leo.multithreadedfilecopier.services.BufferToFileWriter;

/**
 *
 * @author Leo Kallonen
 */
public class FileCopier {
    public static void main(String[] args) {
        CommandLineArgParseResult parsedArgs = CommandLineArgParser.parseCommandLineArgs(args);
        
        if (!parsedArgs.isParseSuccessful()) {
            Logger.getLogger(FileCopier.class.getName()).log(Level.SEVERE, parsedArgs.getErrorText());
            throw new IllegalArgumentException(parsedArgs.getErrorText());
        }
        
        FileCopierProperties props = PropertyLoader.readConfig();
        
        BufferManager bufferManager = new BufferManager(props.getBufferSize());
        
        FileToBufferWriter fileReader = new FileToBufferWriter(
                parsedArgs.getSourceFile(),
                bufferManager,
                parsedArgs.getFileEncoding());
        
        BufferToFileWriter fileWriter = new BufferToFileWriter(
                parsedArgs.getDestFile(),
                bufferManager,
                parsedArgs.getFileEncoding());
        
        Thread readFileThread = new Thread(fileReader, "FileReader");
        Thread writeFileThread = new Thread(fileWriter, "FileWriter");

        // Add error handler callbacks for threads. If one thread fails, interrupt and stop the other also.
        
        fileReader.setErrorHandler((String errorMsg, Exception ex) -> {
            Logger.getLogger(BufferToFileWriter.class.getName()).log(Level.SEVERE, errorMsg, ex);
            writeFileThread.interrupt();
        });
        
        fileWriter.setErrorHandler((String errorMsg, Exception ex) -> {
            Logger.getLogger(FileToBufferWriter.class.getName()).log(Level.SEVERE, errorMsg, ex);
            readFileThread.interrupt();
        });
        
        readFileThread.start();
        writeFileThread.start();
        
        // Wait for threads to finish.
        try {
            readFileThread.join();
            writeFileThread.join();
        } catch (InterruptedException e) {
            Logger.getLogger(FileCopier.class.getName()).log(Level.WARNING, "Thread.join throwed InterruptedException.", e);
        }
    }
}
