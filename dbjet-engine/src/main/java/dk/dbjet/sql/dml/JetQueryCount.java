package dk.dbjet.sql.dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dk.dbjet.common.JetModel;
import dk.dbjet.exception.JetQueryException;
import dk.dbjet.filter.JetSearchCriteria;

public class JetQueryCount extends JetDMLQuery {

	private JetSearchCriteria searchFilter; 
	
	public JetQueryCount(JetModel model, JetSearchCriteria filter) {
		super(model);
		this.searchFilter = filter;
	}
	
	@Override
	public PreparedStatement toStatement(Connection con) throws SQLException, JetQueryException {
		String table = getTableName();
		String sql = String.format("SELECT COUNT(*) FROM %s", table);
		if (this.searchFilter != null) {
			sql = sql + " WHERE " + this.searchFilter.toClauseString();
		}
		return con.prepareStatement(sql);
	}
}
