package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.universe.Universes;

public class ArrayTest {

	private static PrintStream out = System.out;
	private SymbolicUniverse universe;
	// private NumberFactory numberFactory;
	private StringObject a_obj; // "a"
	// private StringObject b_obj; // "b"
	// private SymbolicType realType;
	private SymbolicType integerType;
	private NumericExpression zero, one, two, five, six, seventeen; // integer
																	// constants
	private List<NumericExpression> list1; // {5,6}
	private List<NumericExpression> list2; // {17}

	@Before
	public void setUp() throws Exception {
		universe = Universes.newIdealUniverse();
		// realType = universe.realType();
		integerType = universe.integerType();
		a_obj = universe.stringObject("a");
		// b_obj = universe.stringObject("b");
		zero = universe.integer(0);
		one = universe.integer(1);
		two = universe.integer(2);
		five = universe.integer(5);
		six = universe.integer(6);
		seventeen = universe.integer(17);
		list1 = Arrays.asList(new NumericExpression[] { five, six });
		list2 = Arrays.asList(new NumericExpression[] { seventeen });
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void arrayRead1() {
		SymbolicExpression a = universe.array(integerType, list1);

		out.println("arrayRead1: a = " + a);
		assertEquals(universe.arrayType(integerType, two), a.type());
		assertEquals(five, universe.arrayRead(a, zero));
		assertEquals(six, universe.arrayRead(a, one));
	}

	@Test
	public void jagged1() {
		SymbolicArrayType t1 = universe.arrayType(integerType);
		SymbolicArrayType t2 = universe.arrayType(t1, universe.integer(2));
		SymbolicExpression a1 = universe.array(integerType, list1);
		SymbolicExpression a2 = universe.array(integerType, list2);
		SymbolicExpression a = universe.symbolicConstant(a_obj, t2);

		assertTrue(t2.isComplete());
		a = universe.arrayWrite(a, zero, a1);
		a = universe.arrayWrite(a, one, a2);
		out.println("jagged1: a = " + a);
		// a={{5,6},{17}}
		assertEquals(five,
				universe.arrayRead(universe.arrayRead(a, zero), zero));
		assertEquals(six, universe.arrayRead(universe.arrayRead(a, zero), one));
		assertEquals(seventeen,
				universe.arrayRead(universe.arrayRead(a, one), zero));
		assertTrue(((SymbolicArrayType) a.type()).isComplete());
		assertEquals(two, universe.length(a));
		assertEquals(two, universe.length(universe.arrayRead(a, zero)));
		assertEquals(one, universe.length(universe.arrayRead(a, one)));
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
		SymbolicExpression a = universe.symbolicConstant(a_obj, t);
		NumericExpression zero = universe.integer(0);
		NumericExpression twoInt = universe.integer(2);
		SymbolicExpression read;

		a = write2d(a, zero, zero, twoInt);
		read = read2d(a, zero, zero);
		assertEquals(twoInt, read);
		// for the heck of it...
		out.println("array2d: new row is: " + universe.arrayRead(a, zero));
	}

}
