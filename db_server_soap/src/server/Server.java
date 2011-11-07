package server;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

import compute.DBServer;
import db.DBRecord;

import exceptions.DBExistsException;
import exceptions.DBNotFoundException;
import exceptions.DuplicateKeyException;
import exceptions.KeyNotFoundException;

public class Server implements DBServerSoapInterface{
	
	private static final Logger log = Logger.getLogger("LOG"); //nejake logovani
    DBServer serverUI = new DBServerImpl(); //implementace db serveru
    OMFactory fac = OMAbstractFactory.getOMFactory(); //factory
    OMNamespace omNs = fac.createOMNamespace("http://server", "soap"); //namespace

	@Override
	public OMElement createDB(OMElement gname) throws DBExistsException {
		gname.build();
		gname.detach();
		
		//create root
		OMElement root = fac.createOMElement("createDB",omNs);
		
		//parse data (dbName)
		Iterator i = gname.getChildElements();
		OMElement dbNameEle = (OMElement) i.next();
		String dbName = dbNameEle.getText();
		System.out.println("New database name: "+dbName);
		//call server
		boolean retVal = serverUI.createDB(dbName);
		
		String retValS;
		if(retVal){
			retValS = "true";
		}else{
			retValS = "false";
		}
		
		//create tree
		root.addChild(fac.createOMText(retValS));
		
		return root;
	}

	@Override
	public OMElement flush() {
		OMElement root = fac.createOMElement("flush",omNs);
		serverUI.flush();
		return root;
	}

	@Override
	public OMElement get(OMElement gname) throws DBNotFoundException,
			KeyNotFoundException {
		OMElement root = fac.createOMElement("get",omNs);
		
		gname.build();
		gname.detach();
		System.out.println("++++Get command++++\n"+gname);
		//parse data (DB,key)
		Iterator i = gname.getChildElements();
		OMElement dbNameEle = (OMElement) i.next();
		OMElement keyEle = (OMElement) i.next();
	
		//parse data
		String dbName = dbNameEle.getText();
		int key = Integer.parseInt(keyEle.getText());
		System.out.println("-values"+dbName + " " + key);
		//find record
		DBRecord dbr = serverUI.get(dbName, key);
		//String keyS = String.valueOf(dbr.getKey());
		//String mesS = dbr.getMessage();
		//String tcS = dbr.getTscreate();
		//String tmS = dbr.getTsmodify();
		//create tree (key,message,tscreate,tsmodify
		//root.addChild(fac.createOMText(keyS));
		//root.addChild(fac.createOMText(mesS));
		//root.addChild(fac.createOMText(tcS));
		//root.addChild(fac.createOMText(tmS));
		
		//(CSV_rec CSV_rec)
		root.addChild(fac.createOMText(dbr.getCSVRecord()));
		
		return root;
	}

	@Override
	public OMElement getA(OMElement gname) throws DBNotFoundException,
			KeyNotFoundException {
		//tree (db (key key key ..))
		gname.build();
		gname.detach();
		
		//create root
		OMElement root = fac.createOMElement("getA",omNs);
		System.out.println("++++GetA++++\n"+gname);
		
		//parse input
		Iterator iRoot = gname.getChildElements();
		OMElement dbNameEle = (OMElement) iRoot.next();
		System.out.println(dbNameEle);
		OMElement keysEle = (OMElement) iRoot.next();
		System.out.println(keysEle);
		String dbName = dbNameEle.getText();
		System.out.println("-Database name: "+dbName);
		
		ArrayList<Integer> keyList = new ArrayList<Integer>();
		Iterator iKeyEle = keysEle.getChildElements();
		while(iKeyEle.hasNext()){
			//parse key
			OMElement keyEle = (OMElement) iKeyEle.next();
			String convStr = keyEle.getText();
			System.out.println("-trying to convert "+convStr+" on integer from "+keyEle);
			int keyVal = Integer.parseInt(convStr);
			keyList.add(keyVal);
		}
		
		//get key array
		Integer[] keyArray = new Integer[keyList.size()];
		for(int i=0;i<keyArray.length;i++){
			keyArray[i] = keyList.get(i);
		}
		
		DBRecord[] dbRecs = serverUI.getA(dbName, keyArray);
		
		//create tree - ((key message tsc tsm) (key message tsc tsm) ..)
		for(DBRecord dbr : dbRecs){
			String csvRec = dbr.getCSVRecord();
			OMElement keyVal = fac.createOMElement("value",omNs);
			keyVal.addChild(fac.createOMText(keyVal,csvRec));
			root.addChild(keyVal);
		}
		
		System.out.println("-Sending: "+root);
		
		//..return tree with records
		return root;
	}

	@Override
	public OMElement insert(OMElement gname) throws DBNotFoundException,
			DuplicateKeyException {
		
		System.out.println("++++ insert called ++++");
		System.out.println(gname);
		gname.build();
		gname.detach();
		System.out.println(gname);
		//create root
		OMElement root = fac.createOMElement("insert",omNs);

		//parse data(dbName,key,message)
		Iterator i = gname.getChildElements();
		OMElement dbNameEle = (OMElement) i.next();
		System.out.println(dbNameEle);
		OMElement keyEle = (OMElement) i.next();
		System.out.println(keyEle);
		OMElement mesEle = (OMElement) i.next();
		System.out.println(mesEle);
		
		String dbName = dbNameEle.getText();
		int key = Integer.parseInt(keyEle.getText());
		String message = mesEle.getText();
		
		int retVal = serverUI.insert(dbName, key, message);
		//create tree
		root.addChild(fac.createOMText(root,Integer.toString(retVal)));
		root.addChild(root);
		
		return root;
	}

	@Override
	public OMElement listDB() {
		OMElement root = fac.createOMElement("values",omNs);
		//OMElement val = fac.createOMElement("values",omNs);
		
		System.out.println("List db called:");
		//(db1 db2 db3 ..)
		String[] dbNames = serverUI.listDB();
		//napl uzel s potomkama
		for(String dbName:dbNames){
			root.addChild(fac.createOMText(root,dbName));
			System.out.println("\t--> "+dbName);
		}
		//root.addChild(val);
		
		System.out.println(root);
		//.. a vrat cely uzel
		return root;
	}

	@Override
	public OMElement update(OMElement gname) throws DBNotFoundException,
			KeyNotFoundException {
		gname.build();
		gname.detach();
		
		//create root
		OMElement root = fac.createOMElement("update",omNs);
		
		//parse data (dbName key message)
		Iterator i = gname.getChildElements();
		OMElement dbNameEle = (OMElement) i.next();
		OMElement keyEle = (OMElement) i.next();
		OMElement messEle = (OMElement) i.next();
		
		String dbName = dbNameEle.getText();
		int key = Integer.parseInt(keyEle.getText());
		String message = messEle.getText();
		
		//call server method
		int retVal = serverUI.update(dbName, key, message);
		
		//create return tree ( int )
		root.addChild(fac.createOMText(Integer.toString(retVal)));
		
		return root;
	}

	
}
