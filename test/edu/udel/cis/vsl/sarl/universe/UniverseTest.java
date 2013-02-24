package edu.udel.cis.vsl.sarl.universe;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.util.SingletonMap;

public class UniverseTest {

	private static PrintStream out = System.out;

	private SymbolicUniverse universe;
	// private NumberFactory numberFactory;
	private StringObject Xobj; // "X"
	private StringObject Yobj; // "Y"
	private SymbolicType realType;
	private SymbolicConstant x; // real symbolic constant "X"
	private SymbolicConstant y; // resl symbolic constant "Y"
	private SymbolicExpression two; // real 2.0
	private SymbolicExpression three; // real 3.0

	@Before
	public void setUp() throws Exception {
		universe = Universes.newIdealUniverse();
		// numberFactory = universe.numberFactory();
		Xobj = universe.stringObject("X");
		Yobj = universe.stringObject("Y");
		realType = universe.realType();
		x = universe.symbolicConstant(Xobj, realType);
		y = universe.symbolicConstant(Yobj, realType);
		two = universe.castToReal(universe.symbolic(2));
		three = universe.castToReal(universe.symbolic(3));

		out.println("    x = " + x);
		out.println("    y = " + y);
		out.println("  two = " + two);
		out.println("three = " + three);
		out.println();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Map: x->y. x+2y -> 3y
	 */
	@Test
	public void sub1() {
		SymbolicExpression u = universe.add(x, universe.multiply(two, y));
		// SymbolicExpression v = universe.add(y, universe.multiply(two, x));
		SymbolicExpression expected = universe.multiply(three, y);
		Map<SymbolicConstant, SymbolicExpression> map = new SingletonMap<SymbolicConstant, SymbolicExpression>(
				x, y);
		SymbolicExpression newU = universe.substitute(u, map);

		out.println("sub1:    u = " + u);
		out.println("sub1:  map = " + map);
		out.println("sub1: newU = " + newU);
		assertEquals(expected, newU);
	}

	/**
	 * Map: x->y, y->x. x+2y -> 2x+y
	 */
	@Test
	public void sub2() {
		SymbolicExpression u = universe.add(x, universe.multiply(two, y));
		SymbolicExpression expected = universe
				.add(y, universe.multiply(two, x));
		Map<SymbolicConstant, SymbolicExpression> map = new HashMap<SymbolicConstant, SymbolicExpression>();
		SymbolicExpression newU;

		map.put(x, y);
		map.put(y, x);
		newU = universe.substitute(u, map);
		out.println("sub2:    u = " + u);
		out.println("sub2:  map = " + map);
		out.println("sub2: newU = " + newU);
		assertEquals(expected, newU);
	}
}
