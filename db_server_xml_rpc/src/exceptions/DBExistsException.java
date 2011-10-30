package exceptions;

import org.apache.xmlrpc.XmlRpcException;

public class DBExistsException extends XmlRpcException {

	private static final long serialVersionUID = -1937161867341487000L;

	public DBExistsException(String message) {
		super(message);
	}
}
