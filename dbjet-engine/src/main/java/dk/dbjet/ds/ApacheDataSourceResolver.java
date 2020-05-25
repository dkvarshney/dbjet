package dk.dbjet.ds;

import javax.sql.DataSource;

import org.apache.commons.dbcp2.BasicDataSource;

import dk.dbjet.config.JetDatabaseConfig;

public class ApacheDataSourceResolver implements DataSourceResolver {

	@Override
	public DataSource getDataSource(JetDatabaseConfig config) {
		BasicDataSource ds = new BasicDataSource();
        ds.setUrl(config.getJdbcUrl());
        ds.setDriverClassName(config.getJdbcDriver());
        ds.setUsername(config.getDbUsername());
        ds.setPassword(config.getDbPassword());
        ds.setMinIdle(5);
        ds.setMaxIdle(10);
        return ds;
	}
}
