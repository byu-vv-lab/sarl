package edu.udel.cis.vsl.sarl.ideal.simplify;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class IdealSimplifierSimpExprTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		CommonObjects.setUp();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		assertEquals(true, true);
	}

}
