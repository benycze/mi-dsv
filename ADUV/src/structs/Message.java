/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structs;

import java.io.Serializable;

/**
 * Message for the comunication between nodes.
 * @author pavel
 */
public class Message implements Serializable,MessageTypeInterface,
        TokenColorInterface{
   
    /**
     * Message type variable
     */
    private MessageType type;
    
    /**
     * Message logic clocks
     */
    private int clock;
    
    /**
     * Token color
     */
    private Color color;
    
    
    String payload;

    public Message(MessageType type, int clock, Color color, String strPayload) {
        this.type = type;
        this.clock = clock;
        this.color = color;
        this.payload = strPayload;
    }

    public Message() {
        
    }

    /*
     * Get & Set 
     */
    
    public int getClock() {
        return clock;
    }

    public Color getColor() {
        return color;
    }

    public String getStrPayload() {
        return payload;
    }

    public String getPayload() {
        return payload;
    }

    public MessageType getType() {
        return type;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setClock(int clock) {
        this.clock = clock;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setStrPayload(String payload) {
        this.payload = payload;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ("["+type+";"+color+";"+payload+"]");
    }

}
