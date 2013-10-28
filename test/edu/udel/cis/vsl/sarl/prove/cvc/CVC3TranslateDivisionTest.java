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
import edu.udel.cis.vsl.sarl.IF.type.SymbolicRealType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateDivisionTest {

	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	// types
	private static SymbolicRealType realType = universe.realType();
	// expressions
	private static NumericExpression two = universe.rational(2);
	private static NumericExpression one = universe.rational(1);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	// SymbolicConstants
	private static SymbolicConstant x = universe.symbolicConstant(
			universe.stringObject("x"), realType);
	private static SymbolicConstant y = universe.symbolicConstant(
			universe.stringObject("y"), realType);
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
	 * testTranslateDivision compares the validity checker and a
	 * cvc3ThoremProver using the divide symbolic operator and
	 * numericExpressions
	 */

	@Test
	public void testTranslateDivision() {
		Expr oneExpr = cvcProver.translate(one);
		Expr twoExpr = cvcProver.translate(two);

		NumericExpression divExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.DIVIDE, realType, one, two);
		Expr expr = cvcProver.translate(divExp);
		Expr expected = vc.divideExpr(oneExpr, twoExpr);
		assertEquals(expected, expr);
	}

	/**
	 * testTranslateDivisionSymbolic compares the validity checker and a
	 * cvc3ThoremProver using the divide symbolic operator and symbolic
	 * constants
	 */

	@Test
	public void testTranslateDivisionSymbolic() {
		Expr xExpr = cvcProver.translate(x);
		Expr yExpr = cvcProver.translate(y);

		NumericExpression divExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.DIVIDE, realType, x, y);
		Expr expr = cvcProver.translate(divExp);
		Expr expected = vc.divideExpr(xExpr, yExpr);
		assertEquals(expected, expr);
	}
}
