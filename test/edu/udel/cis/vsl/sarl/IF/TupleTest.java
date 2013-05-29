package edu.udel.cis.vsl.sarl.IF;

import static org.junit.Assert.assertEquals;

import java.io.PrintStream;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;

public class TupleTest {

	private final static PrintStream out = System.out;

	private SymbolicUniverse universe;

	private SymbolicType integerType;

	private NumericExpression one, two, three;

	@Before
	public void setUp() throws Exception {
		universe = SARL.newStandardUniverse();
		integerType = universe.integerType();
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void tuple1() {
		SymbolicTupleType tupleType = universe.tupleType(
				universe.stringObject("tup"),
				Arrays.asList(new SymbolicType[] { integerType, integerType }));
		SymbolicExpression tuple = universe.tuple(tupleType,
				Arrays.asList(new SymbolicExpression[] { one, two }));
		SymbolicExpression expected = universe.tuple(tupleType,
				Arrays.asList(new SymbolicExpression[] { three, two }));
		IntObject i0 = universe.intObject(0);
		IntObject i1 = universe.intObject(1);

		out.println("tuple1: tuple = " + tuple);
		tuple = universe.tupleWrite(tuple, i0, three);
		out.println("tuple1: tuple = " + tuple);
		assertEquals(expected, tuple);
		assertEquals(three, universe.tupleRead(tuple, i0));
		assertEquals(two, universe.tupleRead(tuple, i1));
		tuple = universe.tupleWrite(tuple, i1, one);
		assertEquals(three, universe.tupleRead(tuple, i0));
		assertEquals(one, universe.tupleRead(tuple, i1));
	}

}
