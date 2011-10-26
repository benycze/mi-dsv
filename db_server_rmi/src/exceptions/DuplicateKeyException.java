package exceptions;

public class DuplicateKeyException extends Exception {

	private static final long serialVersionUID = -1937161867341487222L;

	public DuplicateKeyException(String message) {
		super(message);
	}
}
