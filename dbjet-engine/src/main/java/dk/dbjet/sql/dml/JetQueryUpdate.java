package dk.dbjet.sql.dml;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import dk.dbjet.annotation.JetColumn;
import dk.dbjet.annotation.JetColumnUpdatable;
import dk.dbjet.common.JetModel;
import dk.dbjet.exception.JetQueryException;

public class JetQueryUpdate extends JetDMLQuery {

	private final static String SQL_UPDATE = "UPDATE %s SET %s WHERE %s = '%s'";
	private Object pkValue;

	public JetQueryUpdate(Object pkValue, JetModel model) {
		super(model);
		this.pkValue = pkValue;
	}

	@Override
	public PreparedStatement toStatement(Connection con) throws SQLException, JetQueryException {
		String table = getTableName();
		Field pkField = getPrimaryColumnField();
		JetColumn pkColumn = pkField.getAnnotation(JetColumn.class);
		List<Field> allFields = getColumnFields();
		List<Field> applicableFields = new LinkedList<Field>();
		List<String> applicableColumns = new LinkedList<String>();
		for (Field field : allFields) {
			try {
				field.setAccessible(true);
				Object value = field.get(this.model);
				JetColumn column = field.getAnnotation(JetColumn.class);				
				if (field.isAnnotationPresent(JetColumnUpdatable.class)) {
					if (value != null) {						
						applicableColumns.add(column.name() + "=?");		
						applicableFields.add(field);
					}
				} else if (value != null) {
					throw new JetQueryException(column.name() + " is not updatable, Missing @JetColumnUpdatable?");
				}				
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new JetQueryException(e);
			}	
		}
		if (applicableColumns.isEmpty()) {
			throw new JetQueryException("There is nothing to update, Missing @JetColumnUpdatable?");
		}
		String sql = String.format(SQL_UPDATE, table, String.join(", ", applicableColumns), pkColumn.name(), this.pkValue);		
		PreparedStatement statement = prepareStatement(con, sql, applicableFields);
		return statement;
	}

}
