package depprj;

import org.junit.Test;

import junit.framework.TestCase;

public class MainTest extends TestCase{

	@Test
	public void test() {
		assertTrue(new Main().func().equals("correct"));
	}
}
