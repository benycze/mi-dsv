package exceptions;

import org.apache.axis2.AxisFault;

public class DuplicateKeyException extends AxisFault {

	private static final long serialVersionUID = -1937161867341487222L;

	public DuplicateKeyException(String message) {
		super(message);
	}
}
