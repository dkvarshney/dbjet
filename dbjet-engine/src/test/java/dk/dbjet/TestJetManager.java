package dk.dbjet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import dk.dbjet.common.AccountManager;
import dk.dbjet.common.AccountModel;
import dk.dbjet.common.JetInitializer;
import dk.dbjet.config.JetConfig;
import dk.dbjet.config.JetDatabaseConfig;
import dk.dbjet.config.JetDatabaseConfig.PoolProvider;
import dk.dbjet.exception.JetException;
import dk.dbjet.exception.JetRuntimeException;
import dk.dbjet.factory.JetConnectionPoolFactory;
import dk.dbjet.filter.JetEqualsSearchAttribute;
import dk.dbjet.filter.JetSearchControl;
import dk.dbjet.filter.JetSearchControlBuilder;
import dk.dbjet.filter.JetSearchCriteriaBuilder;

@Ignore("Enabled when required")
public class TestJetManager {

	private String ACCOUNT_ID = UUID.randomUUID().toString();
	private String ACCOUNT_NAME = "Test Case";
	private String ACCOUNT_EMAIL = "account@test-example.com";
	
	private static JetConfig config;
	private static AccountManager manager;

	@BeforeClass
	public static void beforeSetup() throws SQLException, Exception {
		config = new JetConfig() {			
			@Override
			public List<String> getModelClassPackages() {
				return Arrays.asList("dk.dbjet.common");
			}

			@Override
			public JetDatabaseConfig getDatabaseConfig() {
				JetDatabaseConfig config = new JetDatabaseConfig();
				config.setJdbcDriver("org.mariadb.jdbc.Driver");
				config.setJdbcUrl("jdbc:mariadb://localhost:3306/test");
				config.setPoolProvider(PoolProvider.ApacheDBCP);
				config.setMaximumPoolSize(10);
				config.setDbUsername("super");
				config.setDbPassword("welcome1");
				return config;
			}
		};
		
		// enable table creation as per the model.
		JetInitializer.initialize(config, true);
		
		// init the manager.
		manager = new AccountManager();
	}

	@AfterClass
	public static void afterSetup() throws SQLException {
		try (Connection con = JetConnectionPoolFactory.getConnectionPoolFactory().getDataSource().getConnection()) {			
			try (PreparedStatement statement = con.prepareStatement("DROP TABLE TEST_ACCOUNT_TABLE;")) {
				System.out.println("## TEST_ACCOUNT_TABLE table dropped!");
				statement.execute();
			}
		}
	}
	
	@Before
	public void beforeTest() {

	}

	@After
	public void afterTest() throws SQLException, JetException {
		manager.delete(ACCOUNT_ID);
	}

	@Test
	public void testInsertAndGetAccount() throws SQLException, JetException {
		AccountModel insertModel = new AccountModel();
		insertModel.setId(ACCOUNT_ID);
		insertModel.setName(ACCOUNT_NAME);
		insertModel.setEmail(ACCOUNT_EMAIL);
		insertModel.setAge(20);
		insertModel.setKey("should be persist as base64 by JetBase64Marshaller");
		insertModel.setSalary(1200.50f);
		insertModel.setAvatar("avatar".getBytes());
		
		// insert the record.
		manager.insert(insertModel);
		
		// get the record by id.
		AccountModel getModel = manager.get(ACCOUNT_ID);
		
		Assert.assertNotNull(getModel);
		Assert.assertEquals(getModel.getId(), ACCOUNT_ID);
		Assert.assertEquals(getModel.getName(), insertModel.getName());
		Assert.assertEquals(getModel.getEmail(), insertModel.getEmail());
		Assert.assertEquals(getModel.getAge(), insertModel.getAge());
		Assert.assertEquals(getModel.getKey(), insertModel.getKey());
		Assert.assertEquals(getModel.getSalary(), insertModel.getSalary(), 1);
		Assert.assertArrayEquals(getModel.getAvatar(), insertModel.getAvatar());
	}
	
	@Test
	public void testUpdateAndGetAccount() throws SQLException, JetException {
		AccountModel insertModel = new AccountModel();
		insertModel.setId(ACCOUNT_ID);
		insertModel.setName(ACCOUNT_NAME);
		insertModel.setEmail(ACCOUNT_EMAIL);
		
		// insert the record.
		manager.insert(insertModel);
		
		
		AccountModel updateModel = new AccountModel();
		updateModel.setName(ACCOUNT_NAME + "TESTUPDATE");
		updateModel.setEmail(ACCOUNT_EMAIL + "TESTUPDATE");
		// update the record.
		manager.update(ACCOUNT_ID, updateModel);
		
		// get the record.
		AccountModel getModel = manager.get(ACCOUNT_ID);
		
		Assert.assertNotNull(getModel);
		Assert.assertEquals(getModel.getId(), ACCOUNT_ID);
		Assert.assertEquals(getModel.getName(), updateModel.getName());
		Assert.assertEquals(getModel.getEmail(), updateModel.getEmail());
	}
	
	@Test
	public void testInsertAndSearchAccount() throws SQLException, JetException {
		AccountModel insertModel = new AccountModel();
		insertModel.setId(ACCOUNT_ID);
		insertModel.setName(ACCOUNT_NAME);
		insertModel.setEmail(ACCOUNT_EMAIL);
		
		// insert the record.
		manager.insert(insertModel);
		
		// search the records.
		JetSearchCriteriaBuilder criteria = JetSearchCriteriaBuilder.with(new JetEqualsSearchAttribute("EMAIL", ACCOUNT_EMAIL));
		JetSearchControl control = JetSearchControlBuilder.builder().build();	
		List<AccountModel> list = manager.search(criteria, control);
		
		// assert result.
		Assert.assertNotNull(list);
		Assert.assertTrue("size: " + list.size(), list.size() == 1);
		Assert.assertEquals(list.get(0).getId(), insertModel.getId());
		Assert.assertEquals(list.get(0).getName(), insertModel.getName());
		Assert.assertEquals(list.get(0).getEmail(), insertModel.getEmail());
	}
	
	@Test
	public void testInsertAndSearchAccountWithBlacklistedChars() throws SQLException, JetException {
		AccountModel insertModel = new AccountModel();
		insertModel.setId(ACCOUNT_ID);
		insertModel.setName(ACCOUNT_NAME);
		insertModel.setEmail(ACCOUNT_EMAIL);
		
		// insert the record.
		manager.insert(insertModel);
				
		try {
			// search the records.
			JetSearchCriteriaBuilder criteria = JetSearchCriteriaBuilder.with(new JetEqualsSearchAttribute("EMAIL", "any blacklist char;"));
			JetSearchControl control = JetSearchControlBuilder.builder().build();	
			
			manager.search(criteria, control);
		} catch (JetRuntimeException e) {
			Assert.assertTrue(e.getMessage().contains("Blacklisted chars were supplied"));
		}
	}
}
