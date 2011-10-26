package server;

import java.net.ServerSocket;
import java.rmi.RMISecurityManager;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

import compute.DBServer;

public class Server {

	public static void main(String[] args) {
		String IP = "127.0.0.1";
		int port = 2010;

		if (args.length == 1) {
			IP = args[0];
		}

		if (args.length == 2) {
			IP = args[0];
			port = Integer.parseInt(args[1]);
		}

		// vytvorime security managera
		// konfigurace je specifikovana bud pomoci
		// -Djava.security.policy=file.policy
		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		// jmeno nasi sluzby
		String name = "DB";

		try {
			// vytvoreni samotneho objektu a jeho stubu
			DBServer dsi = new DBServerImpl();

			// diky tomudle to vyexportuje a ta vec zacne poslouchat na nejakem
			// portu
			DBServer stub = (DBServer) UnicastRemoteObject.exportObject(dsi,
					5000); // start RMI

			// zaregistrovani jmena u objektu --> vytvoreni jmenne sluzby
			Registry registry = LocateRegistry.createRegistry(port);
			registry.rebind(name, stub);
			System.out.println("Starting server on port " + port);
		} catch (Exception e) {
			// neco je spatne :(
			System.err.println("Data exception: " + e.getMessage());
		}
	}
}
