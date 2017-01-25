

import java.util.HashMap;

import org.flywaydb.core.Flyway;
import org.junit.Test;

import junit.framework.TestCase;

public class MainTest extends TestCase{

	@Test
	public void testGetConnection() {
		assertNotNull(DataSourceManager.getInstance().getConnection());
	}

	/**
	 * Test method for {@link fr.argus.socle.db.DataSourceManager#getInstance()}
	 * .
	 */
	@Test
	public void testGetInstance() {
		assertNotNull(DataSourceManager.getInstance());
	}
	
	@Test 
	public void testInsertUser() {
		DataSourceManager.getInstance().getJdbcTemplate().update("CREATE TABLE users2 (id SERIAL PRIMARY KEY, email TEXT)", new HashMap());
		DataSourceManager.getInstance().getJdbcTemplate().update("Insert into users2(email) values('a@a.fr')", new HashMap());
		System.setProperty("flyway.baselineVersion", "1.0.1");
		
		Flyway flyway = new Flyway();
		flyway.setDataSource(DataSourceManager.getInstance().getSource());
		flyway.setBaselineOnMigrate(true);
		flyway.migrate();
		
		String email = DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select * from users where email='a@a.fr'", new HashMap(), String.class);
		assertTrue(email.equals("a@a.fr"));
	}

}
