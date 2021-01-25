/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreadedfilecopier;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreadedfilecopier.commandlineargs.CommandLineArgParseResult;
import leo.multithreadedfilecopier.commandlineargs.CommandLineArgParser;
import leo.multithreadedfilecopier.properties.FileCopierProperties;
import leo.multithreadedfilecopier.properties.PropertyLoader;
import leo.multithreadedfilecopier.services.FileToStackWriter;
import leo.multithreadedfilecopier.services.StackToFileWriter;

/**
 *
 * @author Leo Kallonen
 */
public class FileCopier {
    public static void main(String[] args) {
        CommandLineArgParseResult parsedArgs = CommandLineArgParser.parseCommandLineArgs(args);
        
        if (!parsedArgs.isParseSuccessful()) {
            Logger.getLogger(FileCopier.class.getName()).log(Level.SEVERE, parsedArgs.getErrorText());
            System.exit(1);
        }
        
        FileCopierProperties props = PropertyLoader.readConfig();
        
        BlockingDeque charStack = new LinkedBlockingDeque(props.getStackSize());
        
        Logger.getLogger(FileCopier.class.getName()).log(Level.INFO, String.format("Reading file %s to stack.", parsedArgs.getSourceFile()));
        Logger.getLogger(FileCopier.class.getName()).log(Level.INFO, String.format("Writing to file %s from stack.", parsedArgs.getDestFile()));
                
        FileToStackWriter fileReader = new FileToStackWriter(
                parsedArgs.getSourceFile(),
                charStack,
                props.getStackWriteTimeoutInSeconds());
        
        StackToFileWriter fileWriter = new StackToFileWriter(
                parsedArgs.getDestFile(),
                charStack,
                props.getStackReadTimeoutInSeconds());
        
        fileReader.setErrorHandler((String errorMsg, Exception ex) -> {
            Logger.getLogger(StackToFileWriter.class.getName()).log(Level.SEVERE, errorMsg, ex);
            System.exit(1);
        });
        
        fileWriter.setErrorHandler((String errorMsg, Exception ex) -> {
            Logger.getLogger(FileToStackWriter.class.getName()).log(Level.SEVERE, errorMsg, ex);
            System.exit(1);
        });
        
        Thread readFileThread = new Thread(fileReader, "FileReader");
        Thread writeFileThread = new Thread(fileWriter, "FileWriter");

        readFileThread.start();
        writeFileThread.start();
    }
}
