package dk.dbjet.sql.common;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

import dk.dbjet.factory.JetConnectionPoolFactory;

public final class JetTransaction implements AutoCloseable {

	private Connection connection;
	private Savepoint savepoint;
	private boolean isActive;
	private String name;	

	public static JetTransaction beginTransaction(String name) throws SQLException {
		Connection connection = JetConnectionPoolFactory.getConnectionPoolFactory().getDataSource().getConnection();
		return new JetTransaction(connection, name);
	}
	
	private JetTransaction(Connection connection, String name) throws SQLException {
		this.name = name;		
		this.connection = connection;
		this.savepoint = this.connection.setSavepoint();
		this.connection.setAutoCommit(false);		
		this.connection.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
		this.isActive = true;
	}

	/**
	 * Rollback the changes in current txn.
	 * 
	 * @throws SQLException
	 */
	public void rollback() throws SQLException {
		try {
			if (this.connection != null) {
				this.connection.rollback(this.savepoint);
			}
		} finally {
			close();
		}
	}

	/**
	 * Commit the changes in current transaction.
	 * 
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		try {
			if (this.connection != null) {
				this.connection.commit();
			}
		} finally {
			close();
		}
	}

	public void close() {
		this.isActive = false;		
		if (this.connection != null) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				// do nothing.
			} finally {
				this.savepoint = null;
				this.connection = null;
			}
		}
	}

	/**
	 * Check if the transaction is active.
	 * 
	 * @return
	 */
	public boolean isActive() {
		return isActive;
	}

	/**
	 * Get current/active connection in transaction.
	 * 
	 * @return
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Get the current transaction name.
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}
}
