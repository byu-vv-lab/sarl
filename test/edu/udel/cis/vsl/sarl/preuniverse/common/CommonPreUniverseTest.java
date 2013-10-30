package edu.udel.cis.vsl.sarl.preuniverse.common;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

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
		arrayType = universe.arrayType(integerType); // creates an array of ints
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
		commonUniverse = (CommonPreUniverse) universe;

		assertEquals(commonUniverse.zero(intType), numericFactory.zeroInt());
		assertEquals(commonUniverse.zero(realType), numericFactory.zeroReal());
	}

	@Test(expected = SARLInternalException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/20/13
	public void testZeroErr() {
		SymbolicUnionType unionType;
		LinkedList<SymbolicType> memberTypes;
		CommonPreUniverse commonUniverse;

		commonUniverse = (CommonPreUniverse) universe;

		memberTypes = new LinkedList<SymbolicType>();

		memberTypes.add(integerType);
		memberTypes.add(realType);

		unionType = universe.unionType(universe.stringObject("MyUnion"),
				memberTypes);

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
		assertEquals(universe.and(booleanList2), falseExpr); // test partial
																// false
		assertEquals(universe.and(booleanList3), falseExpr); // test all false
		assertEquals(universe.and(booleanEmptyList), trueExpr); // test empty is
																// true
	}

	@Test
	@Ignore
	public void testUnionTypeStringObjectIterableOfQextendsSymbolicType() {
		fail("Not yet implemented");
	}

	/**
	 * @author blutuu
	 * 
	 *         This tests whether the universe method is equal to the
	 *         objectFactor method. The function obtains the number of objects
	 *         within the given instance and returns an int.
	 */
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

	/**
	 * @author blutuu
	 * 
	 *         Here, a local sequence of universe.objects are tested against a
	 *         global preuniverse.objectCollection. The object() method returns
	 *         a sequence of objects, which populates testCollection.
	 */
	@Test
	// Written by Marlin Blue
	public void testObjects() {
		Collection<SymbolicObject> testCollection = universe.objects();
		assertEquals(objectCollection, testCollection);
	}

	/**
	 * Test for substitute() method
	 */
	@Test
	public void substituteArrayType() {

		NumericExpression length = (NumericExpression) universe
				.symbolicConstant(universe.stringObject("n"), integerType);
		NumericExpression newLength = (NumericExpression) universe
				.symbolicConstant(universe.stringObject("m"), integerType);
		SymbolicType arrayTypeComplete = universe
				.arrayType(integerType, length);
		SymbolicType newArrayTypeComplete = universe.arrayType(integerType,
				newLength);

		SymbolicExpression a = universe.symbolicConstant(
				universe.stringObject("a"), arrayTypeComplete);
		SymbolicExpression b = universe.symbolicConstant(
				universe.stringObject("a"), newArrayTypeComplete);

		Map<SymbolicExpression, SymbolicExpression> map = new HashMap<>();
		map.put(length, newLength);

		// System.out.println("substituteArrayType - a.type(): " + a.type());

		SymbolicExpression result = universe.substitute(a, map);
		// System.out.println("substituteArraytype - result: " + result);
		// System.out.println("substituteArrayType - result.type(): " +
		// result.type());
		// System.out.println("substituteArrayType - b.type(): " + b.type());

		assertEquals(b, result);

	}

	@Test
	// written by Chris Heider
	public void testSymbolicConstant() {
		// create two symbolicConstants to see if they are equal
		StringObject name = universe.stringObject("name");
		SymbolicType type = universe.booleanType(); // need to test for bool
													// type. the other has been
													// done.
		SymbolicConstant scA = universe.symbolicConstant(name, type);
		SymbolicConstant scB = universe.symbolicConstant(name, type);

		assertEquals(scA, scB);
	}

	@Test
	// Written by Jordan Saints on 9/16/13
	// These nullExpression objects will be the same because they were generated
	// by a factory
	public void testNullExpression() {
		SymbolicExpression resultNullExpression = universe.nullExpression();
		assertEquals(nullExpression, resultNullExpression); // test for equality
	}

	@Test(expected = SARLException.class)
	public void testEmptyMultiply() {
		universe.multiply(emptyNumericList);

	}

	@Test
	public void testMultiply() {
		ArrayList<NumericExpression> testList = new ArrayList<NumericExpression>();
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
		NumericExpression x_var;
		NumericExpression y_var;
		NumericExpression x_neg;
		NumericExpression y_neg;
		NumericExpression x_plus_y;
		NumericExpression x_neg_minus_y;
		NumericExpression x_neg_plus_y_neg;
		NumericExpression seventeen = universe.integer(17);
		NumericExpression negativeSeventeen = universe.integer(-17);

		x_var = (NumericExpression) universe.symbolicConstant(
				universe.stringObject("x"), integerType);
		y_var = (NumericExpression) universe.symbolicConstant(
				universe.stringObject("y"), integerType);

		x_neg = universe.minus(x_var);
		y_neg = universe.minus(y_var);

		x_plus_y = (NumericExpression) universe.add(x_var, y_var);
		x_neg_minus_y = (NumericExpression) universe.subtract(x_neg, y_var);
		x_neg_plus_y_neg = (NumericExpression) universe.add(x_neg, y_neg);

		// Check negative and double negative of symbolic variables
		assertEquals(universe.minus(x_neg), x_var);
		assertEquals(universe.minus(universe.minus(x_var)), x_var);
		assertEquals(universe.minus(y_neg), y_var);
		assertEquals(universe.minus(universe.minus(y_var)), y_var);

		// Check correct behavior for symbolic expressions
		assertEquals(x_neg_minus_y, universe.minus(x_plus_y));
		assertEquals(x_neg_plus_y_neg, universe.minus(x_plus_y));
		assertEquals(x_neg_minus_y, x_neg_plus_y_neg);

		assertEquals(universe.minus(seventeen), negativeSeventeen); // test -(
																	// 17) = -17
		assertEquals(universe.minus(negativeSeventeen), seventeen); // test
																	// -(-17) =
																	// 17
	}

	@Test
	// Test written by Jeff DiMarco (jdimarco) 9/17/13
	public void testBoolBooleanObject() {
		BooleanObject booleanObject1;
		BooleanExpression booleanExpression1;

		booleanObject1 = universe.booleanObject(true);
		booleanExpression1 = booleanFactory.symbolic(booleanObject1);
		assertEquals(universe.bool(booleanObject1), booleanExpression1); // trivial
																			// check
																			// of
																			// return
																			// type
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
	// Test written by Chris Heider 9/16/13
	public void testImplies() {
		// setup BooleanExpressions for running in equiv()
		BooleanExpression boolA = universe.bool(true);
		BooleanExpression boolB = universe.bool(true);
		BooleanExpression boolC = universe.bool(false);
		BooleanExpression boolD = universe.bool(false);
		BooleanExpression testTrue = universe.bool(true);
		BooleanExpression testFalse = universe.bool(false);

		assertEquals(universe.implies(boolA, boolB), testTrue); // test for 2
																// true
		assertEquals(universe.implies(boolA, boolC), testFalse); // test for a
																	// failure
		assertEquals(universe.implies(boolC, boolD), testTrue); // test for 2
																// false
		assertEquals(universe.implies(boolA, boolA), testTrue); // test for
																// identical
	}

	@Test
	// Test written by Chris Heider 9/16/13
	public void testEquiv() {
		// setup BooleanExpressions for running in equiv()
		BooleanExpression boolA = universe.bool(true);
		BooleanExpression boolB = universe.bool(true);
		BooleanExpression boolC = universe.bool(false);
		BooleanExpression boolD = universe.bool(false);
		BooleanExpression testTrue = universe.bool(true);
		BooleanExpression testFalse = universe.bool(false);

		assertEquals(universe.equiv(boolA, boolB), testTrue); // test for 2 true
		assertEquals(universe.equiv(boolA, boolC), testFalse); // test for a
																// failure
		assertEquals(universe.equiv(boolC, boolD), testTrue); // test for 2
																// false
		assertEquals(universe.equiv(boolA, boolA), testTrue); // test for
																// identical
	}

	@Test
	@Ignore
	public void testSubstituteSymbolicExpressionSymbolicConstantSymbolicExpression() {
		fail("Not yet implemented");
	}

	/**
	 * @author blutuu
	 * 
	 *         testForAllInt() tests the forAllInt() method. It takes 4
	 *         parameters which checks whether the given index conforms to the
	 *         given upper and lower bounds. This test conducts three different
	 *         assertions (two of which use null constants) which uniquely hit
	 *         each branch of the forAllInt() method.
	 * 
	 */
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
		SymbolicConstant nullConstant = universe.symbolicConstant(
				universe.stringObject("null"), integerType);
		BooleanExpression falseExp = universe.bool(false);
		BooleanExpression trueExp = universe.bool(true);

		BooleanExpression testResult1 = universe.forallInt(
				(NumericSymbolicConstant) index, low, high, trueExp);
		BooleanExpression testResult2 = universe.forallInt(
				(NumericSymbolicConstant) index,
				(NumericExpression) nullConstant, high, trueExp);
		BooleanExpression testResult3 = universe.forallInt(
				(NumericSymbolicConstant) index, low,
				(NumericExpression) nullConstant, trueExp);

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
				(NumericExpression) nullConstant, universe.integer(2000),
				universe.bool(true)), testResult2);
		assertEquals(universe.forallInt(
				(NumericSymbolicConstant) universe.symbolicConstant(
						universe.stringObject("name"), integerType),
				universe.integer(999), (NumericExpression) nullConstant,
				universe.bool(true)), testResult3);
	}

	/**
	 * @author blutuu
	 * 
	 *         testExistsInt() uses the existsInt() method to check whether the
	 *         given index is found between the given upper and lower bounds.
	 *         Within this test there is a collection of variables to be used in
	 *         testing. Each variable fulfills the "index", "low", "high", and
	 *         "predicate" parameters, respectively. The variable parameters
	 *         combine to test null and non null values which aid in reaching
	 *         every branch of existsInt() successfully.
	 */
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
		SymbolicConstant nullConstant = universe.symbolicConstant(
				universe.stringObject("null"), integerType);
		high2 = universe.integer(2350);
		BooleanExpression falseExp = universe.bool(false);
		BooleanExpression trueExp = universe.bool(true);

		BooleanExpression testResult1 = universe.existsInt(
				(NumericSymbolicConstant) index, low1, high1, falseExp);
		BooleanExpression testResult2 = universe.existsInt(
				(NumericSymbolicConstant) index, low2, high2, trueExp);
		BooleanExpression testResult3 = universe.existsInt(
				(NumericSymbolicConstant) index,
				(NumericExpression) nullConstant, high1, falseExp);
		BooleanExpression testResult4 = universe.existsInt(
				(NumericSymbolicConstant) index, low2,
				(NumericExpression) nullConstant, trueExp);

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
				(NumericExpression) nullConstant, universe.integer(1200),
				universe.bool(false)), testResult3);
		assertEquals(universe.existsInt(
				(NumericSymbolicConstant) universe.symbolicConstant(
						universe.stringObject("branch1"), integerType),
				universe.integer(1200), (NumericExpression) nullConstant,
				universe.bool(true)), testResult4);
	}

	/**
	 * Test for equals() method in which UNIONS are passed. Multiple cases where
	 * different components of the unions are not equal.
	 * 
	 * @author jdimarco
	 */
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
		BooleanExpression trueExpression;
		BooleanExpression falseExpression;

		trueExpression = universe.bool(true);
		falseExpression = universe.bool(false);

		x1 = universe
				.symbolicConstant(universe.stringObject("x1"), integerType);

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

		symbolicExpr1 = expressionFactory.expression(
				SymbolicExpression.SymbolicOperator.UNION_INJECT, unionType1,
				universe.intObject(1), x1);
		symbolicExpr2 = expressionFactory.expression(
				SymbolicExpression.SymbolicOperator.UNION_INJECT, unionType2,
				universe.intObject(2), x1);
		symbolicExpr3 = expressionFactory.expression(
				SymbolicExpression.SymbolicOperator.UNION_TEST, unionType3,
				universe.intObject(1), x1);
		symbolicExpr4 = expressionFactory.expression(
				SymbolicExpression.SymbolicOperator.UNION_TEST, unionType4,
				universe.intObject(4), x1);
		symbolicExpr5 = expressionFactory.expression(
				SymbolicExpression.SymbolicOperator.UNION_TEST, unionType5,
				universe.intObject(5), x1);
		symbolicExpr6 = expressionFactory.expression(
				SymbolicExpression.SymbolicOperator.UNION_TEST, unionType6,
				universe.intObject(6), x1);
		symbolicExpr7 = expressionFactory.expression(
				SymbolicExpression.SymbolicOperator.UNION_TEST, unionType7,
				universe.intObject(1), x1);

		// Test that unions with different values are not equal
		assertEquals(universe.equals(symbolicExpr1, symbolicExpr2),
				falseExpression);
		assertEquals(universe.equals(symbolicExpr1, symbolicExpr3),
				falseExpression);
		assertEquals(universe.equals(symbolicExpr3, symbolicExpr1),
				falseExpression);
		assertEquals(universe.equals(symbolicExpr1, symbolicExpr7),
				falseExpression);
		assertEquals(universe.equals(symbolicExpr7, symbolicExpr1),
				falseExpression);
		// Test UNION_INJECT, UNION_TEST
		assertEquals(universe.equals(symbolicExpr1, symbolicExpr4),
				falseExpression);
		assertEquals(universe.equals(symbolicExpr5, symbolicExpr1),
				falseExpression);
		// Test that empty unions with different expressions are not equal
		assertEquals(universe.equals(symbolicExpr5, symbolicExpr6),
				falseExpression);
	}

	@Test
	// Test written by Jeff DiMarco(jdimarco) 9/20/13
	public void testExtractBoolean() {
		BooleanExpression trueExpression;
		BooleanExpression falseExpression;
		BooleanExpression nullExpression;

		trueExpression = universe.bool(true);
		falseExpression = universe.bool(false);
		nullExpression = null;

		assertEquals(universe.extractBoolean(trueExpression), true);
		assertEquals(universe.extractBoolean(falseExpression), false);
		assertEquals(universe.extractBoolean(nullExpression), null);

	}

	@Test
	/*
	 * Tests the comparator() factory method. Written by Jordan Saints on
	 * 9/16/13
	 */
	public void testComparator() {
		Comparator<SymbolicObject> resultComparator = universe.comparator();

		// the comparator objects objectComparator and resultComparator
		// will be the same because they are generated by a factory
		assertEquals(objectComparator, resultComparator); // generic test for
															// equality
		assertTrue(resultComparator.equals(objectComparator)); // test if same
																// attributes
		assertTrue(resultComparator == objectComparator); // test if same
															// instance
	}

	@Test
	@Ignore
	public void testIntegerBigInteger() {
		// NumericExpression BigInt1, BigInt2;
		// long x = 2;
		// BigInt1=universe.integer((int) Math.pow(x,10));

		fail("Not yet implemented");
	}

	@Test
	// Julian Piane
	public void testNumProverValidCalls() {
		assertEquals(universe.numProverValidCalls(), 0); // at the time of
															// tests,
															// universe.proverValidCount
															// should be 0;
	}

	@Test
	/*
	 * Tests the referenceType() method. Written by Jordan Saints on 9/16/13
	 */
	public void testReferenceType() {
		// Setup
		SymbolicType refType = universe.referenceType(); // call referenceType()
															// method
		SymbolicTupleType refTuple = (SymbolicTupleType) refType; // cast to
																	// TUPLE
																	// SymbolicType
		SymbolicTypeSequence refTupleSequence = refTuple.sequence(); // pull out
																		// the
																		// tuple's
																		// SymbolicTypeSequence

		// Tests
		assertEquals(SymbolicTypeKind.TUPLE, refType.typeKind()); // test that
																	// the
																	// refType
																	// is a
																	// TUPLE
																	// kind
		assertEquals(universe.stringObject("Ref"), refTuple.name()); // test the
																		// name
																		// of
																		// the
																		// tuple
		assertEquals("Ref", refTuple.name().getString()); // extra test for the
															// name of the tuple
															// (covers the
															// .getString()
															// branch)
		assertEquals(1, refTupleSequence.numTypes()); // test the number of
														// types available in
														// this tuple's sequence
		assertEquals(integerType, refTupleSequence.getType(0)); // test sequence
																// type
	}

	@Test
	@Ignore
	// Test written by Julian Piane
	public void testDereference() {
		SymbolicType doubleArrayType = universe.arrayType(arrayType); // int[]
		SymbolicExpression arrayTypeExpression = universe.symbolicConstant(
				universe.stringObject("arrayTypeExpression"), doubleArrayType);

		try {
			universe.dereference(arrayTypeExpression, null);
		} catch (Exception e) {
			assertEquals(e.getClass(), SARLException.class); // test class name
																// of thrown
																// exception
			assertEquals(e.getMessage(), "dereference given null reference");
		}

		try {
			universe.dereference(null, null);
		} catch (Exception e) {
			assertEquals(e.getClass(), SARLException.class); // test class name
																// of thrown
																// exception
			assertEquals(e.getMessage(), "dereference given null value");
		}
	}

	@Test
	// This test focuses on testing referencedType() solely with symbolic types
	// containing unions.
	// Written by Julian Piane
	public void referencedTypeUnion() {
		ReferenceExpression nullReference, identityReference, unionReference;
		SymbolicUnionType unionType;

		IntObject zeroInt = universe.intObject(0);
		IntObject oneInt = universe.intObject(1);
		IntObject twoInt = universe.intObject(2);
		IntObject threeInt = universe.intObject(3);

		identityReference = universe.identityReference();

		unionType = universe.unionType(
				universe.stringObject("UnionType"),
				Arrays.asList(new SymbolicType[] { integerType, realType,
						booleanType, arrayType }));

		unionReference = universe.unionMemberReference(identityReference,
				zeroInt);
		// Test Index zero
		assertEquals(universe.referencedType(unionType, unionReference),
				integerType);

		unionReference = universe.unionMemberReference(identityReference,
				oneInt);
		// Test Index one
		assertEquals(universe.referencedType(unionType, unionReference),
				realType);

		unionReference = universe.unionMemberReference(identityReference,
				twoInt);
		// Test Index two
		assertEquals(universe.referencedType(unionType, unionReference),
				booleanType);

		unionReference = universe.unionMemberReference(identityReference,
				threeInt);
		// Test Index three
		assertEquals(universe.referencedType(unionType, unionReference),
				arrayType);
	}

	@Test
	// This test focuses on testing referencedType() solely with symbolic types
	// containing arrays.
	// Written by Julian Piane
	public void referencedTypeArray() {
		ReferenceExpression nullReference, identityReference, arrayReference, twoDimensionalArrayReference;
		NumericExpression zero, one, two, three;
		SymbolicArrayType ArrayType;

		zero = universe.integer(0);
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);

		identityReference = universe.identityReference();
		twoDimensionalArrayReference = universe.arrayElementReference(
				identityReference, zero);

		arrayReference = universe
				.arrayElementReference(identityReference, zero);

		ArrayType = universe.arrayType(arrayType);
		// Two Dimensional Array Test
		assertEquals(universe.referencedType(ArrayType,
				twoDimensionalArrayReference), arrayType);

		// Error testing
		try {
			universe.referencedType(ArrayType, arrayReference);
		} catch (Exception e) {
			assertEquals(e.getClass(), SARLException.class);
		}

		ArrayType = universe.arrayType(ArrayType);
		// Three Dimensional Array Test
		assertEquals(universe.referencedType(ArrayType,
				twoDimensionalArrayReference), universe.arrayType(arrayType));

		ArrayType = universe.arrayType(ArrayType);
		// Four Dimensional Array Test
		assertEquals(universe.referencedType(ArrayType,
				twoDimensionalArrayReference), universe.arrayType(universe
				.arrayType(arrayType)));
	}

	@Test
	// This test focuses on testing referencedType() solely with symbolic types
	// containing tuples.
	// Written by Julian Piane
	public void referencedTypeTuple() {
		ReferenceExpression nullReference, identityReference, tupleReference;
		SymbolicTupleType tupleType;

		IntObject zeroInt = universe.intObject(0);
		IntObject oneInt = universe.intObject(1);
		IntObject twoInt = universe.intObject(2);
		IntObject threeInt = universe.intObject(3);

		identityReference = universe.identityReference();

		tupleType = universe.tupleType(
				universe.stringObject("tupleType"),
				Arrays.asList(new SymbolicType[] { integerType, realType,
						integerType, realType }));

		// Test index 0
		tupleReference = universe.tupleComponentReference(identityReference,
				zeroInt);
		assertEquals(universe.referencedType(tupleType, tupleReference),
				integerType);

		// Test index 1
		tupleReference = universe.tupleComponentReference(identityReference,
				oneInt);
		assertEquals(universe.referencedType(tupleType, tupleReference),
				realType);

		// Test index 2
		tupleReference = universe.tupleComponentReference(identityReference,
				twoInt);
		assertEquals(universe.referencedType(tupleType, tupleReference),
				integerType);

		// Test index 3
		tupleReference = universe.tupleComponentReference(identityReference,
				threeInt);
		assertEquals(universe.referencedType(tupleType, tupleReference),
				realType);
	}

	@Test
	// This test focuses on testing referencedType() solely with symbolic types
	// containing mixed symbolic types containing any combination of unions,
	// arrays and/or tuples
	// Written by Julian Piane 9/22/13
	public void referencedTypeMixed() {
		// instantiate our types
		NumericExpression zero, one, two, three;

		IntObject zeroInt = universe.intObject(0);
		IntObject oneInt = universe.intObject(1);
		IntObject twoInt = universe.intObject(2);
		IntObject threeInt = universe.intObject(3);

		zero = universe.integer(0);
		one = universe.integer(1);
		two = universe.integer(2);
		three = universe.integer(3);

		// Reference Expressions
		ReferenceExpression nullReference, offsetReference, identityReference, unionReference, arrayReference, twoDimensionalArrayReference, tupleInArrayReference;
		nullReference = universe.nullReference();
		identityReference = universe.identityReference();

		tupleInArrayReference = universe.arrayElementReference(
				identityReference, zero);
		offsetReference = universe.offsetReference(identityReference, zero);
		unionReference = universe.unionMemberReference(identityReference,
				zeroInt);

		ReferenceExpression arrayInTupleReference = universe
				.tupleComponentReference(identityReference, zeroInt);

		// Tuple containing array
		SymbolicTupleType tupleOfArrayType = universe.tupleType(
				universe.stringObject("tupleOfArrayType"),
				Arrays.asList(new SymbolicType[] { arrayType }));

		// Tuple containing array test and offset test
		assertEquals(universe.referencedType(tupleOfArrayType,
				arrayInTupleReference), arrayType);

		// Array containing Tuple
		SymbolicArrayType arrayOfTupleType = universe
				.arrayType(tupleOfArrayType);

		SymbolicArrayType twoDimensionalArrayType = universe
				.arrayType(arrayType);

		assertEquals(universe.referencedType(tupleOfArrayType, offsetReference)
				.typeKind(), tupleOfArrayType.typeKind());

		// Array containing Tuple test
		assertEquals(universe.referencedType(arrayOfTupleType,
				tupleInArrayReference), tupleOfArrayType);

		// ERROR TESTS
		try {
			universe.referencedType(arrayOfTupleType, arrayInTupleReference);
		} catch (Exception e) {
			assertEquals(e.getClass(), SARLException.class);
		}
		try {
			universe.referencedType(arrayOfTupleType, nullReference);
		} catch (Exception e) {
			assertEquals(e.getClass(), SARLException.class);
		}
		try {
			universe.referencedType(null, nullReference);
		} catch (Exception e) {
			assertEquals(e.getClass(), SARLException.class);
		}
		try {
			universe.referencedType(arrayOfTupleType, null);
		} catch (Exception e) {
			assertEquals(e.getClass(), SARLException.class);
		}

		try {
			universe.referencedType(tupleOfArrayType, tupleInArrayReference);
		} catch (Exception e) {
			assertEquals(e.getClass(), SARLException.class);
		}
		try {
			assertEquals(universe.referencedType(twoDimensionalArrayType,
					unionReference), arrayType);
		} catch (Exception e) {
			assertEquals(e.getClass(), SARLException.class);
		}
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
		SymbolicExpression ten;
		SymbolicExpression u_ten;
		ReferenceExpression iref;
		ReferenceExpression nref;
		ReferenceExpression offsetReference5;
		ReferenceExpression offsetReference0;

		ten = universe.integer(10);
		u_ten = universe.unionInject(union1, universe.intObject(0), ten);
		iref = expressionFactory.identityReference();
		nref = expressionFactory.nullReference();

		offsetReference5 = universe.offsetReference(
				expressionFactory.identityReference(), universe.integer(5));
		offsetReference0 = universe.offsetReference(
				expressionFactory.identityReference(), universe.integer(0));

		assertEquals(universe.assign(u_ten, iref, u_ten), u_ten); // test for
																	// subvalue
		assertEquals(universe.assign(u_ten, offsetReference0, u_ten), u_ten);

		// Test exception case
		try {
			universe.assign(u_ten, offsetReference5, u_ten);
		} catch (Exception e) {
			assertEquals(e.getClass(), SARLException.class);
		}
	}

	@Test(expected = SARLException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/25/13
	public void testAssignException1() {
		SymbolicExpression ten;
		SymbolicExpression u_ten;
		ReferenceExpression iref;
		ReferenceExpression nref;

		ten = universe.integer(10);
		u_ten = universe.unionInject(union1, universe.intObject(0), ten);
		iref = expressionFactory.identityReference();
		nref = expressionFactory.nullReference();

		universe.assign(u_ten, nref, u_ten); // test for SARLException
	}

	@Test(expected = SARLException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/25/13
	public void testAssignException2() {
		SymbolicExpression ten;
		SymbolicExpression u_ten;
		ReferenceExpression iref;
		ReferenceExpression nref;

		ten = universe.integer(10);
		u_ten = universe.unionInject(union1, universe.intObject(0), ten);

		iref = expressionFactory.identityReference();
		nref = expressionFactory.nullReference();

		universe.assign(u_ten, iref, null); // test for SARLException
	}

	@Test(expected = SARLException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/25/13
	public void testAssignException3() {
		SymbolicExpression ten;
		SymbolicExpression u_ten;
		ReferenceExpression iref;
		ReferenceExpression nref;

		ten = universe.integer(10);
		u_ten = universe.unionInject(union1, universe.intObject(0), ten);

		iref = expressionFactory.identityReference();
		nref = expressionFactory.nullReference();

		universe.assign(null, nref, u_ten); // test for SARLException
	}

	@Test(expected = SARLException.class)
	// Test written by Jeff DiMarco (jdimarco) 9/25/13
	public void testAssignException4() {
		SymbolicExpression ten;
		SymbolicExpression u_ten;

		ReferenceExpression iref;
		ReferenceExpression nref;

		ten = universe.integer(10);
		u_ten = universe.unionInject(union1, universe.intObject(0), ten);

		iref = expressionFactory.identityReference();
		nref = expressionFactory.nullReference();

		universe.assign(u_ten, null, u_ten); // test for SARLException
	}

	@Test(expected = SARLException.class)
	public void testModuloWithExceptions() {
		NumericExpression fiveInt, threeInt;
		NumericExpression fiveReal;
		NumericExpression fiveModthree;

		fiveInt = universe.integer(5);
		threeInt = universe.integer(3);
		fiveModthree = universe.modulo(fiveInt, threeInt);
		assertEquals(universe.integer(2), fiveModthree);

		// exception first arg is realtype

		fiveReal = universe.rational(5.0);
		fiveModthree = universe.modulo(fiveReal, threeInt);

	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testModuloWithExceptions2() {
		NumericExpression fiveInt, threeInt;
		NumericExpression threeReal;
		NumericExpression fiveModthree;

		fiveInt = universe.integer(5);
		threeInt = universe.integer(3);
		fiveModthree = universe.modulo(fiveInt, threeInt);
		assertEquals(universe.integer(2), fiveModthree);
		threeReal = universe.rational(3.0);

		// exception second arg is realtype

		fiveModthree = universe.modulo(fiveInt, threeReal);

	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testPowerException() {
		NumericExpression base = universe.integer(3);
		NumericExpression result = universe.power(base, 2);
		assertEquals(universe.integer(9), result);

		// exception when the exponent is negative

		result = universe.power(base, -2);
	}

	// written by Mohammad Alsulmi
	@Test
	public void testRational() {
		// here we cover the remaining cases of using rational()
		long value1, num1, den1;
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
		result = universe.rational(num1, den1); // long numerator and
												// denominator
		assertEquals(universe.rational(1.5), result);
		result = universe.rational(BigInteger.ONE, BigInteger.TEN); // BigInteger
																	// numerator
																	// and
																	// denominator
		assertEquals(universe.rational(.1), result);

	}

	// written by Mohammad Alsulmi
	@Test
	public void testCompatibleWithUnion() {

		// here we test compatible with tuple types
		SymbolicUnionType type1, type2, type3, type5;
		SymbolicType type4;
		BooleanExpression result, expected;

		type1 = universe.unionType(universe.stringObject("Type1"),
				Arrays.asList(new SymbolicType[] { integerType, realType }));
		type2 = universe.unionType(universe.stringObject("Type1"),
				Arrays.asList(new SymbolicType[] { integerType, realType }));
		type3 = universe.unionType(universe.stringObject("type3"),
				Arrays.asList(new SymbolicType[] { realType, integerType }));
		type5 = universe.unionType(
				universe.stringObject("Type1"),
				Arrays.asList(new SymbolicType[] { integerType,
						universe.booleanType() }));
		type4 = universe.booleanType();

		// here we compare two identical unions types (type1, type2)
		// the expected compatible call should return true
		expected = universe.bool(true);
		result = universe.compatible(type1, type2);
		assertEquals(expected, result);

		// here we compare two different unions types (type1, type3)
		// the expected compatible call should return false
		expected = universe.bool(false);
		result = universe.compatible(type1, type3);
		assertEquals(expected, result);

		// here we compare a union type with boolean type (type1, type4)
		// the expected compatible call should return true
		expected = universe.bool(false);
		result = universe.compatible(type1, type4);
		assertEquals(expected, result);

		// here we compare two different tuple types (type1, type5), but they
		// have the same name
		// the expected compatible call should return false
		expected = universe.bool(false);
		result = universe.compatible(type1, type5);
		assertEquals(expected, result);

	}

	// written by Mohammad Alsulmi
	@Test
	public void testCompatibleWithFunction() {

		// here we test compatible with tuple types
		SymbolicFunctionType functionType1, functionType2;
		BooleanExpression result, expected;

		functionType1 = universe.functionType(
				Arrays.asList(new SymbolicType[] { integerType, integerType }),
				realType);
		functionType2 = universe.functionType(
				Arrays.asList(new SymbolicType[] { integerType, realType }),
				integerType);

		// here we compare two different function types (functionType1,
		// functionType2)
		// the expected compatible call should return true

		expected = universe.bool(false);
		result = universe.compatible(functionType1, functionType2);
		assertEquals(expected, result);

	}

	// written by Mohammad Alsulmi
	@Test
	public void testCompatibleWithReal() {

		// here we test different types
		SymbolicType type1, type2;
		BooleanExpression result, expected;

		type1 = universe.realType(); // real
		type2 = universe.herbrandRealType(); // herbrand

		// here we compare two different types (type1, type2)
		// the expected compatible call should return false
		expected = universe.bool(false);
		result = universe.compatible(type1, type2);
		assertEquals(expected, result);

	}

	// written by Mohammad Alsulmi
	@Test
	public void testInteger() {
		// covering the other cases of integers
		NumericExpression num1, num2;
		long n1;
		n1 = 100;
		BigInteger n2 = BigInteger.ONE;

		num1 = universe.integer(n1);
		num2 = universe.integer(n2);
	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testAddException() {
		// testing the fail when passing a null list to add()
		LinkedList<NumericExpression> numbers;
		NumericExpression sum;

		numbers = null;
		sum = universe.add(numbers);

	}

	// written by Mohammad Alsulmi
	@Test(expected = SARLException.class)
	public void testAddException2() {
		// testing the fail when passing an empty list to add()
		LinkedList<NumericExpression> numbers;
		NumericExpression sum;

		numbers = new LinkedList<>();
		sum = universe.add(numbers);

	}

	// written by Mohammad Alsulmi
	@Test
	public void testNeq() {

		NumericSymbolicConstant x_var, y_var, z_var;
		SymbolicExpression x_plus_y, one_plus_z;
		BooleanExpression expression;
		BooleanExpression expression2, result;

		x_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("x"), realType);
		y_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("y"), realType);
		z_var = (NumericSymbolicConstant) universe.symbolicConstant(
				universe.stringObject("z"), realType);

		x_plus_y = universe.add(x_var, y_var);
		one_plus_z = universe.add(z_var, universe.rational(1));
		expression = universe.neq(x_plus_y, one_plus_z);
		expression2 = universe.neq(x_plus_y, one_plus_z);
		result = universe.neq(expression, expression2);

	}

	// written by Mohammad Alsulmi
	@Test
	public void testDivides() {
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
	public void testNot() {

		// testing and covering two cases of not (LESSTHAN and LESSTHANEQUAL)
		NumericExpression num1, num2;
		NumericSymbolicConstant x_var, y_var;
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
	public void testEqual() {
		// testing some cases of equals
		SymbolicExpression exp1, exp2;
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
		exp1 = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(20),
						universe.integer(40) }));
		exp2 = universe.array(
				integerType,
				Arrays.asList(new NumericExpression[] { universe.integer(2),
						universe.integer(4) }));
		result = universe.equals(exp1, exp2);
		assertEquals(universe.bool(false), result);

		// case 5 when exp1 and exp2 are tuples but with different values
		tupleType = universe.tupleType(universe.stringObject("type1"),
				Arrays.asList(new SymbolicType[] { integerType, integerType }));
		exp1 = universe.tuple(
				tupleType,
				Arrays.asList(new NumericExpression[] { universe.integer(6),
						universe.integer(8) }));
		exp2 = universe.tuple(
				tupleType,
				Arrays.asList(new NumericExpression[] { universe.integer(6),
						universe.integer(9) }));
		result = universe.equals(exp1, exp2);
		assertEquals(universe.bool(false), result);

	}

	/**
	 * @author blutuu
	 * 
	 *         Tests whether the two given Symbolic Constants are equal. This
	 *         test targets the FUNCTION case of the 'equals()' method. An
	 *         ArrayList and a Boolean serve as the parameters. Two assertions
	 *         contribute to this test where one tests the null branch while the
	 *         other tests the non-null branch.Successfully hits all branches.
	 * 
	 */
	@Test
	// Written by Marlin Blue
	public void testEqualsFunction() {
		// SymbolicExpression result1, result2;
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
		SymbolicType functionType1 = universe.functionType(testArray1,
				testBool1);
		SymbolicType functionType2 = universe.functionType(testArray2,
				testBool1);

		SymbolicConstant symFunc1 = universe.symbolicConstant(name1,
				functionType1);
		SymbolicConstant symFunc2 = universe.symbolicConstant(name2,
				functionType1);
		SymbolicConstant symFunc3 = universe.symbolicConstant(name3,
				functionType2);
		SymbolicConstant symFunc4 = universe.symbolicConstant(name4,
				functionType2);

		BooleanExpression result1 = universe.equals(symFunc1, symFunc2);
		BooleanExpression result2 = universe.equals(symFunc3, symFunc4);

		// Testing case 6: FUNCTION
		assertEquals(universe.equals(
				universe.symbolicConstant(name1, functionType1),
				universe.symbolicConstant(name2, functionType1)), result1);
		assertEquals(universe.equals(
				universe.symbolicConstant(name3, functionType2),
				universe.symbolicConstant(name4, functionType2)), result2);

	}

}
