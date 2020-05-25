package dk.dbjet.filter;

import java.util.List;

public final class JetSearchCriteriaBuilder implements JetSearchCriteria {

	private String filterString;
	
	public JetSearchCriteriaBuilder(JetSearchAttribute filter) {
		this.filterString = filter.toClauseString();
	}

	public static JetSearchCriteriaBuilder with(JetSearchAttribute filter) {
		return new JetSearchCriteriaBuilder(filter);
		
	}
	
	@Override
	public JetSearchCriteriaBuilder and(JetSearchAttribute filter) {
		this.filterString = new JetAndSearchCondition().toClauseString(this.filterString, filter);
		return this;
	}

	@Override
	public JetSearchCriteriaBuilder and(List<JetSearchAttribute> filter) {
		this.filterString = new JetAndSearchCondition().toClauseString(this.filterString, filter);
		return this;
	}
	
	@Override
	public JetSearchCriteriaBuilder or(JetSearchAttribute filter) {
		this.filterString = new JetOrSearchCondition().toClauseString(this.filterString, filter);
		return this;
	}
	
	@Override
	public JetSearchCriteria or(List<JetSearchAttribute> filter) {
		this.filterString = new JetOrSearchCondition().toClauseString(this.filterString, filter);
		return this;
	}
	
	@Override
	public String toClauseString() {
		return this.filterString;
	}
}
