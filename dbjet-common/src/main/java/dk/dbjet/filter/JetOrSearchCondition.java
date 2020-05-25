package dk.dbjet.filter;

import java.util.List;

public final class JetOrSearchCondition implements JetSearchCondition {

	private static String CLAUSE_PATTERN = "(%s OR %s)";
	
	@Override
	public final String toClauseString(JetSearchAttribute left, JetSearchAttribute right) {
		return String.format(CLAUSE_PATTERN, left.toClauseString(), right.toClauseString());
	}
	
	@Override
	public final String toClauseString(String left, JetSearchAttribute right) {
		return String.format(CLAUSE_PATTERN, left, right.toClauseString());
	}
	
	@Override
	public final String toClauseString(String left, List<JetSearchAttribute>right) {
		StringBuilder builder = new StringBuilder(right.get(0).toClauseString());
		for (int i=1; i<right.size(); i++) {
			builder.append(" OR ").append(right.get(i).toClauseString());
		}
		return String.format(CLAUSE_PATTERN, left, builder.toString());
	}
	
	@Override
	public final String toClauseString(JetSearchAttribute left, String right) {
		return String.format(CLAUSE_PATTERN, left.toClauseString(), right);
	}
}
