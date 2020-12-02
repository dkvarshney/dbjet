package dk.dbjet.manager;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.common.io.ByteStreams;

import dk.dbjet.annotation.JetColumn;
import dk.dbjet.annotation.JetColumnType;
import dk.dbjet.common.JetManager;
import dk.dbjet.common.JetModel;
import dk.dbjet.exception.JetException;
import dk.dbjet.exception.JetQueryException;
import dk.dbjet.exception.JetResourceConflictException;
import dk.dbjet.exception.JetResourceNotFoundException;
import dk.dbjet.factory.JetConnectionPoolFactory;
import dk.dbjet.factory.JetLoggerFactory;
import dk.dbjet.filter.JetSearchControl;
import dk.dbjet.filter.JetSearchCriteria;
import dk.dbjet.marshaller.JetMarshaller;
import dk.dbjet.sql.common.JetTransaction;
import dk.dbjet.sql.dml.JetDMLQuery;
import dk.dbjet.sql.dml.JetQueryCount;
import dk.dbjet.sql.dml.JetQueryDelete;
import dk.dbjet.sql.dml.JetQueryGet;
import dk.dbjet.sql.dml.JetQueryInsert;
import dk.dbjet.sql.dml.JetQuerySearch;
import dk.dbjet.sql.dml.JetQueryUpdate;
import dk.dbjet.util.CommonUtils;

public abstract class JetAbstractManager<T extends JetModel> implements JetManager<T> {

	protected final Logger logger = JetLoggerFactory.getLogger(getClass());
	protected JetTransaction transaction;

	private static final String ERR_RESOURCE_NOT_FOUND = "Resource with primary column value '%s' not found.";	

	/**
	 * Insert the model to corresponding DB table.
	 * 
	 * @param model
	 * @return
	 * @throws SQLException
	 * @throws JetException
	 */
	@Override
	public T insert(T model) throws SQLException, JetException {
		JetDMLQuery insert = new JetQueryInsert(model);
		Connection connection = getConnection();
		try (PreparedStatement statement = insert.toStatement(connection)) {
			logger.log(Level.FINE, "DBJet - {0}", statement);
			if (statement.executeUpdate() > 0) {
				return model;
			} else {
				throw new JetQueryException("Unable to execute the query.");
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			throw new JetResourceConflictException(e.getMessage());
		} finally {
			closeConnectionIfRequired(connection);	
		}
	}

	/**
	 * Update the record based on primary column.
	 * 
	 * @param pkValue
	 * @param model
	 * @throws SQLException
	 * @throws JetException
	 */
	@Override
	public void update(Object pkValue, T model) throws SQLException, JetException {
		JetDMLQuery update = new JetQueryUpdate(pkValue, model);
		Connection connection = getConnection();
		try (PreparedStatement statement = update.toStatement(connection)) {
			logger.log(Level.FINE, "DBJet - {0}", statement);
			if (statement.executeUpdate() == 0) {
				throw new JetResourceNotFoundException(String.format(ERR_RESOURCE_NOT_FOUND, pkValue));
			}
		} finally {
			closeConnectionIfRequired(connection);
		}
	}

	/**
	 * Delete a record by primary column value.
	 * 
	 * @param pkValue
	 * @throws SQLException
	 * @throws JetException
	 */
	@Override
	public void delete(Object pkValue) throws SQLException, JetException {	
		try {
			JetModel model = getModelType().newInstance();
			JetDMLQuery delete = new JetQueryDelete(pkValue, model);
			Connection connection = getConnection();
			try (PreparedStatement statement = delete.toStatement(connection)) {
				logger.log(Level.FINE, "DBJet - {0}", statement);
				if (statement.executeUpdate() == 0) {
					throw new JetResourceNotFoundException(String.format(ERR_RESOURCE_NOT_FOUND, pkValue));
				}
			} finally {
				closeConnectionIfRequired(connection);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new JetException(e.getMessage());
		}
	}

	/**
	 * Get the model by primary column value.
	 * 
	 * @param pkValue
	 * @return
	 * @throws SQLException
	 * @throws JetException
	 */
	@Override
	public T get(Object pkValue) throws SQLException, JetException {		
		try {
			JetModel model = getModelType().newInstance();
			JetDMLQuery get = new JetQueryGet(pkValue, model);
			Connection connection = getConnection();
			try (PreparedStatement statement = get.toStatement(connection)) {
				logger.log(Level.FINE, "DBJet - {0}", statement);
				try (ResultSet resultSet = statement.executeQuery()) {
					T resource = toModel(resultSet, model.getClass());
					if (resource == null) {
						throw new JetResourceNotFoundException(String.format(ERR_RESOURCE_NOT_FOUND, pkValue));
					} else {
						return resource;
					}
				}
			} catch (IOException e) {
				throw new JetException(e);
			} finally {
				closeConnectionIfRequired(connection);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new JetException(e.getMessage());
		}
	}

	/**
	 * Search the records based on given filter criteria and controls. 
	 * 
	 * @param filter
	 * @param control
	 * @return
	 * @throws SQLException
	 * @throws JetException
	 */
	@Override
	public List<T>search(JetSearchCriteria filter, JetSearchControl control) throws SQLException, JetException {
		try {
			JetModel model = getModelType().newInstance();
			JetDMLQuery search = new JetQuerySearch(model, filter, control);
			Connection connection = getConnection();
			try (PreparedStatement statement = search.toStatement(connection)) {
				logger.log(Level.FINE, "DBJet - {0}", statement);
				try (ResultSet resultSet = statement.executeQuery()) {
					return toModelList(resultSet, model.getClass());									
				}
			} catch (IOException e) {
				throw new JetException(e);
			} finally {
				closeConnectionIfRequired(connection);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new JetException(e.getMessage());
		}
	}

	/**
	 * Count the records based on given filter criteria.
	 * 
	 * @param filter
	 * @return
	 * @throws SQLException
	 * @throws JetException
	 */
	@Override
	public Long count(JetSearchCriteria filter) throws SQLException, JetException {
		try {
			JetModel model = getModelType().newInstance();
			JetDMLQuery search = new JetQueryCount(model, filter);
			Connection connection = getConnection();
			try (PreparedStatement statement = search.toStatement(connection)) {
				logger.log(Level.FINE, "DBJet - {0}", statement);
				try (ResultSet resultSet = statement.executeQuery()) {
					if (resultSet.next()) {
						return resultSet.getLong(1);
					} else {
						return Long.valueOf(0L);
					}
				}
			} finally {
				closeConnectionIfRequired(connection);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			throw new JetException(e.getMessage());
		}
	}

	protected final Connection getConnection() throws SQLException {
		if (Objects.nonNull(transaction) && transaction.isActive()) {
			logger.log(Level.FINE, "Using DB connection within transaction: " + transaction.getName());
			return this.transaction.getConnection();
		} else {
			logger.log(Level.FINE, "Using new DB connection");
			this.transaction = null;
			return JetConnectionPoolFactory.getConnectionPoolFactory().getDataSource().getConnection();			
		}
	}

	protected final void closeConnectionIfRequired(Connection connection) {
		if (Objects.isNull(transaction) && Objects.nonNull(connection)) {
			try {
				connection.close();
				logger.log(Level.FINE, "DB Connection closed");
			} catch (Exception e) {
				// Do nothing.
			}
		} // else - transaction is open, connection should be closed explicitly.
	}

	protected final T toModel(ResultSet resultSet, Class<?> toModel) 
			throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, IOException {
		if (resultSet.next()) {
			ResultSetMetaData metadata = resultSet.getMetaData();
			Set<String> columns = new HashSet<String>();
			for (int index = 1; index <= metadata.getColumnCount(); index++) {
				columns.add(metadata.getColumnName(index));
			}
			return toModel(resultSet, columns, toModel);
		} else {
			return null;
		}
	}

	protected final List<T> toModelList(ResultSet resultSet, Class<?> toModel) 
			throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, IOException {
		List<T> list = new LinkedList<>();
		while (resultSet.next()) {
			ResultSetMetaData metadata = resultSet.getMetaData();
			Set<String> columns = new HashSet<String>();
			for (int index = 1; index <= metadata.getColumnCount(); index++) {
				columns.add(metadata.getColumnName(index));
			}
			list.add(toModel(resultSet, columns, toModel));
		}
		return list;
	}

	@SuppressWarnings("unchecked")
	protected final T toModel(ResultSet resultSet, Set<String> columns, Class<?> toModel) 
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, SQLException, IOException {		
		T model = (T) toModel.newInstance();
		Set<Field> fields = CommonUtils.findClassFields(toModel, JetColumn.class);
		for (Field field: fields) {
			JetColumn column = field.getAnnotation(JetColumn.class);
			if (columns.contains(column.name())) {
				field.setAccessible(true);
				if (column.type() == JetColumnType.TEXT || column.type() == JetColumnType.BIGTEXT) {
					String vaule = resultSet.getString(column.name());
					JetMarshaller<String> marshaller = column.marshaller().newInstance();
					
					//unmarshal the value
					String umlValue = marshaller.unmarshal(column, vaule);
					field.set(model, umlValue);
				} else if (column.type() == JetColumnType.BINARY) {
					InputStream input = resultSet.getBinaryStream(column.name());
					byte[] vaule = (input == null ? null : ByteStreams.toByteArray(input));
					JetMarshaller<byte[]> marshaller = column.marshaller().newInstance();					

					//unmarshal the value					
					byte[] umlValue = marshaller.unmarshal(column, vaule);
					field.set(model, umlValue);
				} else if (column.type() == JetColumnType.FLOAT) { 
					field.set(model, resultSet.getFloat(column.name()));
				} else if (column.type() == JetColumnType.LONG) { 
					field.set(model, resultSet.getLong(column.name()));
				} else if (column.type() == JetColumnType.TIMESTAMP) { 
					field.set(model, resultSet.getTimestamp(column.name()));
				} else if (column.type() == JetColumnType.DATETIME) { 
					field.set(model, resultSet.getDate(column.name()));
				} else if (column.type() == JetColumnType.ENUM) {
					String val = resultSet.getString(column.name());
					if (val != null) {
						Enum<?> enumx = createEnumInstance(val, field.getType());
						field.set(model, enumx);
					}
				} else {
					field.set(model, resultSet.getObject(column.name()));
				}
			}
		}
		return model;
	}

	@SuppressWarnings({ "unchecked", "hiding" })
	private <T extends Enum<T>> T createEnumInstance(String name, Type type) {
		return Enum.valueOf((Class<T>) type, name);
	}


	/**
	 * Begin with an existing DB transaction to perform DB operations.
	 * 
	 * @param transcation
	 * @throws SQLException
	 * @throws JetQueryException
	 */
	public final void setTransaction(JetTransaction transcation) throws SQLException, JetQueryException {
		Objects.requireNonNull(transcation, "Transaction can not be null.");
		if (Objects.nonNull(this.transaction) && this.transaction.isActive()) {
			throw new JetQueryException("Active transaction already exists");
		} else {
			// no existing txn.
			this.transaction = transcation;			
		}
	}

	@SuppressWarnings("unchecked")
	protected final Class<T> getModelType() {
		return (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
	}
}
