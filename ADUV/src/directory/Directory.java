/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package directory;

import compute.DirectoryInterface;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import structs.NodeInfo;
import utils.Log;

/**
 * Base directory with nodes, ID's etc.
 * @author pavel
 */
public class Directory implements DirectoryInterface{
    
    private static final long serialVersionUID = -1937161867341487555L;
    
    /**
     * A number of nodes 
     */
    int nodeCounter;
    List<NodeInfo> nodes;
    
    private Log log;

    public Directory() {
        this.log = new Log("log/directory.txt");
        this.nodeCounter = -1;
        this.nodes = new ArrayList<NodeInfo>();
    }
    
    /////////////////////////////////////////////////////////////
    @Override
    public synchronized void login(NodeInfo address) throws RemoteException {
        nodes.add(address);
        this.log.logDirectory("["+address.getID()+"]New node detected.");
    }

    @Override
    public synchronized int getNextID() throws RemoteException {
        nodeCounter++;
        this.log.logDirectory("Assigning ID="+nodeCounter);
        return nodeCounter;
    }

    @Override
    public NodeInfo[] listNodes() throws RemoteException {
        NodeInfo[] ret = new NodeInfo[nodes.size()];
        for(int i=0;i<nodes.size();i++){
            ret[i] = nodes.get(i);
        }
        return ret;
    }

    @Override
    public String getNodeList() throws RemoteException {
        StringBuilder strb = new StringBuilder();
        for(NodeInfo nodeIn : this.nodes){
            strb.append(nodeIn.toString());
            strb.append("\n");
        }
        
        return strb.toString();
    }

    @Override
    public int getNodeCount() throws RemoteException {
        return this.nodes.size();
    }

    @Override
    public synchronized void logout(NodeInfo address) throws RemoteException {
        for(int i=0;i<this.nodes.size();i++){
            NodeInfo nodeInfo = nodes.get(i);
            if(nodeInfo.getID() == address.getID()){
                //remove it
                this.nodes.remove(i);
                break;
            }
        }
    }
}
