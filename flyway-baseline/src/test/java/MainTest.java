

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public void test_baseline_to_not_empty_db_without_baselineVersion_default_V1() {
		Flyway flyway = new Flyway();
		flyway.setDataSource(DataSourceManager.getInstance().getSource());
		flyway.clean();
		DataSourceManager.getInstance().getJdbcTemplate().update("CREATE TABLE users2 (id SERIAL PRIMARY KEY, email TEXT)", new HashMap());
		DataSourceManager.getInstance().getJdbcTemplate().update("Insert into users2(email) values('a@a.fr')", new HashMap());
		flyway.setBaselineOnMigrate(true); // without_baselineVersion_default_V1: not execute V1__Xx.sql, V1.0__Xx.sql, V1.0.0__Xx.sql, but execute V1.1__Xx.sql, V1.0.1__Xx.sql
		flyway.migrate();
		
		List<Map<String, Object>> list = DataSourceManager.getInstance().getJdbcTemplate().queryForList("select * from SCHEMA_VERSION", new HashMap());
		System.out.println("----------------test_baseline_to_not_empty_db_without_baselineVersion_default_V1---------------------");
		list.stream().forEach(map->{
				System.out.println("------------");
				System.out.println("installed_rank: " + map.get("installed_rank"));
				System.out.println("version: " + map.get("version"));
				System.out.println("description: " + map.get("description"));
				System.out.println("type: " + map.get("type"));
				System.out.println("script: " + map.get("script"));
				System.out.println("checksum: " + map.get("checksum"));
				System.out.println("installed_by: " + map.get("installed_by"));
				System.out.println("installed_on: " + map.get("installed_on"));
				System.out.println("execution_time: " + map.get("execution_time"));
				System.out.println("success: " + map.get("success"));
			});
		
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from users2", new HashMap(), Integer.class)==1);
		
		//if first sql: V1__Xx.sql, V1.0__Xx.sql, V1.0.0__Xx.sql
		Assertions.assertThat(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from SCHEMA_VERSION", new HashMap(), Integer.class)).isEqualTo(3);
		Assertions.assertThatThrownBy(()->DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from users", new HashMap(), Integer.class)).isInstanceOf(BadSqlGrammarException.class);
		
		//if first sql: V1.0.1__Create_tables.sql
//		Assertions.assertThat(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from SCHEMA_VERSION", new HashMap(), Integer.class)).isEqualTo(4);
//		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from users", new HashMap(), Integer.class)==0); 
		
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from a", new HashMap(), Integer.class)==0);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from b", new HashMap(), Integer.class)==0);
	}
	
	@Test 
	public void test_baseline_to_empty_db_without_baselineVersion_default_V1() {
		Flyway flyway = new Flyway();
		flyway.setDataSource(DataSourceManager.getInstance().getSource());
		flyway.clean();
		flyway.setBaselineOnMigrate(true); // without_baselineVersion_default_V1: not execute V1__Xx.sql, V1.0__Xx.sql, V1.0.0__Xx.sql, but execute V1.1__Xx.sql, V1.0.1__Xx.sql
		flyway.migrate();
		
		List<Map<String, Object>> list = DataSourceManager.getInstance().getJdbcTemplate().queryForList("select * from SCHEMA_VERSION", new HashMap());
		System.out.println("----------------test_baseline_to_empty_db_without_baselineVersion_default_V1---------------------");
		list.stream().forEach(map->{
				System.out.println("------------");
				System.out.println("installed_rank: " + map.get("installed_rank"));
				System.out.println("version: " + map.get("version"));
				System.out.println("description: " + map.get("description"));
				System.out.println("type: " + map.get("type"));
				System.out.println("script: " + map.get("script"));
				System.out.println("checksum: " + map.get("checksum"));
				System.out.println("installed_by: " + map.get("installed_by"));
				System.out.println("installed_on: " + map.get("installed_on"));
				System.out.println("execution_time: " + map.get("execution_time"));
				System.out.println("success: " + map.get("success"));
			});
		Assertions.assertThat(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from SCHEMA_VERSION", new HashMap(), Integer.class)).isEqualTo(3);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from users", new HashMap(), Integer.class)==0);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from a", new HashMap(), Integer.class)==0);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from b", new HashMap(), Integer.class)==0);
	}
	
	@Test 
	public void test_baseline_to_not_empty_db() {
		Flyway flyway = new Flyway();
		flyway.setDataSource(DataSourceManager.getInstance().getSource());
		flyway.clean();
		DataSourceManager.getInstance().getJdbcTemplate().update("CREATE TABLE users2 (id SERIAL PRIMARY KEY, email TEXT)", new HashMap());
		DataSourceManager.getInstance().getJdbcTemplate().update("Insert into users2(email) values('a@a.fr')", new HashMap());
		flyway.setBaselineOnMigrate(true);
		flyway.setBaselineVersionAsString("2.0.1"); // not execute V2.0.1__XXX.sql
		flyway.migrate();
		
		List<Map<String, Object>> list = DataSourceManager.getInstance().getJdbcTemplate().queryForList("select * from SCHEMA_VERSION", new HashMap());
		System.out.println("----------------test_baseline_to_not_empty_db---------------------");
		list.stream().forEach(map->{
				System.out.println("------------");
				System.out.println("installed_rank: " + map.get("installed_rank"));
				System.out.println("version: " + map.get("version"));
				System.out.println("description: " + map.get("description"));
				System.out.println("type: " + map.get("type"));
				System.out.println("script: " + map.get("script"));
				System.out.println("checksum: " + map.get("checksum"));
				System.out.println("installed_by: " + map.get("installed_by"));
				System.out.println("installed_on: " + map.get("installed_on"));
				System.out.println("execution_time: " + map.get("execution_time"));
				System.out.println("success: " + map.get("success"));
			});
		Assertions.assertThat(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from SCHEMA_VERSION", new HashMap(), Integer.class)).isEqualTo(2);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from users2", new HashMap(), Integer.class)==1);
		Assertions.assertThatThrownBy(()->DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from users", new HashMap(), Integer.class)).isInstanceOf(BadSqlGrammarException.class);
		Assertions.assertThatThrownBy(()->DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from a", new HashMap(), Integer.class)).isInstanceOf(BadSqlGrammarException.class);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from b", new HashMap(), Integer.class)==0);
	}
	
	@Test 
	public void test_baseline_to_empty_db() {
		
		Flyway flyway = new Flyway();
		flyway.setDataSource(DataSourceManager.getInstance().getSource());
		flyway.clean();
		flyway.setBaselineOnMigrate(true);
		flyway.setBaselineVersionAsString("2.0.1");// not execute V2.0.1__XXX.sql
		flyway.migrate();
		
		List<Map<String, Object>> list = DataSourceManager.getInstance().getJdbcTemplate().queryForList("select * from SCHEMA_VERSION", new HashMap());
		System.out.println("----------------test_baseline_to_empty_db---------------------");
		list.stream().forEach(map->{
				System.out.println("------------");
				System.out.println("installed_rank: " + map.get("installed_rank"));
				System.out.println("version: " + map.get("version"));
				System.out.println("description: " + map.get("description"));
				System.out.println("type: " + map.get("type"));
				System.out.println("script: " + map.get("script"));
				System.out.println("checksum: " + map.get("checksum"));
				System.out.println("installed_by: " + map.get("installed_by"));
				System.out.println("installed_on: " + map.get("installed_on"));
				System.out.println("execution_time: " + map.get("execution_time"));
				System.out.println("success: " + map.get("success"));
			});
	
		Assertions.assertThat(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from SCHEMA_VERSION", new HashMap(), Integer.class)).isEqualTo(3);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from users", new HashMap(), Integer.class)==0);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from a", new HashMap(), Integer.class)==0);
		assertTrue(DataSourceManager.getInstance().getJdbcTemplate().queryForObject("select count(*) from b", new HashMap(), Integer.class)==0);
	}

}
