package dk.dbjet.config;

import java.util.List;

public interface JetConfig {

	List<String> getModelClassPackages();
	
	JetDatabaseConfig getDatabaseConfig();
}
