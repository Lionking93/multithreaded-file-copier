# Multithreaded file copier

This project contains an implementation that copies files in two threads. One thread reads source file one character at a time to a fixed-length stack buffer. When buffer
is full, the buffer writer thread wakes up the buffer reader thread and goes to waiting state. The buffer reader thread then reads all contents from the buffer
and writes them to a destination file. When buffer is empty, buffer reader thread wakes up the buffer writer thread and goes to waiting state. This continues until
end of source file is reached.

## How to install?

This project has been developed using Java version 12.0.1 and Maven version 3.6.3. To install this project, you should go to its root folder and run command

**mvn clean install**

After this, the target folder should contain the build .jar file.

## How to use?

This tool is run from the command line. It takes three parameters of which the first two are mandatory and the third is optional. First parameter is the path source file that should
be copied and the second paramter is the path where destination file should be written. Third parameter is the character encoding to use, default is UTF-8.

### Example 1

The following command copies a text file named source_text-file.txt in the same directory as the project jar to the same folder with name destination_text-file.txt
using default encoding UTF-8.

**java -jar multithreaded-file-copier-1.0-SNAPSHOT.jar source_text-file.txt destination_text-file.txt**

### Example 2

The following command copies a text file named source_text-file.txt in the same directory as the project jar to a folder named target_dir with destination file name 
destination_text-file.txt. Cp1252 encoding is specified.

**java -jar multithreaded-file-copier-1.0-SNAPSHOT.jar source_text-file.txt target_dir/destination_text-file.txt Cp1252**
