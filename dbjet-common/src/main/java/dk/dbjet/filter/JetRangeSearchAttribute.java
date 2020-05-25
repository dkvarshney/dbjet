package dk.dbjet.filter;

import java.util.Objects;

public final class JetRangeSearchAttribute extends JetSearchAttribute {
	
	private static String CONDITION_PATTERN = "%s BETWEEN '%s' AND '%s'";
		
	private Object value2;
	
	public JetRangeSearchAttribute(String column, Object value1, Object value2) {
		super(column, value1);
		Objects.requireNonNull(value1, "Column value can not be null.");
		this.value2 = value2;
	}
		
	@Override
	public String toClauseString() {
		return String.format(CONDITION_PATTERN, this.columnName, this.columnValue, this.value2);
	}
}
