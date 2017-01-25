

import java.util.HashMap;

import org.assertj.core.api.Assertions;
import org.flywaydb.core.Flyway;
import org.junit.Test;
import org.springframework.jdbc.BadSqlGrammarException;

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
		
		Assertions.assertThatThrownBy(()->DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from users", new HashMap(), Integer.class)).isInstanceOf(BadSqlGrammarException.class);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from users2", new HashMap(), Integer.class)==1);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from a", new HashMap(), Integer.class)==0);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from b", new HashMap(), Integer.class)==0);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from SCHEMA_VERSION", new HashMap(), Integer.class)==2);
		
	}

}
