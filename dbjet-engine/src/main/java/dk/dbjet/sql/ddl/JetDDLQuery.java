package dk.dbjet.sql.ddl;

import dk.dbjet.common.JetModel;
import dk.dbjet.exception.JetQueryException;
import dk.dbjet.sql.common.JetQuery;

public abstract class JetDDLQuery extends JetQuery {

	public JetDDLQuery(JetModel model) {
		super(model);
	}
	
	public abstract String toSQLString() throws JetQueryException;
}
