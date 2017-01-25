

import java.sql.Connection;
import java.sql.SQLException;

import org.postgresql.ds.PGPoolingDataSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

public class DataSourceManager {

	String host="localhost";
	Integer port=5432;
	String database="flyway";
	String user="dbuser";
	String password="dbuser";
	Integer poolSize=10;
			
	
	private static PGPoolingDataSource source = new PGPoolingDataSource();
	
	public PGPoolingDataSource getSource() {
		return source;
	}

	private final NamedParameterJdbcTemplate jdbcTemplate;

	private DataSourceManager() {
		init();
		jdbcTemplate = new NamedParameterJdbcTemplate(source);
	}

	private static class DataSourceManagerHolder {
		private static final DataSourceManager instance = new DataSourceManager();
	}

	/**
	 * Initialisation de pool de connexion.
	 */
	private void init() {
		try {
			source.setDataSourceName("name");
			source.setServerName(host);
			source.setDatabaseName(database);
			source.setUser(user);
			source.setPassword(password);
			source.setMaxConnections(poolSize);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
	}

	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	
	/**
	 * récupérer une connecxion depuis le pool de connexion.
	 * 
	 * @return Une connexion avec la base de données.
	 */
	public Connection getConnection() {
		try {
			return source.getConnection();

		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}

	}

	public static DataSourceManager getInstance() {
		return DataSourceManagerHolder.instance;
	}
}
