package compute;

import Generated.*;

public interface DBServer {

	public String[] listDB();

	public boolean createDB(String dbName) throws
			DBExistsException;

	public int insert(String dbname, Integer key, String message)
			throws  DBNotFoundException, DuplicateKeyException;

	public int update(String dbname, Integer key, String message)
			throws DBNotFoundException, KeyNotFoundException;

	public DBRecord get(String dbname, Integer key) throws 
			DBNotFoundException, KeyNotFoundException;

	public DBRecord[] getA(String dbname, int[] key)
			throws  DBNotFoundException, KeyNotFoundException;

	public void flush();

}
