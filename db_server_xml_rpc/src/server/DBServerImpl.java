package server;

import java.io.File;
import java.io.FilenameFilter;
import java.rmi.RemoteException;
import java.util.ArrayList;

import compute.DBServer;
import db.DB;
import db.DBRecord;
import exceptions.DBExistsException;
import exceptions.DBNotFoundException;
import exceptions.DuplicateKeyException;
import exceptions.KeyNotFoundException;

public class DBServerImpl implements DBServer {

	// pro potrebu serializace (marshalingu)
	private static final long serialVersionUID = -1937161867341487386L;

	/**
	 * List of databases
	 */
	private ArrayList<DB> dbList;

	/**
	 * Default constructor
	 * 
	 * @throws DBNotFoundException
	 */
	public DBServerImpl() {
		this.dbList = new ArrayList<DB>();
		// /////////////////////////////////
		// Bootstrap databases

		// filter for .dbcsv file
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(".dbcsv");
			}
		};

		File listDir = new File("./");
		File[] listFiles = listDir.listFiles(filter);
		// for all files --> create database
		try {
			for (File file : listFiles) {
				String dbName = file.getName().replaceAll(".dbcsv", "");
				DB database = new DB(dbName);
				database.loadDatabase(); // load database
				this.dbList.add(database);
			}
		} catch (DBNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Create a database with this name
	 */
	@Override
	public boolean createDB(String dbName) throws DBExistsException {
		if (this.dbExists(dbName) != null) {
			// database doesn't exist
			throw new DBExistsException(
					"Database with this name allready exists.");
		} else {
			// create new DB
			DB newDB = new DB(dbName);
			this.dbList.add(newDB);
		}
		return true;
	}

	/**
	 * Returns one record
	 */
	@Override
	public DBRecord get(String dbname, Integer key) throws DBNotFoundException,
			KeyNotFoundException  {
		DB db = null;
		db = this.dbExists(dbname);
		if (db == null)
			throw new DBNotFoundException("Database not found.");
		// here db exists
		DBRecord dbr = db.findDbRec(key);
		if(dbr == null){
			throw new KeyNotFoundException("Key:" + key
					+ " wasn't found in the database.",key);
		}
		
		return dbr;
	}

	@Override
	public DBRecord[] getA(String dbname, Integer[] key)
			throws DBNotFoundException, KeyNotFoundException  {
		DB db = null;
		db = this.dbExists(dbname);
		if (db == null)
			throw new DBNotFoundException("Database not found.");
		// ////////////////////////////
		// here exists database
		int size = key.length;
		DBRecord[] dbRecords = new DBRecord[size];
		for (int i = 0; i < size; i++) {
			int keyNum = key[i];
			DBRecord tmpRec = db.findDbRec(keyNum);
			if(tmpRec == null){
				throw new KeyNotFoundException("Key:" + keyNum
					+ " wasn't found in the database.",keyNum);
			}
			dbRecords[i] = tmpRec;
		}

		return dbRecords;
	}

	@Override
	public int insert(String dbname, Integer key, String message)
			throws DBNotFoundException, DuplicateKeyException  {
		DB db = null;
		db = this.dbExists(dbname);
		if (db == null)
			throw new DBNotFoundException("Database not found.");
		// ///////////////////////////////////
		// db exists --> insert data
		db.insertNewRecord(key, message);
		// TODO - opravit returny
		return 0;
	}

	/**
	 * Returns DB list
	 */
	@Override
	public String[] listDB()  {
		int size = this.dbList.size();
		String[] retStrList = new String[size];
		for (int i = 0; i < size; i++) {
			DB tmpDB = this.dbList.get(i);
			retStrList[i] = tmpDB.getDbName();
		}
		return retStrList;
	}

	/**
	 * Update database record
	 */
	@Override
	public int update(String dbname, Integer key, String message)
			throws DBNotFoundException, KeyNotFoundException  {
		DB db = null;
		db = this.dbExists(dbname);
		if (db == null)
			throw new DBNotFoundException("Database not found.");
		// //////////////////////////////////////////////////////////////////
		db.updateDbRec(key, message);
		// TODO ret hodnoty!!
		return 0;
	}

	/**
	 * Check if the database with dbname exists
	 * 
	 * @param dbname
	 * @return returns DB if exists
	 */
	private DB dbExists(String dbname) {
		DB retval = null;
		// doesn't exist by default
		for (DB db : this.dbList) {
			if (db.getDbName().equals(dbname)) {
				retval = db;
				break;
			}
		}
		return retval;
	}

	/**
	 * Store databases on the disk
	 */
	@Override
	public boolean flush() {
		for (DB database : this.dbList) {
			database.flush();
		}
		return true;
	}
}
