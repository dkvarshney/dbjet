package dk.dbjet.ds;

import javax.sql.DataSource;

import dk.dbjet.config.JetDatabaseConfig;

public interface DataSourceResolver {

	public DataSource getDataSource(JetDatabaseConfig config);
}
