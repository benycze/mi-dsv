package server;

import org.apache.axiom.om.OMElement;

import db.DBRecord;

import exceptions.DBExistsException;
import exceptions.DBNotFoundException;
import exceptions.DuplicateKeyException;
import exceptions.KeyNotFoundException;

public interface DBServerSoapInterface {

	//public String[] listDB();
	public OMElement listDB();
	
	//public boolean createDB(String dbName) throws DBExistsException;
	public OMElement createDB(OMElement gname) throws DBExistsException;
	
	//public int insert(String dbname, Integer key, String message)
	//		throws DBNotFoundException, DuplicateKeyException;
	public OMElement insert(OMElement gname)
			throws DBNotFoundException, DuplicateKeyException;

	//public int update(String dbname, Integer key, String message)
	//		throws DBNotFoundException, KeyNotFoundException;
	public OMElement update(OMElement gname)
	throws DBNotFoundException, KeyNotFoundException;

	//public DBRecord get(String dbname, Integer key) 
	//		throws DBNotFoundException,	KeyNotFoundException;
	public OMElement get(OMElement gname) 
			throws DBNotFoundException,	KeyNotFoundException;


	//public DBRecord[] getA(String dbname, Integer[] key)
	//		throws DBNotFoundException, KeyNotFoundException;
	public OMElement getA(OMElement gname)
			throws DBNotFoundException, KeyNotFoundException;

	//public boolean flush();
	public OMElement flush();

}
