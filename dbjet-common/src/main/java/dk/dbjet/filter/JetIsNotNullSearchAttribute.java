package dk.dbjet.filter;

public final class JetIsNotNullSearchAttribute extends JetSearchAttribute {
	
	private static String CONDITION_PATTERN = "%s IS NOT NULL";
		
	public JetIsNotNullSearchAttribute(String column) {
		super(column, "");
	}
		
	@Override
	public String toClauseString() {
		return String.format(CONDITION_PATTERN, this.columnName);
	}
}
