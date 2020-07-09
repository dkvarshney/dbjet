package dk.dbjet.sql.ddl;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import dk.dbjet.annotation.JetColumn;
import dk.dbjet.annotation.JetColumnIsIndex;
import dk.dbjet.annotation.JetColumnIsPrimary;
import dk.dbjet.annotation.JetColumnIsUnique;
import dk.dbjet.annotation.JetColumnType;
import dk.dbjet.annotation.JetColumnValue;
import dk.dbjet.common.JetModel;

public class JetQueryTableCreate extends JetDDLQuery {

	public JetQueryTableCreate(JetModel model) {
		super(model);
	}

	@Override
	public String toSQLString() {
		String table = getTableName();
		List<Field> fields = getColumnFields();

		List<String> columns = new LinkedList<String>();
		List<String> pkColumns = new LinkedList<String>();
		List<String> uqColumns = new LinkedList<String>();
		List<String> ixColumns = new LinkedList<String>();

		for (Field field : fields) {
			JetColumn column = field.getAnnotation(JetColumn.class);
			if (column.type() == JetColumnType.TEXT) {
				columns.add(String.format("%s VARCHAR(%d)", column.name(), column.size()));
			} else if (column.type() == JetColumnType.ENUM) {
				columns.add(String.format("%s VARCHAR(%d)", column.name(), column.size()));
			} else if (column.type() == JetColumnType.BINARY) {
				columns.add(String.format("%s BLOB", column.name()));
			} else if (column.type() == JetColumnType.INTEGER) {
				columns.add(String.format("%s INT", column.name()));
			} else if (column.type() == JetColumnType.LONG) {
				columns.add(String.format("%s BIGINT", column.name()));
			} else if (column.type() == JetColumnType.FLOAT) {
				columns.add(String.format("%s FLOAT", column.name()));
			} else if (column.type() == JetColumnType.BOOLEAN) {
				columns.add(String.format("%s TINYINT(1)", column.name()));
			} else if (column.type() == JetColumnType.TIMESTAMP) {
				if (column.defaultValue() == JetColumnValue.CURRENT_TIMESTAMP_ON_UPDATE) {
					columns.add(String.format("%s TIMESTAMP DEFAULT CURRENT_TIMESTAMP", column.name()));					
				} else {
					columns.add(String.format("%s TIMESTAMP", column.name()));	
				}
			} else if (column.type() == JetColumnType.DATETIME) {
				if (column.defaultValue() == JetColumnValue.CURRENT_TIMESTAMP_ON_UPDATE) {
					columns.add(String.format("%s DATETIME DEFAULT CURRENT_TIMESTAMP", column.name()));					
				} else {
					columns.add(String.format("%s DATETIME", column.name()));	
				}
			} 

			// if column is primary
			if (field.isAnnotationPresent(JetColumnIsPrimary.class)) {
				pkColumns.add(column.name());
			} 

			// if column is index
			if (field.isAnnotationPresent(JetColumnIsIndex.class)) {				
				ixColumns.add(column.name());
			}

			// if column is unique
			if (field.isAnnotationPresent(JetColumnIsUnique.class)) {
				uqColumns.add(column.name());
			}
		}
		if (pkColumns.size() > 1) {
			throw new RuntimeException("There must be only one primary key but found: " + pkColumns.size());
		}

		String sql = String.format("CREATE TABLE IF NOT EXISTS %s (%s", table, String.join(", ", columns));
		if (!pkColumns.isEmpty()) {
			sql += String.format(", CONSTRAINT PK_%s PRIMARY KEY (%s)", table, String.join(", ", pkColumns));
		}
		if (!uqColumns.isEmpty()) {
			sql += String.format(", CONSTRAINT UQ_%s UNIQUE (%s)", table, String.join(", ", uqColumns));
		}
		if (!ixColumns.isEmpty()) {
			sql += String.format(", INDEX (%s)", String.join(", ", ixColumns));
		}
		return sql + ")" + SQL_DELIMITER;
	}
}
