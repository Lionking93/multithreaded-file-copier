package leo.multithreadedfilecopier.dto;

/**
 * POJO for storing results when full buffer is read. Contents variable contains
 * the string that is formed when each character in the buffer is read. EndOfFileReached
 * is true if buffer also contained the so called "poison pill", an element that signals
 * that all characters have been read from the source file into buffer.
 * 
 * @author Leo Kallonen
 */
public class BufferContents {
    private final String contents;
    private final boolean endOfFileReached;
    
    public BufferContents(String pContents, boolean pEndOfFileReached) {
        this.contents = pContents;
        this.endOfFileReached = pEndOfFileReached;
    }
    
    public String getContents() {
        return this.contents;
    }
    
    public boolean isEndOfFileReached() {
        return this.endOfFileReached;
    }
}