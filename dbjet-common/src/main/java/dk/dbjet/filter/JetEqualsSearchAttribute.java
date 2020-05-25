package dk.dbjet.filter;

public final class JetEqualsSearchAttribute extends JetSearchAttribute {
	
	private static String CONDITION_PATTERN = "%s = '%s'";
		
	public JetEqualsSearchAttribute(String column, Object value) {
		super(column, value);
	}
		
	@Override
	public String toClauseString() {
		return String.format(CONDITION_PATTERN, this.columnName, this.columnValue);
	}
}
