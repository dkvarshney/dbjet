package dk.dbjet.filter;

public final class JetNotEqualsSearchAttribute extends JetSearchAttribute {
	
	private static String CONDITION_PATTERN = "%s != '%s'";
		
	public JetNotEqualsSearchAttribute(String column, Object value) {
		super(column, value);
	}
		
	@Override
	public String toClauseString() {
		return String.format(CONDITION_PATTERN, this.columnName, this.columnValue);
	}
}

