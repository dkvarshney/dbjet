package dk.dbjet.filter;

import java.util.List;

public interface JetSearchCondition {

	/**
	 * Prepare and returns the Clause as String.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	String toClauseString(JetSearchAttribute left, JetSearchAttribute right);
	
	/**
	 * Prepare and returns the Clause as String.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	String toClauseString(String left, JetSearchAttribute right);
	
	
	/**
	 * Prepare and returns the Clause as String.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	String toClauseString(String left, List<JetSearchAttribute>right);
	
	/**
	 * Prepare and returns the Clause as String.
	 * 
	 * @param left
	 * @param right
	 * @return
	 */
	String toClauseString(JetSearchAttribute left, String right);
}
