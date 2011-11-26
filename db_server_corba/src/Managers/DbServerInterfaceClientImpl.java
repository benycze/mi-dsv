package Managers;
/*
 * The client implementation is generated by the ORB Studio.
 */
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Properties;

import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import client.Client;

class DbServerInterfaceClientImpl {
	private Generated.DbServerInterface target = null;
	private org.omg.CORBA.ORB orb = null;

	/**
	 * Constructor for DbServerInterfaceClientImpl
	 * 
	 * @throws IOException
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 * @throws InvalidName 
	 */
	public DbServerInterfaceClientImpl() throws IOException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		initORB(null);
	}

	/**
	 * Constructor for DbServerInterfaceClientImpl
	 * 
	 * @throws IOException
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 * @throws InvalidName 
	 * @see java.lang.Object#Object()
	 */
	public DbServerInterfaceClientImpl(String[] args) throws IOException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		initORB(args);
	}

	/**
	 * Initialize ORB.
	 *  
	 * @param args
	 * @throws IOException
	 * @throws InvalidName 
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 */
	public void initORB(String[] args) throws IOException, InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {

		Properties props = System.getProperties();
		props.setProperty("org.omg.CORBA.ORBClass", "com.sun.corba.se.internal.POA.POAORB");
		props.setProperty("org.omg.CORBA.ORBSingletonClass", "com.sun.corba.se.internal.corba.ORBSingleton");

		// Initialize the ORB
		orb = org.omg.CORBA.ORB.init((String[])args, props);

		// ---- Uncomment below to enable Naming Service access. ----
		 org.omg.CORBA.Object ncobj = orb.resolve_initial_references("NameService");
		 NamingContextExt nc = NamingContextExtHelper.narrow(ncobj);
		 org.omg.CORBA.Object obj = nc.resolve_str("MyServerObject");

		//LineNumberReader input = new LineNumberReader(new FileReader("server.ior"));
		//String ior = input.readLine();
		//org.omg.CORBA.Object obj = orb.string_to_object(ior);

		target = Generated.DbServerInterfaceHelper.narrow(obj);		
	}

	/**
	 * Obtain ORB Interface.
	 * 
	 * @return
	 */
	public Generated.DbServerInterface getORBInterface() {
		return target;
	}

	/**
	 * Shutdown ORB.
	 */
	public void shutdown() {
		orb.shutdown(true);
	}

	/**
	 * Test driver for DbServerInterfaceClientImpl.
	 * 
	 * @param args
	 * @throws org.omg.CosNaming.NamingContextPackage.InvalidName 
	 * @throws CannotProceed 
	 * @throws NotFound 
	 * @throws InvalidName 
	 */
	public static void main(String[] args) throws InvalidName, NotFound, CannotProceed, org.omg.CosNaming.NamingContextPackage.InvalidName {
		try {
			DbServerInterfaceClientImpl test = new DbServerInterfaceClientImpl();

			// test.getORBInterface().operation1("A message in the bottle...");
			Generated.DbServerInterface dbri = test.getORBInterface();
			
			Client client = new Client(dbri);
			client.run(args[4]);

			test.shutdown();
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}
