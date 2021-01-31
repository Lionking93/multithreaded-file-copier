package leo.multithreadedfilecopier.dto;

/**
 * POJO that represents single element that is read from source file and put to buffer.
 * Type can be either letter if a source file character is but to buffer or "poison pill"
 * if all characters have been read from source file and a signal should be sent to buffer
 * reader that reading can end.
 * 
 * @author Leo Kallonen
 */
public class Message {
    private final MessageType type;
    private final char content;

    public Message(MessageType pType) {
        this.type = pType;
        this.content = '-';
    }
    
    public Message(MessageType pType, char pContent) {
        this.type = pType;
        this.content = pContent;
    }
    
    public MessageType getType() {
        return type;
    }

    public char getContent() {
        return content;
    }
}
