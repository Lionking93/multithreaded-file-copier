/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreaded.file.copier;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import leo.multithreaded.file.copier.services.FileToQueueWriter;
import leo.multithreaded.file.copier.services.QueueToFileWriter;

/**
 *
 * @author Omistaja
 */
public class FileCopier {
    public static void main(String[] args) {
        BlockingQueue charQueue = new LinkedBlockingQueue(20);
        FileToQueueWriter fileReader = new FileToQueueWriter("test.txt", charQueue, 60);
        QueueToFileWriter fileWriter = new QueueToFileWriter("testi2.txt", charQueue, 60);
        
        Thread readFileThread = new Thread(fileReader, "FileReader");
        Thread writeFileThread = new Thread(fileWriter, "FileWriter");

        readFileThread.start();
        writeFileThread.start();
    }
}
