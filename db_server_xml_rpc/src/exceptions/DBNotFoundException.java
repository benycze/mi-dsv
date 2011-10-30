package exceptions;

public class DBNotFoundException extends Exception {
	private static final long serialVersionUID = -1937161867341487111L;

	public DBNotFoundException(String message) {
		super(message);
	}
}
