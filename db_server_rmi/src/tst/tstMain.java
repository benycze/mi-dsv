package tst;

import db.DB;
import db.DBRecord;
import exceptions.DBNotFoundException;
import exceptions.DuplicateKeyException;
import exceptions.KeyNotFoundException;

public class tstMain {

	/**
	 * @param args
	 * @throws DuplicateKeyException 
	 * @throws KeyNotFoundException 
	 * @throws DBNotFoundException 
	 */
	public static void main(String[] args) throws DuplicateKeyException, KeyNotFoundException, DBNotFoundException {
		/*DBRecord dbr;
		
		dbr = new DBRecord(666,"Pokus ....");
		String dbrS = dbr.getCSVRecord();
		dbr.setMessage("HOvno");
		int key = dbr.getKey();
		String s1 = dbr.getTscreate();
		String s2 = dbr.getTsmodify();
		DBRecord dbr2 = new DBRecord(dbrS);
		
		System.out.println(dbr2.getCSVRecord());*/
		/////////////////////////////////////////////////
		DB database = new DB("FOO");
		
		database.insertNewRecord(666, "HOVNOOOO");
		database.insertNewRecord(1, "HOVNOOOOa");
		database.insertNewRecord(2, "HOVNOOOOb");
		database.insertNewRecord(100, "HOVNOOOOc");
		database.insertNewRecord(333, "HOVNOOOOd");
		database.insertNewRecord(444, "HOVNOOOOe");
		
		database.findDbRec(100);

//		database.updateDbRec(1111, "Ted by mela bejt chyba");
		
		database.flush();
		
		DB database2 = new DB("FOO");
		database2.loadDatabase();
		
		DB database3 = new DB("FOO2");
	}

}
