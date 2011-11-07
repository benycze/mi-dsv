package exceptions;

import org.apache.axis2.AxisFault;

public class DBExistsException extends  AxisFault{

	private static final long serialVersionUID = -1937161867341487000L;

	public DBExistsException(String message) {
		super(message);
	}
}
