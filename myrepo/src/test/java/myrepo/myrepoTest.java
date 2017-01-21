package myrepo;

import org.junit.Test;

import junit.framework.TestCase;

public class myrepoTest extends TestCase{

	@Test
	public void test() {
		assertTrue(new Myrepo().func().equals("correct"));
	}

}
