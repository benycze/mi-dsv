package db;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import db.DBRecord;
import exceptions.DBNotFoundException;
import exceptions.DuplicateKeyException;
import exceptions.KeyNotFoundException;

public class DB {
	// TODO --> udelat zamykani zaznamu, kdyz se pouzivaji .. pouzit hashmapu ..
	// to bude stacit, hashovat key

	/**
	 *Array list of records
	 */
	private ArrayList<DBRecord> dbRecList;

	/**
	 * Name of the database
	 */
	String dbName;

	/**
	 * Database path
	 */
	String dbPath;

	/**
	 * Crate new database, with path
	 * 
	 * @param path
	 *            database path
	 * @return database object
	 */
	public DB(String name) {
		this.dbRecList = new ArrayList<DBRecord>();
		this.dbName = name;
		this.dbPath = name + ".dbcsv";
	}

	/**
	 * Find
	 * 
	 * @param key
	 * @return
	 */
	public DBRecord findDbRec(int key) {
		DBRecord retDb = null;
		// binary search
		int low = 0, high = this.dbRecList.size() - 1;

		while (low <= high) {
			final int i = (low + high) >> 1;
			final int v = this.dbRecList.get(i).getKey();

			if (v == key) {
				retDb = this.dbRecList.get(i); // this line does not get covered
												// unless there is a match
				break;
			} else if (v < key)
				low = i + 1;
			else
				// v > key
				high = i - 1;
		}
		
		//wait it the record is locked (for update)
		while(retDb != null && retDb.isLocked());
		
		return retDb;
	}

	/**
	 * Updates DBRecord
	 * 
	 * @param key
	 *            of the dbrecord
	 * @param message
	 *            to set
	 * @return ...
	 * @throws KeyNotFoundException
	 */
	public int updateDbRec(int key, String message) throws KeyNotFoundException {
		int retval = 0;
		DBRecord dbr = this.findDbRec(key);
		if (dbr == null)
			throw new KeyNotFoundException("<< ERROR - Key:" + key
					+ " wasn't found in the database.",key);;
		
		dbr.lockRecord();
		dbr.setMessage(message);
		dbr.unlockRecord();
		
		return retval;
	}

	/**
	 * Write database into the file
	 */
	public void flush() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(this.dbPath));
			DBRecord dbr;
			int listSize = this.dbRecList.size();
			for (int i = 0; i < listSize; i++) {
				dbr = this.dbRecList.get(i);
				String csvRec = dbr.getCSVRecord();
				bw.append(csvRec);
				if (i != listSize - 1) {
					// if not last -> insert new line
					bw.newLine();
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Could not write into file.");
		}
	}

	/**
	 * Creates new db record
	 * 
	 * @param key
	 *            db key
	 * @param message
	 *            db message
	 * @return
	 * @throws DuplicateKeyException
	 */
	public int insertNewRecord(int key, String message)
			throws DuplicateKeyException {
		int retVal = 0;
		// TODO vyplnit retval jak bude potreba
		DBRecord dbr = this.findDbRec(key);
		if (dbr != null)
			throw new DuplicateKeyException(
				"<< ERROR - Key with this value allready exists");
		// --- key doesn't exist
		dbr = new DBRecord(key, message);
		this.dbRecList.add(dbr);
		Collections.sort(this.dbRecList);
		// ---------------------
		return retVal;
	}

	/**
	 * Construct database from the file.
	 * 
	 * @param dbPath
	 * @throws DBNotFoundException
	 */
	public void loadDatabase(String dbPath) throws DBNotFoundException {
		String line;
		try {
			BufferedReader br = new BufferedReader(new FileReader(this.dbPath));
			while ((line = br.readLine()) != null) {
				DBRecord dbr = new DBRecord(line);
				this.dbRecList.add(dbr);
			}
			// sort database
			Collections.sort(this.dbRecList);
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new DBNotFoundException("Could not open :" + dbPath
					+ " database file");
		}
	}

	/**
	 * Load database and use implicit dbpath
	 * 
	 * @throws DBNotFoundException
	 */
	public void loadDatabase() throws DBNotFoundException {
		this.loadDatabase(this.dbPath);
	}

	/**
	 * Returns name
	 * 
	 * @return name
	 */
	public String getDbName() {
		return dbName;
	}
}
