package flyway;

import java.util.HashMap;

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
		DataSourceManager.getInstance().getJdbcTemplate().update("Insert into users(email) values('a@a.fr')", new HashMap());
		String email = DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select email from users where email='a@a.fr'", new HashMap(), String.class);
		assertTrue(email.equals("a@a.fr"));
	}

}
