package dk.dbjet.common;

import java.sql.SQLException;
import java.util.List;

import dk.dbjet.exception.JetException;
import dk.dbjet.filter.JetSearchControl;
import dk.dbjet.filter.JetSearchCriteria;

public interface JetManager <T extends JetModel> {

	/**
	 * Insert the model to corresponding DB table.
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 * @throws JetException
	 */
	JetModel insert(T model) throws SQLException, JetException;

	/**
	 * Update the record based on primary column.
	 * 
	 * @param pkValue
	 * @param model
	 * @throws SQLException
	 * @throws JetException
	 */
	void update(Object pkValue, T model) throws SQLException, JetException;

	/**
	 * Delete a record by primary column value.
	 * 
	 * @param pkValue
	 * @throws SQLException
	 * @throws JetException
	 */
	void delete(Object pkValue) throws SQLException, JetException;

	/**
	 * Get the model by primary column value.
	 * 
	 * @param pkValue
	 * @return
	 * @throws SQLException
	 * @throws JetException
	 */
	T get(Object pkValue) throws SQLException, JetException;

	/**
	 * Search the records based on given filter criteria and controls. 
	 * 
	 * @param filter
	 * @param control
	 * @return
	 * @throws SQLException
	 * @throws JetException
	 */
	List<T>search(JetSearchCriteria filter, JetSearchControl control) throws SQLException, JetException;
}
