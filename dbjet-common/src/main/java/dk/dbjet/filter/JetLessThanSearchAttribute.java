package dk.dbjet.filter;

public class JetLessThanSearchAttribute extends JetSearchAttribute {
	
	private static String CONDITION_PATTERN = "%s < '%s'";
		
	public JetLessThanSearchAttribute(String column, Object value) {
		super(column, value);
	}
		
	@Override
	public String toClauseString() {
		return String.format(CONDITION_PATTERN, this.columnName, this.columnValue);
	}
}
