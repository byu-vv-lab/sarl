package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.QueryResult;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.config.Configurations;
import edu.udel.cis.vsl.sarl.IF.config.Prover.ProverKind;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateMathTest {

	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	// types
	private static SymbolicRealType realType = universe.realType();
	private static SymbolicIntegerType intType = universe.integerType();
	// expressions
	private static NumericExpression ten = universe.rational(10);
	private static NumericExpression five = universe.rational(5);
	private static NumericExpression two = universe.rational(2);
	private static NumericExpression one = universe.rational(1);
	private static NumericExpression oneInt = universe.integer(1);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	// constants
	private static SymbolicConstant x = universe.symbolicConstant(
			universe.stringObject("x"), intType);
	private static SymbolicConstant y = universe.symbolicConstant(
			universe.stringObject("y"), intType);
	private static SymbolicConstant z = universe.symbolicConstant(
			universe.stringObject("z"), intType);
	// Instance fields: instantiated before each test is run...
	private TheoremProverFactory proverFactory;
	private CVC3TheoremProver cvcProver;
	private ValidityChecker vc;

	/**
	 * Set up each test. This method is run before each test.
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		proverFactory = Prove.newProverFactory(universe,
				Configurations.CONFIG.getProverWithKind(ProverKind.CVC3_API));
		cvcProver = (CVC3TheoremProver) proverFactory
				.newProver(booleanExprTrue);
		vc = cvcProver.validityChecker();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * testTranslateIntegerDivision creates two numeric expressions, one using
	 * the symbolic operator INT_DIVIDE, the other using MODULO, and two
	 * symbolic constants. The test compares the validity checker and the
	 * queryresult.
	 */

	@Test
	public void testTranslateIntegerDivision() {

		NumericExpression q = (NumericExpression) expressionFactory.expression(
				SymbolicOperator.INT_DIVIDE, intType, x, y);
		NumericExpression r = (NumericExpression) expressionFactory.expression(
				SymbolicOperator.MODULO, intType, x, y);

		Expr e = cvcProver.translate(x);
		Expr f = cvcProver.translate(y);
		Expr q2 = cvcProver.translate(q);
		Expr r2 = cvcProver.translate(r);

		Expr equationOne = vc.eqExpr(e, vc.plusExpr(r2, vc.multExpr(f, q2))); // e2
																				// =
																				// f
																				// *
																				// q2
																				// +
																				// r2
		Expr equationTwo = vc.leExpr(vc.ratExpr(0), r2); // 0 < r2
		Expr equationThree = vc.ltExpr(r2, f); // r2 < f

		assertEquals(QueryResult.VALID, vc.query(equationOne));
		assertEquals(QueryResult.VALID, vc.query(equationTwo));
		assertEquals(QueryResult.VALID, vc.query(equationThree));
	}

	/**
	 * testTranslateMultiply compares translated numeric expressions when using
	 * the multiply symbolic operator and the validity checker using multExpr.
	 * The test asserts multiple arguments as well.
	 */

	@Test(expected = SARLInternalException.class)
	public void testTranslateMultiply() {

		Expr oneExpr = cvcProver.translate(one);
		Expr twoExpr = cvcProver.translate(two);
		Expr fiveExpr = cvcProver.translate(five);
		Expr tenExpr = cvcProver.translate(ten);

		List<NumericExpression> mulList = new ArrayList<NumericExpression>();
		mulList.add(five);
		mulList.add(ten);
		SymbolicCollection<NumericExpression> mulCollection = universe
				.basicCollection(mulList);

		// One argument
		NumericExpression mulExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, mulCollection);
		Expr expr = cvcProver.translate(mulExp);
		Expr expr2 = vc.multExpr(vc.multExpr(oneExpr, fiveExpr), tenExpr);
		assertEquals(expr2, expr);

		// Two arguments
		NumericExpression mulExp2 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, two, five);
		Expr expr3 = cvcProver.translate(mulExp2);
		Expr expected2 = vc.multExpr(twoExpr, fiveExpr);
		assertEquals(expected2, expr3);

		// More than two arguments
		NumericExpression mulExp3 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, realType, two, five, ten);
		cvcProver.translate(mulExp3);
	}

	/**
	 * testTranslateMultiplySymbolic compares translated symbolic constant
	 * expressions when using the multiply symbolic operator and the validity
	 * checker using multExpr. The test asserts multiple arguments as well.
	 */

	@Test(expected = SARLInternalException.class)
	public void testTranslateMultiplySymbolic() {

		Expr oneExpr = cvcProver.translate(oneInt);
		Expr xExpr = cvcProver.translate(x);
		Expr yExpr = cvcProver.translate(y);

		List<SymbolicConstant> mulList = new ArrayList<SymbolicConstant>();
		mulList.add(x);
		mulList.add(y);
		SymbolicCollection<SymbolicConstant> mulCollection = universe
				.basicCollection(mulList);

		// One argument
		NumericExpression mulExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, intType, mulCollection);

		Expr expr = cvcProver.translate(mulExp);
		Expr expr2 = vc.multExpr(vc.multExpr(oneExpr, xExpr), yExpr);
		assertEquals(expr2, expr);

		// Two arguments
		NumericExpression mulExp2 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, intType, x, y);
		Expr expr3 = cvcProver.translate(mulExp2);
		Expr expected2 = vc.multExpr(xExpr, yExpr);
		assertEquals(expected2, expr3);

		// More than two arguments
		NumericExpression mulExp3 = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.MULTIPLY, intType, x, y, z);
		cvcProver.translate(mulExp3);
	}
}
