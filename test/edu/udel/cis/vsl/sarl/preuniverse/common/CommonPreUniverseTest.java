package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.udel.cis.vsl.sarl.IF.SARLException;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericSymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.ReferenceExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.object.BooleanObject;
import edu.udel.cis.vsl.sarl.IF.object.IntObject;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicCompleteArrayType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicFunctionType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTupleType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType.SymbolicTypeKind;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicTypeSequence;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicUnionType;
import edu.udel.cis.vsl.sarl.expr.IF.BooleanExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.expr.IF.NumericExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

@SuppressWarnings("all")
public class CommonPreUniverseTest {

	// Universe
	private static PreUniverse universe;
	// SymbolicTypes
	private static SymbolicType integerType;
	private static SymbolicType realType;
	private static SymbolicType booleanType;
	private static SymbolicType arrayType, realArray;
	// Factories
	private static ObjectFactory objectFactory;
	private static ExpressionFactory expressionFactory;
	private static BooleanExpressionFactory booleanFactory;
	private static NumericExpressionFactory numericFactory;
	private static SymbolicTypeFactory typeFactory;
	private static NumberFactory numberFactory;
	// SymbolicObjects
	private static Comparator<SymbolicObject> objectComparator;
	private static SymbolicExpression nullExpression;
	private static SymbolicCompleteArrayType symbolicCompleteArrayType;
	// SymbolicExpressions
	private static SymbolicConstant symbolicConstant;
	private static NumericExpression numericExpression;
	// Collections
	private static Collection<SymbolicObject> objectCollection;
	private static ArrayList<NumericExpression> emptyNumericList;
	private static ArrayList<NumericExpression> numericList;
	
	private static SymbolicUnionType union1;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		FactorySystem system = PreUniverses.newIdealFactorySystem();
		universe = PreUniverses.newPreUniverse(system);
		
		// Types
		integerType = universe.integerType();
		booleanType = universe.booleanType();
		realType = universe.realType();
		arrayType = universe.arrayType(integerType); //creates an array of ints
		realArray = universe.arrayType(realType);
		
		// For testing comparator() method
		objectFactory = system.objectFactory();
		objectComparator = objectFactory.comparator();
		
		// For testing nullExpression() method
		expressionFactory = system.expressionFactory();
		nullExpression = expressionFactory.nullExpression();
		
		booleanFactory = system.booleanFactory();
		
		// For testing objects() method
		objectCollection = objectFactory.objects();
		
		// For testing multipl(Iterable) method
		emptyNumericList = new ArrayList<NumericExpression>();
		numericList = new ArrayList<NumericExpression>();
		
		numericFactory = system.numericFactory();
		
		typeFactory = system.typeFactory();
		
		union1 = universe.unionType(
				universe.stringObject("union1"),
				Arrays.asList(new SymbolicType[] { integerType, realType,
						booleanType, realArray }));
		
		
		
	}

	@Test
	// Test written by Jeff DiMarco (jdimarco) 9/20/13
	public void testZero() {
		SymbolicType intType;
		SymbolicType realType;
		CommonPreUniverse commonUniverse;
		
		intType = universe.integerType();
		realType = universe.realType();
		commonUniverse = (CommonPreUniverse)universe;
		
		assertEquals(commonUniverse.zero(intType), numericFactory.zeroInt());
		assertEquals(commonUniverse.zero(realType), numericFactory.zeroReal());
	}
	
	@Test(expected = SARLInternalException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/20/13
	public void testZeroErr(){
		SymbolicUnionType unionType;
		LinkedList<SymbolicType> memberTypes;
		CommonPreUniverse commonUniverse;
		
		commonUniverse = (CommonPreUniverse)universe;
		
		memberTypes = new LinkedList<SymbolicType>();
		
		memberTypes.add(integerType);
		memberTypes.add(realType);
		
		unionType = universe.unionType(universe.stringObject("MyUnion"), memberTypes);
		
		commonUniverse.zero(unionType);
	}

	@Test
	// Test written Jeff DiMarco (jdimarco) 9/24/13
	public void testAndIterableOfQextendsBooleanExpression() {
		LinkedList<BooleanExpression> booleanList1;
		LinkedList<BooleanExpression> booleanList2;
		LinkedList<BooleanExpression> booleanList3;
		LinkedList<BooleanExpression> booleanEmptyList;
		BooleanExpression trueExpr;
		BooleanExpression falseExpr;
		
		booleanList1 = new LinkedList<BooleanExpression>();
		booleanList2 = new LinkedList<BooleanExpression>();
		booleanList3 = new LinkedList<BooleanExpression>();
		booleanEmptyList = new LinkedList<BooleanExpression>();
		trueExpr = universe.bool(true);
		falseExpr = universe.bool(false);
		
		booleanList1.add(trueExpr);
		booleanList1.add(trueExpr);
		booleanList1.add(trueExpr);
		
		booleanList2.add(trueExpr);
		booleanList2.add(trueExpr);
		booleanList2.add(falseExpr);
		
		booleanList3.add(falseExpr);
		booleanList3.add(falseExpr);
		booleanList3.add(falseExpr);
		
		assertEquals(universe.and(booleanList1), trueExpr); // test all true
		assertEquals(universe.and(booleanList2), falseExpr); // test partial false
		assertEquals(universe.and(booleanList3), falseExpr); // test all false
		assertEquals(universe.and(booleanEmptyList), trueExpr); // test empty is true
	}	
	
	@Test
	// Test written by Jeff DiMarco (jdimarco) 9/24/13
	public void testTypeSequenceSymbolicTypeArray() {
		SymbolicType[] typeArray = {typeFactory.booleanType(), typeFactory.integerType()};
		SymbolicTypeSequence typeSequence;
		SymbolicTypeSequence expectedTypeSequence;
		
		CommonPreUniverse commonUniverse = (CommonPreUniverse)universe;
		typeSequence = commonUniverse.typeSequence(typeArray);
		expectedTypeSequence = typeFactory.sequence(typeArray);
		
		assertEquals(expectedTypeSequence.numTypes(), typeSequence.numTypes());
		assertEquals(expectedTypeSequence.getType(0), typeSequence.getType(0));
		assertEquals(expectedTypeSequence.getType(1), typeSequence.getType(1));
	}

	@Test
	// Written by Jeff DiMarco(jdimarco) 9/20/13
	public void testUnionTypeStringObjectSymbolicTypeSequence() {
		LinkedList<SymbolicType> memberTypes = new LinkedList<SymbolicType>();
		SymbolicUnionType unionType;
		SymbolicTypeSequence sequence;
		CommonPreUniverse commonUniverse = (CommonPreUniverse)universe;

		memberTypes.add(integerType);
		memberTypes.add(realType);
		sequence = universe.typeSequence(memberTypes);
		
		unionType = commonUniverse.unionType(universe.stringObject("MyUnion"),
				sequence);
		
		assertEquals(SymbolicTypeKind.UNION, unionType.typeKind());
		sequence = unionType.sequence();
		assertEquals(integerType, sequence.getType(0));
		assertEquals(realType, sequence.getType(1));
		assertEquals(2, sequence.numTypes());
		assertEquals(universe.stringObject("MyUnion"), unionType.name());
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
	// Written by Marlin Blue
	public void testNumObjects() {
		// Testing universe against factory
		int object = universe.numObjects();		
		assertEquals(object, objectFactory.numObjects());
	}

	@Test
	// Test written by Jeff DiMarco (jdimarco) 9/20/13
	public void testObjectWithId() {
		SymbolicObject obj1;
		
		obj1 = objectFactory.objectWithId(3);
		
		assertEquals(obj1.id(), 3);
	}

	@Test
	// Written by Marlin Blue
	public void testObjects() {
		Collection<SymbolicObject> testCollection = 
				universe.objects();
		assertEquals(objectCollection, testCollection);
	}

	@Test
	//written by Chris Heider
	public void testSymbolicConstant() {
		//create two symbolicConstants to see if they are equal
		StringObject name = universe.stringObject("name");
		SymbolicType type = universe.booleanType();  //need to test for bool type. the other has been done.
		SymbolicConstant scA = universe.symbolicConstant(name, type);
		SymbolicConstant scB = universe.symbolicConstant(name, type);
		
		assertEquals(scA,scB);
	}

	@Test
	// Written by Jordan Saints on 9/16/13
	// These nullExpression objects will be the same because they were generated by a factory
	public void testNullExpression() {
		SymbolicExpression resultNullExpression = universe.nullExpression();
		assertEquals(nullExpression, resultNullExpression); //test for equality
	}

	@Test(expected=SARLException.class)	
	public void testEmptyMultiply() {
		universe.multiply(emptyNumericList);
			
	}
	
	@Test 
	public void testMultiply() {
		ArrayList<NumericExpression> testList =
				new ArrayList<NumericExpression>();
		NumericExpression one, three;
		one = universe.integer(1);
		three = universe.integer(3);
		
		testList.add(one);
		testList.add(three);
		numericList.add(one);
		numericList.add(three);
		NumericExpression testResult = universe.multiply(testList);
		
		assertEquals(universe.multiply(numericList), testResult);
	}

	@Test
	// Test written by Jeff DiMarco (jdimarco) 9/17/13
	public void testMinus() {
		NumericExpression seventeen = universe.integer(17);
		NumericExpression negativeSeventeen = universe.integer(-17);
		assertEquals(universe.minus(seventeen), negativeSeventeen); // test -( 17) = -17
		assertEquals(universe.minus(negativeSeventeen), seventeen); // test -(-17) =  17
	}

	@Test
	// Test written by Jeff DiMarco (jdimarco) 9/17/13
	public void testBoolBooleanObject() {
		BooleanObject booleanObj = universe.booleanObject(true);
		BooleanExpression booleanExpr = booleanFactory.symbolic(booleanObj);
		assertEquals(universe.bool(booleanObj), booleanExpr); // trivial check of return type
	}

	@Test
	// Test written by Jeff DiMarco (jdimarco) 9/20/13
	public void testOrIterableOfQextendsBooleanExpression() {
		LinkedList<BooleanExpression> booleanList1;
		LinkedList<BooleanExpression> booleanList2;
		LinkedList<BooleanExpression> booleanList3;
		BooleanExpression trueExpr;
		BooleanExpression falseExpr;
		
		trueExpr = universe.bool(true);
		falseExpr = universe.bool(false);
		
		booleanList1 = new LinkedList<BooleanExpression>();
		booleanList2 = new LinkedList<BooleanExpression>();
		booleanList3 = new LinkedList<BooleanExpression>();
		
		booleanList1.add(universe.bool(false));
		booleanList1.add(universe.bool(false));
		booleanList1.add(universe.bool(true));
		booleanList1.add(universe.bool(false));
		
		booleanList2.add(universe.bool(false));
		booleanList2.add(universe.bool(false));
		booleanList2.add(universe.bool(false));
		booleanList2.add(universe.bool(false));
		
		booleanList3.add(universe.bool(true));
		booleanList3.add(universe.bool(true));
		booleanList3.add(universe.bool(true));
		booleanList3.add(universe.bool(true));
		
		assertEquals(universe.or(booleanList1), trueExpr);
		assertEquals(universe.or(booleanList2), falseExpr);
		assertEquals(universe.or(booleanList3), trueExpr);
			
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
	// Written by Marlin Blue 9/25
	public void testForallInt() {
		StringObject name = universe.stringObject("name");
		SymbolicType type = universe.integerType(); 
		SymbolicConstant index = universe.symbolicConstant(name, type);
		NumericExpression low, high, low2, high2;
		low = universe.integer(999);
		high = universe.integer(2000);
		low2 = universe.integer(1200);
		high2 = universe.integer(2350);
		SymbolicConstant nullConstant = 
				universe.symbolicConstant(
						universe.stringObject("null"), integerType);
		BooleanExpression falseExp = universe.bool(false);
		BooleanExpression trueExp = universe.bool(true);
		
		BooleanExpression testResult1 = 
				universe.forallInt((NumericSymbolicConstant)index, 
						low, high, trueExp);
		BooleanExpression testResult2 = 
				universe.forallInt((NumericSymbolicConstant)index, 
						(NumericExpression)nullConstant, high, trueExp);
		BooleanExpression testResult3 = 
				universe.forallInt((NumericSymbolicConstant)index, 
						low, (NumericExpression)nullConstant, trueExp);
		
		// Testing non-null values
		assertEquals(universe.forallInt(
				(NumericSymbolicConstant) universe.symbolicConstant(
						universe.stringObject("name"), integerType),
				universe.integer(999), universe.integer(2000), 
				universe.bool(true)), testResult1);
		// Testing null values
		assertEquals(universe.forallInt(
				(NumericSymbolicConstant) universe.symbolicConstant(
						universe.stringObject("name"), integerType),
						(NumericExpression)nullConstant, 
						universe.integer(2000), 
						universe.bool(true)), testResult2);
		assertEquals(universe.forallInt(
				(NumericSymbolicConstant) universe.symbolicConstant(
						universe.stringObject("name"), integerType),
						universe.integer(999), (NumericExpression)nullConstant, 
						universe.bool(true)), testResult3);
	}

	@Test
	// Written by Marlin Blue 9/25
	public void testExistsInt() {
		StringObject name = universe.stringObject("branch1");
		SymbolicType type = universe.integerType(); 
		
		SymbolicConstant index = universe.symbolicConstant(name, type);
		NumericExpression low1, high1, low2, high2;
		low1 = universe.integer(1350);
		high1 = universe.integer(1200);
		low2 = universe.integer(1200);
		SymbolicConstant nullConstant = 
				universe.symbolicConstant(
						universe.stringObject("null"), integerType);
		high2 = universe.integer(2350);
		BooleanExpression falseExp = universe.bool(false);
		BooleanExpression trueExp = universe.bool(true);
		
		BooleanExpression testResult1 = 
				universe.existsInt((NumericSymbolicConstant)index,
						low1, high1, falseExp);
		BooleanExpression testResult2 = 
				universe.existsInt((NumericSymbolicConstant)index,
						low2, high2, trueExp);
		BooleanExpression testResult3 = 
				universe.existsInt((NumericSymbolicConstant)index, 
						(NumericExpression)nullConstant, 
						high1, falseExp);
		BooleanExpression testResult4 = 
				universe.existsInt((NumericSymbolicConstant)index, 
						low2, (NumericExpression)nullConstant, trueExp);
		
		// Testing non-null values
		assertEquals(universe.existsInt(
				(NumericSymbolicConstant) universe.symbolicConstant(
						universe.stringObject("branch1"), integerType),
						universe.integer(1350), universe.integer(1200), 
						universe.bool(false)), testResult1);
		assertEquals(universe.existsInt(
				(NumericSymbolicConstant) universe.symbolicConstant(
						universe.stringObject("branch1"), integerType),
						universe.integer(1200), universe.integer(2350), 
						universe.bool(true)), testResult2);
		// Testing null values
		assertEquals(universe.existsInt(
				(NumericSymbolicConstant) universe.symbolicConstant(
						universe.stringObject("branch1"), integerType),
						(NumericExpression)nullConstant, universe.integer(1200), 
						universe.bool(false)), testResult3);
		assertEquals(universe.existsInt(
				(NumericSymbolicConstant) universe.symbolicConstant(
						universe.stringObject("branch1"), integerType),
						universe.integer(1200), (NumericExpression)nullConstant, 
						universe.bool(true)), testResult4);
	}

	@Test
	// Test written by Jeff DiMarco (jdimarco) 9/23/13
	public void testEqualsSymbolicExpressionSymbolicExpression() {
		SymbolicExpression symbolicExpr1;
		SymbolicExpression symbolicExpr2;
		SymbolicExpression symbolicExpr3;
		SymbolicExpression symbolicExpr4;
		SymbolicExpression symbolicExpr5;
		SymbolicExpression symbolicExpr6;
		SymbolicExpression symbolicExpr7;
		LinkedList<SymbolicType> memberTypes;
		LinkedList<SymbolicType> oneType;
		LinkedList<SymbolicType> zeroTypes;
		SymbolicUnionType unionType1;
		SymbolicUnionType unionType2;
		SymbolicUnionType unionType3;
		SymbolicUnionType unionType4;
		SymbolicUnionType unionType5;
		SymbolicUnionType unionType6;
		SymbolicUnionType unionType7;
		SymbolicObject x1;
		BooleanExpression trueExpr = universe.bool(true);
		BooleanExpression falseExpr = universe.bool(false);
		
		x1 = universe.symbolicConstant(universe.stringObject("x1"), integerType);

		memberTypes = new LinkedList<SymbolicType>();
		zeroTypes = new LinkedList<SymbolicType>();
		oneType = new LinkedList<SymbolicType>();
		memberTypes.add(integerType);
		memberTypes.add(realType);
		oneType.add(integerType);
		unionType1 = universe.unionType(universe.stringObject("MyUnion1"),
				memberTypes);
		unionType2 = universe.unionType(universe.stringObject("MyUnion1"),
				memberTypes);
		unionType3 = universe.unionType(universe.stringObject("MyUnion1"),
				oneType);
		unionType4 = universe.unionType(universe.stringObject("MyUnion1"),
				oneType);
		unionType5 = universe.unionType(universe.stringObject("MyUnion1"),
				zeroTypes);
		unionType6 = universe.unionType(universe.stringObject("MyUnion1"),
				zeroTypes);
		unionType7 = universe.unionType(universe.stringObject("MyUnion1"),
				memberTypes);
		
		symbolicExpr1 = expressionFactory.expression(SymbolicExpression.SymbolicOperator.UNION_INJECT, 
				unionType1, universe.intObject(1), x1);
		symbolicExpr2 = expressionFactory.expression(SymbolicExpression.SymbolicOperator.UNION_INJECT, 
				unionType2, universe.intObject(2), x1);
		symbolicExpr3 = expressionFactory.expression(SymbolicExpression.SymbolicOperator.UNION_TEST, 
				unionType3, universe.intObject(1), x1);
		symbolicExpr4 = expressionFactory.expression(SymbolicExpression.SymbolicOperator.UNION_TEST, 
				unionType4, universe.intObject(4), x1);
		symbolicExpr5 = expressionFactory.expression(SymbolicExpression.SymbolicOperator.UNION_TEST, 
				unionType5, universe.intObject(5), x1);
		symbolicExpr6 = expressionFactory.expression(SymbolicExpression.SymbolicOperator.UNION_TEST, 
				unionType6, universe.intObject(6), x1);
		symbolicExpr7 = expressionFactory.expression(SymbolicExpression.SymbolicOperator.UNION_TEST, 
				unionType7, universe.intObject(1), x1);
		
		// Test that unions with different values are not equal
		assertEquals(universe.equals(symbolicExpr1, symbolicExpr2), falseExpr);
		assertEquals(universe.equals(symbolicExpr1, symbolicExpr3), falseExpr);
		assertEquals(universe.equals(symbolicExpr3, symbolicExpr1), falseExpr);
		assertEquals(universe.equals(symbolicExpr1, symbolicExpr7), falseExpr);
		assertEquals(universe.equals(symbolicExpr7, symbolicExpr1), falseExpr);
		// Test UNION_INJECT, UNION_TEST
		assertEquals(universe.equals(symbolicExpr1, symbolicExpr4), falseExpr);
		assertEquals(universe.equals(symbolicExpr5, symbolicExpr1), falseExpr);
		// Test that empty unions with different expressions are not equal
		assertEquals(universe.equals(symbolicExpr5, symbolicExpr6), falseExpr);
	}

	@Test
	// Test written by Jeff DiMarco(jdimarco) 9/20/13
	public void testExtractBoolean() {
		BooleanExpression trueExpr;
		BooleanExpression falseExpr;
		BooleanExpression nullExpr;
		
		trueExpr = universe.bool(true);
		falseExpr = universe.bool(false);
		nullExpr = null;
		
		assertEquals(universe.extractBoolean(trueExpr), true);
		assertEquals(universe.extractBoolean(falseExpr), false);
		assertEquals(universe.extractBoolean(nullExpr), null);
			
	}

	@Test
	public void testArrayLambda() {
		assertEquals(null, universe.arrayLambda(symbolicCompleteArrayType, nullExpression)); //Simple test for coverage.
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
	public void testIntegerBigInteger() {
//		NumericExpression BigInt1, BigInt2;
//		long x = 2;
//		BigInt1=universe.integer((int) Math.pow(x,10));
		
		fail("Not yet implemented");
	}

	@Test
	public void testNumProverValidCalls() {
		assertEquals(universe.numProverValidCalls(), 0); //at the time of tests, universe.proverValidCount should be 0;
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
		assertEquals("Ref", refTuple.name().getString()); //extra test for the name of the tuple (covers the .getString() branch)
		assertEquals(1, refTupleSequence.numTypes()); //test the number of types available in this tuple's sequence
		assertEquals(integerType, refTupleSequence.getType(0)); //test sequence type
	}

	@Test
	@Ignore
	public void testDereference() {
		SymbolicType doubleArrayType = universe.arrayType(arrayType); //int[]
		SymbolicExpression arrayTypeExpression = universe.symbolicConstant(universe.stringObject("arrayTypeExpression"), doubleArrayType);
		
		try
		{
			universe.dereference(arrayTypeExpression, null);
		}
		catch(Exception e)
		{
			assertEquals(e.getClass(), SARLException.class); //test class name of thrown exception	
			assertEquals(e.getMessage(), "dereference given null reference");
		}
		
		try
		{
			universe.dereference(null, null);
		}
		catch(Exception e)
		{
			assertEquals(e.getClass(), SARLException.class); //test class name of thrown exception	
			assertEquals(e.getMessage(), "dereference given null value");
		}
	}

	@Test
	//ReferencedType test. Written by Julian Piane 9/22/13
	public void testReferencedType() {
		//instantiate our types 
		NumericExpression zero, one,two, three;
		
		IntObject zeroInt = universe.intObject(0);

		zero = universe.integer(0);
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		
		//Reference Expressions
		ReferenceExpression nullReference, offsetReference, identityReference, unionReference, arrayReference, twoDimensionalArrayReference, tupleInArrayReference, arrayInTupleReference;
		nullReference = universe.nullReference();
		identityReference = universe.identityReference();
		arrayReference = universe.arrayElementReference(identityReference, zero);
		twoDimensionalArrayReference = universe.arrayElementReference(identityReference, zero);
		tupleInArrayReference = universe.arrayElementReference(identityReference, zero);
		arrayInTupleReference = universe.tupleComponentReference(identityReference, zeroInt);
		offsetReference = universe.offsetReference(identityReference, zero);
		unionReference = universe.unionMemberReference(identityReference, zeroInt);

		//Tuple containing array
		SymbolicTupleType tupleOfArrayType = universe.tupleType(universe.stringObject("tupleOfArrayType"), Arrays.asList(new SymbolicType[]{arrayType}));

		//Tuple containing array test and offset test
		assertEquals(universe.referencedType(tupleOfArrayType, arrayInTupleReference), arrayType);
		assertEquals(universe.referencedType(tupleOfArrayType, offsetReference).typeKind(), tupleOfArrayType.typeKind());
		
		//Array containing Tuple
		SymbolicArrayType arrayOfTupleType = universe.arrayType(tupleOfArrayType);

		//Array containing Tuple test
		assertEquals(universe.referencedType(arrayOfTupleType, tupleInArrayReference), tupleOfArrayType);
		
		//Two Dimensional Array
		SymbolicArrayType twoDimensionalArrayType = universe.arrayType(arrayType);
		
		//Union Type
		SymbolicUnionType unionType = universe.unionType(universe.stringObject("UnionType"), Arrays.asList(new SymbolicType[]{integerType,realType}));

		//Two Dimensional Array test and UnionTest
		assertEquals(universe.referencedType(twoDimensionalArrayType, twoDimensionalArrayReference), arrayType);
		assertEquals(universe.referencedType(unionType, unionReference), integerType);
		
		
		//ERROR TESTS
		try{universe.referencedType(arrayOfTupleType, arrayInTupleReference);}
		catch(Exception e){assertEquals(e.getClass(), SARLException.class);}
		try{universe.referencedType(arrayOfTupleType, nullReference);}
		catch(Exception e){assertEquals(e.getClass(), SARLException.class);}
		try{universe.referencedType(null, nullReference);}
		catch(Exception e){assertEquals(e.getClass(), SARLException.class);}
		try{universe.referencedType(arrayOfTupleType, null);}
		catch(Exception e){assertEquals(e.getClass(), SARLException.class);}
		try{universe.referencedType(twoDimensionalArrayType, arrayReference);}
		catch(Exception e){assertEquals(e.getClass(), SARLException.class);}
		try{universe.referencedType(tupleOfArrayType, tupleInArrayReference);}
		catch(Exception e){assertEquals(e.getClass(), SARLException.class);}
		try{assertEquals(universe.referencedType(twoDimensionalArrayType, unionReference), arrayType);}
		catch(Exception e){assertEquals(e.getClass(), SARLException.class);}
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
	// Test written by Jeff DiMarco/Julian Piane (jdimarco) 9/25/13
	public void testAssign() {
		SymbolicExpression ten = universe.integer(10);
		SymbolicExpression u_ten = universe.unionInject(union1,
				universe.intObject(0), ten);
		
		ReferenceExpression iref = expressionFactory.identityReference();
		ReferenceExpression nref = expressionFactory.nullReference();
		
		assertEquals(universe.assign(u_ten, iref, u_ten), u_ten); // test for subvalue
		
		ReferenceExpression offsetReference5 = universe.offsetReference(expressionFactory.identityReference(), universe.integer(5));
		ReferenceExpression offsetReference0 = universe.offsetReference(expressionFactory.identityReference(), universe.integer(0));
		
		assertEquals(universe.assign(u_ten, offsetReference0, u_ten), u_ten);
		
		//Test exception case
		try{universe.assign(u_ten, offsetReference5, u_ten);}
		catch(Exception e){assertEquals(e.getClass(), SARLException.class);}
	}
	
	@Test (expected = SARLException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/25/13
	public void testAssignException1() {
		SymbolicExpression ten = universe.integer(10);
		SymbolicExpression u_ten = universe.unionInject(union1,
				universe.intObject(0), ten);
		
		ReferenceExpression iref = expressionFactory.identityReference();
		ReferenceExpression nref = expressionFactory.nullReference();
		
		universe.assign(u_ten, nref, u_ten); // test for SARLException
	}
	
	@Test (expected = SARLException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/25/13
	public void testAssignException2() {
		SymbolicExpression ten = universe.integer(10);
		SymbolicExpression u_ten = universe.unionInject(union1,
				universe.intObject(0), ten);
		
		ReferenceExpression iref = expressionFactory.identityReference();
		ReferenceExpression nref = expressionFactory.nullReference();
		
		universe.assign(u_ten, iref, null); // test for SARLException
	}
	
	@Test (expected = SARLException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/25/13
	public void testAssignException3() {
		SymbolicExpression ten = universe.integer(10);
		SymbolicExpression u_ten = universe.unionInject(union1,
				universe.intObject(0), ten);
		
		ReferenceExpression iref = expressionFactory.identityReference();
		ReferenceExpression nref = expressionFactory.nullReference();
		
		universe.assign(null, nref, u_ten); // test for SARLException
	}
	
	@Test (expected = SARLException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/25/13
	public void testAssignException4() {
		SymbolicExpression ten = universe.integer(10);
		SymbolicExpression u_ten = universe.unionInject(union1,
				universe.intObject(0), ten);
		
		ReferenceExpression iref = expressionFactory.identityReference();
		ReferenceExpression nref = expressionFactory.nullReference();
		
		universe.assign(u_ten, null, u_ten); // test for SARLException
	}
	
	
	
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void tupleExceptionTest1(){
		
		SymbolicTupleType tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType,realType}));
		SymbolicExpression tuple = universe.tuple(tupleType1, Arrays.asList(new SymbolicExpression[]{universe.integer(1),universe.integer(2)}));
	}
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void tupleExceptionTest2(){
		SymbolicTupleType tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType,realType}));
		
		SymbolicExpression tuple = universe.tuple(tupleType1, Arrays.asList(new SymbolicExpression[]{universe.rational(1),universe.integer(2),universe.integer(2)}));

		
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
	@Test(expected= SARLException.class)
	public void tupleWriteTest(){
		SymbolicTupleType tupleType1;
		SymbolicExpression tuple, resultedTuple;
		IntObject i1;
		i1 = universe.intObject(1);
		tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType}));
		tuple = universe.tuple(tupleType1, Arrays.asList(new SymbolicExpression[]{universe.integer(1),universe.integer(2)}));

		resultedTuple = universe.tupleWrite(tuple, i1, universe.integer(2));
		assertEquals(tuple, resultedTuple);
		
		
		// exception
		tuple = universe.tupleWrite(tuple, i1, universe.rational(3));
		
			
	}
	// written by Mohammad Alsulmi
	@Test
	public void emptyArrayTest(){
		// get an empty array with size 0
		SymbolicExpression array = universe.emptyArray(integerType);
		NumericExpression zero = universe.integer(0);
		assertEquals(zero,universe.length(array));
	}
	@Test(expected= SARLException.class)
	public void testModuloWithExceptions(){
		NumericExpression fiveInt, threeInt;
		NumericExpression fiveReal;
		NumericExpression fiveModthree;
		
		fiveInt = universe.integer(5);
		threeInt = universe.integer(3);
		fiveModthree = universe.modulo(fiveInt, threeInt);
		assertEquals(universe.integer(2),fiveModthree);
		
		//exception first arg is realtype
		
		fiveReal = universe.rational(5.0);
		fiveModthree = universe.modulo(fiveReal, threeInt);
		
		


	}
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void testModuloWithExceptions2(){
		NumericExpression fiveInt, threeInt;
		NumericExpression threeReal;
		NumericExpression fiveModthree;
		
		fiveInt = universe.integer(5);
		threeInt = universe.integer(3);
		fiveModthree = universe.modulo(fiveInt, threeInt);
		assertEquals(universe.integer(2),fiveModthree);
		threeReal = universe.rational(3.0);
		
		//exception second arg is realtype
		
		fiveModthree = universe.modulo(fiveInt, threeReal);

	}
	// written by Mohammad Alsulmi
	@Test(expected= SARLException.class)
	public void testPowerException(){
		NumericExpression base = universe.integer(3);
		NumericExpression result = universe.power(base, 2);
		assertEquals(universe.integer(9), result);
		
		// exception when the exponent is negative
		
		result = universe.power(base, -2);
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
	@Test
	public void testRational(){
		// here we cover the remaining cases of using rational()
		long value1, num1,den1 ;
		float value2;
		NumericExpression result;

		num1 = 3;
		den1 = 2;
		value1 = 5;
		value2 = 5;
		result = universe.rational(value1); // long case
		assertEquals(universe.rational(5), result);
		result = universe.rational(value2); // float case
		assertEquals(universe.rational(5), result); 
		result = universe.rational(BigInteger.TEN); // BigInteger case
		assertEquals(universe.rational(10), result);
		result = universe.rational(num1, den1); // long numerator and denominator
		assertEquals(universe.rational(1.5), result);
		result = universe.rational(BigInteger.ONE, BigInteger.TEN); // BigInteger numerator and denominator
		assertEquals(universe.rational(.1), result);
		
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
	public void testCompatibleWithTuple(){
		
		// here we test compatible with tuple types 
		SymbolicTupleType type1, type2, type3,type5, type6,type7;
		SymbolicType type4;
		BooleanExpression result, expected;
		SymbolicTypeSequence sequence;
		LinkedList<SymbolicType> members = new LinkedList<>();
		
		
		type1 = universe.tupleType(universe.stringObject("Type1"), Arrays.asList(new SymbolicType[]{integerType,integerType}));
		type2 = universe.tupleType(universe.stringObject("Type1"), Arrays.asList(new SymbolicType[]{integerType,integerType}));		
		type3 = universe.tupleType(universe.stringObject("type2"),Arrays.asList(new SymbolicType[]{realType, integerType}));
		type5 = universe.tupleType(universe.stringObject("Type1"), Arrays.asList(new SymbolicType[]{integerType,realType}));
		type6 = universe.tupleType(universe.stringObject("Type1"), Arrays.asList(new SymbolicType[]{integerType,realType, integerType}));
		type7 = universe.tupleType(universe.stringObject("Type1"), members);
		type4 = universe.integerType();
		
		// here we compare two identical tuple types (type1, type2)
		// the expected compatible call should return true
		expected = universe.bool(true);
		result = universe.compatible(type1, type2);
		assertEquals(expected, result);
		
		// here we compare two different tuple types (type1, type3)
		// the expected compatible call should return false
		expected = universe.bool(false);
		result  = universe.compatible(type1, type3);
		assertEquals(expected, result);
		
		// here we compare a tuple type with integer type (type1, type4)
		// the expected compatible call should return false
		expected = universe.bool(false);
		result  = universe.compatible(type1, type4);
		assertEquals(expected, result);
		
		// here we compare two different tuple types (type1, type5), but they have the same name
		// the expected compatible call should return false
		expected = universe.bool(false);
		result  = universe.compatible(type1, type5);
		assertEquals(expected, result);
		
		// here we compare two different tuple types (type1, type6), but they have the same name
		// the expected compatible call should return false
		expected = universe.bool(false);
		result  = universe.compatible(type1, type6);
		assertEquals(expected, result);
		
		// here we compare two different tuple types (type7, type6), but they have the same name
		// the expected compatible call should return false
		expected = universe.bool(false);
		result  = universe.compatible(type7, type6);
		assertEquals(expected, result);
				

	}
	// written by Mohammad Alsulmi
	@Test
	public void testCompatibleWithUnion(){
		
		// here we test compatible with tuple types 
		SymbolicUnionType type1, type2, type3, type5;
		SymbolicType type4;
		BooleanExpression result, expected;
		
		
		type1 = universe.unionType(universe.stringObject("Type1"), Arrays.asList(new SymbolicType[]{integerType,realType}));
		type2 = universe.unionType(universe.stringObject("Type1"), Arrays.asList(new SymbolicType[]{integerType,realType}));		
		type3 = universe.unionType(universe.stringObject("type3"),Arrays.asList(new SymbolicType[]{realType, integerType}));
		type5 = universe.unionType(universe.stringObject("Type1"), Arrays.asList(new SymbolicType[]{integerType,universe.booleanType()}));
		type4 = universe.booleanType();
		
		// here we compare two identical unions types (type1, type2)
		// the expected compatible call should return true
		expected = universe.bool(true);
		result = universe.compatible(type1, type2);
		assertEquals(expected, result);
		
		// here we compare two different unions types (type1, type3)
		// the expected compatible call should return false
		expected = universe.bool(false);
		result  = universe.compatible(type1, type3);
		assertEquals(expected, result);
		
		// here we compare a union type with boolean type (type1, type4)
		// the expected compatible call should return true
		expected = universe.bool(false);
		result  = universe.compatible(type1, type4);
		assertEquals(expected, result);
		
		// here we compare two different tuple types (type1, type5), but they have the same name
		// the expected compatible call should return false
		expected = universe.bool(false);
		result  = universe.compatible(type1, type5);
		assertEquals(expected, result);

	}
	// written by Mohammad Alsulmi
	@Test
	public void testCompatibleWithFunction(){
	
		// here we test compatible with tuple types 
		SymbolicFunctionType functionType1, functionType2;
		BooleanExpression result, expected;
		
		functionType1 = universe.functionType(Arrays.asList(new SymbolicType[]{integerType,integerType}), realType);
		functionType2 = universe.functionType(Arrays.asList(new SymbolicType[]{integerType,realType}), integerType);
		
		
		// here we compare two different function types (functionType1, functionType2)
		// the expected compatible call should return true
		
		expected = universe.bool(false);
		result = universe.compatible(functionType1, functionType2);
		assertEquals(expected, result);
		
	}

	// written by Mohammad Alsulmi
	@Test
	public void testCompatibleWithReal(){
	
		// here we test different types 
		SymbolicType type1, type2;
		BooleanExpression result, expected;
		
		type1 = universe.realType(); // real 
		type2 = universe.herbrandRealType(); //herbrand
		
		// here we compare two different types (type1, type2)
		// the expected compatible call should return false
		expected = universe.bool(false);
		result = universe.compatible(type1, type2);
		assertEquals(expected, result);
		
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
	public void testInteger(){
		// covering the other cases of integers
		NumericExpression num1,num2;
		long n1;
		n1 = 100;
		BigInteger n2 = BigInteger.ONE;
		
		num1 = universe.integer(n1);
		num2 = universe.integer(n2);	
	}
	// written by Mohammad Alsulmi
	@Test (expected= SARLException.class)
	public void testAddException(){
		// testing the fail when passing a null list to add()
		LinkedList<NumericExpression> numbers;
		NumericExpression sum;
		
		numbers = null;
		sum = universe.add(numbers);
		
	}
	// written by Mohammad Alsulmi
	@Test (expected= SARLException.class)
	public void testAddException2(){
		// testing the fail when passing an empty list to add()
		LinkedList<NumericExpression> numbers;
		NumericExpression sum;
		
		numbers = new LinkedList<>();
		sum = universe.add(numbers);
		
	}
	// written by Mohammad Alsulmi
	@Test 
	public void testNeq(){
		
		 NumericSymbolicConstant x_var,y_var,z_var;
		 SymbolicExpression x_plus_y, one_plus_z;
		 BooleanExpression expression;
		 BooleanExpression expression2, result;
		 
		 x_var = (NumericSymbolicConstant) universe.symbolicConstant(
					universe.stringObject("x"), realType);
		y_var = (NumericSymbolicConstant) universe.symbolicConstant(
					universe.stringObject("y"), realType);
		z_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("z"), realType);
	
			
		x_plus_y = universe.add(x_var,y_var);
		one_plus_z = universe.add(z_var, universe.rational(1));
		expression = universe.neq(x_plus_y, one_plus_z);
		expression2 = universe.neq(x_plus_y, one_plus_z);
		result = universe.neq(expression, expression2);
		
		
	}
		// written by Mohammad Alsulmi
	@Test 
	public void testDivides(){
		// test if one integer a divides another integer b
		NumericExpression num1, num2, num3;
		BooleanExpression res;
		
		num1 = universe.integer(10);
		num2 = universe.integer(5);
		num3 = universe.integer(3);
		
		// check if num2 divides num1
		// here the result should be true since 5 divides 10
		res = universe.divides(num2, num1);
		assertEquals(universe.bool(true), res);
		
		// check if num3 divides num1
		// here the result should be false since 3 doesn't divide 10
		res = universe.divides(num3, num1);
		assertEquals(universe.bool(false), res);

	}
	// written by Mohammad Alsulmi
	@Test
	public void testNot(){
		
		// testing and covering two cases of not (LESSTHAN and LESSTHANEQUAL)
		NumericExpression num1,num2;
		NumericSymbolicConstant x_var,y_var;
		BooleanExpression exp, notExp;
		
		x_var = (NumericSymbolicConstant) universe.symbolicConstant(
					universe.stringObject("x"), integerType);
		y_var = (NumericSymbolicConstant) universe.symbolicConstant(
					universe.stringObject("y"), integerType);
		
		num1 = universe.add(x_var, universe.integer(1));
		num2 = universe.add(y_var, universe.integer(1));
		
		// case: less than
		exp = universe.lessThan(num1, num2);
		notExp = universe.not(exp);
		
		// case: less than equal
		exp = universe.lessThanEquals(num1, num2);
		notExp = universe.not(exp);		
		
	}
	// written by Mohammad Alsulmi
	@Test
	public void testEqual(){
		// testing some cases of equals
		SymbolicExpression exp1,exp2;
		BooleanExpression result;
		SymbolicTupleType tupleType;
		// case 1 when exp1 is boolean and exp2 is integer
		exp1 = universe.bool(false);
		exp2 = universe.integer(11);
		
		result = universe.equals(exp1, exp2);
		assertEquals(universe.bool(false), result);
		
		// case 2 when exp1 and exp2 are booleans but with different values
		exp1 = universe.bool(false);
		exp2 = universe.bool(true);
		
		
		result = universe.equals(exp1, exp2);
		assertEquals(universe.bool(false), result);

		
		// case 3 when exp1 and exp2 are integers but with different values
		exp1 = universe.integer(100);
		exp2 = universe.integer(60);
		
		result = universe.equals(exp1, exp2);
		assertEquals(universe.bool(false), result);
		
		// case 4 when exp1 and exp2 are arrays but with different values	
		exp1 = universe.array(integerType, Arrays.asList(new NumericExpression[]{universe.integer(20),universe.integer(40)}));
		exp2 = universe.array(integerType, Arrays.asList(new NumericExpression[]{universe.integer(2),universe.integer(4)}));
		result = universe.equals(exp1, exp2);
		assertEquals(universe.bool(false), result);
		
		// case 5 when exp1 and exp2 are tuples but with different values	
		tupleType = universe.tupleType(universe.stringObject("type1"), Arrays.asList(new SymbolicType[]{integerType,integerType}));
		exp1 = universe.tuple(tupleType, Arrays.asList(new NumericExpression[]{universe.integer(6),universe.integer(8)}));
		exp2 = universe.tuple(tupleType, Arrays.asList(new NumericExpression[]{universe.integer(6),universe.integer(9)}));
		result = universe.equals(exp1, exp2);
		assertEquals(universe.bool(false), result);
		
		
		
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
	
	@Test
	// Written by Marlin Blue
	public void testEqualsFunction() {
		SymbolicExpression result1, result2;
		SymbolicConstant nullConstant = universe.symbolicConstant(
				universe.stringObject("null"), integerType);

		ArrayList<SymbolicType> testArray1 = new ArrayList<SymbolicType>();
		testArray1.add(integerType);
		LinkedList<SymbolicType> testArray2 = new LinkedList<SymbolicType>();

		StringObject name1 = universe.stringObject("f");
		StringObject name2 = universe.stringObject("g");
		StringObject name3 = universe.stringObject("x");
		StringObject name4 = universe.stringObject("y");

		SymbolicType testBool1 = universe.booleanType();

		SymbolicType functionType1 = universe.functionType(testArray1, testBool1);
		SymbolicType functionType2 = universe.functionType(testArray2, testBool1);

		SymbolicConstant symFunc1 = universe.symbolicConstant(
				name1, functionType1);
		SymbolicConstant symFunc2 = universe.symbolicConstant(
				name2, functionType1);
		SymbolicConstant symFunc3 = universe.symbolicConstant(
				name3, functionType2);
		SymbolicConstant symFunc4 = universe.symbolicConstant(
				name4, functionType2);

		result1 = universe.equals(symFunc1, symFunc2);
		result2 = universe.equals(symFunc3, symFunc4);

		// Testing case 6: FUNCTION
		assertEquals(universe.equals(
				universe.symbolicConstant(name1, functionType1), 
				universe.symbolicConstant(name2, functionType1)), result1);
		/*assertEquals(universe.equals(
				universe.symbolicConstant(name3, functionType2),
				universe.symbolicConstant(name4, functionType2)), result2);
				*/
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

	
	@Test

	public void testMake(){
		NumericExpression zero,one,two,three,five, N_one;
		BooleanExpression resultTrue, resultFalse;
		SymbolicExpression array = null,resultArray,symbolicExpr1,symbolicExpr2,symbolicExpr3;
		resultTrue=universe.bool(true);
		resultFalse=universe.bool(false);
		SymbolicType Integer,Bool,Real;// For testing nullExpression() method

		Integer = universe.integerType();
		Bool = universe.booleanType();
		Real = universe.realType();
		
		
		zero=universe.integer(0);
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);
		five = universe.integer(5);
		N_one=universe.integer(-1);
		universe.minus(N_one);

		//case ADD;
		SymbolicObject[] Args_ADD={one,two};
		assertEquals(universe.add(one,two),universe.make(SymbolicOperator.ADD,Integer,Args_ADD));
		//case AND;
		SymbolicObject[] Args_AND1={resultTrue,resultTrue};
		SymbolicObject[] Args_AND2={resultTrue,resultFalse};
		SymbolicObject[] Args_AND3={resultTrue};
		assertEquals(universe.make(SymbolicOperator.AND,Bool,Args_AND2),resultFalse);
		assertEquals((universe.make(SymbolicOperator.AND,Bool,Args_AND1)),resultTrue);
		//assertEquals((universe.make(SymbolicOperator.AND,Bool,Args_AND3)),resultTrue);
		//case ARRAY_LAMBDA
		SymbolicObject[] Args_Array_Lambda={nullExpression};
		assertEquals(null,universe.make(SymbolicOperator.ARRAY_LAMBDA,symbolicCompleteArrayType,Args_Array_Lambda));
		//case ARRAY_WRITE
		SymbolicObject[] Args_Array_Write={two,two,five};
		array = universe.array(integerType, Arrays.asList(new NumericExpression[]{two,two,five}));
		//do not implement yet
		//assertEquals(array,universe.make(SymbolicOperator.ARRAY_WRITE,symbolicCompleteArrayType,Args_Array_Write));
		//case CONCRETE
		SymbolicObject[] Args_Concrete={one};
		//do not implement yet
		//assertEquals(universe.make(SymbolicOperator.CONCRETE, Real, Args_Concrete),one);
		//case COND
		SymbolicObject[] Args_COND={resultTrue,resultTrue,resultTrue};
		assertEquals(universe.make(SymbolicOperator.COND,Bool,Args_COND),resultTrue);
		//case DIVIDE:
		SymbolicObject[] Args_Divide={two,one};
		assertEquals(universe.make(SymbolicOperator.DIVIDE,Integer,Args_Divide),two);
		//case MULTIPLY
		SymbolicObject[] testList =new SymbolicObject[] {one};
		ArrayList<SymbolicObject> Args_MULTIPLY =new ArrayList<SymbolicObject>();
		Args_MULTIPLY.add(one);
		//assertEquals(universe.make(SymbolicOperator.MULTIPLY,Integer,testList),testList);
		//case MODULO
		SymbolicObject[] Args_Modulo= {three, one};
		assertEquals(universe.make(SymbolicOperator.MODULO,Integer,Args_Modulo), zero);
		//case NEGATIVE;
		SymbolicObject[] Args_Negative={one};
		assertEquals(N_one,universe.make(SymbolicOperator.NEGATIVE,Integer,Args_Negative));
		//case NEQ
		SymbolicObject[] Args_NEQ={resultTrue,resultTrue};
		assertEquals(universe.make(SymbolicOperator.OR,Bool,Args_NEQ),resultTrue);
		//case NOT:
		SymbolicObject[] Args_NOT={resultTrue};
		assertEquals(universe.make(SymbolicOperator.NOT,Bool,Args_NOT),resultFalse);
		//case OR;
		SymbolicObject[] Args_OR1={resultTrue,resultTrue};
		SymbolicObject[] Args_OR2={resultFalse,resultFalse};
		SymbolicObject[] Args_OR3={resultTrue};
		assertEquals(universe.make(SymbolicOperator.OR,Bool,Args_OR2),resultFalse);
		assertEquals((universe.make(SymbolicOperator.OR,Bool,Args_OR1)),resultTrue);
		//assertEquals((universe.make(SymbolicOperator.OR,Bool,Args_OR3)),resultTrue);
	 	//case POWER:
		IntObject I1;
		I1 = universe.intObject(1);
		SymbolicObject[] Args_Power1= {one, one};
		SymbolicObject[] Args_Power2= {one, I1};
		SymbolicExpression result = universe.make(SymbolicOperator.POWER,Integer,Args_Power1);		
		assertEquals(universe.make(SymbolicOperator.POWER,Integer,Args_Power1), result);
		SymbolicExpression result1 = universe.make(SymbolicOperator.POWER,Integer,Args_Power2);		
		assertEquals(universe.make(SymbolicOperator.POWER,Integer,Args_Power2), result1);		
		//case SUBTRACT:
		SymbolicObject[] Args_Substract= {three, one};
		assertEquals(universe.make(SymbolicOperator.SUBTRACT,Integer,Args_Substract), two);
		//case TUPLE_WRITE
		SymbolicTupleType tupleType1;
		SymbolicExpression tuple, resultedTuple;
		IntObject i1;
		i1 = universe.intObject(1);
		tupleType1 = universe.tupleType(universe.stringObject("tupleType1"), Arrays.asList(new SymbolicType[]{integerType,integerType}));
		tuple = universe.tuple(tupleType1, Arrays.asList(new SymbolicExpression[]{universe.integer(1),universe.integer(2)}));
		resultedTuple = universe.tupleWrite(tuple, i1, universe.integer(2));
		SymbolicObject[] Args_TupleWrite={tuple,i1,two};
		assertEquals(universe.make(SymbolicOperator.TUPLE_WRITE,Integer,Args_TupleWrite),resultedTuple);
		//case UNION_INJECT
//		LinkedList<SymbolicType> memberTypes;
//		memberTypes = new LinkedList<SymbolicType>();
//		memberTypes.add(integerType);
//		memberTypes.add(realType);
//		union1 = universe.unionType(
//				universe.stringObject("union1"),
//				Arrays.asList(new SymbolicType[] { integerType, realType,
//						booleanType, realArray }));
//		SymbolicObject x1 = universe.symbolicConstant(universe.stringObject("x1"), integerType);
//		symbolicExpr1 = expressionFactory.expression(SymbolicExpression.SymbolicOperator.UNION_INJECT, 
//				universe.unionType(universe.stringObject("MyUnion1"),
//						memberTypes), universe.intObject(1), x1);
//		symbolicExpr2 = universe.unionInject(union1, I1, symbolicExpr1);
//		SymbolicObject[] Args_Union_Inject={I1,symbolicExpr1};
//		symbolicExpr3 =universe.make(SymbolicOperator.UNION_INJECT,union1,Args_Union_Inject);	
	}

}
