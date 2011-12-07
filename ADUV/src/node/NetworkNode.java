package node;

import compute.ADUV_constants;
import compute.DirectoryInterface;
import compute.NodeInterface;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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
    public static void main(String[] args) throws AlreadyBoundException {
        
       //load parameters
        if(args.length != 4){
            System.out.println("Not enough parameters.\n"
                    + "usage: DirIP dirPORT nodeRegIP nodeRegPort");
        }
        
        String dirIP = args[0];
        int dirPort = Integer.parseInt(args[1]);
        
        String nodeRegIP = args[2];
        int nodeRegPort = Integer.parseInt(args[3]);

        System.out.println("Starting NODE program : connecting to "+dirIP+":"+dirPort);
        System.out.println("Starting registry: "+nodeRegIP+":"+nodeRegPort);
        
        new NetworkNode().start(dirIP,dirPort,nodeRegIP,nodeRegPort);
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
    private void start(String dirIp, int dirPort,String nodeRegIP,int nodeRegPort) throws AlreadyBoundException {
        try {
            //1]Get Directory from tracker
            this.registr = LocateRegistry.getRegistry(dirIp, dirPort);
            this.nodeDirectory = (DirectoryInterface)this.registr.lookup(DIRECTORY_RMI_NAME);
            
            //2]Create Node information -> ID,IP,PORT
            NodeInfo nodeInformation = new NodeInfo();
            nodeInformation.setID(this.nodeDirectory.getNextID());
            nodeInformation.setIP(nodeRegIP);
            nodeInformation.setPort(nodeRegPort);
            
            //3]Create node worker core
            this.nodeCore = new NodeCore(nodeInformation);
            
            //4]Export node core & register in directory
            if (System.getSecurityManager() == null)
                System.setSecurityManager(new RMISecurityManager());
            
            Registry nodeRegistry = LocateRegistry.createRegistry(nodeRegPort);
            
            String nodeName = NODE_NAME_PREFIX+nodeInformation.getID();
            nodeRegistry.bind(nodeName, this.nodeCore);
            System.out.println("Node interface exported. Logging into network.");
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
