package exceptions;

import org.apache.axis2.AxisFault;

public class KeyNotFoundException extends AxisFault {

	private static final long serialVersionUID = -1937161867341487444L;
	
	int key;

	public KeyNotFoundException(String message,int key) {
		super(message);
		this.key = key;
	}

}
