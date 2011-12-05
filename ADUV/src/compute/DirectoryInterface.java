/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package compute;

import java.rmi.Remote;
import java.rmi.RemoteException;
import structs.NodeInfo;

/**
 * Directory with all nodes (with network)
 * @author pavel
 */
public interface DirectoryInterface extends Remote{
    
    //login into network
    public void login(NodeInfo address) throws RemoteException;
    
    //logout from network
    public void logout(NodeInfo address) throws RemoteException;
    
    //return next free ID
    public int getNextID() throws RemoteException;
    
    public int getNodeCount() throws RemoteException;
    
    //list all nodes
    public NodeInfo[] listNodes() throws RemoteException;
    
    //return string with all nodes
    public String getNodeList() throws RemoteException;
}
