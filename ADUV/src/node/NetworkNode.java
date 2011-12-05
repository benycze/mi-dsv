package node;

import compute.ADUV_constants;
import compute.DirectoryInterface;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import structs.NodeInfo;
import utils.Log;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Main class which starts node
 * @author pavel
 */
public class NetworkNode implements ADUV_constants{

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String IP = "127.0.0.1";
        int port = DIRECTORY_NAMESPACE_PORT;
        
        if(args.length == 1){
            IP = args[1];
        }
        
        if(args.length == 2){
            IP = args[1];
            port = Integer.parseInt(args[2]);
        }

        System.out.println("Starting NODE program ...");
        
        new NetworkNode().start(IP,port);
    }

    //////////////////////////////////////
    //  START                           //
    //////////////////////////////////////
    /**
     * Directory interface
     */
    private DirectoryInterface nodeDirectory;
    
    /**
     * Register with directory
     */
    private Registry registr;
    /**
     * Logging object
     */
    private Log log;
    /**
     * Core object (to export)
     */
    private NodeCore nodeCore;
    
    /**
     * Connect with IP and port
     * @param ip - RMI address
     * @param port - remote port
     */
    private void start(String ip, int port) {
        try {
            //1]Get Directory from tracker
            this.registr = LocateRegistry.getRegistry(ip, port);
            this.nodeDirectory = (DirectoryInterface)this.registr.lookup(DIRECTORY_RMI_NAME);
            
            //2]Create Node information -> ID
            NodeInfo nodeInformation = new NodeInfo();
            nodeInformation.setID(this.nodeDirectory.getNextID());
            
            //3]Create node worker core
            this.nodeCore = new NodeCore(nodeInformation);
            
            //4]Export node core & register in directory
            if (System.getSecurityManager() == null)
                System.setSecurityManager(new RMISecurityManager());
            String nodeName = NODE_NAME_PREFIX+nodeInformation.getID();
            this.registr.rebind(nodeName, this.nodeCore);
            this.nodeDirectory.login(nodeInformation);
            
            
            //5]Create log
            this.log = new Log("log/"+nodeName);
            this.nodeCore.setLog(log);
            
            //6]Set directory & registry
            this.nodeCore.setDirectory(this.nodeDirectory);
            this.nodeCore.setRegistry(registr);
            
            //7]Start node
            this.nodeCore.run();
        } catch (RemoteException ex) {
            System.out.print("ERROR - ");
            ex.printStackTrace();
        } catch (NotBoundException ex){
            System.out.print("ERROR - ");
            ex.printStackTrace();
        }
        
    }
}
