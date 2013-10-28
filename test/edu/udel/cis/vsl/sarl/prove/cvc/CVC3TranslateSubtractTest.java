package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.NumericExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicConstant;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateSubtractTest {

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
	private static NumericExpression two = universe.rational(2);
	private static NumericExpression one = universe.rational(1);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	// SymbolicConstants
	private static SymbolicConstant x = universe.symbolicConstant(
			universe.stringObject("x"), intType);
	private static SymbolicConstant y = universe.symbolicConstant(
			universe.stringObject("y"), intType);
	private static SymbolicConstant z = universe.symbolicConstant(
			universe.stringObject("z"), realType);
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
		proverFactory = Prove.newCVC3TheoremProverFactory(universe);
		cvcProver = (CVC3TheoremProver) proverFactory
				.newProver(booleanExprTrue);
		vc = cvcProver.validityChecker();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * testTranslateSubtract creates a numeric expression using the symbolic
	 * operator SUBTRACT and rational numeric expressions. The test then
	 * translates the numeric expression and compares it to the validity checker
	 * using .minusExpr.
	 */

	@Test
	public void testTranslateSubtract() {
		Expr oneExpr = cvcProver.translate(one);
		Expr twoExpr = cvcProver.translate(two);

		NumericExpression subExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.SUBTRACT, realType, two, one);
		Expr expr = cvcProver.translate(subExp);
		Expr expected = vc.minusExpr(twoExpr, oneExpr);
		assertEquals(expected, expr);
	}

	/**
	 * testTranslateSubtractSymbolic creates a numeric expression using the
	 * symbolic operator SUBTRACT and symbolic constants. The test then
	 * translates the numeric expression and compares it to the validity checker
	 * using .minusExpr.
	 */

	@Test
	public void testTranslateSubtractSymbolic() {
		Expr xExpr = cvcProver.translate(x);
		Expr yExpr = cvcProver.translate(y);

		NumericExpression subExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.SUBTRACT, intType, x, y);
		Expr expr = cvcProver.translate(subExp);
		Expr expected = vc.minusExpr(xExpr, yExpr);
		assertEquals(expected, expr);
	}

	/**
	 * testTranslateTwoTypeInt takes two SymbolicConstants of different types
	 * and subtracts one from another and returns an Int. This is compared to
	 * the CVC3 result of the same expression.
	 */
	@Test
	public void testTranslateSubtractTwoTypeInt() {
		Expr yExpr = cvcProver.translate(y);
		Expr zExpr = cvcProver.translate(z);

		NumericExpression subExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.SUBTRACT, intType, y, z);
		Expr expr = cvcProver.translate(subExp);
		Expr expected = vc.minusExpr(yExpr, zExpr);
		assertEquals(expected, expr);
	}

	/**
	 * testTranslateTwoTypeInt takes two SymbolicConstants of different types
	 * and subtracts one from another and returns a Real. This is compared to
	 * the CVC3 result of the same expression.
	 */
	@Test
	public void testTranslateSubtractTwoTypeReal() {
		Expr yExpr = cvcProver.translate(y);
		Expr zExpr = cvcProver.translate(z);

		NumericExpression subExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.SUBTRACT, realType, y, z);
		Expr expr = cvcProver.translate(subExp);
		Expr expected = vc.minusExpr(yExpr, zExpr);
		assertEquals(expected, expr);
	}

}
