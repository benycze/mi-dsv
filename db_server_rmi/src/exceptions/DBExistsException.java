package exceptions;

public class DBExistsException extends Exception {

	private static final long serialVersionUID = -1937161867341487000L;

	public DBExistsException(String message) {
		super(message);
	}
}
