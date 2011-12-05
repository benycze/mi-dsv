/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compute;

import java.rmi.Remote;
import java.rmi.RemoteException;
import structs.Message;

/**
 * Interface for each remote object
 * @author pavel
 */
public interface NodeInterface extends Remote{
    
    //put message into message queue
    public void putMessage(Message message) throws RemoteException;    
    
}
