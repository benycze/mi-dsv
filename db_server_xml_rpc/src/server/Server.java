package server;

import java.net.InetAddress;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

import compute.DBServer;

public class Server {

	public static void main(String[] args) {
		String IP = "127.0.0.1";
        InetAddress addr = null;
        
		int port = 8080;

		if (args.length == 1) {
			IP = args[0];
		}

		if (args.length == 2) {
			IP = args[0];
			port = Integer.parseInt(args[1]);
		}

		try {
			addr = InetAddress.getByName(IP);
			
			WebServer webServer = new WebServer(port,addr);
			// ziskej XML-RPC Server
	        XmlRpcServer xmlRpcServer = webServer.getXmlRpcServer();

	        PropertyHandlerMapping phm = new PropertyHandlerMapping();
	        phm.addHandler("db", DBServerImpl.class);
	        
	        xmlRpcServer.setHandlerMapping(phm);
	        XmlRpcServerConfigImpl serverConfig =
	            (XmlRpcServerConfigImpl) xmlRpcServer.getConfig();
	        serverConfig.setEnabledForExtensions(true);
	        serverConfig.setContentLengthOptional(false);
	        // spust server
	        webServer.start();

			System.out.println("Starting server on port " + port);
		} catch (Exception e) {
			// neco je spatne :(
			System.err.println("Data exception: " + e.getMessage());
		}
	}
}
