package dk.dbjet.config;

public class JetDatabaseConfig {
	
	public enum PoolProvider {
		/**
		 * HikariCP - https://brettwooldridge.github.io/HikariCP/
		 */
		HikariCP,		
		
		/**
		 * Apache DBCP - https://commons.apache.org/proper/commons-dbcp/
		 */
		ApacheDBCP
	}
	
	private String jdbcDriver;
	private String jdbcUrl;
	private String dbUsername;
	private String dbPassword;
	
	/* default values, can be overridden by setters */
	private PoolProvider poolProvider = PoolProvider.HikariCP;	
	private long connectionTimeout = 20000;
	private int minimumIdleTime = 10;
	private int maximumPoolSize = 10;
	
	public String getJdbcDriver() {
		return jdbcDriver;
	}
	
	public void setJdbcDriver(String jdbcDriver) {
		this.jdbcDriver = jdbcDriver;
	}
	
	public String getJdbcUrl() {
		return jdbcUrl;
	}
	
	public void setJdbcUrl(String jdbcUrl) {
		this.jdbcUrl = jdbcUrl;
	}
	
	public String getDbUsername() {
		return dbUsername;
	}
	
	public void setDbUsername(String dbUsername) {
		this.dbUsername = dbUsername;
	}
	
	public String getDbPassword() {
		return dbPassword;
	}
	
	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}
	
	public PoolProvider getPoolProvider() {
		return poolProvider;
	}
	
	public void setPoolProvider(PoolProvider poolProvider) {
		this.poolProvider = poolProvider;
	}
	
	public long getConnectionTimeout() {
		return connectionTimeout;
	}
	
	public void setConnectionTimeout(long connectionTimeout) {
		this.connectionTimeout = connectionTimeout;
	}	
	
	public long getMinimumIdleTime() {
		return minimumIdleTime;
	}
	
	public void setMinimumIdleTime(int minimumIdleTime) {
		this.minimumIdleTime = minimumIdleTime;
	}
	
	public int getMaximumPoolSize() {
		return maximumPoolSize;
	}
	
	public void setMaximumPoolSize(int maximumPoolSize) {
		this.maximumPoolSize = maximumPoolSize;
	}
}
