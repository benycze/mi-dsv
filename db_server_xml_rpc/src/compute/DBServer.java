package compute;

import db.DBRecord;

import exceptions.DBExistsException;
import exceptions.DBNotFoundException;
import exceptions.DuplicateKeyException;
import exceptions.KeyNotFoundException;

public interface DBServer {

	public String[] listDB();

	public boolean createDB(String dbName) throws DBExistsException;

	public int insert(String dbname, Integer key, String message)
			throws DBNotFoundException, DuplicateKeyException;

	public int update(String dbname, Integer key, String message)
			throws DBNotFoundException, KeyNotFoundException;

	public DBRecord get(String dbname, Integer key) 
			throws DBNotFoundException,	KeyNotFoundException;

	public DBRecord[] getA(String dbname, Integer[] key)
			throws DBNotFoundException, KeyNotFoundException;

	public boolean flush();

}
