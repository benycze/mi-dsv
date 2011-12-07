/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structs;

import java.io.Serializable;

/**
 * Node information (ID
 * @author pavel
 */
public class NodeInfo implements Serializable{
    
    private static final long serialVersionUID = -1937161867341487111L;
    
    /**
     * Process ID
     */
    private int ID;
 
    /**
     * Where the process Registry is
     */
    private String IP;
    private int port;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    @Override
    public String toString() {
        return "[Node ID:"+ID+";IP:"+IP+":"+port+"]";
    }

    @Override
    public int hashCode() {
        return ID;
    }
}
