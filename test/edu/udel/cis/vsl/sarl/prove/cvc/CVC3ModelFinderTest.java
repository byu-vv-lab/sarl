package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import edu.udel.cis.vsl.sarl.IF.object.StringObject;
import edu.udel.cis.vsl.sarl.IF.object.SymbolicObject;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.object.IF.ObjectFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;
import edu.udel.cis.vsl.sarl.type.IF.SymbolicTypeFactory;

public class CVC3ModelFinderTest {

	// Static fields: instantiated once and used for all tests...

	private static PrintStream out = System.out;

	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();

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

	// Instance fields: instantiated before each test is run...

	private TheoremProverFactory proverFactory;

	private CVC3TheoremProver cvcProver;

	private ValidityChecker vc;
	
	/**
	 * 
	 * @throws Exception
	 */

	@Before
	public void setUp() throws Exception {
		proverFactory = Prove.newCVC3TheoremProverFactory(universe);
		cvcProver = (CVC3TheoremProver) proverFactory
				.newProver(booleanExprTrue);
		vc = cvcProver.validityChecker();
	}
	
	/**
	 * Test for universeEquals creates a CVC3Theorem Prover that assumes
	 * y = 6 and sets the validityChecker to that value. The test then
	 * creates StringObjects and SymbolicConstants (another y = 6) to checks
	 * that the created validityChecker equates to the 
	 * predicate expression created.
	 */
	
	@Test
	public void universeEquals() {
		//Give the prover the assumption y = 6
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
				SymbolicOperator.ADD, universe.integerType(),
				symConstYInt, symConstXInt);
	
		NumericExpression numExprSix = universe.integer(6);
		//I believe this is where casting error was being thrown.
		BooleanExpression predicate = universe.equals(numExprSix, symExprXplusY);
		ValidityResult result = cvcProverYIs6.validOrModel(predicate);
		assertEquals(ResultType.NO, result.getResultType());
	}

	/*
	 * Tests the constructor for CVC3ModelFinder which calls the computeModel()
	 * method and with the case given calls assign(Expr, SymbolicExpression)
	 */

	@Test(expected = SARLInternalException.class)
	public void createCVC3ModelFinder() {
		HashMap<Expr, Expr> h = new HashMap<Expr, Expr>();
		h.put(vc.falseExpr(), vc.falseExpr());
		CVC3TheoremProver prover = (CVC3TheoremProver) proverFactory
				.newProver(universe.bool(true));
		prover.setOutput(out);
		CVC3ModelFinder c = new CVC3ModelFinder(prover, h);
	}
	
	/**
	 * Test for isApplyExpr creates three expressions; a varExpr, and 
	 * two expression values. It then creates a HashMap that maps
	 * the varExpr to a funExpr that uses minusOp() with the two 
	 * expression values. It then uses a CVC3TheoremProver created and the
	 * HashMap with the varExpr and minusOP to create a CVC3ModelFinder.
	 */

	@Test
	public void isApplyExpr() {
		Expr varExpr = vc.varExpr("var",
				cvcProver.translateType(universe.realType()));
		Expr expr2 = cvcProver.translate(two);
		Expr expr5 = cvcProver.translate(five);
		HashMap<Expr, Expr> h = new HashMap<Expr, Expr>();
		h.put(varExpr, vc.funExpr(vc.minusOp(), expr2, expr5));
		CVC3TheoremProver prover = (CVC3TheoremProver) proverFactory
				.newProver(universe.bool(true));
		CVC3ModelFinder c = new CVC3ModelFinder(prover, h);
	}
	
	/**
	 * Test for isBooleanExpr creates two CVC boolean expressions, creates a
	 * Hashmap and inserts the two expressions. The test creates a CVC3TheoremProver
	 * and uses the created Hashmap and TheoremProver to create a CVC3ModelFinder.
	 */

	@Test
	public void isBooleanExpr() {
		Expr exprTrue = cvcProver.translate(booleanExprTrue);
		Expr exprFalse = cvcProver.translate(booleanExprFalse);
		HashMap<Expr, Expr> h = new HashMap<Expr, Expr>();
		h.put(exprTrue, exprFalse);
		BooleanExpression predicate = universe.equals(booleanExprTrue, booleanExprFalse);
		ValidityResult result = cvcProver.validOrModel(predicate);
		ResultType resultType = result.getResultType();
		assertEquals(ResultType.NO, resultType);
	}
	
	/**
	 * test for CVC3ModelFinderInvalid creates a Boolean expression predicate
	 * and creates a ValidityResult from the given predicate. The test then
	 * assesses the resultType. The test then takes the ModelResult of the
	 * ValidityResult made from the predicate and creates a map, mapping symbolic
	 * constant to symbolic expressions and verifies that there is only one
	 * element in the map. It then creates a counterexample that verifies
	 * that a given value does not equal zero.
	 */

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
				universe.tupleType(universe.stringObject("tuple"), tupleType),
				tupleList);
		SymbolicExpression s2 = expressionFactory.expression(
				SymbolicOperator.TUPLE_WRITE, s1.type(), s1,
				universe.intObject(1), eightInt);

		BooleanExpression predicate = universe.equals(s1, s2);

		ValidityResult result = cvcProver.validOrModel(predicate);

		ResultType resultType = result.getResultType();

		assertEquals(ResultType.NO, resultType);

		ModelResult model = (ModelResult) result;
	}

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

		ModelResult model = (ModelResult) result;
	}

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
		// Any X divided by y should return x (false).
		SymbolicExpression xDivideByY = expressionFactory.expression(
				SymbolicOperator.DIVIDE, universe.integerType(), symConstXInt,
				symConstYInt);

		BooleanExpression predicate = universe.equals(xDivideByY, symConstXInt);
		//ValidityResult result = cvcProver.validOrModel(predicate);

		//ResultType resultType = result.getResultType();
		//assertEquals(ResultType.NO, resultType);
	}

	@Test
	public void CVC3ModelFinderApplyBoolean() {

		SymbolicTypeFactory typeFactory = factorySystem.typeFactory();
		ObjectFactory objectFactory = factorySystem.objectFactory();

		SymbolicObject[] args = new SymbolicObject[5];
		args[1] = objectFactory.booleanObject(true);
		args[2] = objectFactory.booleanObject(true);
		args[3] = objectFactory.booleanObject(false);
		args[4] = objectFactory.booleanObject(true);
		args[5] = objectFactory.booleanObject(true);

		SymbolicExpression applyExpr = expressionFactory.expression(
				SymbolicOperator.APPLY, typeFactory.booleanType(), args);
	}

	@Test
	public void CVC3ModelFinderApplyReferenced() {
		ObjectFactory objectFactory = factorySystem.objectFactory();

		SymbolicObject[] args = new SymbolicObject[3];
		args[0] = objectFactory.stringObject("x");
		args[1] = objectFactory.stringObject("y");
		args[2] = objectFactory.stringObject("z");

		SymbolicExpression applyExpr = expressionFactory.expression(
				SymbolicOperator.APPLY, universe.referenceType(), args);
	}

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
	
	@Test
	public void AssignArrayRealType() {
		SymbolicType arrayRealType = universe.arrayType(universe.realType());
		SymbolicExpression a = universe.symbolicConstant(
				universe.stringObject("a"), arrayRealType);
		NumericExpression one = universe.integer(1);
		NumericExpression two = universe.rational(2);
		BooleanExpression predicate = universe.equals(
				universe.arrayRead(a, one), two);
		ValidityResult result = cvcProver.validOrModel(predicate);
		
		assertEquals(ResultType.NO, result.getResultType());
		ModelResult model = (ModelResult) result;
		
		System.out.println(model.getModel());
	}
	
	@Test
	public void assignTupleInt() {
		List<SymbolicExpression> tupleList = new ArrayList<SymbolicExpression>();
		tupleList.add(universe.integer(1));
		tupleList.add(universe.integer(2));
		tupleList.add(universe.integer(3));
		tupleList.add(universe.integer(4));

		List<SymbolicType> tupleType = new ArrayList<SymbolicType>();
		tupleType.add(universe.integerType());
		
		SymbolicExpression sarlTuple = universe.tuple(universe.tupleType(
				universe.stringObject("myTuple"), tupleType), tupleList);
		
		SymbolicExpression sarlTupleRead = expressionFactory.expression(
				SymbolicOperator.TUPLE_READ, sarlTuple.type(), sarlTuple, universe.intObject(0));
		
		BooleanExpression predicate = universe.equals(sarlTupleRead ,universe.integer(4));
		
		ValidityResult result = cvcProver.validOrModel(predicate);
		
		assertEquals(ResultType.NO, result.getResultType());
		
		ModelResult modelResult = (ModelResult) result;
		
		Map<SymbolicConstant, SymbolicExpression> model = modelResult.getModel();
	
		System.out.println(model);
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
				universe.integerType(), 
				soArray);
		
		BooleanExpression predicate = universe.equals(ne, ne);
		ValidityResult result = cvcProver.validOrModel(predicate);
		ResultType actual = result.getResultType();
		assertEquals(actual, actual);
	}
}
