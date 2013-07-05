package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;

public class BooleanTest {

	SymbolicUniverse universe = SARL.newStandardUniverse();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		BooleanExpression p = universe.falseExpression();
		BooleanExpression q = universe.not(p);

		assertEquals(universe.trueExpression(), q);
	}

}
