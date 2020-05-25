package dk.dbjet.filter;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;

import dk.dbjet.exception.JetRuntimeException;

public final class JetNoneOfSearchAttribute extends JetSearchAttribute {

	private static String CONDITION_PATTERN = "%s NOT IN (%s)";

	public JetNoneOfSearchAttribute(String column, Object value) {
		super(column, value);
		if (!(value instanceof List || value instanceof Set)) {
			throw new JetRuntimeException("Column value must be type of List or Set.");
		}
	}

	@Override
	public String toClauseString() {
		@SuppressWarnings("unchecked")
		Collection<Object> list = (Collection<Object>) this.columnValue;
		if (list.size() > 0) {
			StringBuilder builder = new StringBuilder();
			Spliterator<Object> spliterator = list.spliterator();
			spliterator.forEachRemaining( item -> {
				builder.append("'").append(item).append("'").append(",");
			});
			// remove a extra ',' from the builder.
			builder.delete(builder.length()-1, builder.length());
			return String.format(CONDITION_PATTERN, this.columnName, builder.toString());			
		}
		return "";
	}
}
