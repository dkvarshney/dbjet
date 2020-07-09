package dk.dbjet.filter;

public final class JetIsNullSearchAttribute extends JetSearchAttribute {
	
	private static String CONDITION_PATTERN = "%s IS NULL";
		
	public JetIsNullSearchAttribute(String column) {
		super(column, "");
	}
		
	@Override
	public String toClauseString() {
		return String.format(CONDITION_PATTERN, this.columnName);
	}
}
