package client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import compute.DBServer;
import db.DBRecord;
import exceptions.DBExistsException;
import exceptions.DBNotFoundException;
import exceptions.DuplicateKeyException;
import exceptions.KeyNotFoundException;

public class Client {

	public static void main(String args[]) {
		if (args.length < 3) {
			System.out.println("Check input parameters: IP PORT FILE");
		}

		// get parameters
		String IP = args[0];
		int port = Integer.parseInt(args[1]);
		String filePath = args[2];

		if (System.getSecurityManager() == null)
			System.setSecurityManager(new RMISecurityManager());
		try {
			// vyhledani vzdaleneho objektu
			DBServer dbs;
			String name = "DB";
			// vytazeni objektu ze jmenne sluzby
			Registry registry = LocateRegistry.getRegistry(IP, port);
			dbs = (DBServer) registry.lookup(name); // get database server
			// definice vstupnich promennych
			parseInput(filePath, dbs);
		} catch (Exception e) {
			// neco je spatne :(
			System.err.println("Data exception: " + e.getMessage());
		}
	}

	/**
	 * Parse input
	 * 
	 * @param string
	 */
	private static void parseInput(String path, DBServer dbs) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null) {
				// for all lines
				if (line.length() != 0 && !line.equals("")) {
					line = line.replaceAll("\"", "");
					parseCommand(line, dbs);
				}
			}
			dbs.flush();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Parse line of the command
	 * 
	 * @param line
	 */
	private static void parseCommand(String line, DBServer dbs) {
		String[] parsedCSV = line.split(";");
		// ///////////////////////////////////////
		if (parsedCSV.length == 0)
			return;
		// ///////////////////////////////////////
		String cmd = parsedCSV[0];
		try {
			if (cmd.equals("listdb")) {
				listDB(dbs);
			} else if (cmd.equals("insert")) {
				String db = parsedCSV[1];
				int key = Integer.parseInt(parsedCSV[2]);
				String message = parsedCSV[3];
				insertCmd(db, key, message, dbs);
			} else if (cmd.equals("update")) {
				String db = parsedCSV[1];
				int key = Integer.parseInt(parsedCSV[2]);
				String message = parsedCSV[3];
				updateCmd(db, key, message, dbs);
			} else if (cmd.equals("get")) {
				String db = parsedCSV[1];
				int key = Integer.parseInt(parsedCSV[2]);
				getCmd(db, key, dbs);
			} else if(cmd.equals("getA")){
				String db = parsedCSV[1];
				String keyStr = parsedCSV[2];
				String[] keys = keyStr.split(",");
				Integer[] keyArr = new Integer[keys.length];
				for(int i=0;i<keys.length;i++){
					keyArr[i] = Integer.parseInt(keys[i]);
				}
				getACmd(db,keyArr,dbs);
			} else if (cmd.equals("createdb")) {
				String db = parsedCSV[1];
				createDb(db, dbs);
			} else {
				System.out.println(">> ERROR - command " + "\"" + cmd + "\""
						+ " not implemented.");
			}
		} catch (RemoteException ex) {
			ex.printStackTrace();
		}
	}

	private static void getACmd(String db,Integer[] keys,DBServer dbs) throws RemoteException {
		try{
			System.out.print(">>> get records with keys \"");
			for(int i=0;i<keys.length-1;i++){
				System.out.print(keys[i]+",");
			}
			System.out.println(keys[keys.length-1]+"\" from database \'"+db+"\'");
				
			DBRecord[] records = dbs.getA(db, keys);
			for(DBRecord dbr : records){
				System.out.println("<<< record from database \"" + db
						+ "\" with key \"" + dbr.getKey() + "\" --> [ \"" + dbr.getMessage() + "\" ]");
			}
			
		} catch (DBNotFoundException e) {
			System.out.println(">> ERROR - database " + "'" + db
					+ "' does not exist.");
		} catch (KeyNotFoundException e) {
			System.out.println(">> ERROR - record with key \"" + e.getKey()
					+ "\" does not exist.");
		}
		
	}

	/**
	 * Create database
	 * 
	 * @param db
	 *            database name
	 * @param dbs
	 *            database server object
	 * @throws RemoteException
	 */
	private static void createDb(String db, DBServer dbs)
			throws RemoteException {
		try {
			dbs.createDB(db);
		} catch (DBExistsException ex) {
			System.out.println(">> ERROR - database with name \"" + db
					+ "\" allready exists.");
		}

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
	 * @throws RemoteException
	 */
	private static void getCmd(String db, int key, DBServer dbs)
			throws RemoteException {
		try {
			System.out.println(">>> get record with key \""+key+"\" from database \'"+db+"\'");
			DBRecord dbRec = dbs.get(db, key);
			System.out.println("<<< record from database \"" + db
					+ "\" with key \"" + key + "\" --> [ \"" + dbRec.getMessage() + "\" ]");
		} catch (DBNotFoundException e) {
			System.out.println(">> ERROR - database " + "'" + db
					+ "' does not exist.");
		} catch (KeyNotFoundException e) {
			System.out.println(">> ERROR - record with key \"" + key
					+ "\" does not exist.");
		}
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
	 */
	private static void updateCmd(String db, int key, String message,
			DBServer dbs) throws RemoteException {
		try {
			System.out.println(">> Updating record \"" + key
					+ "\" in database \"" + db + "\" with \"" + message + "\"");
			dbs.update(db, key, message);
		} catch (DBNotFoundException e) {
			System.out.println(">> ERROR - database " + "'" + db
					+ "' does not exist.");
		} catch (KeyNotFoundException e) {
			System.out.println(">> ERROR - record with key \"" + key
					+ "\" does not exist.");
		}

	}

	/**
	 * Inserts new record into database
	 * 
	 * @param db
	 * @param key
	 * @param message
	 * @param dbs
	 * @throws RemoteException
	 */
	private static void insertCmd(String db, int key, String message,
			DBServer dbs) throws RemoteException {
		try {
			System.out.println(">> Inserting into database \"" + db
					+ "\" record[ \"" + message + "\" ]");
			dbs.insert(db, key, message);
		} catch (DBNotFoundException e) {
			System.out.println("<< ERROR - database \"" + db
					+ "\" does not exists.");
		} catch (DuplicateKeyException e) {
			System.out.println("<< ERROR - database record with key " + "\""
					+ key + "\"" + " allready exists.");
		}

	}

	/**
	 * Print list db command
	 * 
	 * @param dbs
	 */
	private static void listDB(DBServer dbs) throws RemoteException {
		System.out.println(">> listdb");
		String[] dbList = dbs.listDB();
		StringBuilder strb = new StringBuilder();
		strb.append("<< Databases:");
		for (String dbName : dbList) {
			strb.append("'" + dbName + "' ");
		}
		System.out.println(strb.toString());
	}

}
