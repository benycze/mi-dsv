package client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.rmi.RemoteException;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import org.apache.xmlrpc.client.XmlRpcCommonsTransportFactory;
import org.apache.xmlrpc.client.XmlRpcSunHttpTransportFactory;

import db.DBRecord;

public class Client {

	public static void main(String args[]) {
		if (args.length < 3) {
			System.out.println("Check input parameters: IP PORT FILE");
		}

		// get parameters
		String IP = args[0];
		int port = Integer.parseInt(args[1]);
		String filePath = args[2];

		try {
			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			// konfigurace
			InetAddress addr = InetAddress.getByName(IP);
			config.setServerURL(new URL("http://" + addr.getHostAddress() + ":"
					+ port)); // nastaveni adresy serveru
			config.setEnabledForExtensions(true); // true -> pridani extensions
													// - odkloneni od standardu!
			config.setConnectionTimeout(60 * 1000); // nastaveni timeoutu
			config.setReplyTimeout(60 * 1000); // nastaveni reply timeoutu
			// vytvoreni klienta
			XmlRpcClient client = new XmlRpcClient();
			// use Commons HttpClient as transport
			// client.setTransportFactory(new
			// XmlRpcCommonsTransportFactory(client));
			client
					.setTransportFactory(new XmlRpcSunHttpTransportFactory(
							client));
			// set configuration
			client.setConfig(config);
			// ////////////////////////////////////////
			parseInput(filePath, client);
		} catch (XmlRpcException e) {
			System.err.println("Data exception: " + e.getMessage());
		} catch (Exception e) {
			// neco je spatne :(
			System.err.println("Data exception: " + e.getMessage());
		}
	}

	/**
	 * Parse input
	 * 
	 * @param string
	 * @throws XmlRpcException
	 */
	private static void parseInput(String path, XmlRpcClient client)
			throws XmlRpcException {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null) {
				// for all lines
				if (line.length() != 0 && !line.equals("")) {
					line = line.replaceAll("\"", "");
					parseCommand(line, client);
				}
			}
			Object[] parm = {};
			client.execute("db.flush", parm);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Parse line of the command
	 * 
	 * @param line
	 */
	private static void parseCommand(String line, XmlRpcClient client) {
		String[] parsedCSV = line.split(";");
		// ///////////////////////////////////////
		if (parsedCSV.length == 0)
			return;
		// ///////////////////////////////////////
		String cmd = parsedCSV[0];
		try {
			if (cmd.equals("listdb")) {
				listDB(client);
			} else if (cmd.equals("insert")) {
				String db = parsedCSV[1];
				int key = Integer.parseInt(parsedCSV[2]);
				String message = parsedCSV[3];
				insertCmd(db, key, message, client);
			} else if (cmd.equals("update")) {
				String db = parsedCSV[1];
				int key = Integer.parseInt(parsedCSV[2]);
				String message = parsedCSV[3];
				updateCmd(db, key, message, client);
			} else if (cmd.equals("get")) {
				String db = parsedCSV[1];
				int key = Integer.parseInt(parsedCSV[2]);
				getCmd(db, key, client);
			} else if (cmd.equals("getA")) {
				String db = parsedCSV[1];
				String keyStr = parsedCSV[2];
				String[] keys = keyStr.split(",");
				Integer[] keyArr = new Integer[keys.length];
				for (int i = 0; i < keys.length; i++) {
					keyArr[i] = Integer.parseInt(keys[i]);
				}
				getACmd(db, keyArr, client);
			} else if (cmd.equals("createdb")) {
				String db = parsedCSV[1];
				createDB(db, client);
			} else {
				System.out.println(">> ERROR - command " + "\"" + cmd + "\""
						+ " not implemented.");
			}
		} catch (XmlRpcException ex) {
			String mess = ex.getMessage();
			System.out.println("<< " + mess);
		}
	}

	private static void getACmd(String db, Integer[] keys, XmlRpcClient dbs)
			throws XmlRpcException {
		System.out.print(">>> get records with keys \"");
		for (int i = 0; i < keys.length - 1; i++) {
			System.out.print(keys[i] + ",");
		}
		System.out.println(keys[keys.length - 1] + "\" from database \'" + db
				+ "\'");
		Object[] parm = { db, keys };
		Object[] recordsTmp = (Object[]) dbs.execute("db.getA", parm);
		for (Object dbrTmp : recordsTmp) {
			DBRecord dbr = (DBRecord) dbrTmp;
			System.out.println("<<< record from database \"" + db
					+ "\" with key \"" + dbr.getKey() + "\" --> [ \""
					+ dbr.getMessage() + "\" ]");
		}
	}

	/**
	 * Create database
	 * 
	 * @param db
	 *            database name
	 * @param dbs
	 *            database server object
	 * @throws XmlRpcException
	 */
	private static void createDB(String db, XmlRpcClient dbs)
			throws XmlRpcException {
		Object[] parm = { db };
		dbs.execute("db.createDB", parm);
	}

	/**
	 * Returns DBRecord from database
	 * 
	 * @param db
	 *            database name
	 * @param key
	 *            record key
	 * @param dbs
	 *            database server object
	 * @throws XmlRpcException
	 * @throws RemoteException
	 */
	private static void getCmd(String db, int key, XmlRpcClient dbs)
			throws XmlRpcException {
		System.out.println(">>> get record with key \"" + key
				+ "\" from database \'" + db + "\'");
		Object[] parm = { db, key };
		DBRecord dbRec = (DBRecord) dbs.execute("db.get", parm);
		System.out.println("<<< record from database \"" + db
				+ "\" with key \"" + key + "\" --> [ \"" + dbRec.getMessage() + "\" ]");

	}

	/**
	 * Update command
	 * 
	 * @param db
	 *            - database name
	 * @param key
	 *            - key of the record
	 * @param message
	 *            - new message
	 * @param dbs
	 *            - dbs object
	 * @throws RemoteException
	 * @throws XmlRpcException
	 */
	private static void updateCmd(String db, int key, String message,
			XmlRpcClient dbs) throws XmlRpcException {
		System.out.println(">> Updating record \"" + key + "\" in database \""
				+ db + "\" with \"" + message + "\"");
		Object[] parm = { db, key, message };
		dbs.execute("db.update", parm);
	}

	/**
	 * Inserts new record into database
	 * 
	 * @param db
	 * @param key
	 * @param message
	 * @param dbs
	 * @throws RemoteException
	 * @throws XmlRpcException
	 */
	private static void insertCmd(String db, int key, String message,
			XmlRpcClient dbs) throws XmlRpcException {

		System.out.println(">> Inserting into database \"" + db + "\" record[ "
				+ message + " ]");

		Object[] parm = { db, key, message };
		dbs.execute("db.insert", parm);

	}

	/**
	 * Print list db command
	 * 
	 * @param dbs
	 * @throws XmlRpcException
	 */
	private static void listDB(XmlRpcClient dbs) throws XmlRpcException {
		System.out.println(">> listdb");
		Object[] parm = {};
		Object[] dbListO = (Object[]) dbs.execute("db.listDB", parm);
		StringBuilder strb = new StringBuilder();
		strb.append("<< Databases:");

		for (Object dbName : dbListO) {
			strb.append("'" + dbName + "' ");
		}
		System.out.println(strb.toString());
	}

}
