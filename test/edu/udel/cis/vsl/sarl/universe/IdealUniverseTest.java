package edu.udel.cis.vsl.sarl.universe;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.util.SingletonMap;

public class IdealUniverseTest {

	private static PrintStream out = System.out;
	private SymbolicUniverse universe;
	// private NumberFactory numberFactory;
	private StringObject Xobj; // "X"
	private StringObject Yobj; // "Y"
	private SymbolicType realType, integerType;
	private NumericSymbolicConstant x; // real symbolic constant "X"
	private NumericSymbolicConstant y; // resl symbolic constant "Y"
	private NumericExpression two; // real 2.0
	private NumericExpression three; // real 3.0

	@Before
	public void setUp() throws Exception {
		universe = Universes.newIdealUniverse();
		// numberFactory = universe.numberFactory();
		Xobj = universe.stringObject("X");
		Yobj = universe.stringObject("Y");
		realType = universe.realType();
		integerType = universe.integerType();
		x = (NumericSymbolicConstant) universe.symbolicConstant(Xobj, realType);
		y = (NumericSymbolicConstant) universe.symbolicConstant(Yobj, realType);
		two = universe.castToReal(universe.integer(2));
		three = universe.castToReal(universe.integer(3));

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

	@Test
	public void subArray1() {
		int n = 3;
		SymbolicConstant[] sc = new SymbolicConstant[n];
		Map<SymbolicConstant, SymbolicExpression> map = new HashMap<SymbolicConstant, SymbolicExpression>();
		SymbolicExpression array1 = universe.symbolicConstant(
				universe.stringObject("a"), universe.arrayType(integerType));
		SymbolicExpression array2;

		for (int i = 0; i < n; i++)
			sc[i] = universe.symbolicConstant(universe.stringObject("x" + i),
					integerType);
		for (int i = 0; i < n; i++)
			map.put(sc[i], sc[n - 1 - i]);
		for (int i = 0; i < n; i++)
			array1 = universe.arrayWrite(array1, universe.integer(i), sc[i]);
		array2 = universe.substitute(array1, map);
		out.println("subArray1: array1 = " + array1);
		out.println("subArray1: array2 = " + array2);
		for (int i = 0; i < n; i++)
			assertEquals(sc[n - 1 - i],
					universe.arrayRead(array2, universe.integer(i)));
	}

	private SymbolicExpression write2d(SymbolicExpression array,
			NumericExpression i, NumericExpression j, SymbolicExpression value) {
		SymbolicExpression row = universe.arrayRead(array, i);
		SymbolicExpression newRow = universe.arrayWrite(row, j, value);

		return universe.arrayWrite(array, i, newRow);
	}

	private SymbolicExpression read2d(SymbolicExpression array,
			NumericExpression i, NumericExpression j) {
		SymbolicExpression row = universe.arrayRead(array, i);

		return universe.arrayRead(row, j);
	}

	/**
	 * Write and read a 2d array.
	 */
	@Test
	public void array2d() {
		SymbolicArrayType t = universe.arrayType(universe
				.arrayType(integerType));
		SymbolicExpression a = universe.symbolicConstant(
				universe.stringObject("a"), t);
		NumericExpression zero = universe.zeroInt();
		NumericExpression twoInt = universe.integer(2);
		SymbolicExpression read;

		a = write2d(a, zero, zero, twoInt);
		read = read2d(a, zero, zero);
		assertEquals(twoInt, read);
		// for the heck of it...
		out.println("array2d: new row is: " + universe.arrayRead(a, zero));
	}
}
