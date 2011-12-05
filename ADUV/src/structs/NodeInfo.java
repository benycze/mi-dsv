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
    
    private int ID;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    @Override
    public String toString() {
        return "[Node ID:"+ID+"]";
    }

    @Override
    public int hashCode() {
        return ID;
    }
}
