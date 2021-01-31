package leo.multithreadedfilecopier.services;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.logging.Level;
import java.util.logging.Logger;
import leo.multithreadedfilecopier.dto.BufferContents;
import leo.multithreadedfilecopier.dto.Message;
import leo.multithreadedfilecopier.dto.MessageType;

public class BufferManager {
    private final Deque<Message> buffer;
    private boolean timeToTransfer = true;
    private boolean timeToReceive = false;
    private final int bufferMaxSize;
    
    public BufferManager(int pBufferMaxSize) {
        this.buffer = new ArrayDeque();
        this.bufferMaxSize = pBufferMaxSize;
    }
    
    public boolean isBuffer() {
        synchronized(this.buffer) {
            return this.buffer.size() == this.bufferMaxSize;
        }
    }
    
    public boolean writeMessageToBuffer(Message charMsg) throws InterruptedException {
        synchronized(this.buffer) {
            if (isBuffer()) {
                return false;
            }
            
            return this.buffer.offerFirst(charMsg);
        }
    }
    
    public BufferContents getBufferContents() {
        synchronized(this.buffer) {            
            String stackContentString = "";
            boolean endOfFileReached = false;

            while (!this.buffer.isEmpty()) {
                Message msg = this.buffer.pollFirst();

                if (msg.getType() == MessageType.POISON_PILL) {
                    endOfFileReached = true;
                } else if (msg.getType() == MessageType.LETTER) {
                    stackContentString = msg.getContent() + stackContentString;
                }
            }

            return new BufferContents(stackContentString, endOfFileReached);
        }
    }
    
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
    
    public void signalReceiveEnded() {
        synchronized(this.buffer) {
            this.timeToReceive = false;
            this.timeToTransfer = true;
            this.buffer.notify();
        }
    }
    
    public void signalTransferEnded() {
        synchronized(this.buffer) {
            this.timeToReceive = true;
            this.timeToTransfer = false;
            this.buffer.notify();
        }
    }
}