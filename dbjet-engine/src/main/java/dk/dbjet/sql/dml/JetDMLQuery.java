package dk.dbjet.sql.dml;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import dk.dbjet.annotation.JetColumn;
import dk.dbjet.annotation.JetColumnType;
import dk.dbjet.common.JetModel;
import dk.dbjet.exception.JetQueryException;
import dk.dbjet.marshaller.JetMarshaller;
import dk.dbjet.sql.common.JetQuery;

public abstract class JetDMLQuery extends JetQuery {
	
	public JetDMLQuery(JetModel model) {
		super(model);
	}
	
	protected String getStatementPlaceholder(int no) {
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<no; i++) {
			if (i > 0) {
				sb.append(",");
			}
			sb.append("?");
		}
		return sb.toString();
	}
	
	@SuppressWarnings("unchecked")
	protected PreparedStatement prepareStatement(Connection con, String sql, List<Field> fields) throws SQLException, JetQueryException {
		PreparedStatement statement = con.prepareStatement(sql);
		for (int fIndex = 0; fIndex < fields.size(); fIndex ++) {
			Field field = fields.get(fIndex);
			field.setAccessible(true);
			int stIndex = fIndex + 1;
			try {
				Object value = field.get(this.model);
				JetColumn column = field.getAnnotation(JetColumn.class);
				if (column.type() == JetColumnType.TEXT || column.type() == JetColumnType.BIGTEXT) {								
					JetMarshaller<String> marshaller = column.marshaller().newInstance();
					String mlValue = marshaller.marshal(column, (String) value);
					statement.setString(stIndex, mlValue);
				} else if (column.type() == JetColumnType.ENUM) {
					statement.setString(stIndex, value.toString());
				} else if (column.type() == JetColumnType.BINARY) {
					JetMarshaller<byte[]> marshaller = column.marshaller().newInstance();					
					byte[] mlValue = marshaller.marshal(column, (byte[]) value);
					statement.setBinaryStream(stIndex, new ByteArrayInputStream(mlValue));					
				} else if (column.type() == JetColumnType.FLOAT) {					
					statement.setFloat(stIndex, (Float) value);
				} else if (column.type() == JetColumnType.LONG) {					
					statement.setLong(stIndex, (Long) value);
				} else if (column.type() == JetColumnType.BOOLEAN) {
					statement.setBoolean(stIndex, (Boolean)value);
				} else if (column.type() == JetColumnType.INTEGER) {
					statement.setInt(stIndex, (Integer)value);
				} else if (column.type() == JetColumnType.TIMESTAMP) {
					statement.setTimestamp(stIndex, (Timestamp) value);
				} else if (column.type() == JetColumnType.DATETIME) {
					statement.setDate(stIndex, (Date) value);
				}
			} catch (IllegalArgumentException | IllegalAccessException | InstantiationException e) {
				throw new JetQueryException(e);
			}
		}
		return statement;
	}
	
	public abstract PreparedStatement toStatement(Connection con) throws SQLException, JetQueryException;
}
