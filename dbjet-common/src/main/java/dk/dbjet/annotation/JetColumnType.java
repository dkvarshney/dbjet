package dk.dbjet.annotation;

public enum JetColumnType {
	/**
	 * Text or String.
	 */
	TEXT (String.class, true),
	
	/**
	 * Enumeration.
	 */
	ENUM (Enum.class, false),
	
	/**
	 * Blob or Bytes.
	 */
	BINARY (byte[].class, false),
	
	/**
	 * Bool or Boolean
	 */
	BOOLEAN (Boolean.class, false),
	
	/**
	 * Int or Integer.
	 */
	INTEGER (Integer.class, true),
	
	/**
	 * long or Long.
	 */
	LONG (Long.class, true),
	
	/**
	 * Float or Double.
	 */
	FLOAT (Float.class, false),
	
	/**
	 * Timestamp.
	 */
	TIMESTAMP (java.sql.Timestamp.class, false),
	
	/**
	 * DateTime.
	 */
	DATETIME (java.sql.Date.class, false);
	
	private JetColumnType(Class<?> allowedType, boolean isAllowedAsPrimary) {
		this.allowedType = allowedType;
		this.isAllowedAsPrimary = isAllowedAsPrimary;
	}

	Class<?> allowedType;
		
	boolean isAllowedAsPrimary;
	
	
	/**
	 * Allowed java data types.
	 */
	public Class<?> getAllowedType() {
		return allowedType;
	}
		
	
	/**
	 * Specified if a filed type can be primary. 
	 */
	public boolean isAllowedAsPrimary() {
		return isAllowedAsPrimary;
	}
}
