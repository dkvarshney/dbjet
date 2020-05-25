package dk.dbjet.exception;

public class JetQueryException extends JetException {

	private static final long serialVersionUID = 1L;

	public JetQueryException(String msg) {
		super(msg);
	}
	
	public JetQueryException(Throwable e) {
		super(e);
	}
}
