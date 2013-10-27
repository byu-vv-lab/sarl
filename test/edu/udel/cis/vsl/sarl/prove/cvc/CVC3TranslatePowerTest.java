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

public class CVC3TranslatePowerTest {

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
	private static NumericExpression five = universe.rational(5);
	private static NumericExpression two = universe.rational(2);
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	// SymbolicConstants
	private static SymbolicConstant e = universe.symbolicConstant(
			universe.stringObject("e"), intType);
	private static SymbolicConstant f = universe.symbolicConstant(
			universe.stringObject("f"), intType);
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
	 * testTranslate power compares the validity checker and a created expr from numeric expressions
	 * using the symbolic operator POWER and powExpr.
	 */

	@Test
	public void testTranslatePower() {
		Expr twoExpr = cvcProver.translate(two);
		Expr fiveExpr = cvcProver.translate(five);

		NumericExpression powerExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.POWER, realType, two, five);
		Expr expr9 = cvcProver.translate(powerExp);
		Expr expected9 = vc.powExpr(twoExpr, fiveExpr);
		assertEquals(expected9, expr9);
	}
	
	/**
	 * testTranslateSymbolic power compares the validity checker and a created expr from symbolic constants
	 * using the symbolic operator POWER and powExpr.
	 */

	@Test
	public void testTranslatePowerSymbolic() {
		Expr eExpr = cvcProver.translate(e);
		Expr fExpr = cvcProver.translate(f);

		NumericExpression powerExp = (NumericExpression) expressionFactory
				.expression(SymbolicOperator.POWER, intType, e, f);
		Expr expr9 = cvcProver.translate(powerExp);
		Expr expected9 = vc.powExpr(eExpr, fExpr);
		assertEquals(expected9, expr9);
	}

}
