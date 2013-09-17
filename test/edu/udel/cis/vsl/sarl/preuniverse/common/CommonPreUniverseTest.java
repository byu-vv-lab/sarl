package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicSequence;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;

public class CommonPreUniverseTest {

	// Universe
	private static PreUniverse universe;
	// SymbolicTypes
	private static SymbolicType integerType;
	private static SymbolicType realType;
	private static SymbolicType arrayType;
	// Factories
	private static ObjectFactory objectFactory;
	private static ExpressionFactory expressionFactory;
	// SymbolicObjects
	private static Comparator<SymbolicObject> objectComparator;
	private static SymbolicExpression nullExpression;
	private static NumericExpression two, four;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		universe = PreUniverses.newPreUniverse(system);
		
		// Types
		integerType = universe.integerType();
		realType = universe.realType();
		arrayType = universe.arrayType(integerType);
		
		// NumberExpressions
		two = universe.integer(2);
		four = universe.integer(4);
		
		// For testing comparator() method
		objectFactory = system.objectFactory();
		objectComparator = objectFactory.comparator();
		
		// For testing nullExpression() method
		expressionFactory = system.expressionFactory();
		nullExpression = expressionFactory.nullExpression();
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
	@Ignore
	public void testCommonPreUniverse() {
		fail("Not yet implemented");
	}
	
	@Test
	@Ignore
	public void testErr() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIerr() {
		
	}

	@Test
	@Ignore
	public void testCanonicSymbolicExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testExpressionSymbolicOperatorSymbolicTypeSymbolicObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testExpressionSymbolicOperatorSymbolicTypeSymbolicObject() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testExpressionSymbolicOperatorSymbolicTypeSymbolicObjectSymbolicObject() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testExpressionSymbolicOperatorSymbolicTypeSymbolicObjectSymbolicObjectSymbolicObject() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testZero() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testHashSet() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testCompatible() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIncompatible() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testForallIntConcrete() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testExistsIntConcrete() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testTupleUnsafe() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testNumericExpressionFactory() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testCanonicSymbolicObject() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testMake() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testNumberFactory() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testAddIterableOfQextendsNumericExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testAndIterableOfQextendsBooleanExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testAndBooleanExpressionBooleanExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPureType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testBooleanType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIntegerType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testHerbrandIntegerType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRealType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testBoundedIntegerType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testHerbrandRealType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testCharacterType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testArrayTypeSymbolicTypeNumericExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testArrayTypeSymbolicType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testTypeSequenceSymbolicTypeArray() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testTypeSequenceIterableOfQextendsSymbolicType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testTupleTypeStringObjectSymbolicTypeSequence() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testTupleTypeStringObjectIterableOfQextendsSymbolicType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testFunctionTypeSymbolicTypeSequenceSymbolicType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testFunctionTypeIterableOfQextendsSymbolicTypeSymbolicType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testUnionTypeStringObjectSymbolicTypeSequence() {
		fail("Not yet implemented");
	}

	@Test
	public void testUnionType() {
		LinkedList<SymbolicType> memberTypes = new LinkedList<SymbolicType>();
		SymbolicUnionType unionType;
		SymbolicTypeSequence sequence;

		memberTypes.add(integerType);
		memberTypes.add(realType);
		unionType = universe.unionType(universe.stringObject("MyUnion"),
				memberTypes);
		assertEquals(SymbolicTypeKind.UNION, unionType.typeKind());
		sequence = unionType.sequence();
		assertEquals(integerType, sequence.getType(0));
		assertEquals(realType, sequence.getType(1));
		assertEquals(2, sequence.numTypes());
		assertEquals(universe.stringObject("MyUnion"), unionType.name());
	}

	@Test
	@Ignore
	public void testUnionTypeStringObjectIterableOfQextendsSymbolicType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testNumObjects() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testObjectWithId() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testObjects() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testBooleanObject() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testCharObject() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIntObject() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testNumberObject() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testStringObject() {
		fail("Not yet implemented");
	}

	@Test
	//written by Chris Heider
	public void testSymbolicConstant() {
		//create two symbolicConstants to see if they are equal
		StringObject name = universe.stringObject("name");
		SymbolicConstant scA = universe.symbolicConstant(name,integerType);
		SymbolicConstant scB = universe.symbolicConstant(name,integerType);
		
		assertEquals(scA,scB);
	}

	@Test
	// Written by Jordan Saints on 9/16/13
	// These nullExpression objects will be the same because they were generated by a factory
	public void testNullExpression() {
		SymbolicExpression resultNullExpression = universe.nullExpression();
		assertEquals(nullExpression, resultNullExpression); //generic test for equality
		assertTrue(resultNullExpression.equals(nullExpression)); //test if same attributes
		assertTrue(resultNullExpression == nullExpression); //test if same instance
		assertTrue(universe.equals(resultNullExpression, nullExpression).isTrue()); //use the PreUniverse method to test for equality
	}

	@Test
	@Ignore
	public void testNumberNumberObject() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIntegerInt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRationalDouble() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRationalIntInt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testZeroInt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testZeroReal() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testOneInt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testOneReal() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testCharacter() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testExtractCharacter() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testStringExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testAddNumericExpressionNumericExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSubtract() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testMultiplyNumericExpressionNumericExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testMultiplyIterableOfQextendsNumericExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDivide() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testModulo() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testMinus() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPowerNumericExpressionIntObject() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPowerNumericExpressionInt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testPowerNumericExpressionNumericExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testExtractNumber() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSubstituteSymbolicConstants() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testSubstituteSymbolicExpressionMapOfSymbolicExpressionSymbolicExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testBoolBooleanObject() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testBoolBoolean() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testOrBooleanExpressionBooleanExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testOrIterableOfQextendsBooleanExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testNot() {
		fail("Not yet implemented");
	}

	@Test
	//Test written by Chris Heider 9/16/13
	public void testImplies() {
		//setup BooleanExpressions for running in equiv()
		BooleanExpression boolA = universe.bool(true);
		BooleanExpression boolB = universe.bool(true);
		BooleanExpression boolC = universe.bool(false);
		BooleanExpression boolD = universe.bool(false);
		BooleanExpression testTrue = universe.bool(true);
		BooleanExpression testFalse = universe.bool(false);
		
		assertEquals(universe.implies(boolA, boolB), testTrue); //test for 2 true
		assertEquals(universe.implies(boolA, boolC), testFalse); //test for a failure
		assertEquals(universe.implies(boolC, boolD), testTrue); //test for 2 false
		assertEquals(universe.implies(boolA, boolA), testTrue); //test for identical
	}

	@Test
	//Test written by Chris Heider 9/16/13
	public void testEquiv() {
		//setup BooleanExpressions for running in equiv()
		BooleanExpression boolA = universe.bool(true);
		BooleanExpression boolB = universe.bool(true);
		BooleanExpression boolC = universe.bool(false);
		BooleanExpression boolD = universe.bool(false);
		BooleanExpression testTrue = universe.bool(true);
		BooleanExpression testFalse = universe.bool(false);
		
		assertEquals(universe.equiv(boolA, boolB), testTrue); //test for 2 true
		assertEquals(universe.equiv(boolA, boolC), testFalse); //test for a failure
		assertEquals(universe.equiv(boolC, boolD), testTrue); //test for 2 false
		assertEquals(universe.equiv(boolA, boolA), testTrue); //test for identical
	}

	@Test
	@Ignore
	public void testSubstituteSymbolicExpressionSymbolicConstantSymbolicExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testForallInt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testExistsInt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testLessThan() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testLessThanEquals() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testEqualsSymbolicExpressionSymbolicExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testNeq() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDivides() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testForall() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testExists() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testExtractBoolean() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testLambda() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testApply() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testUnionInject() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testUnionTest() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testUnionExtract() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testArray() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testAppend() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRemoveElementAt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testEmptyArray() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testLength() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testArrayRead() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testArrayWrite() {
		fail("Not yet implemented");
	}
	
	@Test
	/*
	 * Tests the denseArrayWrite(SymbolicExpression, Iterable) method.
	 * Written by Jordan Saints on 9/16/13
	 */
	public void testDenseArrayWrite() {
		testDenseArrayWriteSuccess(); //test method for success of usual functionality
		testDenseArrayWriteIntTypeFailure(); //trigger failure exception 1 of 2
		testDenseArrayWriteArrayTypeFailure(); //trigger failure exception 2 of 2
	}
	
	/*
	 * Auxiliary function to testDenseArrayWrite()
	 * Tests for NORMAL operation of denseArrayWrite()
	 * Written by Jordan Saints on 9/16/13
	 */
	private void testDenseArrayWriteSuccess() {
		SymbolicExpression symExpression = universe.symbolicConstant(universe.stringObject("symExpression"), arrayType);
		SymbolicExpression denseResult = universe.denseArrayWrite(
				symExpression,
				Arrays.asList(new SymbolicExpression[] { null, null, two, null, two, null, null }));

		// Test by comparing to normal write operation in arrayWrite()
		//universe.arrayWrite(array to write to, index in array to write at, value to write @ that index)
		SymbolicExpression writeResult = universe.arrayWrite(symExpression, two, two); //adds a 2 at index 2 in symExpression array
		writeResult = universe.arrayWrite(writeResult, four, two); //replace writeResult's entry at index 4 (NumExpr four) with a 2 (NumExpr two) 
		assertEquals(writeResult, denseResult); //ensure arrays are the same
		assertTrue(universe.equals(writeResult, denseResult).isTrue()); //use PreUniverse's SymbolicExpression equals() method
		
		// Test types
		assertEquals(universe.arrayType(integerType), arrayType); //test that arrayType is correct
		assertEquals(arrayType, denseResult.type()); //check that denseResult is of arrayType

		// Test arguments
		assertEquals(2, denseResult.numArguments()); //test numArguments
		@SuppressWarnings("unchecked")
		SymbolicSequence<SymbolicExpression> expressions = (SymbolicSequence<SymbolicExpression>) denseResult.argument(1);
		assertEquals(5, expressions.size()); //the 2 trailing null SymExprs will be chopped off ("made dense")
		assertTrue(universe.equals(nullExpression, expressions.get(0)).isTrue());
		assertTrue(universe.equals(nullExpression, expressions.get(1)).isTrue());
		assertTrue(universe.equals(two, expressions.get(2)).isTrue());
		assertTrue(universe.equals(nullExpression, expressions.get(3)).isTrue());
		assertTrue(universe.equals(two, expressions.get(4)).isTrue());
	}
	
	/*
	 * Auxiliary function #1 for testDenseArrayWrite()
	 * Trigger failure branch 1 of 2; throws SARLException
	 * Written by Jordan Saints on 9/16/13
	 */
	private void testDenseArrayWriteIntTypeFailure() {
		SymbolicExpression integerTypeExpression = universe.symbolicConstant(universe.stringObject("notInstanceOfSymbolicArrayType"), integerType);
		SymbolicExpression exception1Result = null;
		String exception1Message = "Argument 0 of denseArrayWrite must have array type but had type int";
		try {
			exception1Result = universe.denseArrayWrite(
					integerTypeExpression,
					Arrays.asList(new SymbolicExpression[] { null, null, two, null, two, null, null }));
			
			// Test for undesired universe.denseArrayWrite(x,x) success
			if (exception1Result != null) { //if no exception is thrown...
				assertTrue(false); //this test has failed (an exception SHOULD be thrown!)
			}
		} catch (Exception e) {
			assertEquals(e.getClass(), SARLException.class); //test class name of thrown exception
			assertEquals(e.getMessage(), exception1Message); //test message of thrown exception
			assertTrue(e.getMessage().equals(exception1Message)); //double check
		}
	}
	
	/*
	 * Auxiliary function #2 for testDenseArrayWrite()
	 * Trigger failure branch 2 of 2; throws SARLException
	 * Written by Jordan Saints on 9/16/13
	 */
	private void testDenseArrayWriteArrayTypeFailure() {
		SymbolicType doubleArrayType = universe.arrayType(arrayType); //int[]
		SymbolicExpression arrayTypeExpression = universe.symbolicConstant(universe.stringObject("arrayTypeExpression"), doubleArrayType);
		SymbolicExpression exception2Result = null;
		String exception2Message = "Element 2 of values argument to denseArrayWrite has incompatible type.\nExpected: int[]\nSaw: int";
		try {
			exception2Result = universe.denseArrayWrite(
					arrayTypeExpression,
					Arrays.asList(new SymbolicExpression[] { null, null, two, null, two, null, null }));
			
			// Test for undesired universe.denseArrayWrite(x,x) success
			if (exception2Result != null) { //if no exception is thrown...
				assertTrue(false); //this test has failed (an exception SHOULD be thrown!)
			}
		} catch (Exception e) {
			assertEquals(e.getClass(), SARLException.class); //test class name of thrown exception
			assertEquals(e.getMessage(), exception2Message); //test message of thrown exception
			assertTrue(e.getMessage().equals(exception2Message)); //double check
		}
	}

	@Test
	@Ignore
	public void testDenseTupleWrite() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testArrayLambda() {
		assertEquals(null, universe.arrayLambda((SymbolicCompleteArrayType)integerType, nullExpression)); //Simple test for coverage.
	}

	@Test
	@Ignore
	public void testTuple() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testTupleRead() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testTupleWrite() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testCast() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testCond() {
		fail("Not yet implemented");
	}

	@Test
	/*
	 * Tests the comparator() factory method.
	 * Written by Jordan Saints on 9/16/13
	 */
	public void testComparator() {
		Comparator<SymbolicObject> resultComparator = universe.comparator();
		
		//the comparator objects objectComparator and resultComparator
		//will be the same because they are generated by a factory
		assertEquals(objectComparator, resultComparator); //generic test for equality
		assertTrue(resultComparator.equals(objectComparator)); //test if same attributes
		assertTrue(resultComparator == objectComparator); //test if same instance
	}

	@Test
	@Ignore
	public void testIntegerLong() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIntegerBigInteger() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRationalInt() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRationalLong() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRationalBigInteger() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRationalFloat() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRationalLongLong() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testRationalBigIntegerBigInteger() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testNumberNumber() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testTrueExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testFalseExpression() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testNumValidCalls() {
		fail("Not yet implemented");
	}

	@Test
	public void testNumProverValidCalls() {
		assertEquals(universe.numProverValidCalls(), 0); //at the time of tests, universe.proverValidCount should be 0;
	}

	@Test
	@Ignore
	public void testIncrementValidCount() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIncrementProverValidCount() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testBasicCollection() {
		fail("Not yet implemented");
	}

	@Test
	/*
	 * Tests the referenceType() method.
	 * Written by Jordan Saints on 9/16/13
	 */
	public void testReferenceType() {
		// Setup
		SymbolicType refType = universe.referenceType(); //call referenceType() method
		SymbolicTupleType refTuple = (SymbolicTupleType) refType; //cast to TUPLE SymbolicType
		SymbolicTypeSequence refTupleSequence = refTuple.sequence(); //pull out the tuple's SymbolicTypeSequence
		
		// Tests
		assertEquals(SymbolicTypeKind.TUPLE, refType.typeKind()); //test that the refType is a TUPLE kind
		assertEquals(universe.stringObject("Ref"), refTuple.name()); //test the name of the tuple
		//assertEquals("Ref", refTuple.name().getString()); //test the name of the tuple // ALSO WORKS
		assertEquals(1, refTupleSequence.numTypes()); //test the number of types available in this tuple's sequence
		assertEquals(integerType, refTupleSequence.getType(0)); //test sequence type
	}

	@Test
	@Ignore
	public void testNullReference() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testDereference() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testReferencedType() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testIdentityReference() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testArrayElementReference() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testTupleComponentReference() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testUnionMemberReference() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testOffsetReference() {
		fail("Not yet implemented");
	}

	@Test
	@Ignore
	public void testAssign() {
		fail("Not yet implemented");
	}
	
	@Test
	@Ignore
	public void testRational() { //tests cannot have any parameters
		fail("Not yet implemented");
//		numerator = 50;
//		denominator = 49;
//		N1= number(numberFactory.rational(numerator, denominator));
//		assertEquals(N1,(50/49));
	}
	
	@Test
	@Ignore
	public void testInteger() { //tests cannot have any parameters
		fail("Not yet implemented");
//		value = 50;
//		BigInteger N1= number(numberFactory.integer(value));
//		assertEquals(N1,50);
	}
}
