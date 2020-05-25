package dk.dbjet.filter;

import java.util.List;

public interface JetSearchCriteria {

	/**
	 * Prepare and returns the AND SearchFilter.
	 *  
	 * @param filter
	 * @return
	 */
	public JetSearchCriteria and(JetSearchAttribute filter);
	
	/**
	 * Prepare and returns the AND SearchFilter from given list.
	 *  
	 * @param filter
	 * @return
	 */
	public JetSearchCriteriaBuilder and(List<JetSearchAttribute> filter);
	
	/**
	 * Prepare and returns the OR SearchFilter.
	 *  
	 * @param filter
	 * @return
	 */
	public JetSearchCriteria or(JetSearchAttribute filter);
	
	/**
	 * Prepare and returns the OR SearchFilter from given list.
	 *  
	 * @param filter
	 * @return
	 */
	public JetSearchCriteria or(List<JetSearchAttribute> filter);
	
	/**
	 * Prepare and returns the SQL string clause.
	 *  
	 * @param filter
	 * @return
	 */
	public String toClauseString();
}
