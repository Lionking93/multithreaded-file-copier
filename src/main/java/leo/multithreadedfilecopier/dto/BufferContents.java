package leo.multithreadedfilecopier.dto;

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