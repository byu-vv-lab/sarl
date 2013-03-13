package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertFalse;

import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class HerbrandTest {

	private static PrintStream out = System.out;
	private SymbolicUniverse universe;
	//private SymbolicType realType, integerType;

	@Before
	public void setUp() throws Exception {
		this.universe = Universes.newHerbrandUniverse();
		// this.realType = universe.realType();
		// this.integerType = universe.integerType();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test12() {
		NumericExpression one = universe.rational(1);
		NumericExpression two = universe.rational(2);
		NumericExpression a = universe.add(one, two);
		NumericExpression b = universe.add(two, one);

		out.println("test12: a = " + a);
		out.println("test12: b = " + b);
		assertFalse(a.equals(b));
	}

}
