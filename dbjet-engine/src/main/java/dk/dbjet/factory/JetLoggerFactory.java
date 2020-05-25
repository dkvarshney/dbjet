package dk.dbjet.factory;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class JetLoggerFactory {
		
	private JetLoggerFactory() {
		// no constructor.
	}
	
	public static Logger getLogger(Class<?> klass) {
		Logger logger = Logger.getLogger(klass.getName());
		logger.setLevel(Level.FINE);
		return logger;
	}
	
}
