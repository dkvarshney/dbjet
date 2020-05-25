package dk.dbjet.exception;

public class JetRuntimeException extends RuntimeException {

	public JetRuntimeException(String msg) {
		super(msg);
	}

	public JetRuntimeException(Throwable e) {
		super(e);
	}
	
	public JetRuntimeException(String msg, Throwable e) {
		super(msg, e);
	}
	
	private static final long serialVersionUID = 1L;

}
