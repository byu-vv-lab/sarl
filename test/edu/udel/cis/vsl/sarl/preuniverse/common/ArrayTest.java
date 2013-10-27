package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.SARL;
import edu.udel.cis.vsl.sarl.IF.Reasoner;
import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.SymbolicUniverse;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

/**
 * This class tests array functionality in the PreUniverse package.
 * 
 * @author jsaints
 */
public class ArrayTest {

	// Universe
	private static PreUniverse universe;
	// Factories
	private static ExpressionFactory expressionFactory;
	// SymbolicTypes
	private static SymbolicType integerType;
	private static SymbolicType realType;
	private static SymbolicType intArrayType;
	private static SymbolicType doubleIntArrayType;
	// SymbolicExpressions
	private static SymbolicExpression nullExpression;
	private static NumericExpression two, four;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		universe = PreUniverses.newPreUniverse(system);

		// Instantiate Types
		integerType = universe.integerType();
		realType = universe.realType();
		intArrayType = universe.arrayType(integerType); // creates an array of
														// ints
		doubleIntArrayType = universe.arrayType(intArrayType); // creates an
																// array of
																// int[]s

		// Instantiate NumberExpressions
		two = universe.integer(2);
		four = universe.integer(4);

		// Instantiate the nullExpression SymbolicExpression
		expressionFactory = system.expressionFactory();
		nullExpression = expressionFactory.nullExpression();
	}

	/**
	 * Main function that tests for successful completion of denseArrayWrite()
	 * Written by Jordan Saints on 9/16/13
	 */
	@Test
	public void testDenseArrayWriteSuccess() {
		// The SymbolicExpression, of type ARRAY(int) (array of ints), that we
		// will be writing to
		SymbolicExpression intArrayTypeExpression = universe.symbolicConstant(
				universe.stringObject("intArrayTypeExpression"), intArrayType);

		// A List of expressions to write to new array (in dense format),
		// where, after calling denseArrayWrite(), an element's index in this
		// List == index in resulting expressions sequence
		List<SymbolicExpression> expressionsToWrite = Arrays
				.asList(new SymbolicExpression[] { null, null, two, null, two,
						null, null });

		// Call denseArrayWrite()!
		SymbolicExpression denseResult = universe.denseArrayWrite(
				intArrayTypeExpression, expressionsToWrite);

		// Test by comparing to normal write operation in arrayWrite()
		// USAGE: universe.arrayWrite(array to write to, index in array to write
		// at, value to write @ that index)
		SymbolicExpression writeResult = universe.arrayWrite(
				intArrayTypeExpression, two, two); // adds a 2 at index 2 in
													// arrayTypeExpression array
		writeResult = universe.arrayWrite(writeResult, four, two); // replace
																	// writeResult's
																	// entry at
																	// index 4
																	// (NumExpr
																	// four)
																	// with a 2
																	// (NumExpr
																	// two)
		assertEquals(writeResult, denseResult); // test if arrays are equal

		// Test denseResult's type
		assertEquals(universe.arrayType(integerType), intArrayType); // check
																		// that
																		// arrayType
																		// is
																		// actually
																		// correct
																		// type
																		// to
																		// begin
																		// with
		assertEquals(intArrayType, denseResult.type()); // check that
														// denseResult is of
														// arrayType

		// Test denseResult's arguments
		assertEquals(2, denseResult.numArguments()); // test total numArguments
		@SuppressWarnings("unchecked")
		SymbolicSequence<SymbolicExpression> expressions = (SymbolicSequence<SymbolicExpression>) denseResult
				.argument(1); // get sequence of expressions
		assertEquals(5, expressions.size()); // the 2 trailing null SymExprs
												// will be chopped off
												// ("made dense")

		// Test denseResult's expressions sequence against known values in
		// expressionsToWrite List
		assertEquals(nullExpression, expressions.get(0));
		assertEquals(nullExpression, expressions.get(1));
		assertEquals(two, expressions.get(2));
		assertEquals(nullExpression, expressions.get(3));
		assertEquals(two, expressions.get(4));
	}

	/**
	 * Auxiliary function #1 that tests failure branch (1 of 2) of
	 * denseArrayWrite() Written by Jordan Saints on 9/16/13
	 */
	@Test(expected = SARLException.class)
	public void testDenseArrayWriteParam1Exception() {
		// Create SymbolicExpression of type INTEGER (no array at all)
		SymbolicExpression integerTypeExpression = universe.symbolicConstant(
				universe.stringObject("integerTypeExpression"), integerType);

		// A List of expressions to write to new array (in dense format)
		List<SymbolicExpression> expressionsToWrite = Arrays
				.asList(new SymbolicExpression[] { null, null, two, null, two,
						null, null });

		// Will throw a SARLException because input param #1
		// (SymbolicExpression) must be of type ARRAY
		// "Argument 0 of denseArrayWrite must have array type but had type int"
		universe.denseArrayWrite(integerTypeExpression, expressionsToWrite);
	}

	/**
	 * Auxiliary function #2 that tests failure branch (2 of 2) of
	 * denseArrayWrite() Written by Jordan Saints on 9/16/13
	 */
	@Test(expected = SARLException.class)
	public void testDenseArrayWriteParam2Exception() {
		// Create SymbolicExpression of type ARRAY(int[]) (an array of int
		// arrays) to fill with new values
		SymbolicExpression doubleIntArrayTypeExpression = universe
				.symbolicConstant(
						universe.stringObject("doubleIntArrayTypeExpression"),
						doubleIntArrayType);

		// A List of expressions to write to new array (in dense format)
		List<SymbolicExpression> expressionsToWrite = Arrays
				.asList(new SymbolicExpression[] { null, null, two, null, two,
						null, null });

		// Will throw a SARLException because input param #2
		// (List<SymbolicExpressions>) must be of type ARRAY
		// "Element 2 of values argument to denseArrayWrite has incompatible type.\nExpected: int[]\nSaw: int"
		universe.denseArrayWrite(doubleIntArrayTypeExpression,
				expressionsToWrite);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testLengthExceptions() {

		NumericExpression[] arrayMembers = new NumericExpression[2];
		SymbolicExpression array;
		@SuppressWarnings("unused")
		NumericExpression length;

		arrayMembers[0] = universe.integer(1);
		arrayMembers[1] = universe.integer(2);
		array = universe.array(integerType, Arrays.asList(arrayMembers));
		array = null;
		// exception for null array
		length = universe.length(array);

	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testLengthExceptions2() {
		// exception for non array type
		SymbolicTupleType tupleType1;
		SymbolicExpression tuple;
		@SuppressWarnings("unused")
		NumericExpression length;
		tupleType1 = universe.tupleType(universe.stringObject("tupleType1"),
				Arrays.asList(new SymbolicType[] { integerType, integerType }));
		tuple = universe.tuple(
				tupleType1,
				Arrays.asList(new SymbolicExpression[] { universe.integer(1),
						universe.integer(2) }));
		length = universe.length(tuple);

	}

	// written by Mohammad Alsulmi
	@Test
	public void emptyArrayTest() {
		// get an empty array with size 0
		SymbolicExpression array = universe.emptyArray(integerType);
		NumericExpression zero = universe.integer(0);
		assertEquals(zero, universe.length(array));
	}

	// written by Mohammad Alsulmi
	@Test
	public void testRemoveElementAt() {
		SymbolicExpression array, expected, resultedArray;
		NumericExpression one, two, three;

		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		array = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { one, two, three }));
		expected = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { one, three }));
		resultedArray = universe.removeElementAt(array, 1);

		assertEquals(expected, resultedArray);

	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testRemoveElementAtException() {

		SymbolicTupleType tupleType1;
		@SuppressWarnings("unused")
		SymbolicExpression tuple, resultedArray;

		tupleType1 = universe.tupleType(universe.stringObject("tupleType1"),
				Arrays.asList(new SymbolicType[] { integerType, integerType }));
		tuple = universe.tuple(
				tupleType1,
				Arrays.asList(new SymbolicExpression[] { universe.integer(1),
						universe.integer(2) }));
		// passing an argument from type other than array
		resultedArray = universe.removeElementAt(tuple, 0);

	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testRemoveElementAtException2() {
		SymbolicExpression array, expected, resultedArray;
		NumericExpression one, two, three;

		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		array = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { one, two, three }));
		expected = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { one, three }));
		// index out of range exception
		resultedArray = universe.removeElementAt(array, 3);
		assertEquals(expected, resultedArray);

	}

	// written by Mohammad Alsulmi
	@Test
	public void testArrayWrite() {
		SymbolicExpression array, resultedArray, expected;
		NumericExpression one, two, three, five;

		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);

		array = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { two, three, five }));
		expected = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { two, two, five }));

		resultedArray = universe.arrayWrite(array, one, two);
		assertEquals(expected, resultedArray);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testArrayWriteException() {
		// testing the fail when pass a null array to arrayWrite()
		@SuppressWarnings("unused")
		SymbolicExpression array, resultedArray;
		NumericExpression one, two, three, five;

		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);

		array = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { two, three, five }));
		array = null;
		resultedArray = universe.arrayWrite(array, one, two);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testArrayWriteException2() {
		// testing the fail when pass a null index to arrayWrite()
		@SuppressWarnings("unused")
		SymbolicExpression array, resultedArray;
		NumericExpression one, two, three, five;

		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);

		one = null;

		array = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { two, three, five }));
		resultedArray = universe.arrayWrite(array, one, two);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testArrayWriteException3() {
		// testing the fail when pass a null value to arrayWrite()
		@SuppressWarnings("unused")
		SymbolicExpression array, resultedArray;
		NumericExpression one, two, three, five;

		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);

		array = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { two, three, five }));
		two = null;
		resultedArray = universe.arrayWrite(array, one, two);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testArrayWriteException4() {
		// testing the fail when pass a non array type to arrayWrite()
		// here we use a tuple instead of array
		@SuppressWarnings("unused")
		SymbolicExpression resultedArray, tuple;
		NumericExpression one, two;
		SymbolicTupleType tupleType1;

		tupleType1 = universe.tupleType(universe.stringObject("tupleType1"),
				Arrays.asList(new SymbolicType[] { integerType, integerType }));
		tuple = universe.tuple(
				tupleType1,
				Arrays.asList(new SymbolicExpression[] { universe.integer(1),
						universe.integer(2) }));

		one = universe.integer(1);
		two = universe.integer(2);

		resultedArray = universe.arrayWrite(tuple, one, two);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testArrayWriteException5() {
		// testing the fail when pass a non integer index to arrayWrite()
		@SuppressWarnings("unused")
		SymbolicExpression array, resultedArray;
		NumericExpression one, two, three, five;

		one = universe.rational(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);

		array = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { two, three, five }));
		resultedArray = universe.arrayWrite(array, one, two);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testArrayWriteException6() {
		// testing the fail when passing an incompatible value to arrayWrite()
		// here the array has integer type, so we pass real type instead of
		// integer
		SymbolicExpression array, resultedArray;
		NumericExpression one, two, three, five;

		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);

		array = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { two, three, five }));
		two = universe.rational(2.0);
		resultedArray = universe.arrayWrite(array, one, two);
		assertEquals(resultedArray, array);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void TestArrayReadException() {
		// testing the fail when pass a null array to arrayRead()
		SymbolicExpression array, resultedArray;
		NumericExpression one, two, three, five;

		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);
		array = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { two, three, five }));
		array = null; // null array
		resultedArray = universe.arrayRead(array, one);
		assertEquals(resultedArray, array);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void TestArrayReadException2() {
		// testing the fail when pass a null index to arrayRead()

		SymbolicExpression array, resultedArray;
		NumericExpression two, three, five;

		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);
		array = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { two, three, five }));

		two = null; // null index
		resultedArray = universe.arrayRead(array, two);
		assertEquals(resultedArray, array);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void TestArrayReadException3() {
		// testing the fail when pass a non array type to arrayRead()
		// here we use a tuple instead of array
		@SuppressWarnings("unused")
		SymbolicExpression resultedArray, tuple;
		NumericExpression two;
		SymbolicTupleType tupleType1;

		tupleType1 = universe.tupleType(universe.stringObject("tupleType1"),
				Arrays.asList(new SymbolicType[] { integerType, integerType }));
		tuple = universe.tuple(
				tupleType1,
				Arrays.asList(new SymbolicExpression[] { universe.integer(1),
						universe.integer(2) }));
		two = universe.integer(2);

		resultedArray = universe.arrayRead(tuple, two);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void TestArrayReadException4() {
		// testing the fail when pass a negative index to arrayRead()
		@SuppressWarnings("unused")
		SymbolicExpression array, resultedArray;
		NumericExpression negativeOne, two, three, five;

		negativeOne = universe.integer(-1); // negative number
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);

		array = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { two, three, five }));
		resultedArray = universe.arrayRead(array, negativeOne);
	}

	// written by Mohammad Alsulmi
	@Test
	public void testCompatibleWithArray() {

		// here we test two array types
		SymbolicCompleteArrayType type1, type2;
		BooleanExpression result, expected;

		type1 = universe.arrayType(integerType, universe.integer(3));
		type2 = universe.arrayType(integerType, universe.integer(3));

		// here we compare two identical types (type1, type2)
		// the expected compatible call should return true
		expected = universe.bool(true);
		result = universe.compatible(type1, type2);
		assertEquals(expected, result);

		type2 = universe.arrayType(integerType, universe.integer(5));

		// here we compare two different types (type1, type2)
		// the expected compatible call should return false
		expected = universe.bool(false);
		result = universe.compatible(type1, type2);
		assertEquals(expected, result);

	}

	// written by Mohammad Alsulmi
	@Test
	public void testArray() {
		// testing array() when passing with no exceptions
		LinkedList<SymbolicExpression> elements; // list of elements
		@SuppressWarnings("unused")
		SymbolicExpression array;

		elements = new LinkedList<>();
		elements.add(universe.integer(5));
		elements.add(universe.integer(10));
		elements.add(universe.integer(0));
		elements.add(universe.integer(20));
		array = universe.array(integerType, elements);

	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testArrayException() {
		// testing the fail when passing a null elements reference to array()
		LinkedList<SymbolicExpression> elements; // list of elements
		@SuppressWarnings("unused")
		SymbolicExpression array;

		elements = null; // null reference
		array = universe.array(integerType, elements);

	}

	@Test(expected = SARLException.class)
	public void testArrayException2() {
		// testing the fail when passing a null elements type to array()
		LinkedList<SymbolicExpression> elements; // list of elements
		@SuppressWarnings("unused")
		SymbolicExpression array;
		SymbolicType realType;

		elements = new LinkedList<>();
		elements.add(universe.integer(1));
		elements.add(universe.integer(4));

		realType = null;

		array = universe.array(realType, elements);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testArrayException3() {
		// testing the fail when passing an array with null elements to array()
		NumericExpression elements[]; // array of elements
		@SuppressWarnings("unused")
		SymbolicExpression array;

		elements = new NumericExpression[4];
		// here each one of the elements need to be intialized

		array = universe.array(integerType, Arrays.asList(elements));

	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testArrayException4() {
		// testing the fail when passing non compatible type to array
		NumericExpression elements[]; // array of elements
		@SuppressWarnings("unused")
		SymbolicExpression array;

		elements = new NumericExpression[2];
		elements[0] = universe.integer(1); // integer
		elements[1] = universe.integer(10);// integer

		array = universe.array(realType, Arrays.asList(elements)); // non
																	// compatible
																	// type

	}

	// written by Mohammad Alsulmi
	@Test
	public void testAppend() {

		// here we test the regular case of array append when it passes
		SymbolicExpression array, expected;
		SymbolicExpression value;

		array = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(7),
						universe.integer(10) }));
		value = universe.integer(5);
		// expected array after append
		expected = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(7),
						universe.integer(10), universe.integer(5) }));
		// appending here
		array = universe.append(array, value);

		assertEquals(expected, array);

	}

	// written by Mohammad Alsulmi
	@Test
	public void testAppendTwoArrays() {

		// here we test the regular case of array append when it passes
		SymbolicExpression array1, array2, expected;

		array1 = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(7),
						universe.integer(10) }));
		array2 = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(7),
						universe.integer(110) }));

		// expected array after append call
		expected = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(7),
						universe.integer(10), universe.integer(7),
						universe.integer(110) }));
		// appending here
		array1 = universe.append(array1, array2);

		// assertion call

		assertEquals(expected, array1);

	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testAppendException() {

		// testing the fail when passing tuple to array append
		SymbolicTupleType tupleType;
		SymbolicExpression tuple;
		SymbolicExpression value;

		tupleType = universe.tupleType(universe.stringObject("type1"),
				Arrays.asList(new SymbolicType[] { integerType, realType }));
		tuple = universe.tuple(
				tupleType,
				Arrays.asList(new NumericExpression[] { universe.integer(10),
						universe.rational(6) }));
		value = universe.integer(100);
		// we expect exception since append() cannot accept other than arrays
		tuple = universe.append(tuple, value);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testAppendException2() {

		// testing the fail when passing a null value to array append
		SymbolicExpression array;
		SymbolicExpression value;

		array = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(7),
						universe.integer(10) }));
		value = null;
		// we expect exception since append() cannot accept null values
		array = universe.append(array, value);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testAppendException3() {

		// testing the fail when passing a value with incompatible to array
		// append
		SymbolicExpression array;
		SymbolicExpression value;

		array = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(7),
						universe.integer(10) }));
		// the value is real but the array is integer
		value = universe.rational(6.0);
		array = universe.append(array, value);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testAppendException4() {

		// here we test append an array of realtype to another with intgerType
		SymbolicExpression array1, array2, expected;

		array1 = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(7),
						universe.integer(10) }));
		array2 = universe.array(
				realType,
				Arrays.asList(new NumericExpression[] { universe.rational(6),
						universe.rational(110) }));

		// expected array after append call
		expected = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(7),
						universe.integer(10), universe.integer(7),
						universe.integer(110) }));
		// appending here
		array1 = universe.append(array1, array2);

		// assertion call

		assertEquals(expected, array1);

	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testMixedArray1() {
		// showing an example of creating nested arrays
		SymbolicArrayType arrayType;
		SymbolicExpression array1, array2, array3, array4;

		// initialization of integer type array
		arrayType = universe.arrayType(integerType);

		// creating first array
		array1 = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(10),
						universe.integer(25) }));
		// creating second array
		array2 = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(1),
						universe.integer(5), universe.integer(8) }));
		// creating an array that contains two arrays: array1 and array2 both of
		// type integer type
		array3 = universe.array(arrayType,
				Arrays.asList(new SymbolicExpression[] { array1, array2 }));
		// do some assertion to ensure that the arrays are equals
		assertEquals(universe.length(array3), universe.integer(2));
		assertEquals(universe.arrayRead(array3, universe.integer(0)), array1);
		assertEquals(universe.arrayRead(array3, universe.integer(1)), array2);

		array4 = universe.array(realType, Arrays.asList(universe.rational(8)));

		// performing write an array inside another array
		array3 = universe.arrayWrite(array4, universe.integer(1), array4);

	}

	// written by Mohammad Alsulmi
	@Test
	public void testMixedArray2() {

		// showing an example of creating array of unions

		SymbolicTupleType tupleType;
		SymbolicExpression array;
		SymbolicExpression tuple1, tuple2;

		// initialization of tuple type

		tupleType = universe.tupleType(
				universe.stringObject("TupleType"),
				Arrays.asList(new SymbolicType[] { integerType, realType,
						integerType }));

		// tuple1 and tuple2 will use the previous tuple type
		tuple1 = universe.tuple(
				tupleType,
				Arrays.asList(new NumericExpression[] { universe.integer(20),
						universe.rational(30), universe.integer(100) }));
		tuple2 = universe.tuple(
				tupleType,
				Arrays.asList(new NumericExpression[] { universe.integer(8),
						universe.rational(8.2), universe.integer(100000) }));
		// creating an array that contain two tuples
		array = universe.array(tupleType,
				Arrays.asList(new SymbolicExpression[] { tuple1, tuple2 }));

		assertEquals(tuple1, universe.arrayRead(array, universe.integer(0)));
		assertEquals(tuple2, universe.arrayRead(array, universe.integer(1)));
		assertEquals(universe.integer(2), universe.length(array));

		tuple1 = universe.tuple(
				tupleType,
				Arrays.asList(new NumericExpression[] { universe.integer(1000),
						universe.rational(1.5), universe.integer(11) }));
		// performing an array write to a specific position in the array.
		array = universe.arrayWrite(array, universe.integer(1), tuple1);

		assertEquals(tuple1, universe.arrayRead(array, universe.integer(1)));
	}

	// written by Mohammad Alsulmi
	@Test
	public void testMixedArray3() {
		// Testing two levels of nested arrays
		SymbolicArrayType arrayType;
		SymbolicArrayType nestedType;
		SymbolicExpression array1, array2, array3, array4;
		SymbolicExpression nestedArray;

		// initialization of integer type array
		arrayType = universe.arrayType(integerType);

		// creating first array
		array1 = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(10),
						universe.integer(25) }));
		// creating second array
		array2 = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(1),
						universe.integer(5), universe.integer(8) }));
		// creating one array that contains two arrays: array1 and array2 both
		// of type integer type
		array3 = universe.array(arrayType,
				Arrays.asList(new SymbolicExpression[] { array1, array2 }));

		// creating another array that contains two arrays: array2 and array1
		// both of type integer type
		// here, it is similar to array3 except the order of the elements
		array4 = universe.array(arrayType,
				Arrays.asList(new SymbolicExpression[] { array2, array1 }));

		// do some assertion to ensure that the arrays are equals
		assertEquals(universe.length(array3), universe.integer(2));
		assertEquals(universe.arrayRead(array3, universe.integer(0)), array1);
		assertEquals(universe.arrayRead(array3, universe.integer(1)), array2);

		// initialization of nested array type that contains the previous
		// initialized array type
		nestedType = universe.arrayType(arrayType);

		try {
			// error is expected since array2 is different type
			nestedArray = universe.array(
					nestedType,
					Arrays.asList(new SymbolicExpression[] { array3, array4,
							array2 }));

		} catch (SARLException ex) {

			// no Errors
			// it will create a two level nested array
			nestedArray = universe.array(nestedType,
					Arrays.asList(new SymbolicExpression[] { array3, array4 }));

			// do some assertions to ensure the data was inserted properly

			assertEquals(array3,
					universe.arrayRead(nestedArray, universe.integer(0)));
			assertEquals(array4,
					universe.arrayRead(nestedArray, universe.integer(1)));

		}

	}

	// written by Mohammad Alsulmi
	@Test
	public void testCompleteArrays() {

		// testing complete type arrays
		SymbolicExpression array1, array2, array3, array4;
		SymbolicExpression completeArray;
		SymbolicArrayType arrayType;
		SymbolicCompleteArrayType arraycompleteType;

		arraycompleteType = universe.arrayType(intArrayType,
				universe.integer(2));
		arrayType = universe.arrayType(integerType);

		// creating first array
		array1 = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(10),
						universe.integer(25), universe.integer(50) }));
		// creating second array
		array2 = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(1),
						universe.integer(5), universe.integer(8),
						universe.integer(11) }));
		// creating one array that contains two arrays: array1 and array2 both
		// of type integer type
		array3 = universe.array(arrayType,
				Arrays.asList(new SymbolicExpression[] { array1, array2 }));

		array4 = universe.array(arrayType,
				Arrays.asList(new SymbolicExpression[] { array2, array1 }));

		// creating the complete type array from the previous arrays

		completeArray = universe.array(arraycompleteType,
				Arrays.asList(new SymbolicExpression[] { array3, array4 }));
		// do some assertions
		assertEquals(array4,
				universe.arrayRead(completeArray, universe.integer(1)));

	}

	// written by Mohammad Alsulmi
	@Test
	public void complexArrayTest() {
		NumericSymbolicConstant x_var, y_var;
		NumericExpression x_plus_y, x_minus_y, x_plus_2y, x_plus_y_multiply_x_plus_y;
		SymbolicExpression array, expected, simplifiedArray;
		BooleanExpression claim1, claim2, claim;
		Reasoner reasoner;
		SymbolicUniverse reasonerUniverse = SARL.newStandardUniverse();

		x_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), integerType);
		y_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), integerType);

		x_plus_y = (NumericExpression) universe.add(x_var, y_var);

		x_minus_y = (NumericExpression) universe.subtract(x_var, y_var);
		x_plus_2y = (NumericExpression) universe.multiply(universe.integer(2),
				y_var);
		x_plus_2y = (NumericExpression) universe.add(x_var, x_plus_2y);

		x_plus_y_multiply_x_plus_y = (NumericExpression) universe.multiply(
				x_plus_y, x_plus_y);

		array = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { x_plus_y, x_minus_y,
						x_plus_2y, x_plus_y_multiply_x_plus_y }));

		claim1 = universe.equals(x_var, universe.integer(5));
		claim2 = universe.equals(y_var, universe.integer(3));
		claim = universe.and(claim1, claim2);
		reasoner = reasonerUniverse.reasoner(claim);

		expected = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(8),
						universe.integer(2), universe.integer(11),
						universe.integer(64) }));
		simplifiedArray = reasoner.simplify(array);

		assertEquals(expected, simplifiedArray);

	}
	// written by Mohammad Alsulmi
	@Test
	public void testComplexMixedArray() {
		// Testing two levels of nested arrays
		SymbolicArrayType arrayType;
		SymbolicArrayType nestedType;
		SymbolicExpression array1, array2;
		SymbolicExpression nestedArray, array3, array4;
		NumericSymbolicConstant x_var, y_var;
		NumericExpression x_plus_y, x_minus_y, x_plus_2y, x_plus_y_multiply_x_plus_y;
		@SuppressWarnings("unused")
		SymbolicExpression simplifiedArray;
		BooleanExpression claim1, claim2, claim;
		Reasoner reasoner;
		SymbolicUniverse reasonerUniverse = SARL.newStandardUniverse();

		x_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), integerType);
		y_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), integerType);

		x_plus_y = (NumericExpression) universe.add(x_var, y_var);

		x_minus_y = (NumericExpression) universe.subtract(x_var, y_var);
		x_plus_2y = (NumericExpression) universe.multiply(universe.integer(2),
				y_var);
		x_plus_2y = (NumericExpression) universe.add(x_var, x_plus_2y);

		x_plus_y_multiply_x_plus_y = (NumericExpression) universe.multiply(
				x_plus_y, x_plus_y);

		// initialization of integer type array
		arrayType = universe.arrayType(integerType);

		// creating first array
		array1 =  universe.array(integerType,
				Arrays.asList(new NumericExpression[] { x_plus_y, x_minus_y }));
		// creating second array
		array2 =  universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { x_plus_2y,
						x_plus_y_multiply_x_plus_y, x_minus_y }));
		// creating one array that contains two arrays: array1 and array2 both
		// of type integer type
		array3 = universe.array(arrayType,
				Arrays.asList(new SymbolicExpression[] { array1, array2 }));

		// creating another array that contains two arrays: array2 and array1
		// both of type integer type
		// here, it is similar to array3 except the order of the elements
		array4 = universe.array(arrayType,
				Arrays.asList(new SymbolicExpression[] { array2, array1 }));

		// do some assertion to ensure that the arrays are equals
		assertEquals(universe.length(array3), universe.integer(2));
		assertEquals(universe.arrayRead(array3, universe.integer(0)), array1);
		assertEquals(universe.arrayRead(array3, universe.integer(1)), array2);

		// initialization of nested array type that contains the previous
		// initialized array type
		nestedType = universe.arrayType(arrayType);

		claim1 = universe.equals(x_var, universe.integer(5));
		claim2 = universe.equals(y_var, universe.integer(3));
		claim = universe.and(claim1, claim2);

		try {
			// error is expected since array2 is different type
			nestedArray = universe.array(
					nestedType,
					Arrays.asList(new SymbolicExpression[] { array3, array4,
							array2 }));

		} catch (SARLException ex) {

			// no Errors
			// it will create a two level nested array
			nestedArray = universe.array(nestedType,
					Arrays.asList(new SymbolicExpression[] { array3, array4 }));
			reasoner = reasonerUniverse.reasoner(claim);
			simplifiedArray = reasoner.simplify(nestedArray);

			// do some assertions to ensure the data was inserted properly

			assertEquals(array3,
					universe.arrayRead(nestedArray, universe.integer(0)));
			assertEquals(array4,
					universe.arrayRead(nestedArray, universe.integer(1)));

		}

	}

}
