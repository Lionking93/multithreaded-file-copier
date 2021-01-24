/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreaded.file.copier;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreaded.file.copier.commandlineargs.CommandLineArgParseResult;
import leo.multithreaded.file.copier.commandlineargs.CommandLineArgParser;
import leo.multithreaded.file.copier.services.FileToQueueWriter;
import leo.multithreaded.file.copier.services.QueueToFileWriter;

/**
 *
 * @author Omistaja
 */
public class FileCopier {
    public static void main(String[] args) {
        CommandLineArgParseResult parsedArgs = CommandLineArgParser.parseCommandLineArgs(args);
        
        if (!parsedArgs.isParseSuccessful()) {
            Logger.getLogger(FileCopier.class.getName()).log(Level.SEVERE, parsedArgs.getErrorText());
            System.exit(1);
        }
        
        BlockingQueue charQueue = new LinkedBlockingQueue(20);
        FileToQueueWriter fileReader = new FileToQueueWriter(parsedArgs.getSourceFile(), charQueue, 60);
        QueueToFileWriter fileWriter = new QueueToFileWriter(parsedArgs.getDestFile(), charQueue, 60);
        
        Thread readFileThread = new Thread(fileReader, "FileReader");
        Thread writeFileThread = new Thread(fileWriter, "FileWriter");

        readFileThread.start();
        writeFileThread.start();
    }
}
