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
    
    /**
     * Payload of the message
     */
    String payload;

    /**
     * Parameterized constructor
     * @param type - type of the message
     * @param clock - clock value
     * @param color - message color it token is generated
     * @param strPayload - payload message
     */
    public Message(MessageType type, int clock, Color color, String strPayload) {
        this.type = type;
        this.clock = clock;
        this.color = color;
        this.payload = strPayload;
    }

    /**
     * Explicit constructor
     */
    public Message() {
        this.payload = "";
        this.clock = 0;
        this.color = Color.WHITE;
        this.type = MessageType.IDLE;
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

    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ("[Type="+type+";Color="+color+";Payload="+payload+";Clock="+clock+"]");
    }

}
