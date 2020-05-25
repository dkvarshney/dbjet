package dk.dbjet.sql.dml;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import dk.dbjet.common.JetModel;
import dk.dbjet.exception.JetQueryException;
import dk.dbjet.exception.JetRuntimeException;
import dk.dbjet.factory.JetReflectionFactory;
import dk.dbjet.filter.JetSearchControl;
import dk.dbjet.filter.JetSearchCriteria;
import dk.dbjet.util.CommonUtils;

public class JetQuerySearch extends JetDMLQuery {

	private JetSearchCriteria searchFilter; 
	private JetSearchControl searchControl;
	
	public JetQuerySearch(JetModel model, JetSearchCriteria filter, JetSearchControl control) {
		super(model);
		this.searchFilter = filter;
		this.searchControl = control;
	}
		
	@Override
	public PreparedStatement toStatement(Connection con) throws SQLException, JetQueryException {
		String table = getTableName();
		String columns = "*";
		String controls = "";				
		if (this.searchControl != null) {
			if (CommonUtils.isNotEmpty(this.searchControl.getColumns())) {				
				List<String> columnsName = JetReflectionFactory.getInstance().getColumnsName(this.model.getClass());				
				for (String column : this.searchControl.getColumns()) {
					if (!columnsName.contains(column)) {
						throw new JetRuntimeException(column + " unknown column");
					}
				}
				columns = String.join(",", this.searchControl.getColumns());
			}			
			if (CommonUtils.isNotEmpty(this.searchControl.getSortBy())) {
				controls = controls + " ORDER BY " + this.searchControl.getSortBy() + " " + this.searchControl.getSortOrder().toClauseString();
			}
			controls = String.format(" LIMIT %d, %d", ((this.searchControl.getPage()-1) * this.searchControl.getSize()), this.searchControl.getSize());
		}
		String sql = String.format("SELECT %s FROM %s", columns, table);
		if (this.searchFilter != null) {
			sql = sql + " WHERE " + this.searchFilter.toClauseString();
		}
		return con.prepareStatement(sql + controls);
	}
}
