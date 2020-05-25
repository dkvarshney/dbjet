package dk.dbjet.sql.dml;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import dk.dbjet.annotation.JetColumn;
import dk.dbjet.annotation.JetColumnType;
import dk.dbjet.common.JetModel;
import dk.dbjet.exception.JetQueryException;

public class JetQueryGet extends JetDMLQuery {
	
	private final static String SQL_GET = "SELECT * FROM %s WHERE %s = ?";
	private Object pkValue;
	
	public JetQueryGet(Object pkValue, JetModel toModel) {
		super(toModel);
		this.pkValue = pkValue;
	}
	
	@Override
	public PreparedStatement toStatement(Connection con) throws SQLException, JetQueryException {
		String table = getTableName();
		Field pkField = getPrimaryColumnField();
		JetColumn pkColumn = pkField.getAnnotation(JetColumn.class);
		PreparedStatement statement = con.prepareStatement(String.format(SQL_GET, table, pkColumn.name()));
		if (pkColumn.type() == JetColumnType.INTEGER) {
			statement.setInt(1, (int) this.pkValue);
		} else {
			statement.setString(1, (String) this.pkValue);
		}
		return statement;
	}
}
