package dk.dbjet.annotation;

public enum JetColumnValue {
	/**
	 * Same as the schema or data type.
	 */
	NONE,
	
	/**
	 * Allow NULL and set NULL as default value.
	 */
	NULL,
	
	
	/**
	 * Current system timestamp.
	 */
	CURRENT_TIMESTAMP,
	
	/**
	 * Current system timestamp only on update.
	 */
	CURRENT_TIMESTAMP_ON_UPDATE
}
