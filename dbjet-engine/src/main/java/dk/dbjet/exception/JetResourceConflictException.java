package dk.dbjet.exception;

public class JetResourceConflictException extends JetException {

	private static final long serialVersionUID = 1L;

	public JetResourceConflictException(String msg) {
		super(msg);
	}
	
	public JetResourceConflictException(Throwable e) {
		super(e);
	}
}
