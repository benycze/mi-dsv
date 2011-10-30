package exceptions;

import org.apache.xmlrpc.XmlRpcException;

public class DuplicateKeyException extends XmlRpcException {

	private static final long serialVersionUID = -1937161867341487222L;

	public DuplicateKeyException(String message) {
		super(message);
	}
}
