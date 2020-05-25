package dk.dbjet.filter;

public final class JetPatternSearchAttribute extends JetSearchAttribute {
	
	private static String CONDITION_PATTERN = "%s REGEXP '%s'";
		
	public JetPatternSearchAttribute(String column, Object value) {
		super(column, value);
	}
		
	@Override
	public String toClauseString() {
		return String.format(CONDITION_PATTERN, this.columnName, this.columnValue);
	}
}
