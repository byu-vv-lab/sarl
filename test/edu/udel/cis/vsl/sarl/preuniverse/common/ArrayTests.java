package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
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
 * @author jsaints
 */
public class ArrayTests {

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
		intArrayType = universe.arrayType(integerType); //creates an array of ints
		doubleIntArrayType = universe.arrayType(intArrayType); //creates an array of int[]s
		
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
	public void testDenseArrayWrite_success() {
		// The SymbolicExpression, of type ARRAY(int) (array of ints), that we will be writing to
		SymbolicExpression intArrayTypeExpression = universe.symbolicConstant(universe.stringObject("intArrayTypeExpression"), intArrayType);
		
		// A List of expressions to write to new array (in dense format),
		// where, after calling denseArrayWrite(), an element's index in this List == index in resulting expressions sequence
		List<SymbolicExpression> expressionsToWrite = Arrays.asList(new SymbolicExpression[] { null, null, two, null, two, null, null });
		
		// Call denseArrayWrite()!
		SymbolicExpression denseResult = universe.denseArrayWrite(intArrayTypeExpression, expressionsToWrite);
		
		// Test by comparing to normal write operation in arrayWrite()
		// USAGE: universe.arrayWrite(array to write to, index in array to write at, value to write @ that index)
		SymbolicExpression writeResult = universe.arrayWrite(intArrayTypeExpression, two, two); //adds a 2 at index 2 in arrayTypeExpression array
		writeResult = universe.arrayWrite(writeResult, four, two); //replace writeResult's entry at index 4 (NumExpr four) with a 2 (NumExpr two) 
		assertEquals(writeResult, denseResult); //test if arrays are equal
		
		// Test denseResult's type
		assertEquals(universe.arrayType(integerType), intArrayType); //check that arrayType is actually correct type to begin with
		assertEquals(intArrayType, denseResult.type()); //check that denseResult is of arrayType

		// Test denseResult's arguments
		assertEquals(2, denseResult.numArguments()); //test total numArguments
		@SuppressWarnings("unchecked")
		SymbolicSequence<SymbolicExpression> expressions = (SymbolicSequence<SymbolicExpression>) denseResult.argument(1); //get sequence of expressions
		assertEquals(5, expressions.size()); //the 2 trailing null SymExprs will be chopped off ("made dense")
		
		// Test denseResult's expressions sequence against known values in expressionsToWrite List
		assertEquals(nullExpression, expressions.get(0));
		assertEquals(nullExpression, expressions.get(1));
		assertEquals(two, expressions.get(2));
		assertEquals(nullExpression, expressions.get(3));
		assertEquals(two, expressions.get(4));
	}
	
	/**
	 * Auxiliary function #1 that tests failure branch (1 of 2) of denseArrayWrite()
	 * Written by Jordan Saints on 9/16/13
	 */
	@Test(expected=SARLException.class)
	public void testDenseArrayWrite_param1Exception() {
		// Create SymbolicExpression of type INTEGER (no array at all)
		SymbolicExpression integerTypeExpression = universe.symbolicConstant(universe.stringObject("integerTypeExpression"), integerType);
		
		// A List of expressions to write to new array (in dense format)
		List<SymbolicExpression> expressionsToWrite = Arrays.asList(new SymbolicExpression[] { null, null, two, null, two, null, null });
		
		// Will throw a SARLException because input param #1 (SymbolicExpression) must be of type ARRAY
		// "Argument 0 of denseArrayWrite must have array type but had type int"
		universe.denseArrayWrite(integerTypeExpression, expressionsToWrite);
	}
	
	/**
	 * Auxiliary function #2 that tests failure branch (2 of 2) of denseArrayWrite()
	 * Written by Jordan Saints on 9/16/13
	 */
	@Test(expected=SARLException.class)
	public void testDenseArrayWrite_param2Exception() {
		// Create SymbolicExpression of type ARRAY(int[]) (an array of int arrays) to fill with new values
		SymbolicExpression doubleIntArrayTypeExpression = universe.symbolicConstant(universe.stringObject("doubleIntArrayTypeExpression"), doubleIntArrayType);
		
		// A List of expressions to write to new array (in dense format)
		List<SymbolicExpression> expressionsToWrite = Arrays.asList(new SymbolicExpression[] { null, null, two, null, two, null, null });
		
		// Will throw a SARLException because input param #2 (List<SymbolicExpressions>) must be of type ARRAY
		// "Element 2 of values argument to denseArrayWrite has incompatible type.\nExpected: int[]\nSaw: int"
		universe.denseArrayWrite(doubleIntArrayTypeExpression, expressionsToWrite);
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void testLengthExceptions(){
		
		NumericExpression[] arrayMembers = new NumericExpression[2] ;
		SymbolicExpression array;
		NumericExpression length;
		
		arrayMembers[0] = universe.integer(1);
		arrayMembers[1] = universe.integer(2);
		array = universe .array(integerType, Arrays.asList(arrayMembers));
		array = null;
		// exception for null array
		length = universe.length(array);

		
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void testLengthExceptions2(){
		// exception for non array type
		SymbolicTupleType tupleType1;
		SymbolicExpression tuple;
		NumericExpression length;
		tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType}));
		tuple = universe.tuple(tupleType1, Arrays.asList(new SymbolicExpression[]{universe.integer(1),universe.integer(2)}));
		length = universe.length(tuple);	


	}
	
	// written by Mohammad Alsulmi
	@Test
	public void emptyArrayTest(){
		// get an empty array with size 0
		SymbolicExpression array = universe.emptyArray(integerType);
		NumericExpression zero = universe.integer(0);
		assertEquals(zero,universe.length(array));
	}
	
	// written by Mohammad Alsulmi
	@Test
	public void testRemoveElementAt(){
		SymbolicExpression array, expected, resultedArray;
		NumericExpression one,two, three;
		
		
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{one,two,three}));
		expected = universe.array(integerType, Arrays.asList(new NumericExpression[]{one,three}));
		resultedArray = universe.removeElementAt(array, 1);
		
		assertEquals(expected, resultedArray);
		
		
	}
	
	// written by Mohammad Alsulmi
	@Test (expected= SARLException.class)
	public void testRemoveElementAtException(){
		
		SymbolicTupleType tupleType1;
		SymbolicExpression tuple, resultedArray;
		
		tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType}));
		tuple = universe.tuple(tupleType1, Arrays.asList(new SymbolicExpression[]{universe.integer(1),universe.integer(2)}));
		// passing an argument from type other than array
		resultedArray = universe.removeElementAt(tuple, 0);
				
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void testRemoveElementAtException2(){
		SymbolicExpression array, expected, resultedArray;
		NumericExpression one,two, three;
		
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{one,two,three}));
		expected = universe.array(integerType, Arrays.asList(new NumericExpression[]{one,three}));
		// index out of range exception
		resultedArray = universe.removeElementAt(array, 3);
		
	}
	
	// written by Mohammad Alsulmi
	@Test
	public void testArrayWrite()
	{
		SymbolicExpression array, resultedArray, expected;
		NumericExpression one,two, three, five;
		
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);
		
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{two,three,five}));
		expected = universe.array(integerType, Arrays.asList(new NumericExpression[]{two,two,five}));
		
		resultedArray = universe.arrayWrite(array, one, two);
		assertEquals(expected, resultedArray);
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void testArrayWriteException()
	{
		// testing the fail when pass a null array to arrayWrite()
		SymbolicExpression array, resultedArray;
		NumericExpression one,two, three, five;
		
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);
		
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{two,three,five}));
		array = null;
		resultedArray = universe.arrayWrite(array, one, two);
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void testArrayWriteException2()
	{
		// testing the fail when pass a null index to arrayWrite()
		SymbolicExpression array, resultedArray;
		NumericExpression one,two, three, five;
		
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);
		
		one = null;
		
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{two,three,five}));
		resultedArray = universe.arrayWrite(array, one, two);
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void testArrayWriteException3()
	{
		// testing the fail when pass a null value to arrayWrite()
		SymbolicExpression array, resultedArray;
		NumericExpression one,two, three, five;
		
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);
		
		
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{two,three,five}));
		two = null;
		resultedArray = universe.arrayWrite(array, one, two);
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void testArrayWriteException4()
	{
		// testing the fail when pass a non array type to arrayWrite()
		// here we use a tuple instead of array
		SymbolicExpression  resultedArray,tuple;
		NumericExpression one,two,five;
		SymbolicTupleType tupleType1;
		
		tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType}));
		tuple = universe.tuple(tupleType1, Arrays.asList(new SymbolicExpression[]{universe.integer(1),universe.integer(2)}));
		
		one = universe.integer(1);
		two = universe.integer(2);
		
		resultedArray = universe.arrayWrite(tuple, one, two);
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void testArrayWriteException5()
	{
		// testing the fail when pass a non integer index to arrayWrite()
		SymbolicExpression array, resultedArray;
		NumericExpression one,two, three, five;
		
		one = universe.rational(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);
		
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{two,three,five}));
		resultedArray = universe.arrayWrite(array, one, two);
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void testArrayWriteException6()
	{
		// testing the fail when passing an incompatible value to arrayWrite()
		// here the array has integer type, so we pass real type instead of integer
		SymbolicExpression array, resultedArray;
		NumericExpression one,two, three, five;
		
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);
		
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{two,three,five}));
		two = universe.rational(2.0);
		resultedArray = universe.arrayWrite(array, one, two);
	}

	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)	
	public void TestArrayReadException(){
		// testing the fail when pass a null array to arrayRead()
		SymbolicExpression array, resultedArray;
		NumericExpression one,two, three, five;
				
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{two,three,five}));
		array = null;	// null array
		resultedArray = universe.arrayRead(array, one);
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void TestArrayReadException2(){
		// testing the fail when pass a null index to arrayRead()
				
		SymbolicExpression array, resultedArray;
		NumericExpression one,two, three, five;

		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);		
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{two,three,five}));

		two = null; // null index
		resultedArray = universe.arrayRead(array, two);
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void TestArrayReadException3(){
		// testing the fail when pass a non array type to arrayRead()
		// here we use a tuple instead of array
		SymbolicExpression  resultedArray,tuple;
		NumericExpression one,two,five;
		SymbolicTupleType tupleType1;

		tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType}));
		tuple = universe.tuple(tupleType1, Arrays.asList(new SymbolicExpression[]{universe.integer(1),universe.integer(2)}));
		one = universe.integer(1);
		two = universe.integer(2);
		
		resultedArray = universe.arrayRead(tuple, two);
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void TestArrayReadException4(){
		// testing the fail when pass a negative index to arrayRead()
		SymbolicExpression array, resultedArray;
		NumericExpression negativeOne,two, three, five;
		
		negativeOne = universe.integer(-1); // negative number
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);
		
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{two,three,five}));
		resultedArray = universe.arrayRead(array, negativeOne);
	}
	
	// written by Mohammad Alsulmi
	@Test
	public void testCompatibleWithArray(){
	
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
	public void testArray(){
		// testing array() when passing with no exceptions
		LinkedList<SymbolicExpression> elements; // list of elements
		SymbolicExpression array;
		
		elements = new LinkedList<>();
		elements.add(universe.integer(5));
		elements.add(universe.integer(10));
		elements.add(universe.integer(0));
		elements.add(universe.integer(20));
		array = universe.array(integerType, elements);
		
	}
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void testArrayException(){
		// testing the fail when passing a null elements reference to array()
		LinkedList<SymbolicExpression> elements; // list of elements
		SymbolicExpression array;
		
		elements = null; // null reference
		array = universe.array(integerType, elements);
		
	}
	
	@Test (expected= SARLException.class)
	public void testArrayException2(){
		// testing the fail when passing a null elements type to array()
		LinkedList<SymbolicExpression> elements; // list of elements
		SymbolicExpression array;
		SymbolicType realType;

		elements = new LinkedList<>(); 
		elements.add(universe.integer(1));
		elements.add(universe.integer(4));
		
		realType = null;
		
		array = universe.array(realType, elements);
	}
	
	// written by Mohammad Alsulmi
	@Test (expected= SARLException.class)
	public void testArrayException3(){
		// testing the fail when passing an array with null elements to array()
		NumericExpression elements[]; // array of elements
		SymbolicExpression array;

		elements = new NumericExpression[4]; 
		// here each one of the elements need to be intialized
		
		array = universe.array(integerType, Arrays.asList(elements));

	}
	
	// written by Mohammad Alsulmi
	@Test (expected= SARLException.class)
	public void testArrayException4(){
		// testing the fail when passing non compatible type to array
		NumericExpression elements[]; // array of elements
		SymbolicExpression array;

		elements = new NumericExpression[2];
		elements[0] = universe.integer(1); // integer
		elements[1] = universe.integer(10);// integer
		
		array = universe.array(realType, Arrays.asList(elements)); // non compatible type

	}
	
	// written by Mohammad Alsulmi
	@Test 
	public void testAppend(){
	
		// here we test the regular case of array append when it passes
		SymbolicExpression array, expected;
		SymbolicExpression value;
		
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{universe.integer(7),universe.integer(10)}));
		value = universe.integer(5);
		// expected array after append
		expected = universe.array(integerType, Arrays.asList(new NumericExpression[]{universe.integer(7),universe.integer(10),universe.integer(5)}));
		// appending here
		array = universe.append(array, value);
		
		assertEquals(expected, array);
		
	}
	
	// written by Mohammad Alsulmi
	@Test (expected= SARLException.class)
	public void testAppendException(){
	
		// testing the fail when passing tuple to array append
		SymbolicTupleType tupleType;
		SymbolicExpression tuple;
		SymbolicExpression value;
		
		tupleType = universe.tupleType(universe.stringObject("type1"), Arrays.asList(new SymbolicType[]{integerType,realType}));
		tuple = universe.tuple(tupleType, Arrays.asList(new NumericExpression[]{universe.integer(10), universe.rational(6)}));
		value = universe.integer(100);
		// we expect exception since append() cannot accept other than arrays
		tuple = universe.append(tuple, value);
	}
	
	// written by Mohammad Alsulmi
	@Test (expected= SARLException.class)
	public void testAppendException2(){
	
		// testing the fail when passing a null value to array append
		SymbolicExpression array;
		SymbolicExpression value;
		
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{universe.integer(7),universe.integer(10)}));
		value = null;
		// we expect exception since append() cannot accept null values 
		array = universe.append(array, value);
	}
	
	// written by Mohammad Alsulmi
	@Test (expected= SARLException.class)
	public void testAppendException3(){
	
		// testing the fail when passing a value with incompatible to array append
		SymbolicExpression array;
		SymbolicExpression value;
		
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{universe.integer(7),universe.integer(10)}));
		// the value is real but the array is integer
		value = universe.rational(6.0);
		array = universe.append(array, value);
	}
}
