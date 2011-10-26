package compute;

import java.rmi.Remote;
import java.rmi.RemoteException;

import db.DBRecord;

import exceptions.DBExistsException;
import exceptions.DBNotFoundException;
import exceptions.DuplicateKeyException;
import exceptions.KeyNotFoundException;

public interface DBServer extends Remote {

	public String[] listDB() throws RemoteException;

	public boolean createDB(String dbName) throws RemoteException,
			DBExistsException;

	public int insert(String dbname, Integer key, String message)
			throws RemoteException, DBNotFoundException, DuplicateKeyException;

	public int update(String dbname, Integer key, String message)
			throws RemoteException, DBNotFoundException, KeyNotFoundException;

	public DBRecord get(String dbname, Integer key) throws RemoteException,
			DBNotFoundException, KeyNotFoundException;

	public DBRecord[] getA(String dbname, Integer[] key)
			throws RemoteException, DBNotFoundException, KeyNotFoundException;

	public void flush() throws RemoteException;

}
