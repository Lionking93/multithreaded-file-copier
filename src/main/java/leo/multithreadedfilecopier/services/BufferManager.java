package leo.multithreadedfilecopier.services;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreadedfilecopier.dto.BufferContents;
import leo.multithreadedfilecopier.dto.Message;
import leo.multithreadedfilecopier.dto.MessageType;

/**
 * This class provides a thread safe interface for adding elements to a buffer stack
 * and reading elements from it. The class will not allow to write to stack if its
 * max size has already been reached.
 * 
 * @author Leo Kallonen
 */
public class BufferManager {
    private final Deque<Message> buffer;
    private boolean timeToTransfer = true;
    private boolean timeToReceive = false;
    private final int bufferMaxSize;
    
    public BufferManager(int pBufferMaxSize) {
        this.buffer = new ArrayDeque();
        this.bufferMaxSize = pBufferMaxSize;
    }
    
    public boolean isBufferFull() {
        synchronized(this.buffer) {
            return this.buffer.size() == this.bufferMaxSize;
        }
    }
    
    public boolean writeMessageToBuffer(Message charMsg) throws InterruptedException {
        synchronized(this.buffer) {
            if (isBufferFull()) {
                return false;
            }
            
            // Stack is implemented by adding and removing elements from the front of the deque.
            return this.buffer.offerFirst(charMsg);
        }
    }
    
    /**
     * This method empties the entire stack buffer and forms a string of the characters
     * that have been read. If element with type POISON_PILL was encountered in stack buffer,
     * then this method also stores the information to the return value that after this buffer read,
     * the file copying has been completed
     * 
     * @return BufferContents Contains the buffer contents as string and information on whether buffer reading can
     * be ended.
     */
    public BufferContents getBufferContents() {
        synchronized(this.buffer) {            
            String stackContentString = "";
            boolean endOfFileReached = false;

            while (!this.buffer.isEmpty()) {
                Message msg = this.buffer.pollFirst();

                if (msg.getType() == MessageType.POISON_PILL) {
                    endOfFileReached = true;
                } else if (msg.getType() == MessageType.LETTER) {
                    // Because characters are in reverse order in stack, the read character always needs to
                    // be put to the front of the string that represents the stack buffer contents.
                    stackContentString = msg.getContent() + stackContentString;
                }
            }

            return new BufferContents(stackContentString, endOfFileReached);
        }
    }
    
    /**
     * BufferToFileWriter object calls this method when it needs to go to wait state to wait
     * that FileToBufferWriter object has added elements to the stack buffer.
     */
    public void waitForBufferToFill() {
        synchronized(this.buffer) {
            while (!this.timeToReceive && this.timeToTransfer) {
                try {
                    this.buffer.wait();
                } catch (InterruptedException e) {
                    Logger.getLogger(BufferManager.class.getName()).log(Level.INFO, String.format("Stack wait failed: %s", e.getLocalizedMessage()));
                }
            }
        }
    }
    
    /**
     * FileToBufferWriter object calls this method when it needs to go to wait state to wait
     * that BufferToFileWriter object reads elements from the stack buffer.
     */
    public void waitForBufferToEmpty() {
        synchronized(this.buffer) {
            while (this.timeToReceive && !this.timeToTransfer) {
                try {
                    this.buffer.wait();
                } catch (InterruptedException e) {
                    Logger.getLogger(BufferManager.class.getName()).log(Level.INFO, String.format("Stack wait failed: %s", e.getLocalizedMessage()));
                }
            }
        }
    }
    
    /**
     * BufferToFileWriter object calls this method to report to FileToBufferWriter object
     * that buffer has been emptied and buffer can be filled again.
     */
    public void signalReceiveEnded() {
        synchronized(this.buffer) {
            this.timeToReceive = false;
            this.timeToTransfer = true;
            this.buffer.notify();
        }
    }
    
    /**
     * FileToBufferWriter object calls this method to report to BufferToFileWriter object
     * that buffer is full and it should be emptied.
     */
    public void signalTransferEnded() {
        synchronized(this.buffer) {
            this.timeToReceive = true;
            this.timeToTransfer = false;
            this.buffer.notify();
        }
    }
}