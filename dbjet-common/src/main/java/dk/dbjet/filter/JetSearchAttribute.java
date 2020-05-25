package dk.dbjet.filter;

import java.util.Objects;

import dk.dbjet.exception.JetRuntimeException;
import dk.dbjet.util.CommonUtils;

public abstract class JetSearchAttribute {
	
	protected static char[] BLACKLIST_CHARS = {'(', ')', ';', '\'', '"'};
		
	protected String columnName;
	protected Object columnValue;
	
	protected JetSearchAttribute(String column, Object value) {
		Objects.requireNonNull(column, "Column can not be null.");
		Objects.requireNonNull(value, "Column value can not be null.");
		verifyBlacklistedChars(value);
		this.columnName = column;
		this.columnValue = value;
	}
	
	private void verifyBlacklistedChars(Object value) {
		// to avoid SQL injection.		
		if ((value instanceof String) && CommonUtils.stringContainsAnyOfCharacter(value.toString(), BLACKLIST_CHARS)) {
			throw new JetRuntimeException("Blacklisted chars were supplied.");
		}
	}
	
	public abstract String toClauseString();
}

