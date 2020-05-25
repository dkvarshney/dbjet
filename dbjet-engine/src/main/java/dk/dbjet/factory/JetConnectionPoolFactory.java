package dk.dbjet.factory;

import javax.sql.DataSource;

import dk.dbjet.config.JetDatabaseConfig;
import dk.dbjet.config.JetDatabaseConfig.PoolProvider;
import dk.dbjet.ds.DataSourceResolver;

public final class JetConnectionPoolFactory {

	private final static JetConnectionPoolFactory instance = new JetConnectionPoolFactory();

	private DataSource dataSource;

	private JetConnectionPoolFactory() {
		// no constructor.
	}

	public static JetConnectionPoolFactory getConnectionPoolFactory() {
		return instance;
	}

	public void initialize(JetDatabaseConfig config) {
		try {
			long ct = System.currentTimeMillis();
			Class<?> dsResolver = null; 
			if (PoolProvider.HikariCP.equals(config.getPoolProvider())) {
				dsResolver = Class.forName("dk.dbjet.ds.HikariDataSourceResolver");				
			} else if (PoolProvider.ApacheDBCP.equals(config.getPoolProvider())) {
				dsResolver = Class.forName("dk.dbjet.ds.ApacheDataSourceResolver");
			} else {
				throw new RuntimeException("Unknown connection pool provider.");
			}			
			DataSourceResolver dsr = (DataSourceResolver) dsResolver.newInstance();
			this.dataSource = dsr.getDataSource(config);
			System.out.println("Time Taken to initialize connection pool (ms): " + ((System.currentTimeMillis() - ct) / 1000));			
		} catch (Exception e) {
			throw new RuntimeException(e);			
		}
	}

	public DataSource getDataSource() {			
		return this.dataSource;
	}

}
