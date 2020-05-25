package dk.dbjet.ds;

import javax.sql.DataSource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import dk.dbjet.config.JetDatabaseConfig;

public class HikariDataSourceResolver implements DataSourceResolver {

	@Override
	public DataSource getDataSource(JetDatabaseConfig config) {
		HikariConfig hikariConfig = new HikariConfig();
		hikariConfig.setJdbcUrl(config.getJdbcUrl());
		hikariConfig.setUsername(config.getDbUsername());
		hikariConfig.setPassword(config.getDbPassword());
		hikariConfig.setMaximumPoolSize(config.getMaximumPoolSize());
		hikariConfig.setDriverClassName(config.getJdbcDriver());
		hikariConfig.setConnectionTimeout(config.getConnectionTimeout());
		return new HikariDataSource(hikariConfig);
	}
}
