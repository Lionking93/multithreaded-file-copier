/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package leo.multithreaded.file.copier.dto;

/**
 *
 * @author Omistaja
 */
public class Message {
    private MessageType type;
    private char content;

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

    public void setType(MessageType type) {
        this.type = type;
    }

    public char getContent() {
        return content;
    }

    public void setContent(char content) {
        this.content = content;
    }
}
