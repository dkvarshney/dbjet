package dk.dbjet.sql.dml;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dk.dbjet.annotation.JetColumn;
import dk.dbjet.common.JetModel;
import dk.dbjet.exception.JetQueryException;

public class JetQueryInsert extends JetDMLQuery {
	
	private final static String SQL_INSERT = "INSERT INTO %s (%s) VALUES (%s)";
	
	public JetQueryInsert(JetModel model) {
		super(model);
	}
		
	public PreparedStatement toStatement(Connection con) throws SQLException, JetQueryException {		
		String table = getTableName();
		List<Field> allFields = getColumnFields();
		List<Field> applicableFields = new LinkedList<Field>();
		List<String> applicableColumns = new LinkedList<String>();
		for (Field field : allFields) {
			field.setAccessible(true);
			try {
				Object value = field.get(this.model);
				if (value != null) {
					JetColumn column = field.getAnnotation(JetColumn.class);
					applicableColumns.add(column.name());		
					applicableFields.add(field);
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new JetQueryException(e);
			}
		}
		if (applicableColumns.isEmpty()) {
			throw new JetQueryException("There is nothing to insert.");
		}
		String sql = String.format(SQL_INSERT, table, String.join(", ", applicableColumns), getStatementPlaceholder(applicableFields.size()));
		PreparedStatement statement = prepareStatement(con, sql, applicableFields);
		return statement;
	}
}
