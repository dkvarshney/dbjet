package dk.dbjet.exception;

public class JetException extends Exception {

	private static final long serialVersionUID = 1L;

	public JetException(String msg) {
		super(msg);
	}
	
	public JetException(Throwable e) {
		super(e);
	}
	
	public JetException(String msg, Throwable e) {
		super(msg, e);
	}
}
