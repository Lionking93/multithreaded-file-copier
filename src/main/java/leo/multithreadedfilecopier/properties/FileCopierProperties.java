package leo.multithreadedfilecopier.properties;

/**
 * POJO object that contains values read from config file.
 * 
 * @author Leo Kallonen
 */
public class FileCopierProperties {
    private int bufferSize = 8192;

    public int getBufferSize() {
        return bufferSize;
    }

    public void getBufferSize(int pBufferSize) {
        this.bufferSize = pBufferSize;
    }
}
