package edu.udel.cis.vsl.sarl.prove.cvc;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cvc3.Expr;
import cvc3.ValidityChecker;
import edu.udel.cis.vsl.sarl.IF.SARLInternalException;
import edu.udel.cis.vsl.sarl.IF.config.Configurations;
import edu.udel.cis.vsl.sarl.IF.config.ProverInfo.ProverKind;
import edu.udel.cis.vsl.sarl.IF.expr.BooleanExpression;
import edu.udel.cis.vsl.sarl.IF.expr.SymbolicExpression.SymbolicOperator;
import edu.udel.cis.vsl.sarl.IF.type.SymbolicType;
import edu.udel.cis.vsl.sarl.collections.IF.SymbolicCollection;
import edu.udel.cis.vsl.sarl.expr.IF.ExpressionFactory;
import edu.udel.cis.vsl.sarl.preuniverse.PreUniverses;
import edu.udel.cis.vsl.sarl.preuniverse.IF.FactorySystem;
import edu.udel.cis.vsl.sarl.preuniverse.IF.PreUniverse;
import edu.udel.cis.vsl.sarl.prove.Prove;
import edu.udel.cis.vsl.sarl.prove.IF.TheoremProverFactory;

public class CVC3TranslateAndTest {

	// Static fields: instantiated once and used for all tests...
	private static FactorySystem factorySystem = PreUniverses
			.newIdealFactorySystem();
	private static PreUniverse universe = PreUniverses
			.newPreUniverse(factorySystem);
	private static ExpressionFactory expressionFactory = factorySystem
			.expressionFactory();
	private static SymbolicType boolType = universe.booleanType();
	// expressions
	private static BooleanExpression booleanExprTrue = universe
			.trueExpression();
	private static BooleanExpression booleanExprFalse = universe
			.falseExpression();
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
		proverFactory = Prove.newProverFactory(
				universe,
				Configurations.getDefaultConfiguration().getProverWithKind(
						ProverKind.CVC3_API));
		cvcProver = (CVC3TheoremProver) proverFactory
				.newProver(booleanExprTrue);
		vc = cvcProver.validityChecker();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * testTranslateAndOneArg compares a Arraylist of BooleanExpressions with
	 * the validity checker using the AND symbolic operator.
	 */

	@Test
	public void testTranslateAndOneArg() {
		Expr trueExpr = cvcProver.translate(booleanExprTrue);
		Expr falseExpr = cvcProver.translate(booleanExprFalse);

		List<BooleanExpression> s1 = new ArrayList<BooleanExpression>();
		s1.add(booleanExprFalse);
		s1.add(booleanExprTrue);

		SymbolicCollection<BooleanExpression> boolList = universe
				.basicCollection(s1);
		BooleanExpression andExp = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, boolList);
		Expr expr = cvcProver.translate(andExp);
		Expr expected = vc.andExpr(falseExpr, trueExpr);
		assertEquals(expected, expr);

	}

	/**
	 * testTranslateandTwoArg compares two boolean expressions with the and
	 * symbolic operator
	 */

	@Test
	public void testTranslateAndTwoArg() {

		Expr trueExpr = cvcProver.translate(booleanExprTrue);
		Expr falseExpr = cvcProver.translate(booleanExprFalse);

		BooleanExpression andExp = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, booleanExprTrue,
						booleanExprTrue);
		Expr expr = cvcProver.translate(andExp);
		Expr expected = vc.andExpr(trueExpr, trueExpr);
		assertEquals(expected, expr);

		BooleanExpression andExp2 = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, booleanExprTrue,
						booleanExprFalse);
		Expr expr2 = cvcProver.translate(andExp2);
		Expr expected2 = vc.andExpr(trueExpr, falseExpr);
		assertEquals(expected2, expr2);
	}

	/**
	 * testTranslateAndException translates a boolean expression using the and
	 * symbolic operator
	 * 
	 * @exception SARLInternalException.class
	 */

	@Test(expected = SARLInternalException.class)
	public void testTranslateAndException() {

		BooleanExpression andExp = (BooleanExpression) expressionFactory
				.expression(SymbolicOperator.AND, boolType, booleanExprTrue,
						booleanExprFalse, booleanExprFalse);
		cvcProver.translate(andExp);
	}
}
