package client;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Iterator;

import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.client.ServiceClient;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.axis2.client.Options;

import exceptions.DBNotFoundException;

public class Client {
	
    private static EndpointReference targetEPR = new EndpointReference("http://localhost:8080/axis2/services/Server");
    private static ServiceClient sender;
    private static OMFactory fac = OMAbstractFactory.getOMFactory();
    private static OMNamespace omNs = fac.createOMNamespace("http://server", "soap");

	public static void main(String args[]) {
		if (args.length < 3) {
			System.out.println("Check input parameters: IP PORT FILE");
		}

		// get parameters
		String IP = args[0];
		int port = Integer.parseInt(args[1]);
		String filePath = args[2];

		try {
	        targetEPR = new EndpointReference("http://" + IP + ":" + port + "/axis2/services/Server");
	        Options options = new Options();
	        options.setTo(targetEPR);
	        
	        sender = new ServiceClient();
	        sender.setOptions(options);
			// ////////////////////////////////////////
			parseInput(filePath);
		} catch (AxisFault e) {
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
	private static void parseInput(String path) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line;
			while ((line = br.readLine()) != null) {
				// for all lines
				if (line.length() != 0 && !line.equals("")) {
					line = line.replaceAll("\"", "");
					parseCommand(line);
				}
			}
			
			//flush
			OMElement method = fac.createOMElement("flush",omNs);
			OMElement ret = sender.sendReceive(method);
			ret.build();
			ret.detach();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Parse line of the command
	 * 
	 * @param line
	 */
	private static void parseCommand(String line) {
		String[] parsedCSV = line.split(";");
		// ///////////////////////////////////////
		if (parsedCSV.length == 0)
			return;
		// ///////////////////////////////////////
		String cmd = parsedCSV[0];
		try {
			if (cmd.equals("listdb")) {
				listDB();
			} else if (cmd.equals("insert")) {
				String db = parsedCSV[1];
				int key = Integer.parseInt(parsedCSV[2]);
				String message = parsedCSV[3];
				insertCmd(db, key, message);
			} else if (cmd.equals("update")) {
				String db = parsedCSV[1];
				int key = Integer.parseInt(parsedCSV[2]);
				String message = parsedCSV[3];
				updateCmd(db, key, message);
			} else if (cmd.equals("get")) {
				String db = parsedCSV[1];
				int key = Integer.parseInt(parsedCSV[2]);
				getCmd(db, key);
			} else if (cmd.equals("getA")) {
				String db = parsedCSV[1];
				String keyStr = parsedCSV[2];
				String[] keys = keyStr.split(",");
				Integer[] keyArr = new Integer[keys.length];
				for (int i = 0; i < keys.length; i++) {
					keyArr[i] = Integer.parseInt(keys[i]);
				}
				getACmd(db, keyArr);
			} else if (cmd.equals("createdb")) {
				String db = parsedCSV[1];
				createDB(db);
			} else {
				System.out.println(">> ERROR - command " + "\"" + cmd + "\""
						+ " not implemented.");
			}
		} catch (AxisFault ex) {
			String mess = ex.getMessage();
			System.out.println(mess);
		}
	}

	private static void getACmd(String db, Integer[] keys) throws AxisFault {
		System.out.print(">> get records with keys \"");
		for (int i = 0; i < keys.length - 1; i++) {
			System.out.print(keys[i] + ",");
		}
		System.out.println(keys[keys.length - 1] + "\" from database \'" + db
				+ "\'");

		//create tree
		OMElement method = fac.createOMElement("getA",omNs);
		OMElement gname = fac.createOMElement("gname",omNs);
		OMElement dbEle = fac.createOMElement("db",omNs);
		OMElement keysEle = fac.createOMElement("keys",omNs);
		
		method.addChild(gname);
		dbEle.addChild(fac.createOMText(dbEle,db));
		gname.addChild(dbEle);
		gname.addChild(keysEle);
		
		for(Integer key: keys){
			OMElement keySum = fac.createOMElement("key",omNs);
			keySum.addChild(fac.createOMText(keySum,Integer.toString(key)));
			keysEle.addChild(keySum);
		}
		
		System.out.println(method);
		OMElement retVal = sender.sendReceive(method);
		retVal.build();
		retVal.detach();
		Iterator i = retVal.getFirstElement().getFirstElement().getChildElements();
		
		while (i.hasNext()) {
			OMElement csvResEle = (OMElement) i.next();
			String[] csv = csvResEle.toString().toString().replaceAll("\"", "").split(";");
			
			String key = csv[2];
			String message = csv[3];
			
			System.out.println("<<< record from database \"" + db
					+ "\" with key \"" + key + "\" --> [ \""
					+ message + "\" ]");
		}
	}

	/**
	 * Create database
	 * 
	 * @param db
	 *            database name
	 * @param dbs
	 *            database server object
	 * @throws AxisFault 
	 * @throws XmlRpcException
	 */
	private static void createDB(String db) throws AxisFault {
		OMElement method = fac.createOMElement("createDB",omNs);
		OMElement nameEle = fac.createOMElement("dbName",omNs);
		OMElement gname = fac.createOMElement("gname",omNs);
		
		nameEle.addChild(fac.createOMText(nameEle,db));
		gname.addChild(nameEle);
		method.addChild(gname);
		
		OMElement retEle = sender.sendReceive(method);
		retEle.build();
		retEle.detach();
		
		//System.out.println(retEle);
		System.out.println(">> Creating database - \""+db+"\"");
		OMElement revValEle = (OMElement) retEle.getChildElements().next();
		String text = revValEle.getText();
		int retVal = Integer.parseInt(text);
		System.out.println("<< Database \""+db+"\" created.");
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
	 * @throws AxisFault 
	 * @throws XmlRpcException
	 * @throws RemoteException
	 */
	private static void getCmd(String db, int key) throws AxisFault {
		System.out.println(">> get record with key \"" + key
				+ "\" from database \'" + db + "\'");
		//create tree (db,key)
		OMElement method = fac.createOMElement("get",omNs);
		OMElement gname = fac.createOMElement("gname",omNs);
		OMElement dbEle = fac.createOMElement("db",omNs);
		OMElement keyEle = fac.createOMElement("key",omNs);
		
		method.addChild(gname);
		gname.addChild(dbEle);
		gname.addChild(keyEle);
		
		dbEle.addChild(fac.createOMText(dbEle,db));
		keyEle.addChild(fac.createOMText(keyEle,Integer.toString(key)));
		
		OMElement retValEle = sender.sendReceive(method);
		OMElement csvResEle = (OMElement) retValEle.getFirstElement().getFirstElement();
		//System.out.println(csvResEle);
		
		String[] csv = csvResEle.getText().replaceAll("\"", "").split(";");
		
		System.out.println("<< record from database \"" + db
				+ "\" with key \"" + key + "\" --> [ \"" + csv[3] + "\" ]");

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
	 * @throws AxisFault 
	 * @throws RemoteException
	 * @throws XmlRpcException
	 */
	private static void updateCmd(String db, int key, String message) throws AxisFault {
		System.out.println(">> Updating record \"" + key + "\" in database \""
				+ db + "\" with \"" + message + "\"");

		OMElement method = fac.createOMElement("update",omNs);
		OMElement gnameEl = fac.createOMElement("gname",omNs);
		OMElement dbEle = fac.createOMElement("db",omNs);
		OMElement keyEle = fac.createOMElement("key",omNs);
		OMElement messEle = fac.createOMElement("message",omNs);
		
		method.addChild(gnameEl);
		gnameEl.addChild(dbEle);
		gnameEl.addChild(keyEle);
		gnameEl.addChild(messEle);
		
		dbEle.addChild(fac.createOMText(dbEle,db));
		keyEle.addChild(fac.createOMText(keyEle,Integer.toString(key)));
		messEle.addChild(fac.createOMText(messEle,message));
		
		OMElement retValEle = sender.sendReceive(method);
		retValEle.build();
		retValEle.detach();
		System.out.println("<< DB \""+db+"\" - record updated.");
	}

	/**
	 * Inserts new record into database
	 * 
	 * @param db
	 * @param key
	 * @param message
	 * @param dbs
	 * @throws AxisFault 
	 * @throws RemoteException
	 * @throws XmlRpcException
	 */
	private static void insertCmd(String db, int key, String message) throws AxisFault {

		System.out.println(">> Inserting into database \"" + db + "\" record[ "
				+ message + " ]");

		OMElement method = fac.createOMElement("insert",omNs);
		OMElement gname = fac.createOMElement("gname",omNs);
		OMElement dbEle = fac.createOMElement("db",omNs);
		OMElement keyEle = fac.createOMElement("key",omNs);
		OMElement messEle = fac.createOMElement("message",omNs);
		
		dbEle.addChild(fac.createOMText(dbEle, db));
		keyEle.addChild(fac.createOMText(keyEle,Integer.toString(key)));
		messEle.addChild(fac.createOMText(messEle,message));
		
		gname.addChild(dbEle);
		gname.addChild(keyEle);
		gname.addChild(messEle);
		method.addChild(gname);
		
		//System.out.println(method);

		OMElement retValEle = sender.sendReceive(method);
		retValEle.build();
		retValEle.detach();
		
		System.out.println(retValEle);
		
		OMElement retEle = (OMElement) retValEle.getFirstElement().getFirstElement();
		//System.out.println(retEle);
		String retText = retEle.getText();
		//System.out.println(retText);
		//int retVal = Integer.parseInt(retText);
		System.out.println("Database \""+db+"\" - record inserted");
	}

	/**
	 * Print list db command
	 * 
	 * @param dbs
	 * @throws AxisFault 
	 * @throws XmlRpcException
	 */
	private static void listDB() throws AxisFault {
		System.out.println(">> listdb");
		OMElement method = fac.createOMElement("listDB",omNs);
		OMElement result = sender.sendReceive(method);
		result.build();
		result.detach();
		
		Iterator i = result.getFirstElement().getChildElements();
		
		StringBuilder strb = new StringBuilder();
		strb.append("<< Databases:");
		while (i.hasNext()) {
			OMElement dbNameEle = (OMElement) i.next();
			if(!dbNameEle.toString().equals(""))
				strb.append("'" + dbNameEle.getText() + "' ");
		}
		System.out.println(strb.toString());
	}

}
