package tst;

import db.DB;
import db.DBRecord;
import exceptions.DBNotFoundException;
import exceptions.DuplicateKeyException;
import exceptions.KeyNotFoundException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;


public class tstMain {

	/**
	 * @param args
	 * @throws DuplicateKeyException 
	 * @throws KeyNotFoundException 
	 * @throws DBNotFoundException 
	 * @throws MalformedURLException 
	 * @throws XmlRpcException 
	 */
	public static void main(String[] args) throws DuplicateKeyException, KeyNotFoundException, DBNotFoundException, MalformedURLException, XmlRpcException {
		/*DBRecord dbr;
		
		dbr = new DBRecord(666,"Pokus ....");
		String dbrS = dbr.getCSVRecord();
		dbr.setMessage("HOvno");
		int key = dbr.getKey();
		String s1 = dbr.getTscreate();
		String s2 = dbr.getTsmodify();
		DBRecord dbr2 = new DBRecord(dbrS);
		
		System.out.println(dbr2.getCSVRecord());
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
		
		DB database3 = new DB("FOO2");*/

    	
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://test.xmlrpc.wordtracker.com/"));
        XmlRpcClient client = new XmlRpcClient();
        client.setConfig(config);
  
        Vector<String> params = new Vector<String>();
       params.addElement( "guest" );

        Object result = client.execute( "ping", params );

        if ( result != null ) {
            System.out.println( "Successfully pinged guest account." );
            System.out.println("Result: "+result);
        }
	}

}
