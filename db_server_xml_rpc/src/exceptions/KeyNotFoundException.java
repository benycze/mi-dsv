package exceptions;

import org.apache.xmlrpc.XmlRpcException;

public class KeyNotFoundException extends XmlRpcException {

	private static final long serialVersionUID = -1937161867341487444L;
	
	int key;

	public KeyNotFoundException(String message,int key) {
		super(message);
		this.key = key;
	}

}
