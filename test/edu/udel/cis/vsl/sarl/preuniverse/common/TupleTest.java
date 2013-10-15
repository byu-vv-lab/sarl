package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class TupleTest {
	private static PreUniverse universe;

	private static SymbolicType integerType;

	private static SymbolicType realType;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();

		universe = PreUniverses.newPreUniverse(system);
		integerType = universe.integerType();
		realType = universe.realType();
		universe.integer(1);
		universe.integer(2);
		universe.rational(3);
		universe.rational(5);

	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void tupleTypeTest() {

		SymbolicTupleType tupleType1 = universe.tupleType(
				universe.stringObject("tupleType1"),
				Arrays.asList(new SymbolicType[] { integerType, integerType,
						realType }));
		SymbolicTupleType tupleType2 = universe.tupleType(
				universe.stringObject("tupleType1"),
				Arrays.asList(new SymbolicType[] { integerType, integerType,
						realType }));
		SymbolicTupleType tupleType3;
		SymbolicTypeSequence sequence;
		LinkedList<SymbolicType> members = new LinkedList<>();
		members.add(integerType);
		members.add(integerType);
		members.add(realType);
		tupleType3 = universe.tupleType(universe.stringObject("tupleType1"),
				members);

		assertEquals(SymbolicTypeKind.TUPLE, tupleType1.typeKind());

		sequence = tupleType1.sequence();
		assertEquals(integerType, sequence.getType(0));
		assertEquals(integerType, sequence.getType(1));
		assertEquals(realType, sequence.getType(2));
		assertEquals(universe.stringObject("tupleType1"), tupleType1.name());

		assertEquals(tupleType1, tupleType2);
		assertEquals(3, sequence.numTypes());

		assertEquals(tupleType1, tupleType3);

		members.remove();
		members = null;
		assertEquals(tupleType1, tupleType3);

	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void tupleExceptionTest1() {

		SymbolicTupleType tupleType1 = universe.tupleType(
				universe.stringObject("tupleType1"),
				Arrays.asList(new SymbolicType[] { integerType, integerType,
						realType }));
		@SuppressWarnings("unused")
		SymbolicExpression tuple = universe.tuple(
				tupleType1,
				Arrays.asList(new SymbolicExpression[] { universe.integer(1),
						universe.integer(2) }));
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void tupleExceptionTest2() {
		SymbolicTupleType tupleType1 = universe.tupleType(
				universe.stringObject("tupleType1"),
				Arrays.asList(new SymbolicType[] { integerType, integerType,
						realType }));

		@SuppressWarnings("unused")
		SymbolicExpression tuple = universe.tuple(
				tupleType1,
				Arrays.asList(new SymbolicExpression[] { universe.rational(1),
						universe.integer(2), universe.integer(2) }));

	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void tupleWriteTest() {
		SymbolicTupleType tupleType1;
		SymbolicExpression tuple, resultedTuple;
		IntObject i1;
		i1 = universe.intObject(1);
		tupleType1 = universe.tupleType(universe.stringObject("tupleType1"),
				Arrays.asList(new SymbolicType[] { integerType, integerType }));
		tuple = universe.tuple(
				tupleType1,
				Arrays.asList(new SymbolicExpression[] { universe.integer(1),
						universe.integer(2) }));

		resultedTuple = universe.tupleWrite(tuple, i1, universe.integer(2));
		assertEquals(tuple, resultedTuple);

		// exception
		tuple = universe.tupleWrite(tuple, i1, universe.rational(3));

	}

	// written by Mohammad Alsulmi
	@Test
	public void testCompatibleWithTuple() {

		// here we test compatible with tuple types
		SymbolicTupleType type1, type2, type3, type4, type5, type6;
		SymbolicType type7;
		BooleanExpression result, expected;
		LinkedList<SymbolicType> members = new LinkedList<>();

		type1 = universe.tupleType(universe.stringObject("Type1"),
				Arrays.asList(new SymbolicType[] { integerType, integerType }));
		type2 = universe.tupleType(universe.stringObject("Type1"),
				Arrays.asList(new SymbolicType[] { integerType, integerType }));
		type3 = universe.tupleType(universe.stringObject("type2"),
				Arrays.asList(new SymbolicType[] { realType, integerType }));
		type4 = universe.tupleType(universe.stringObject("Type1"),
				Arrays.asList(new SymbolicType[] { integerType, realType }));
		type5 = universe.tupleType(
				universe.stringObject("Type1"),
				Arrays.asList(new SymbolicType[] { integerType, realType,
						integerType }));
		type6 = universe.tupleType(universe.stringObject("Type1"), members);
		type7 = universe.integerType();

		// here we compare two identical tuple types (type1, type2)
		// the expected compatible call should return true
		expected = universe.bool(true);
		result = universe.compatible(type1, type2);
		assertEquals(expected, result);

		// here we compare two different tuple types (type1, type3)
		// the expected compatible call should return false
		expected = universe.bool(false);
		result = universe.compatible(type1, type3);
		assertEquals(expected, result);

		// here we compare a tuple type with integer type (type1, type4)
		// the expected compatible call should return false
		expected = universe.bool(false);
		result = universe.compatible(type1, type7);
		assertEquals(expected, result);

		// here we compare two different tuple types (type1, type5), but they
		// have the same name
		// the expected compatible call should return false
		expected = universe.bool(false);
		result = universe.compatible(type1, type4);
		assertEquals(expected, result);

		// here we compare two different tuple types (type1, type6), but they
		// have the same name
		// the expected compatible call should return false
		expected = universe.bool(false);
		result = universe.compatible(type1, type5);
		assertEquals(expected, result);

		// here we compare two different tuple types (type7, type6), but they
		// have the same name
		// the expected compatible call should return false
		expected = universe.bool(false);
		result = universe.compatible(type6, type5);
		assertEquals(expected, result);

	}
	
}
