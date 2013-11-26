package edu.udel.cis.vsl.sarl.preuniverse;

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
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

/**
 * This class tests array functionality in the PreUniverse package.
 * 
 * @author jsaints and malsulmi
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
		intArrayType = universe.arrayType(integerType); // array of ints
		doubleIntArrayType = universe.arrayType(intArrayType); // array of
															   // int[]s
		// Instantiate NumberExpressions
		two = universe.integer(2);
		four = universe.integer(4);

		// Instantiate the nullExpression SymbolicExpression
		expressionFactory = system.expressionFactory();
		nullExpression = expressionFactory.nullExpression();
	}

	/**
	 * Main function that tests for successful completion of denseArrayWrite().
	 * Written by Jordan Saints.
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

		// USAGE: universe.arrayWrite(array to write to, index in array to
		// write at, value to write @ that index)

		// TEST by comparing to normal write operation in arrayWrite()
		// add a 2 at index 2 in arrayTypeExpression array
		SymbolicExpression writeResult = universe.arrayWrite(
				intArrayTypeExpression, two, two);
		// replace entry at index 4 (NumExpr four) with a 2 (NumExpr two)
		writeResult = universe.arrayWrite(writeResult, four, two);
		assertEquals(writeResult, denseResult); // test if arrays are equal

		// TEST denseResult's type
		// check that arrayType is actually correct type to begin with
		assertEquals(universe.arrayType(integerType), intArrayType);
		// check that denseResult is of arrayType
		assertEquals(intArrayType, denseResult.type());

		// TEST denseResult's arguments
		assertEquals(2, denseResult.numArguments()); // test total numArguments
		// get sequence of expressions
		@SuppressWarnings("unchecked")
		SymbolicSequence<SymbolicExpression> expressions = (SymbolicSequence<SymbolicExpression>) denseResult
				.argument(1);
		// the 2 trailing null SymExprs will be chopped off ("made dense")
		assertEquals(5, expressions.size());

		// TEST denseResult's expressions sequence against known values in
		// expressionsToWrite List
		assertEquals(nullExpression, expressions.get(0));
		assertEquals(nullExpression, expressions.get(1));
		assertEquals(two, expressions.get(2));
		assertEquals(nullExpression, expressions.get(3));
		assertEquals(two, expressions.get(4));
	}

	/**
	 * Auxiliary function #1 that tests failure branch (1 of 2) of
	 * denseArrayWrite(). Written by Jordan Saints.
	 * @exception A SARLException will be thrown (intentially) by this test.
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
	 * denseArrayWrite(). Written by Jordan Saints.
	 * @exception A SARLException will be thrown (intentially) by this test.
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
	
	/**
	 * Tests the method length(SymbolicExpression array) with an exception
	 * for a case when passing a null reference
	 * 	  
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the method length(SymbolicExpression array) with an exception
	 * for a case when passing a non array type
	 * 
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the method length(SymbolicExpression array) to make sure
	 * it returns 0 when passing an empty array
	 * 
	 * @author malsulmi
	 * 
	 */
	@Test
	public void emptyArrayTest() {
		// get an empty array with size 0
		SymbolicExpression array = universe.emptyArray(integerType);
		NumericExpression zero = universe.integer(0);
		assertEquals(zero, universe.length(array));
	}
	
	/**
	 * Tests the method removeElementAt(SymbolicExpression array, int position) 
	 * which will remove a specific element at a specific position
	 * 
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the method removeElementAt(SymbolicExpression array, int position) with an exception case
	 * of passing a non array type
	 * 
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the method removeElementAt(SymbolicExpression array, int position) with an exception case
	 * of passing index out of range
	 * 
	 * @author malsulmi
	 * 
	 */
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

	/**
	 * Tests the method arrayWrite(SymbolicExpression array, NumericExpression index, SymbolicExpression value ) 
	 * with a regular case 
	 * 
	 * @author malsulmi
	 * 
	 */
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

	/**
	 * Tests the method arrayWrite(SymbolicExpression array, NumericExpression index, SymbolicExpression value ) 
	 * with an exception case of passing null array
	 * 
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the method arrayWrite(SymbolicExpression array, NumericExpression index, SymbolicExpression value ) 
	 * with an exception case of passing null index
	 * 
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the method arrayWrite(SymbolicExpression array, NumericExpression index, SymbolicExpression value ) 
	 * with an exception case of passing null value
	 * 
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the method arrayWrite(SymbolicExpression array, NumericExpression index, SymbolicExpression value ) 
	 * with an exception case of passing non array type
	 * 
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the method arrayWrite(SymbolicExpression array, NumericExpression index, SymbolicExpression value ) 
	 * with an exception case of passing non integer index array
	 * 
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the method arrayWrite(SymbolicExpression array, NumericExpression index, SymbolicExpression value ) 
	 * with an exception case of passing when passing an incompatible value to array type
	 * here the array has integer type, but we pass real type instead of integer
	 * 
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testArrayWriteException6() {
		
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
	
	/**
	 * Tests the method arrayRead(SymbolicExpression array, NumericExpression index ) 
	 * with no exceptions
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testArrayReadException() {
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
	
	/**
	 * Tests the method arrayRead(SymbolicExpression array, NumericExpression index ) 
	 * with an exception case of passing null index 
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testArrayReadException2() {
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
	
	/**
	 * Tests the method arrayRead(SymbolicExpression array, NumericExpression index ) 
	 * with an exception case of passing a non array type 
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testArrayReadException3() {
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
	
	/**
	 * Tests the method arrayRead(SymbolicExpression array, NumericExpression index ) 
	 * with an exception case of passing an index out of bound
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testArrayReadException4() {
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
	
	/**
	 * Tests the method universe.compatible( SymbolicType type1,SymbolicType type2)
	 * to verify when passing two arguments of the same type it returns true BooleanExpression, and
	 * when passing two arguments of non compatible it returns false BooleanExpression
	 * 
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the method universe.array( SymbolicType type, elements)
	 * to verify initialization of an array with no exception
	 * @author malsulmi
	 * 
	 */
	@Test
	public void testArray() {
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

	/**
	 * Tests the method universe.array( SymbolicType type, elements)
	 * to verify initialization of an array with an an exception case of passing 
	 * a null elements
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testArrayException() {
		LinkedList<SymbolicExpression> elements; // list of elements
		@SuppressWarnings("unused")
		SymbolicExpression array;

		elements = null; // null reference
		array = universe.array(integerType, elements);

	}
	
	/**
	 * Tests the method universe.array( SymbolicType type, elements)
	 * to verify initialization of an array with an an exception case of passing 
	 * a null symbolic type
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the method universe.array( SymbolicType type, elements)
	 * to verify initialization of an array with an an exception case of passing 
	 * null elements to an array
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testArrayException3() {
		NumericExpression elements[]; // array of elements
		@SuppressWarnings("unused")
		SymbolicExpression array;

		elements = new NumericExpression[4];
		// here each one of the elements need to be intialized

		array = universe.array(integerType, Arrays.asList(elements));

	}
	
	/**
	 * Tests the method universe.array( SymbolicType type, elements)
	 * to verify initialization of an array with an an exception case of passing 
	 * non compatible type to an array
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testArrayException4() {
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
	
	/**
	 * Tests the method universe.append( SymbolicExpression array, element)
	 * to verify a regular case of passing an element to an array with no exception expected
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the method universe.append( SymbolicExpression array, element)
	 * with an expected exception case of passing an element to be appended to non array type
	 * e.g. tuple
	 * 
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testAppendException() {

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
	
	/**
	 * Tests the method universe.append( SymbolicExpression array, element)
	 * with an expected exception case of passing a null element to be appended to an array 
	 * 
	 * 
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testAppendException2() {

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

	/**
	 * Tests the method universe.append( SymbolicExpression array, element)
	 * with an expected exception case of passing an element with incompatible type to be appended to an array
	 *
	 * 
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testAppendException3() {

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
	
	/**
	 * Tests the method universe.append( SymbolicExpression array, element)
	 * with an expected exception case of passing an array to another array 
	 * which is not accepted by the method
	 * 
	 * 
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testAppendException4() {

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

	/**
	 * Tests the creation of nested arrays e.g. array of arrays, and performing 
	 * some array operations such as arrayWrite
	 * 
	 * @author malsulmi
	 * 
	 */
	@Test(expected = SARLException.class)
	public void testNestedArrays1() {
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
	
	/**
	 * Tests the creation of complicated arrays e.g. array of tuples, and performing 
	 * some array operations such as arrayWrite
	 * 
	 * @author malsulmi
	 * 
	 */
	@Test
	public void testArrayOfTuples() {

		// showing an example of creating array of tuples

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
	
	/**
	 * Tests the creation of more complicated and more nested arrays e.g. array of arrays, and performing 
	 * some array operations such as arrayWrite
	 * 
	 * @author malsulmi
	 * 
	 */
	@Test
	public void testNestedArrays2() {
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

	// Written by Chris Heider
	@Test
	public void testArrayLambda() {
		SymbolicCompleteArrayType arrayType;
		NumericSymbolicConstant x;
		NumericSymbolicConstant p;
		SymbolicExpression function1;
		SymbolicExpression function2;
		SymbolicExpression arrayL1;
		SymbolicExpression arrayL2;
		SymbolicExpression ans;

		arrayType = universe.arrayType(integerType, universe.integer(6));
		x = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), integerType);
		function1 = universe.lambda(x, universe.multiply(x, x));
		arrayL1 = universe.arrayLambda(arrayType, function1);
		p = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("p"), integerType);
		function2 = universe.lambda(p, universe.add(p, p));
		arrayL2 = universe.arrayLambda(arrayType, function2);
		ans = universe.integer(4);

		assertEquals(ans, universe.arrayRead(arrayL1, universe.integer(2)));
		assertEquals(ans, universe.arrayRead(arrayL2, universe.integer(2)));
	}

	// Written by Chris Heider
	@Test(expected = SARLException.class)
	public void testArrayLambdaException1() {
		SymbolicCompleteArrayType arrayType;
		NumericSymbolicConstant x;
		SymbolicExpression function;
		@SuppressWarnings("unused")
		SymbolicExpression arrayL;

		arrayType = universe.arrayType(integerType, universe.integer(6));
		x = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), integerType);
		function = universe.multiply(x, x);
		arrayL = universe.arrayLambda(arrayType, function);
	}

	// Written by Chris Heider
	@Test(expected = SARLException.class)
	public void testArrayLambdaException2() {
		SymbolicCompleteArrayType arrayType;
		SymbolicExpression function;
		@SuppressWarnings("unused")
		SymbolicExpression arrayL;

		arrayType = universe.arrayType(integerType, universe.integer(6));
		function = universe.nullExpression();
		arrayL = universe.arrayLambda(arrayType, function);
	}

	/**
	 * Tests the creation of completeArray type, and performing 
	 * some array operations such as arrayWrite
	 * 
	 * @author malsulmi
	 * 
	 */
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
	
	/**
	 * Tests the creation of complicated arrays with using symbolicConstant 
	 * and applying some reasoning to verify that reasoning works well with arrays 
	 * 
	 * @author malsulmi
	 * 
	 */
	@Test
	public void complexArrayTest() {

		NumericSymbolicConstant x_var, y_var;
		NumericExpression x_plus_y, x_minus_y, x_plus_2y, x_plus_y_multiply_x_plus_y;
		SymbolicExpression array, expected, simplifiedArray;
		BooleanExpression claim1, claim2, claim;
		Reasoner reasoner;
		SymbolicUniverse reasonerUniverse = SARL.newStandardUniverse();

		// initialization of symbolic constants
		x_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), integerType);
		y_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), integerType);

		// mixing the constant in numeric expressions
		x_plus_y = (NumericExpression) universe.add(x_var, y_var);

		x_minus_y = (NumericExpression) universe.subtract(x_var, y_var);
		x_plus_2y = (NumericExpression) universe.multiply(universe.integer(2),
				y_var);
		x_plus_2y = (NumericExpression) universe.add(x_var, x_plus_2y);

		x_plus_y_multiply_x_plus_y = (NumericExpression) universe.multiply(
				x_plus_y, x_plus_y);

		// initialization of the array
		array = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { x_plus_y, x_minus_y,
						x_plus_2y, x_plus_y_multiply_x_plus_y }));

		// initialization of boolean expressions
		claim1 = universe.equals(x_var, universe.integer(5));
		claim2 = universe.equals(y_var, universe.integer(3));

		// combining claim1, claim2 into claim
		claim = universe.and(claim1, claim2);
		// using the claim in the reasoner
		reasoner = reasonerUniverse.reasoner(claim);
		// creating the expected result array
		expected = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(8),
						universe.integer(2), universe.integer(11),
						universe.integer(64) }));

		// using the reasoner to substitute the values of expression in the
		// original array
		simplifiedArray = reasoner.simplify(array);

		// doing assertion
		assertEquals(expected, simplifiedArray);

	}
	
	/**
	 * Tests the creation of more complicated arrays (complete and nested arrays) with using symbolicConstant 
	 * and applying some reasoning to verify that reasoning works very well when we mix different types 
	 * 
	 * @author malsulmi
	 * 
	 */
	@Test
	public void testComplexMixedArray() {

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

		// initialization of symbolic constants
		x_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), integerType);
		y_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), integerType);

		// mixing the constant in numeric expressions
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
		array1 = universe.array(integerType,
				Arrays.asList(new NumericExpression[] { x_plus_y, x_minus_y }));
		// creating second array
		array2 = universe.array(
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

		// initialization of boolean expressions
		claim1 = universe.equals(x_var, universe.integer(5));
		claim2 = universe.equals(y_var, universe.integer(3));

		// combining claim1, claim2 into claim
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
			// using the claim in a reasoner
			reasoner = reasonerUniverse.reasoner(claim);
			// Substitute the values of the constant
			simplifiedArray = reasoner.simplify(nestedArray);

			// do some assertions to ensure the data was inserted properly

			assertEquals(array3,
					universe.arrayRead(nestedArray, universe.integer(0)));
			assertEquals(array4,
					universe.arrayRead(nestedArray, universe.integer(1)));

		}
	}
}
