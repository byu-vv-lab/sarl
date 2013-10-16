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
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
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
	
	private static SymbolicArrayType arrayIntegerType;
	
	private static SymbolicArrayType arrayRealType;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();

		universe = PreUniverses.newPreUniverse(system);
		integerType = universe.integerType();
		realType = universe.realType();
		arrayIntegerType = universe.arrayType(integerType);
		arrayRealType = universe.arrayType(realType);
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
	
	@Test
	public void testTupleOfArrays(){
		
		SymbolicTupleType type1;
		SymbolicExpression tuple;
		SymbolicExpression intArray;
		SymbolicExpression realArray;
		SymbolicExpression readingResult;
		
		// creating a tuple type containing two different array types
		type1 = universe.tupleType(universe.stringObject("Type1"),
				Arrays.asList(new SymbolicType[] { arrayIntegerType, arrayRealType }));
	
		// an array of integer
		intArray =  universe.array(integerType, Arrays.asList(new NumericExpression[]{universe.integer(2),universe.integer(8)}));
		// an array of real
		realArray =  universe.array(realType, Arrays.asList(new NumericExpression[]{universe.rational(6.7),universe.rational(9.99), universe.rational(9)}));
		
		// creating the tuple
		tuple = universe.tuple(type1, Arrays.asList(new SymbolicExpression[]{intArray,realArray}));
		
		readingResult = universe.tupleRead(tuple, universe.intObject(0));
		
		// checking if the resulted is an intArray
		assertEquals(intArray, readingResult);
		
		readingResult = universe.tupleRead(tuple, universe.intObject(1));
		
		// checking if the resulted is an intArray
		assertEquals(realArray, readingResult);
			
		
	}
	
	@Test
	public void testMixedTypeTuples(){
		
		SymbolicTupleType type1;
		SymbolicTupleType type2;
		SymbolicTupleType type3;

		SymbolicExpression tuple1;
		SymbolicExpression tuple2;
		SymbolicExpression tuple3;
		
		SymbolicExpression intArray;
		SymbolicExpression realArray;
		SymbolicExpression readingResult;
		BooleanExpression boolExp;
		
		// creating a tuple type containing two different array types
		type1 = universe.tupleType(universe.stringObject("Type1"),
				Arrays.asList(new SymbolicType[] { arrayIntegerType, arrayRealType }));
		// creating a tuple type containing three primitive types
		type2 = universe.tupleType(universe.stringObject("Type2"),
				Arrays.asList(new SymbolicType[] { integerType, integerType, realType }));
		// creating a tuple type containing the previous two types  + real type
		type3 = universe.tupleType(universe.stringObject("Type3"),
				Arrays.asList(new SymbolicType[] { type1, type2, realType }));
	
	
		// an array of integer
		intArray =  universe.array(integerType, Arrays.asList(new NumericExpression[]{universe.integer(2),universe.integer(8)}));
		// an array of real
		realArray =  universe.array(realType, Arrays.asList(new NumericExpression[]{universe.rational(6.7),universe.rational(9.99), universe.rational(9)}));
		
		// creating the 1st tuple
		tuple1 = universe.tuple(type1, Arrays.asList(new SymbolicExpression[]{intArray,realArray}));
		// creating the 2nd tuple
		tuple2 = universe.tuple(type2, Arrays.asList(new NumericExpression[]{universe.integer(50),universe.integer(40),universe.rational(3.14)}));
		// creating the 3rd tuple
		tuple3 = universe.tuple(type3, Arrays.asList(new SymbolicExpression[]{tuple1,tuple2,universe.rational(8.9)}));
		
		// reading the first position in tuple3
		readingResult = universe.tupleRead(tuple3, universe.intObject(0));
		// checking if it equals to tuple1
		assertEquals(tuple1, readingResult);
		// extracting the array from the resulted tuple
		readingResult = universe.tupleRead(readingResult, universe.intObject(0));
		// checking if the result equals to intArrray
		assertEquals(intArray, readingResult);
		
		// reading the second position in tuple3
		readingResult = universe.tupleRead(tuple3, universe.intObject(1));
		// checking if it equals to tuple2
		assertEquals(tuple2, readingResult);
		
		try{
			// here, we expect an exception since the index is out of bound
			readingResult = universe.tupleRead(tuple3, universe.intObject(3));
			assertEquals(universe.rational(8.9), readingResult);
			
			
		}catch(java.lang.IndexOutOfBoundsException ex){
			
			readingResult = universe.tupleRead(tuple3, universe.intObject(2));
			assertEquals(universe.rational(8.9), readingResult);
			
		}
		readingResult = universe.tupleRead(tuple3, universe.intObject(0));
		readingResult = universe.tupleRead(readingResult, universe.intObject(1));
		try{
			// here, we expect an exception since appending invalid type
			readingResult = universe.append(readingResult, universe.integer(50));
		}
		catch(SARLException ex){
			readingResult = universe.append(readingResult, universe.rational(50));
		}
		// writing the array back to the tuple1
		tuple1 = universe.tupleWrite(tuple1, universe.intObject(1), readingResult);
		readingResult = universe.tupleWrite(tuple3, universe.intObject(0), tuple1);
		// it should return false expression
		boolExp = universe.equals(readingResult, tuple3);
		assertEquals(universe.bool(false), boolExp);
		
	}
	
	
}
