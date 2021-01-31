/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
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
                bufferManager);
        
        BufferToFileWriter fileWriter = new BufferToFileWriter(
                parsedArgs.getDestFile(),
                bufferManager);
        
        Thread readFileThread = new Thread(fileReader, "FileReader");
        Thread writeFileThread = new Thread(fileWriter, "FileWriter");

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
        
        try {
            readFileThread.join();
            writeFileThread.join();
        } catch (InterruptedException e) {
            Logger.getLogger(FileCopier.class.getName()).log(Level.WARNING, "Join throwed InterruptedException.", e);
        }
    }
}
