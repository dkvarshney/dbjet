package dk.dbjet.filter;

public class JetGreaterThanSearchAttribute extends JetSearchAttribute {
	
	private static String CONDITION_PATTERN = "%s > '%s'";
		
	public JetGreaterThanSearchAttribute(String column, Object value) {
		super(column, value);
	}
		
	@Override
	public String toClauseString() {
		return String.format(CONDITION_PATTERN, this.columnName, this.columnValue);
	}
}
