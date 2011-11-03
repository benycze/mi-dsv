package exceptions;

public class KeyNotFoundException extends Exception {

	private static final long serialVersionUID = -1937161867341487444L;
	
	int key;

	public KeyNotFoundException(String message,int key) {
		super(message);
		this.key = key;
	}

	public int getKey(){
		return this.key;
	}
}
