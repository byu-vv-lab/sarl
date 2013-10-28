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
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicIntegerType;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateNegativeTest {

	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	// types
	private static SymbolicIntegerType intType = universe.integerType();
	// expressions
	private static NumericExpression one = universe.rational(1);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	// SymbolicConstant
	private static SymbolicConstant x = universe.symbolicConstant(
			universe.stringObject("x"), intType);
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
	 * testTranslateNegative compares a negative numeric expression and the
	 * validity checker
	 */

	@Test
	public void testTranslateNegative() {

		NumericExpression negExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.NEGATIVE, intType, one);
		Expr expr = cvcProver.translate(negExp);
		Expr expected = vc.uminusExpr(cvcProver
				.translate((SymbolicExpression) negExp.argument(0)));
		assertEquals(expected, expr);
	}

	/**
	 * testTranslateNegative compares a negative symbolic constant expression
	 * and the validity checker
	 */

	@Test
	public void testTranslateNegativeSymbolic() {

		NumericExpression negExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.NEGATIVE, intType, x);
		Expr expr = cvcProver.translate(negExp);
		Expr expected = vc.uminusExpr(cvcProver
				.translate((SymbolicExpression) negExp.argument(0)));
		assertEquals(expected, expr);
	}
}
