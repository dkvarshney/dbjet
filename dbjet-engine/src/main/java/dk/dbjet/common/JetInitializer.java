package dk.dbjet.common;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.reflections.Reflections;

import dk.dbjet.annotation.JetColumn;
import dk.dbjet.annotation.JetColumnIsPrimary;
import dk.dbjet.annotation.JetColumnType;
import dk.dbjet.annotation.JetTable;
import dk.dbjet.config.JetConfig;
import dk.dbjet.exception.JetException;
import dk.dbjet.factory.JetConnectionPoolFactory;
import dk.dbjet.factory.JetLoggerFactory;
import dk.dbjet.factory.JetReflectionFactory;
import dk.dbjet.sql.ddl.JetQueryTableCreate;

public class JetInitializer {

	private static Logger logger = JetLoggerFactory.getLogger(JetInitializer.class);
	private static String MODEL_FIELD_MISTMATCH_PATTERN = "%s.%s must be type of %s only.";
	private static String MODEL_FIELD_PRIMARY_PATTERN = "%s.%s is not allowed to be primary.";

	/**
	 * Must be very first call to initialize database pools and to create schema.
	 * 
	 * @param config
	 * @param initSchema
	 * @throws SQLException
	 * @throws Exception
	 */
	public static void initialize(JetConfig config, boolean initSchema) throws SQLException, Exception {
		logger.log(Level.INFO, "DBJet - {0}", "initialization start");		
		JetConnectionPoolFactory.getConnectionPoolFactory().initialize(config.getDatabaseConfig());
		Reflections reflections = new Reflections(config.getModelClassPackages());
		Set<Class<? extends JetModel>> models = reflections.getSubTypesOf(JetModel.class);
		logger.log(Level.INFO, "DBJet - Total model implementation found: {0}", models.size());
		for (Class<? extends JetModel> model : models) {			
			validateModel(model);
			if (initSchema) {
				if (Modifier.isInterface(model.getModifiers())) {
					logger.log(Level.INFO, "DBJet - Skipping interface {0}", model.getName());
				} else if (Modifier.isAbstract(model.getModifiers())) {
					logger.log(Level.INFO, "DBJet - Skipping abstract class {0}", model.getName());					
				} else {
					logger.log(Level.INFO, "DBJet - Creating schema for: {0}", model.getName());
					updateSchema(model);
				}
			}
		}			
		logger.log(Level.INFO, "DBJet - {0}", "initialization finished.");		
	}


	private static void validateModel(Class<? extends JetModel> model) throws JetException {
		JetTable tableAnt = model.getAnnotation(JetTable.class);
		if (Objects.isNull(tableAnt) && !Modifier.isAbstract(model.getModifiers()) && !model.isInterface()) {
			throw new JetException(String.format("Missing @JetTable annotation in model: %s", model.getCanonicalName()));
		}

		List<Field> fields = JetReflectionFactory.getInstance().getColumnFields(model);
		for (Field field : fields) {
			JetColumn column = field.getAnnotation(JetColumn.class);
			for (JetColumnType jetColumn : JetColumnType.values()) {
				if ((column.type() == jetColumn)) {
					// column of same type found.
					if (!(jetColumn.getAllowedType().isAssignableFrom(field.getType()))) {
						throw new JetException(
								String.format(MODEL_FIELD_MISTMATCH_PATTERN, model.getCanonicalName(), field.getName(), jetColumn.getAllowedType()));
					}
					if ((field.isAnnotationPresent(JetColumnIsPrimary.class) && !jetColumn.isAllowedAsPrimary())) {
						throw new JetException(
								String.format(MODEL_FIELD_PRIMARY_PATTERN, model.getCanonicalName(), field.getName()));
					}	
				}
			}
		}
	}

	/**
	 * update the table schema.
	 * 
	 * @param model
	 * @throws SQLException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private static void updateSchema(Class<? extends JetModel> model) throws SQLException, InstantiationException, IllegalAccessException {
		final String sql = new JetQueryTableCreate(model.newInstance()).toSQLString();
		logger.log(Level.INFO, "DBJet - {0}", sql);
		try (Connection con = JetConnectionPoolFactory.getConnectionPoolFactory().getDataSource().getConnection()) {
			con.setAutoCommit(true);			
			try (Statement stmt = con.createStatement()) {
				stmt.executeUpdate(sql);
			}
		}
	}
}
