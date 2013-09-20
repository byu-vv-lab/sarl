package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;
import java.util.Arrays;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
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
	private static SymbolicType arrayType;
	private static SymbolicType doubleArrayType;
	// SymbolicExpressions
	private static SymbolicExpression nullExpression;
	private static NumericExpression two, four;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		universe = PreUniverses.newPreUniverse(system);
		
		// Instantiate Types
		integerType = universe.integerType();
		arrayType = universe.arrayType(integerType); //creates an array of ints
		doubleArrayType = universe.arrayType(arrayType); //creates an array of int[]s
		
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
		SymbolicExpression arrayTypeExpression = universe.symbolicConstant(universe.stringObject("arrayTypeExpression"), arrayType);
		
		// A List of expressions to write to new array (in dense format),
		// where, after calling denseArrayWrite(), an element's index in this List == index in resulting expressions sequence
		List<SymbolicExpression> expressionsToWrite = Arrays.asList(new SymbolicExpression[] { null, null, two, null, two, null, null });
		
		// Call denseArrayWrite()!
		SymbolicExpression denseResult = universe.denseArrayWrite(arrayTypeExpression, expressionsToWrite);
		
		// Test by comparing to normal write operation in arrayWrite()
		// USAGE: universe.arrayWrite(array to write to, index in array to write at, value to write @ that index)
		SymbolicExpression writeResult = universe.arrayWrite(arrayTypeExpression, two, two); //adds a 2 at index 2 in arrayTypeExpression array
		writeResult = universe.arrayWrite(writeResult, four, two); //replace writeResult's entry at index 4 (NumExpr four) with a 2 (NumExpr two) 
		assertEquals(writeResult, denseResult); //test if arrays are equal
		
		// Test denseResult's type
		assertEquals(universe.arrayType(integerType), arrayType); //check that arrayType is actually correct type to begin with
		assertEquals(arrayType, denseResult.type()); //check that denseResult is of arrayType

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
		SymbolicExpression doubleArrayTypeExpression = universe.symbolicConstant(universe.stringObject("doubleArrayTypeExpression"), doubleArrayType);
		
		// A List of expressions to write to new array (in dense format)
		List<SymbolicExpression> expressionsToWrite = Arrays.asList(new SymbolicExpression[] { null, null, two, null, two, null, null });
		
		// Will throw a SARLException because input param #2 (List<SymbolicExpressions>) must be of type ARRAY
		// "Element 2 of values argument to denseArrayWrite has incompatible type.\nExpected: int[]\nSaw: int"
		universe.denseArrayWrite(doubleArrayTypeExpression, expressionsToWrite);
	}
}
