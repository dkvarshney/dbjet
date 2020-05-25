package dk.dbjet.sql.common;

import java.lang.reflect.Field;
import java.util.List;

import dk.dbjet.common.JetModel;
import dk.dbjet.exception.JetQueryException;
import dk.dbjet.factory.JetReflectionFactory;

public abstract class JetQuery {
	
	protected final static String SQL_DELIMITER = ";";
	
	protected JetModel model;
	
	public JetQuery(JetModel model) {
		this.model = model;
	}
	
	protected List<Field> getColumnFields() {
		return JetReflectionFactory.getInstance().getColumnFields(this.model.getClass());
	}
	
	protected Field getPrimaryColumnField() throws JetQueryException {		
		List<Field> fields = JetReflectionFactory.getInstance().getPrimaryFields(this.model.getClass());
		if (!fields.isEmpty()) {
			return fields.iterator().next();
		}
		throw new JetQueryException("Primary column not found, Missing @JetColumnIsPrimary?");
	}
	
	protected String getTableName() {
		return JetReflectionFactory.getInstance().getTableName(this.model.getClass());
	}
}
