package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import cvc3.Expr;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.ModelResult;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.ValidityResult;
import edu.udel.cis.vsl.sarl.IF.ValidityResult.ResultType;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.number.Number;
import edu.udel.cis.vsl.sarl.IF.number.NumberFactory;
import edu.udel.cis.vsl.sarl.IF.number.RationalNumber;
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProver;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;


// TODO: DON'T COMMIT BROKEN TESTS. 
// Make sure these tests don't generate false errors
// before you commit them.

public class CVC3ModelFinderTest {

	// Static fields: instantiated once and used for all tests...

	private static PrintStream out = System.out;

	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();

	private static NumberFactory numberFactory = factorySystem.numberFactory();

	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);

	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();

	private static NumberFactory numFactory = factorySystem.numberFactory();

	private static NumericExpression two = universe.rational(2);

	private static NumericExpression five = universe.rational(5);

	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	private static BooleanExpression booleanExprFalse = universe
			.falseExpression();
	
	private static BooleanExpression context = universe.lessThan(two, five);

	// Types
	private static SymbolicIntegerType intType = universe.integerType();
	private static SymbolicRealType realType = universe.realType();

	// Instance fields: instantiated before each test is run...

	private TheoremProverFactory proverFactory;

	private TheoremProver cvcProver;

	private ValidityChecker vc;
	
	private static LinkedList<TheoremProver> provers;


	/**
	 * 
	 * @throws Exception
	 */

	@Before
	public void setUp() throws Exception {
		provers = new LinkedList<TheoremProver>();

		provers.add(Prove.newCVC3TheoremProverFactory(universe).
				newProver(context));
	}

	/**
	 * Tests real number division against an assumption that is made to prove
	 * that a model can be provided that serves as a counter- example to the
	 * proof.
	 */
	@Test
	@Ignore
	public void blackBoxReal() {
		// Create the assumption y = 0.
		StringObject strY = universe.stringObject("y");
		SymbolicConstant symConstYInt = universe.symbolicConstant(strY,
				universe.realType());

		BooleanExpression assumption = universe.equals(symConstYInt,
				universe.rational(0));
		// Give the prover the assumption that y = 0.
		CVC3TheoremProver cvcProverYIs0 = (CVC3TheoremProver) proverFactory
				.newProver(assumption);

		vc = cvcProverYIs0.validityChecker();
		StringObject strX = universe.stringObject("x");
		SymbolicConstant symConstXInt = universe.symbolicConstant(strX,
				universe.realType());
		// Any X Int divided by zero (since y is zero) should return invalid.
		SymbolicExpression xDivideByY = expressionFactory.expression(
				SymbolicOperator.DIVIDE, universe.realType(), symConstXInt,
				symConstYInt);
		// 6 divided by 0 should return invalid
		SymbolicExpression sixDivideByZero = expressionFactory.expression(
				SymbolicOperator.DIVIDE, universe.realType(),
				universe.rational(6), universe.rational(0));
		// These 2 expressions should be equivalent in what they return.
		BooleanExpression predicate = universe.equals(xDivideByY,
				sixDivideByZero);
		ValidityResult result = cvcProver.validOrModel(predicate);

		ResultType resultType = result.getResultType();
		assertEquals(ResultType.NO, resultType);

		ModelResult modelResult = (ModelResult) result;
		Map<SymbolicConstant, SymbolicExpression> model = modelResult.getModel();
		assertNotNull(model);
	}

	/**
	 * Reads in an array index 0 and checks to see if it is equal to zero.
	 * This predicate is then shown to be invalid because the element at array
	 * index 0 does not have to be one. The element can be many values.
	 */
	@Test
	@Ignore
	public void arrayIndexModel() {
		SymbolicType arrayType = universe.arrayType(realType);
		SymbolicExpression a = universe.symbolicConstant(
				universe.stringObject("a"), arrayType);

		SymbolicExpression read = universe.arrayRead(a, universe.zeroInt());

		BooleanExpression predicate = universe.equals(read,
				universe.rational(1));

		ValidityResult result = cvcProver.validOrModel(predicate);
		ResultType resultType = result.getResultType();
		assertEquals(ResultType.NO, resultType);
		ModelResult model = (ModelResult) result;

		Map<SymbolicConstant, SymbolicExpression> map = model.getModel();
		assertEquals(1, map.size());

		Entry<SymbolicConstant, SymbolicExpression> entry = map.entrySet()
				.iterator().next();
		SymbolicConstant key = entry.getKey();
		assertEquals(a, key);

		SymbolicExpression value = entry.getValue();
		NumericExpression actualEntry = (NumericExpression) universe.arrayRead(
				value, universe.zeroInt());
		Number number = (RationalNumber) universe.extractNumber(actualEntry);
		int compareResult = numberFactory.compare(number,
				numberFactory.zeroRational());
		assertNotEquals(0, compareResult);
	}

	/**
	 * Test for universeEquals creates a CVC3Theorem Prover that assumes y = 6
	 * and sets the validityChecker to that value. The test then creates
	 * StringObjects and SymbolicConstants (another y = 6) to checks that the
	 * created validityChecker equates to the predicate expression created.
	 */

	@Test
	@Ignore
	public void universeEquals() {
		// Give the prover the assumption y = 6
		CVC3TheoremProver cvcProverYIs6 = (CVC3TheoremProver) proverFactory
				.newProver(universe.equals(universe.symbolicConstant(
						universe.stringObject("y"), universe.integerType()),
						universe.integer(6)));
		vc = cvcProverYIs6.validityChecker();

		StringObject strY = universe.stringObject("y");
		SymbolicConstant symConstYInt = universe.symbolicConstant(strY,
				universe.integerType());
		StringObject strX = universe.stringObject("x");
		SymbolicConstant symConstXInt = universe.symbolicConstant(strX,
				universe.integerType());

		SymbolicExpression symExprXplusY = expressionFactory.expression(
				SymbolicOperator.ADD, universe.integerType(), symConstYInt,
				symConstXInt);

		NumericExpression numExprSix = universe.integer(6);
		// I believe this is where casting error was being thrown.
		BooleanExpression predicate = universe
				.equals(numExprSix, symExprXplusY);
		ValidityResult result = cvcProverYIs6.validOrModel(predicate);
		assertEquals(ResultType.NO, result.getResultType());
	}

	/*
	 * Tests the constructor for CVC3ModelFinder which calls the computeModel()
	 * method and with the case given calls assign(Expr, SymbolicExpression)
	 */

	@Test(expected = SARLInternalException.class)
	@Ignore
	public void createCVC3ModelFinder() {
		HashMap<Expr, Expr> h = new HashMap<Expr, Expr>();
		h.put(vc.falseExpr(), vc.falseExpr());
		CVC3TheoremProver prover = (CVC3TheoremProver) proverFactory
				.newProver(universe.bool(true));
		prover.setOutput(out);
		CVC3ModelFinder c = new CVC3ModelFinder(prover, h);
		assertFalse(c.model.containsValue(null));
		assertNotNull(c);
	}

	/**
	 * Test for isApplyExpr creates three expressions; a varExpr, and two
	 * expression values. It then creates a HashMap that maps the varExpr to a
	 * funExpr that uses minusOp() with the two expression values. It then uses
	 * a CVC3TheoremProver created and the HashMap with the varExpr and minusOP
	 * to create a CVC3ModelFinder.
	 */

	/**
	 * TODO The reason of the NullPointerException is that this test is not
	 * designed in the right way. CVCModelFinder is not supposed to be called here.
	 * Instead, it is called in TheoremProver.ValidOrModel() when the validity result
	 * is invalid and a counterexample is to be established.
	 * So this test should be ignored.
	 * Moreover, tests for theorem provers should avoid directly dealing with a specific 
	 * prover, which prevents it to be reused for testing new theorem provers.
	 * Please refer to sarl.prove.TheoremProverTestTemplate.java, where it shows how to
	 * design reusable tests for theorem provers. The trick is to declare theorem provers
	 * as TheoremProver (not a specific prover) and invoke only methods of the interface TheoremProver.
	 */
	@Ignore @Test
	public void isApplyExpr() {
//		Expr varExpr = vc.varExpr("var",
//				cvcProver.translateType(universe.realType()));
//		Expr expr2 = cvcProver.translate(two);
//		Expr expr5 = cvcProver.translate(five);
//		HashMap<Expr, Expr> h = new HashMap<Expr, Expr>();
//		h.put(varExpr, vc.funExpr(vc.minusOp(), expr2, expr5));
//		TheoremProver prover = (CVC3TheoremProver) proverFactory
//				.newProver(universe.bool(true));
//		SymbolicConstant var = universe
//				.symbolicConstant(universe.stringObject("var"), realType);
		
//		assertEquals(prover.validOrModel());
//		assertNotNull(c);
	}

	/**
	 * Test for isBooleanExpr creates two CVC boolean expressions, creates a
	 * Hashmap and inserts the two expressions. The test creates a
	 * CVC3TheoremProver and uses the created Hashmap and TheoremProver to
	 * create a CVC3ModelFinder.
	 */

	@Test
	@Ignore
	public void isBooleanExpr() {
//		Expr exprTrue = cvcProver.translate(booleanExprTrue);
//		Expr exprFalse = cvcProver.translate(booleanExprFalse);
//		HashMap<Expr, Expr> h = new HashMap<Expr, Expr>();
//		h.put(exprTrue, exprFalse);
		BooleanExpression predicate = universe.equals(booleanExprTrue,
				booleanExprFalse);
		ValidityResult result = cvcProver.validOrModel(predicate);
		ResultType resultType = result.getResultType();
		assertEquals(ResultType.NO, resultType);
	}

	/**
	 * test for CVC3ModelFinderInvalid creates a Boolean expression predicate
	 * and creates a ValidityResult from the given predicate. The test then
	 * assesses the resultType. The test then takes the ModelResult of the
	 * ValidityResult made from the predicate and creates a map, mapping
	 * symbolic constant to symbolic expressions and verifies that there is only
	 * one element in the map. It then creates a counterexample that verifies
	 * that a given value does not equal zero.
	 */

	@Ignore
	@Test
	public void CVC3ModelFinderInvalid() {
		StringObject strX = universe.stringObject("x");
		SymbolicIntegerType sInt = universe.integerType();
		SymbolicConstant symConstXInt = universe.symbolicConstant(strX, sInt);
		NumericExpression symExprZero = universe.integer(0);
		BooleanExpression predicate = universe
				.equals(symConstXInt, symExprZero);
		ValidityResult result = cvcProver.validOrModel(predicate);

		ResultType resultType = result.getResultType();
		assertEquals(ResultType.NO, resultType);

		ModelResult modelResult = (ModelResult) result;
		Map<SymbolicConstant, SymbolicExpression> map = modelResult.getModel();
		// Verify there is exactly 1 element in the map.
		assertEquals(1, map.size());

		Collection<SymbolicExpression> s = map.values();
		Number n = null;
		for (SymbolicExpression se : s) {
			if (se instanceof NumericExpression) {
				NumericExpression p = (NumericExpression) se;
				n = universe.extractNumber(p);
			}
		}
		// Verify that the result (counterExample) does not equal zero.
		assertFalse(n.equals(numFactory.integer(0)));
	}

	@Ignore
	@Test
	public void CVC3ModelFinderTuple() {
		NumericExpression sevenInt = universe.integer(7);
		NumericExpression fourInt = universe.integer(4);
		NumericExpression eightInt = universe.integer(8);
		SymbolicType intType = universe.integerType();
		List<SymbolicExpression> tupleList = new ArrayList<SymbolicExpression>();
		tupleList.add(sevenInt);
		tupleList.add(fourInt);
		tupleList.add(eightInt);
		List<SymbolicType> tupleType = new ArrayList<SymbolicType>();
		tupleType.add(intType);
		tupleType.add(intType);
		tupleType.add(intType);

		SymbolicExpression s1 = universe.tuple(
				universe.tupleType(universe.stringObject("_TUPLE"), tupleType),
				tupleList);

		SymbolicExpression s2 = expressionFactory.expression(
				SymbolicOperator.TUPLE_WRITE, s1.type(), s1,
				universe.intObject(1), eightInt);

		BooleanExpression predicate = universe.equals(s1, s2);

		ValidityResult result = cvcProver.validOrModel(predicate);

		ResultType resultType = result.getResultType();

		assertEquals(ResultType.NO, resultType);

		ModelResult modelResult = (ModelResult) result;
		
		Map<SymbolicConstant, SymbolicExpression> model = modelResult.getModel();
		
		assertNotNull(model);
	}

	@Ignore
	@Test
	public void CVC3ModelFinderWithContext() {
		// Give the prover the assumption that y = 0.
		CVC3TheoremProver cvcProverYIs0 = (CVC3TheoremProver) proverFactory
				.newProver(universe.equals(universe.symbolicConstant(
						universe.stringObject("y"), universe.integerType()),
						universe.integer(0)));
		vc = cvcProverYIs0.validityChecker();

		StringObject strX = universe.stringObject("x");
		StringObject strY = universe.stringObject("y");
		SymbolicConstant symConstXInt = universe.symbolicConstant(strX,
				universe.integerType());
		SymbolicConstant symConstYInt = universe.symbolicConstant(strY,
				universe.integerType());
		SymbolicExpression xPlusY = expressionFactory.expression(
				SymbolicOperator.ADD, universe.integerType(), symConstXInt,
				symConstYInt);
		// Predicate is that "x = x + y" given the assumption that "y = 0".
		BooleanExpression predicate = universe.equals(symConstXInt, xPlusY);
		ValidityResult result = cvcProverYIs0.validOrModel(predicate);

		ResultType resultType = result.getResultType();
		assertEquals(ResultType.YES, resultType);
	}

	@Ignore
	@Test
	public void CVC3ModelFinderRational() {
		// Create the assumption y = 0.
		StringObject strY = universe.stringObject("y");
		SymbolicConstant symConstYInt = universe.symbolicConstant(strY,
				universe.integerType());
		BooleanExpression assumption = universe.equals(symConstYInt,
				universe.integer(0));
		// Give the prover the assumption that y = 0.
		CVC3TheoremProver cvcProverYIs0 = (CVC3TheoremProver) proverFactory
				.newProver(assumption);

		vc = cvcProverYIs0.validityChecker();
		StringObject strX = universe.stringObject("x");
		SymbolicConstant symConstXInt = universe.symbolicConstant(strX,
				universe.integerType());
		// Any X Int divided by zero (since y is zero) should return invalid.
		SymbolicExpression xDivideByY = expressionFactory.expression(
				SymbolicOperator.DIVIDE, universe.integerType(), symConstXInt,
				symConstYInt);
		// 6 divided by 0 should return invalid
		SymbolicExpression sixDivideByZero = expressionFactory.expression(
				SymbolicOperator.DIVIDE, universe.integerType(),
				universe.integer(6), universe.integer(0));
		// These 2 expressions should be equivalent in what they return.
		BooleanExpression predicate = universe.equals(xDivideByY,
				sixDivideByZero);
		ValidityResult result = cvcProver.validOrModel(predicate);

		ResultType resultType = result.getResultType();
		assertEquals(ResultType.NO, resultType);

		ModelResult modelResult = (ModelResult) result;
		Map<SymbolicConstant, SymbolicExpression> model = modelResult.getModel();
		assertNotNull(model);
	}

	@Ignore
	@Test
	public void CVC3ModelFinderTestDivideByOne() {
		// Create the assumption y = 2.
		StringObject strY = universe.stringObject("y");
		SymbolicConstant symConstYInt = universe.symbolicConstant(strY,
				universe.integerType());
		BooleanExpression assumption = universe.equals(symConstYInt,
				universe.integer(2));
		// Give the prover the assumption that y = 2.
		CVC3TheoremProver cvcProverYIs0 = (CVC3TheoremProver) proverFactory
				.newProver(assumption);
		vc = cvcProverYIs0.validityChecker();

		StringObject strX = universe.stringObject("x");
		SymbolicConstant symConstXInt = universe.symbolicConstant(strX,
				universe.integerType());
//		 Any X divided by y should return x (false).
		SymbolicExpression xDivideByY = expressionFactory.expression(
				SymbolicOperator.DIVIDE, universe.integerType(), symConstXInt,
				symConstYInt);

		BooleanExpression predicate = universe.equals(xDivideByY, symConstXInt);
		 ValidityResult result = cvcProver.validOrModel(predicate);

		ResultType resultType = result.getResultType();
		assertEquals(ResultType.NO, resultType);
	}

	@Ignore
	@Test
	public void CVC3ModelFinderApplyBoolean() {

//		SymbolicTypeFactory typeFactory = factorySystem.typeFactory();
		ObjectFactory objectFactory = factorySystem.objectFactory();

		SymbolicObject[] args = new SymbolicObject[5];
		args[0] = objectFactory.booleanObject(true);
		args[1] = objectFactory.booleanObject(true);
		args[2] = objectFactory.booleanObject(false);
		args[3] = objectFactory.booleanObject(true);
		args[4] = objectFactory.booleanObject(true);

//		SymbolicExpression applyExpr = expressionFactory.expression(
//				SymbolicOperator.APPLY, typeFactory.booleanType(), args);
//	
//		assertNotNull(applyExpr);
	}

	@Ignore
	@Test
	public void CVC3ModelFinderApplyReferenced() {
		List<SymbolicType> types = new ArrayList<SymbolicType>();
		types.add(intType);

//		SymbolicType type = universe.functionType(types, intType);
//		SymbolicConstant function = universe.symbolicConstant(
//				universe.stringObject("SymbolicConstant"), type);

		List<SymbolicExpression> funList = new ArrayList<SymbolicExpression>();
		funList.add(two);
//		SymbolicCollection<SymbolicExpression> collect = universe
//				.basicCollection(funList);

//		SymbolicExpression applyExpr = expressionFactory.expression(
//				SymbolicOperator.APPLY, function.type(), function, collect);
		
//		assertNotNull(applyExpr);
	}

	@Ignore
	@Test
	public void assignApplyElseStatement() {
		SymbolicType arrayIntType = universe.arrayType(universe.integerType());
		SymbolicExpression a = universe.symbolicConstant(
				universe.stringObject("a"), arrayIntType);
		NumericExpression one = universe.integer(1);
		NumericExpression two = universe.integer(2);
		BooleanExpression predicate = universe.equals(
				universe.arrayRead(a, one), two);
		ValidityResult result = cvcProver.validOrModel(predicate);

		assertEquals(ResultType.NO, result.getResultType());
		ModelResult model = (ModelResult) result;

		System.out.println(model.getModel());
	}

	@Ignore
	@Test
	public void assignArrayRealType() {
		SymbolicType arrayRealType = universe.arrayType(universe.realType());
		SymbolicExpression a = universe.symbolicConstant(
				universe.stringObject("a"), arrayRealType);
		NumericExpression x = universe.integer(1);
		SymbolicConstant y = universe.symbolicConstant(
				universe.stringObject("y"), realType);
//		SymbolicExpression s1 = expressionFactory.expression(
//				SymbolicOperator.ARRAY_WRITE, a.type(), a, x, y);
		BooleanExpression predicate = universe.equals(universe.arrayRead(a, x),
				y);
		ValidityResult result = cvcProver.validOrModel(predicate);

		assertEquals(ResultType.NO, result.getResultType());
		ModelResult modelResult = (ModelResult) result;
		
		Map<SymbolicConstant, SymbolicExpression> model = modelResult.getModel();
		assertNotNull(model);
	}

	@Ignore
	@Test
	public void assignTupleInt() {
		List<SymbolicExpression> tupleList = new ArrayList<SymbolicExpression>();
		tupleList.add(universe.integer(1));
		tupleList.add(universe.integer(2));
		tupleList.add(universe.integer(3));
		tupleList.add(universe.integer(4));

		List<SymbolicType> tupleType = new ArrayList<SymbolicType>();
		tupleType.add(universe.integerType());
		tupleType.add(universe.integerType());
		tupleType.add(universe.integerType());
		tupleType.add(universe.integerType());

		SymbolicExpression sarlTuple = universe
				.tuple(universe.tupleType(universe.stringObject("myTuple"),
						tupleType), tupleList);

		SymbolicExpression sarlTupleRead = expressionFactory.expression(
				SymbolicOperator.TUPLE_READ, sarlTuple.type(), sarlTuple,
				universe.intObject(0));

		BooleanExpression predicate = universe.equals(sarlTupleRead,
				universe.integer(4));

		ValidityResult result = cvcProver.validOrModel(predicate);

		assertEquals(ResultType.NO, result.getResultType());

		ModelResult modelResult = (ModelResult) result;

		Map<SymbolicConstant, SymbolicExpression> model = modelResult
				.getModel();
		
		assertNotNull(model);
	}

	@Ignore
	public void concreteArray() {
		SymbolicObject[] soArray = new SymbolicObject[5];
		soArray[0] = universe.integer(1);
		soArray[1] = universe.integer(2);
		soArray[2] = universe.integer(3);
		soArray[3] = universe.integer(4);
		soArray[4] = universe.integer(5);
		SymbolicOperator concrete = SymbolicOperator.CONCRETE;
		SymbolicExpression ne = expressionFactory.expression(concrete,
				universe.integerType(), soArray);

		BooleanExpression predicate = universe.equals(ne, ne);
		ValidityResult result = cvcProver.validOrModel(predicate);
		ResultType actual = result.getResultType();
		assertEquals(actual, actual);
	}
}
