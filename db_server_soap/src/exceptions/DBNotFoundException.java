package exceptions;

import org.apache.axis2.AxisFault;

public class DBNotFoundException extends AxisFault {
	private static final long serialVersionUID = -1937161867341487111L;

	public DBNotFoundException(String message) {
		super(message);
	}
}
