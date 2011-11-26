package Managers;

import server.DBServerImpl;
import Generated.DBExistsException;
import Generated.DBNotFoundException;
import Generated.DBRecord;
import Generated.DuplicateKeyException;
import Generated.KeyNotFoundException;

/**
 * This class is the implemetation object for your IDL interface.
 *
 * Let the Eclipse complete operations code by choosing 'Add unimplemented methods'.
 */
public class DbServerInterfaceServerImpl extends Generated.DbServerInterfacePOA {
	
	DBServerImpl dbs;
	
	/**
	 * Constructor for DbServerInterfaceServerImpl 
	 */
	public DbServerInterfaceServerImpl() {
		this.dbs = new DBServerImpl();
	}

	@Override
	public String[] listDB() {
		return dbs.listDB();
	}

	@Override
	public boolean createDB(String dbName) throws DBExistsException {
		return dbs.createDB(dbName);
	}

	@Override
	public int insert(String dbname, int key, String message)
			throws DBNotFoundException, DuplicateKeyException {
		return dbs.insert(dbname, key, message);
	}

	@Override
	public int update(String dbname, int key, String message)
			throws DBNotFoundException, KeyNotFoundException {
		return dbs.update(dbname, key, message);
	}

	@Override
	public DBRecord get(String dbname, int key) throws DBNotFoundException,
			KeyNotFoundException {
		return dbs.get(dbname, key);
	}

	@Override
	public DBRecord[] getA(String dbname, int[] key)
			throws DBNotFoundException, KeyNotFoundException {
		return dbs.getA(dbname, key);
	}

	@Override
	public void flush() {
		dbs.flush();		
	}
}
