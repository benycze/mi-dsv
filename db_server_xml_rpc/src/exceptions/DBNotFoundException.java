package exceptions;

import org.apache.xmlrpc.XmlRpcException;

public class DBNotFoundException extends XmlRpcException {
	private static final long serialVersionUID = -1937161867341487111L;

	public DBNotFoundException(String message) {
		super(message);
	}
}
